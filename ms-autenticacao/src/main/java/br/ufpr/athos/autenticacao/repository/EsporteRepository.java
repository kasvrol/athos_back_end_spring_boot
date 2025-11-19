package br.ufpr.athos.autenticacao.repository;

import br.ufpr.athos.autenticacao.model.Esporte;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EsporteRepository extends MongoRepository<Esporte, String> {
    Optional<Esporte> findByNomeIgnoreCase(String nome);
    boolean existsByNomeIgnoreCase(String nome);
}
