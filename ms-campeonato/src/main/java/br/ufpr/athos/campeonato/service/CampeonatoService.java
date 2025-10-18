package br.ufpr.athos.campeonato.service;

import br.ufpr.athos.campeonato.dto.CampeonatoRequestDTO;
import br.ufpr.athos.campeonato.dto.CampeonatoResponseDTO;
import br.ufpr.athos.campeonato.model.Campeonato;
import br.ufpr.athos.campeonato.repository.CampeonatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampeonatoService {

    @Autowired
    private CampeonatoRepository campeonatoRepository;

    public CampeonatoResponseDTO criarCampeonato(CampeonatoRequestDTO request) {
        Campeonato campeonato = new Campeonato();
        campeonato.setNome(request.getNome());
        campeonato.setEsporte(request.getEsporte());
        campeonato.setDataInicio(request.getDataInicio());
        campeonato.setDataFim(request.getDataFim());
        campeonato.setFormato(Campeonato.FormatoCampeonato.valueOf(request.getFormato()));
        campeonato.setOrganizadorId(request.getOrganizadorId());
        campeonato.setDescricao(request.getDescricao());
        campeonato.setMaxEquipes(request.getMaxEquipes());

        Campeonato salvo = campeonatoRepository.save(campeonato);
        return new CampeonatoResponseDTO(salvo);
    }

    public List<CampeonatoResponseDTO> listarTodos() {
        return campeonatoRepository.findAll()
                .stream()
                .map(CampeonatoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public CampeonatoResponseDTO buscarPorId(String id) {
        Campeonato campeonato = campeonatoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campeonato não encontrado"));
        return new CampeonatoResponseDTO(campeonato);
    }

    public List<CampeonatoResponseDTO> listarPorOrganizador(String organizadorId) {
        return campeonatoRepository.findByOrganizadorIdOrderByDataCriacaoDesc(organizadorId)
                .stream()
                .map(CampeonatoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public CampeonatoResponseDTO atualizarStatus(String id, String novoStatus) {
        Campeonato campeonato = campeonatoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campeonato não encontrado"));

        campeonato.setStatus(Campeonato.StatusCampeonato.valueOf(novoStatus));
        Campeonato atualizado = campeonatoRepository.save(campeonato);

        return new CampeonatoResponseDTO(atualizado);
    }

    public void deletarCampeonato(String id) {
        campeonatoRepository.deleteById(id);
    }
}
