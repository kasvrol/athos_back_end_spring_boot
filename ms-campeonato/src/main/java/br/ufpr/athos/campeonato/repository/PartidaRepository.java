package br.ufpr.athos.campeonato.repository;

import br.ufpr.athos.campeonato.model.Partida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, String> {

    List<Partida> findByCampeonatoIdOrderByDataHoraAsc(String campeonatoId);

    List<Partida> findByEquipe1IdOrEquipe2IdOrderByDataHoraAsc(String equipe1Id, String equipe2Id);

    List<Partida> findByCampeonatoIdAndFaseOrderByRodada(String campeonatoId, String fase);
}
