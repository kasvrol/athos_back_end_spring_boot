package br.ufpr.athos.campeonato.repository;

import br.ufpr.athos.campeonato.model.Campeonato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampeonatoRepository extends JpaRepository<Campeonato, String> {

    List<Campeonato> findByOrganizadorIdOrderByDataCriacaoDesc(String organizadorId);

    List<Campeonato> findByEsporteOrderByDataCriacaoDesc(String esporte);

    List<Campeonato> findByStatusOrderByDataCriacaoDesc(Campeonato.StatusCampeonato status);
}
