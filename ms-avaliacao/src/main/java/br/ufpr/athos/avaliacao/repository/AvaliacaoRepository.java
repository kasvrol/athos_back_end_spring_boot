package br.ufpr.athos.avaliacao.repository;

import br.ufpr.athos.avaliacao.model.Avaliacao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AvaliacaoRepository extends MongoRepository<Avaliacao, String> {

    List<Avaliacao> findByAvaliadoIdOrderByDataCriacaoDesc(String avaliadoId);

    List<Avaliacao> findByAvaliadorIdOrderByDataCriacaoDesc(String avaliadorId);

    List<Avaliacao> findByEventoIdOrderByDataCriacaoDesc(String eventoId);

    Optional<Avaliacao> findByAvaliadorIdAndAvaliadoIdAndEventoId(String avaliadorId, String avaliadoId, String eventoId);

    @Query("{'avaliadoId': ?0}")
    List<Avaliacao> findAllByAvaliadoId(String avaliadoId);

    Long countByAvaliadoId(String avaliadoId);
}
