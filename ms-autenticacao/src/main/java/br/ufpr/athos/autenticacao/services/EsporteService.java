package br.ufpr.athos.autenticacao.services;

import br.ufpr.athos.autenticacao.dto.EsporteResponseDTO;
import br.ufpr.athos.autenticacao.model.Esporte;
import br.ufpr.athos.autenticacao.repository.EsporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EsporteService {

    @Autowired
    private EsporteRepository repository;

    public List<EsporteResponseDTO> listarTodos() {
        return repository.findAll()
            .stream()
            .map(e -> new EsporteResponseDTO(e.getId(), e.getNome()))
            .collect(Collectors.toList());
    }

    public boolean validarEsporte(String nome) {
        return repository.existsByNomeIgnoreCase(nome);
    }

    public EsporteResponseDTO buscarPorNome(String nome) {
        return repository.findByNomeIgnoreCase(nome)
            .map(e -> new EsporteResponseDTO(e.getId(), e.getNome()))
            .orElse(null);
    }
}
