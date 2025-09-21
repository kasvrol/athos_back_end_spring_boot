package br.ufpr.athos.evento.repository;

import br.ufpr.athos.evento.model.ParticipacaoEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipacaoEventoRepository extends JpaRepository<ParticipacaoEvento, String> {

    // Buscar participação específica de usuário em evento
    Optional<ParticipacaoEvento> findByUsuarioIdAndEventoId(String usuarioId, String eventoId);

    // Verificar se usuário já está inscrito em evento
    boolean existsByUsuarioIdAndEventoId(String usuarioId, String eventoId);

    // Buscar todas as participações de um usuário
    List<ParticipacaoEvento> findByUsuarioId(String usuarioId);

    // Buscar todos os participantes de um evento
    List<ParticipacaoEvento> findByEventoId(String eventoId);

    // Buscar participações confirmadas de um evento
    List<ParticipacaoEvento> findByEventoIdAndConfirmadoTrue(String eventoId);

    // Buscar participações pagas de um evento
    List<ParticipacaoEvento> findByEventoIdAndPagoTrue(String eventoId);

    // Buscar participações pendentes de confirmação
    List<ParticipacaoEvento> findByEventoIdAndConfirmadoFalse(String eventoId);

    // Contar total de participantes de um evento
    @Query("SELECT COUNT(p) FROM ParticipacaoEvento p WHERE p.eventoId = :eventoId")
    Long countParticipantesByEvento(@Param("eventoId") String eventoId);

    // Contar participantes confirmados de um evento
    @Query("SELECT COUNT(p) FROM ParticipacaoEvento p WHERE p.eventoId = :eventoId AND p.confirmado = true")
    Long countParticipantesConfirmadosByEvento(@Param("eventoId") String eventoId);

    // Contar participantes que pagaram
    @Query("SELECT COUNT(p) FROM ParticipacaoEvento p WHERE p.eventoId = :eventoId AND p.pago = true")
    Long countParticipantesPagosByEvento(@Param("eventoId") String eventoId);

    // Buscar eventos que o usuário participa (confirmados)
    @Query("SELECT p FROM ParticipacaoEvento p JOIN FETCH p.evento e WHERE p.usuarioId = :usuarioId AND p.confirmado = true ORDER BY e.data ASC")
    List<ParticipacaoEvento> findEventosConfirmadosDoUsuario(@Param("usuarioId") String usuarioId);

    // Buscar eventos futuros que o usuário participa
    @Query("SELECT p FROM ParticipacaoEvento p JOIN FETCH p.evento e WHERE p.usuarioId = :usuarioId AND p.confirmado = true AND e.data >= CURRENT_DATE ORDER BY e.data ASC")
    List<ParticipacaoEvento> findEventosFuturosDoUsuario(@Param("usuarioId") String usuarioId);

    // Buscar eventos passados que o usuário participou
    @Query("SELECT p FROM ParticipacaoEvento p JOIN FETCH p.evento e WHERE p.usuarioId = :usuarioId AND p.confirmado = true AND e.data < CURRENT_DATE ORDER BY e.data DESC")
    List<ParticipacaoEvento> findEventosPassadosDoUsuario(@Param("usuarioId") String usuarioId);

    // Remover participação específica
    void deleteByUsuarioIdAndEventoId(String usuarioId, String eventoId);

    // Buscar participações por evento com dados do evento
    @Query("SELECT p FROM ParticipacaoEvento p JOIN FETCH p.evento WHERE p.eventoId = :eventoId")
    List<ParticipacaoEvento> findByEventoIdWithEvento(@Param("eventoId") String eventoId);

    // Verificar se usuário pode avaliar outros participantes (se participou e evento já passou)
    @Query("SELECT COUNT(p) > 0 FROM ParticipacaoEvento p JOIN p.evento e " +
           "WHERE p.usuarioId = :usuarioId AND p.eventoId = :eventoId " +
           "AND p.confirmado = true AND e.data < CURRENT_DATE")
    boolean podeAvaliarParticipantes(@Param("usuarioId") String usuarioId, @Param("eventoId") String eventoId);
}