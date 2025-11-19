package br.ufpr.athos.campeonato.service;

import br.ufpr.athos.campeonato.config.RabbitMQConfig;
import br.ufpr.athos.campeonato.dto.EquipeRequestDTO;
import br.ufpr.athos.campeonato.dto.EquipeResponseDTO;
import br.ufpr.athos.campeonato.dto.MembroEquipeSimpleDTO;
import br.ufpr.athos.campeonato.event.EquipeEvent;
import br.ufpr.athos.campeonato.exception.ResourceNotFoundException;
import br.ufpr.athos.campeonato.messaging.UsuarioConsultaEvent;
import br.ufpr.athos.campeonato.messaging.UsuarioResponseEvent;
import br.ufpr.athos.campeonato.messaging.UsuarioResponseListener;
import br.ufpr.athos.campeonato.model.Campeonato;
import br.ufpr.athos.campeonato.model.Equipe;
import br.ufpr.athos.campeonato.model.MembroEquipe;
import br.ufpr.athos.campeonato.repository.CampeonatoRepository;
import br.ufpr.athos.campeonato.repository.EquipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class EquipeService {

    private static final Logger logger = LoggerFactory.getLogger(EquipeService.class);
    private static final long USUARIO_LOOKUP_TIMEOUT_MS = 2000; // 2 seconds

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private CampeonatoRepository campeonatoRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private UsuarioResponseListener usuarioResponseListener;

    // In-memory cache for user names: usuarioId -> nome
    private final Map<String, String> userNameCache = new ConcurrentHashMap<>();

    public EquipeResponseDTO criarEquipe(EquipeRequestDTO request) {
        Campeonato campeonato = campeonatoRepository.findById(request.getCampeonatoId())
                .orElseThrow(() -> new ResourceNotFoundException("Campeonato", request.getCampeonatoId()));

        Equipe equipe = new Equipe();
        equipe.setNome(request.getNome());
        equipe.setCampeonato(campeonato);
        equipe.setCapitaoId(request.getCapitaoId());

        Equipe salva = equipeRepository.save(equipe);

        // Publish event
        EquipeEvent event = new EquipeEvent();
        event.setEquipeId(salva.getId());
        event.setEquipeNome(salva.getNome());
        event.setCampeonatoId(salva.getCampeonato().getId());
        event.setCampeonatoNome(salva.getCampeonato().getNome());
        event.setCapitaoId(salva.getCapitaoId());
        event.setTipoEvento("equipe.inscrita");

        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_NAME,
            RabbitMQConfig.ROUTING_KEY_EQUIPE_INSCRITA,
            event
        );

        return convertToDTO(salva);
    }

    public List<EquipeResponseDTO> listarPorCampeonato(String campeonatoId) {
        return equipeRepository.findByCampeonatoIdOrderByNome(campeonatoId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EquipeResponseDTO> listarPorCapitao(String capitaoId) {
        return equipeRepository.findByCapitaoIdOrderByDataCriacaoDesc(capitaoId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EquipeResponseDTO> listarPorCapitaoInscricoesAbertas(String capitaoId) {
        return equipeRepository.findByCapitaoIdAndCampeonato_StatusOrderByDataCriacaoDesc(
                        capitaoId,
                        Campeonato.StatusCampeonato.INSCRICOES_ABERTAS
                )
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public EquipeResponseDTO buscarPorId(String id) {
        Equipe equipe = equipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipe", id));
        return convertToDTO(equipe);
    }

    public void deletarEquipe(String id) {
        equipeRepository.deleteById(id);
    }

    /**
     * Converts Equipe entity to EquipeResponseDTO with user names populated via RabbitMQ
     */
    private EquipeResponseDTO convertToDTO(Equipe equipe) {
        EquipeResponseDTO dto = new EquipeResponseDTO(equipe);

        // Populate membros with user names
        List<MembroEquipeSimpleDTO> membrosDTO = new ArrayList<>();

        for (MembroEquipe membro : equipe.getMembros()) {
            String usuarioId = membro.getUsuarioId();
            String usuarioNome = getUserName(usuarioId);
            String dataEntrada = membro.getDataEntrada() != null
                    ? membro.getDataEntrada().toString()
                    : null;

            MembroEquipeSimpleDTO membroDTO = new MembroEquipeSimpleDTO(
                    usuarioId,
                    usuarioNome,
                    dataEntrada
            );

            membrosDTO.add(membroDTO);
        }

        dto.setMembros(membrosDTO);
        return dto;
    }

    /**
     * Gets user name via RabbitMQ with caching and timeout
     *
     * @param usuarioId The user ID to lookup
     * @return User name or usuarioId as fallback
     */
    private String getUserName(String usuarioId) {
        // Check cache first
        if (userNameCache.containsKey(usuarioId)) {
            logger.debug("Cache hit for usuarioId: {}", usuarioId);
            return userNameCache.get(usuarioId);
        }

        logger.debug("Cache miss for usuarioId: {}, fetching via RabbitMQ", usuarioId);

        try {
            // Register pending request
            CompletableFuture<UsuarioResponseEvent> future =
                    usuarioResponseListener.registerPendingRequest(usuarioId);

            // Send request to ms-autenticacao
            UsuarioConsultaEvent request = new UsuarioConsultaEvent(usuarioId);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.USUARIO_EXCHANGE,
                    RabbitMQConfig.USUARIO_CONSULTA_ROUTING_KEY,
                    request
            );

            logger.debug("Sent user lookup request for usuarioId: {}", usuarioId);

            // Wait for response with timeout
            UsuarioResponseEvent response = future.get(USUARIO_LOOKUP_TIMEOUT_MS, TimeUnit.MILLISECONDS);

            if (response != null && response.getNome() != null) {
                // Cache the result
                userNameCache.put(usuarioId, response.getNome());
                logger.debug("Cached user name for usuarioId: {}", usuarioId);
                return response.getNome();
            }
        } catch (Exception e) {
            logger.warn("Failed to fetch user name for usuarioId: {} - {}", usuarioId, e.getMessage());
            // Cleanup pending request on error
            usuarioResponseListener.cancelPendingRequest(usuarioId);
        }

        // Fallback to usuarioId if lookup fails or times out
        logger.debug("Using fallback (usuarioId) for: {}", usuarioId);
        return usuarioId;
    }

    /**
     * Clears the user name cache (useful for testing or manual cache invalidation)
     */
    public void clearUserNameCache() {
        userNameCache.clear();
        logger.info("User name cache cleared");
    }

    /**
     * Gets current cache size (useful for monitoring)
     */
    public int getUserNameCacheSize() {
        return userNameCache.size();
    }
}
