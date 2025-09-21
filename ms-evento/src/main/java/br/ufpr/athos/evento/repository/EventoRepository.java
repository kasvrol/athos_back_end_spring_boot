package br.ufpr.athos.evento.repository;

import br.ufpr.athos.evento.model.Evento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventoRepository extends JpaRepository<Evento, String> {

    // Buscar eventos por criador
    List<Evento> findByCriadorId(String criadorId);

    // Buscar eventos por esporte
    List<Evento> findByEsporte(String esporte);

    // Buscar eventos públicos
    List<Evento> findByPublicoTrue();

    // Buscar eventos privados
    List<Evento> findByPublicoFalse();

    // Buscar eventos futuros
    @Query("SELECT e FROM Evento e WHERE e.data >= :dataAtual ORDER BY e.data ASC, e.horario ASC")
    List<Evento> findEventosFuturos(@Param("dataAtual") LocalDate dataAtual);

    // Buscar eventos passados
    @Query("SELECT e FROM Evento e WHERE e.data < :dataAtual ORDER BY e.data DESC")
    List<Evento> findEventosPassados(@Param("dataAtual") LocalDate dataAtual);

    // Buscar eventos por faixa de data
    @Query("SELECT e FROM Evento e WHERE e.data BETWEEN :dataInicio AND :dataFim ORDER BY e.data ASC, e.horario ASC")
    List<Evento> findEventosPorPeriodo(@Param("dataInicio") LocalDate dataInicio,
                                       @Param("dataFim") LocalDate dataFim);

    // Buscar eventos por esporte e data futura (para filtros)
    @Query("SELECT e FROM Evento e WHERE e.esporte = :esporte AND e.data >= :dataAtual ORDER BY e.data ASC")
    List<Evento> findByEsporteAndDataFutura(@Param("esporte") String esporte,
                                            @Param("dataAtual") LocalDate dataAtual);

    // Buscar eventos por CEP (proximidade)
    List<Evento> findByCepContaining(String cep);

    // Buscar eventos públicos futuros com paginação
    @Query("SELECT e FROM Evento e WHERE e.publico = true AND e.data >= :dataAtual ORDER BY e.data ASC, e.horario ASC")
    Page<Evento> findEventosPublicosFuturos(@Param("dataAtual") LocalDate dataAtual, Pageable pageable);

    // Buscar eventos com filtros combinados
    @Query("SELECT e FROM Evento e WHERE " +
           "(:esporte IS NULL OR e.esporte = :esporte) AND " +
           "(:publico IS NULL OR e.publico = :publico) AND " +
           "(:dataInicio IS NULL OR e.data >= :dataInicio) AND " +
           "(:dataFim IS NULL OR e.data <= :dataFim) AND " +
           "(:cep IS NULL OR e.cep LIKE %:cep%) " +
           "ORDER BY e.data ASC, e.horario ASC")
    Page<Evento> findEventosComFiltros(@Param("esporte") String esporte,
                                       @Param("publico") Boolean publico,
                                       @Param("dataInicio") LocalDate dataInicio,
                                       @Param("dataFim") LocalDate dataFim,
                                       @Param("cep") String cep,
                                       Pageable pageable);

    // Buscar eventos que o usuário pode ver (públicos + privados que ele criou)
    @Query("SELECT e FROM Evento e WHERE e.publico = true OR e.criadorId = :usuarioId ORDER BY e.data ASC")
    List<Evento> findEventosVisivelParaUsuario(@Param("usuarioId") String usuarioId);

    // Verificar se usuário é criador do evento
    @Query("SELECT COUNT(e) > 0 FROM Evento e WHERE e.id = :eventoId AND e.criadorId = :usuarioId")
    boolean isUsuarioCriadorDoEvento(@Param("eventoId") String eventoId, @Param("usuarioId") String usuarioId);

    // Buscar eventos por nome (busca parcial)
    @Query("SELECT e FROM Evento e WHERE LOWER(e.nome) LIKE LOWER(CONCAT('%', :nome, '%')) ORDER BY e.data ASC")
    List<Evento> findByNomeContainingIgnoreCase(@Param("nome") String nome);

    // Contar eventos criados por usuário
    @Query("SELECT COUNT(e) FROM Evento e WHERE e.criadorId = :usuarioId")
    Long countEventosByCriador(@Param("usuarioId") String usuarioId);

    // Buscar eventos próximos (para notificações)
    @Query("SELECT e FROM Evento e WHERE e.data = :data")
    List<Evento> findEventosNaData(@Param("data") LocalDate data);
}