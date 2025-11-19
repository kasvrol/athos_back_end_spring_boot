package br.ufpr.athos.campeonato.repository;

import br.ufpr.athos.campeonato.model.MembroEquipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MembroEquipeRepository extends JpaRepository<MembroEquipe, String> {

    List<MembroEquipe> findByEquipeId(String equipeId);

    boolean existsByEquipeIdAndUsuarioId(String equipeId, String usuarioId);

    void deleteByEquipeIdAndUsuarioId(String equipeId, String usuarioId);
}
