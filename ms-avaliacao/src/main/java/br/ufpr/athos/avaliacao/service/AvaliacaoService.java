package br.ufpr.athos.avaliacao.service;

import br.ufpr.athos.avaliacao.config.RabbitMQConfig;
import br.ufpr.athos.avaliacao.dto.AvaliacaoRequestDTO;
import br.ufpr.athos.avaliacao.dto.AvaliacaoResponseDTO;
import br.ufpr.athos.avaliacao.dto.MediaAvaliacaoDTO;
import br.ufpr.athos.avaliacao.event.AvaliacaoEvent;
import br.ufpr.athos.avaliacao.model.Avaliacao;
import br.ufpr.athos.avaliacao.repository.AvaliacaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AvaliacaoService {

    private static final Logger logger = LoggerFactory.getLogger(AvaliacaoService.class);

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public AvaliacaoResponseDTO criarAvaliacao(AvaliacaoRequestDTO request) {
        // Verificar se já avaliou este usuário neste evento
        Optional<Avaliacao> existing = avaliacaoRepository.findByAvaliadorIdAndAvaliadoIdAndEventoId(
            request.getAvaliadorId(),
            request.getAvaliadoId(),
            request.getEventoId()
        );

        if (existing.isPresent()) {
            throw new RuntimeException("Você já avaliou este usuário neste evento");
        }

        // Usuário não pode se auto-avaliar
        if (request.getAvaliadorId().equals(request.getAvaliadoId())) {
            throw new RuntimeException("Você não pode se auto-avaliar");
        }

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setAvaliadorId(request.getAvaliadorId());
        avaliacao.setAvaliadorNome(request.getAvaliadorNome());
        avaliacao.setAvaliadoId(request.getAvaliadoId());
        avaliacao.setEventoId(request.getEventoId());
        avaliacao.setEventoNome(request.getEventoNome());
        avaliacao.setNota(request.getNota());
        avaliacao.setComentario(request.getComentario());

        Avaliacao salva = avaliacaoRepository.save(avaliacao);

        // Publicar evento para notificação e recomendação
        publicarEventoAvaliacao(salva);

        return new AvaliacaoResponseDTO(salva);
    }

    private void publicarEventoAvaliacao(Avaliacao avaliacao) {
        try {
            AvaliacaoEvent event = new AvaliacaoEvent(
                avaliacao.getId(),
                avaliacao.getAvaliadorId(),
                avaliacao.getAvaliadorNome(),
                avaliacao.getAvaliadoId(),
                avaliacao.getEventoId(),
                avaliacao.getEventoNome(),
                avaliacao.getNota()
            );

            rabbitTemplate.convertAndSend(
                RabbitMQConfig.AVALIACAO_EXCHANGE,
                RabbitMQConfig.AVALIACAO_RECEBIDA_KEY,
                event
            );

            logger.info("Evento de avaliação publicado: {}", avaliacao.getId());
        } catch (Exception e) {
            logger.error("Erro ao publicar evento de avaliação: {}", e.getMessage(), e);
        }
    }

    public List<AvaliacaoResponseDTO> listarAvaliacoesRecebidas(String usuarioId) {
        return avaliacaoRepository.findByAvaliadoIdOrderByDataCriacaoDesc(usuarioId)
                .stream()
                .map(AvaliacaoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<AvaliacaoResponseDTO> listarAvaliacoesFeitas(String usuarioId) {
        return avaliacaoRepository.findByAvaliadorIdOrderByDataCriacaoDesc(usuarioId)
                .stream()
                .map(AvaliacaoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<AvaliacaoResponseDTO> listarAvaliacoesPorEvento(String eventoId) {
        return avaliacaoRepository.findByEventoIdOrderByDataCriacaoDesc(eventoId)
                .stream()
                .map(AvaliacaoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public MediaAvaliacaoDTO calcularMediaAvaliacao(String usuarioId) {
        List<Avaliacao> avaliacoes = avaliacaoRepository.findAllByAvaliadoId(usuarioId);

        if (avaliacoes.isEmpty()) {
            return new MediaAvaliacaoDTO(usuarioId, 0.0, 0L);
        }

        double media = avaliacoes.stream()
                .mapToInt(Avaliacao::getNota)
                .average()
                .orElse(0.0);

        return new MediaAvaliacaoDTO(usuarioId, media, (long) avaliacoes.size());
    }

    public void deletarAvaliacao(String avaliacaoId) {
        avaliacaoRepository.deleteById(avaliacaoId);
    }
}
