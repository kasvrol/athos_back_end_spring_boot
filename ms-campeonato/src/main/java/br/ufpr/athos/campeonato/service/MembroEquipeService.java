package br.ufpr.athos.campeonato.service;

import br.ufpr.athos.campeonato.dto.AdicionarMembroDTO;
import br.ufpr.athos.campeonato.dto.MembroEquipeResponseDTO;
import br.ufpr.athos.campeonato.exception.BusinessRuleException;
import br.ufpr.athos.campeonato.exception.ResourceNotFoundException;
import br.ufpr.athos.campeonato.exception.ValidationException;
import br.ufpr.athos.campeonato.model.Equipe;
import br.ufpr.athos.campeonato.model.MembroEquipe;
import br.ufpr.athos.campeonato.repository.EquipeRepository;
import br.ufpr.athos.campeonato.repository.MembroEquipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MembroEquipeService {

    @Autowired
    private MembroEquipeRepository membroRepository;

    @Autowired
    private EquipeRepository equipeRepository;

    /**
     * Adds a member to a team
     * Validations:
     * - Team must exist
     * - User cannot already be in this team
     */
    @Transactional
    public MembroEquipeResponseDTO adicionarMembro(String equipeId, AdicionarMembroDTO dto) {
        // 1. Validate team exists
        Equipe equipe = equipeRepository.findById(equipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipe", equipeId));

        // 2. Check if user is already in team
        if (membroRepository.existsByEquipeIdAndUsuarioId(equipeId, dto.getUsuarioId())) {
            throw new BusinessRuleException("Usuário já é membro desta equipe");
        }

        // 3. Create MembroEquipe entity
        MembroEquipe membro = new MembroEquipe();

        // 4. Set equipeId, usuarioId, dataEntrada = LocalDateTime.now()
        membro.setEquipe(equipe);
        membro.setUsuarioId(dto.getUsuarioId());
        membro.setDataEntrada(LocalDateTime.now());

        // 5. Save to repository
        MembroEquipe savedMembro = membroRepository.save(membro);

        // 6. Convert to DTO and return
        return converterParaDTO(savedMembro);
    }

    /**
     * Lists all members of a team
     */
    public List<MembroEquipeResponseDTO> listarMembros(String equipeId) {
        // 1. Get all MembroEquipe by equipeId
        List<MembroEquipe> membros = membroRepository.findByEquipeId(equipeId);

        // 2. Convert each to DTO
        // 3. For now, set usuarioNome = usuarioId (will be replaced with FeignClient later)
        return membros.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Removes a member from a team
     * Note: Cannot remove the captain
     */
    @Transactional
    public void removerMembro(String equipeId, String membroId) {
        // 1. Find MembroEquipe by id
        MembroEquipe membro = membroRepository.findById(membroId)
                .orElseThrow(() -> new ResourceNotFoundException("Membro", membroId));

        // 2. Validate it belongs to the specified team
        if (!membro.getEquipe().getId().equals(equipeId)) {
            throw new ValidationException("Membro não pertence a esta equipe");
        }

        // 3. Get the team and check if member.usuarioId == equipe.capitaoId
        Equipe equipe = membro.getEquipe();

        // 4. If is captain, throw exception
        if (membro.getUsuarioId().equals(equipe.getCapitaoId())) {
            throw new BusinessRuleException("Não é possível remover o capitão da equipe");
        }

        // 5. Delete the member
        membroRepository.delete(membro);
    }

    /**
     * Helper method to convert entity to DTO
     */
    private MembroEquipeResponseDTO converterParaDTO(MembroEquipe membro) {
        MembroEquipeResponseDTO dto = new MembroEquipeResponseDTO();

        // Map all fields
        dto.setId(membro.getId());
        dto.setUsuarioId(membro.getUsuarioId());

        // Set usuarioNome = usuarioId for now (placeholder)
        dto.setUsuarioNome(membro.getUsuarioId());

        // Get equipe info
        dto.setEquipeId(membro.getEquipe().getId());

        // Get equipe nome from membro.getEquipe().getNome()
        dto.setEquipeNome(membro.getEquipe().getNome());

        dto.setDataEntrada(membro.getDataEntrada());

        return dto;
    }
}
