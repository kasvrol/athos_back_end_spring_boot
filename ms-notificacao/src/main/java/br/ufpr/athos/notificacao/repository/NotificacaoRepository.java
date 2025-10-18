package br.ufpr.athos.notificacao.repository;

import br.ufpr.athos.notificacao.model.Notificacao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacaoRepository extends MongoRepository<Notificacao, String> {

    List<Notificacao> findByUsuarioIdOrderByDataCriacaoDesc(String usuarioId);

    List<Notificacao> findByUsuarioIdAndLidaOrderByDataCriacaoDesc(String usuarioId, Boolean lida);

    Long countByUsuarioIdAndLida(String usuarioId, Boolean lida);
}
