package br.ufpr.athos.campeonato.repository;

import br.ufpr.athos.campeonato.model.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipeRepository extends JpaRepository<Equipe, String> {

    List<Equipe> findByCampeonatoIdOrderByNome(String campeonatoId);

    List<Equipe> findByCapitaoIdOrderByDataCriacaoDesc(String capitaoId);
}
