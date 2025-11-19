package br.ufpr.athos.campeonato.service;

import br.ufpr.athos.campeonato.config.RabbitMQConfig;
import br.ufpr.athos.campeonato.dto.EquipeRequestDTO;
import br.ufpr.athos.campeonato.dto.EquipeResponseDTO;
import br.ufpr.athos.campeonato.event.EquipeEvent;
import br.ufpr.athos.campeonato.exception.ResourceNotFoundException;
import br.ufpr.athos.campeonato.model.Campeonato;
import br.ufpr.athos.campeonato.model.Equipe;
import br.ufpr.athos.campeonato.repository.CampeonatoRepository;
import br.ufpr.athos.campeonato.repository.EquipeRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipeService {

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private CampeonatoRepository campeonatoRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

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

        return new EquipeResponseDTO(salva);
    }

    public List<EquipeResponseDTO> listarPorCampeonato(String campeonatoId) {
        return equipeRepository.findByCampeonatoIdOrderByNome(campeonatoId)
                .stream()
                .map(EquipeResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<EquipeResponseDTO> listarPorCapitao(String capitaoId) {
        return equipeRepository.findByCapitaoIdOrderByDataCriacaoDesc(capitaoId)
                .stream()
                .map(EquipeResponseDTO::new)
                .collect(Collectors.toList());
    }

    public EquipeResponseDTO buscarPorId(String id) {
        Equipe equipe = equipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipe", id));
        return new EquipeResponseDTO(equipe);
    }

    public void deletarEquipe(String id) {
        equipeRepository.deleteById(id);
    }
}
