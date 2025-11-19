package br.ufpr.athos.campeonato.service;

import br.ufpr.athos.campeonato.dto.ClassificacaoDTO;
import br.ufpr.athos.campeonato.dto.EstatisticasDTO;
import br.ufpr.athos.campeonato.model.Equipe;
import br.ufpr.athos.campeonato.model.Partida;
import br.ufpr.athos.campeonato.repository.EquipeRepository;
import br.ufpr.athos.campeonato.repository.PartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClassificacaoService {

    @Autowired
    private PartidaRepository partidaRepository;

    @Autowired
    private EquipeRepository equipeRepository;

    /**
     * Calculates the standings table for a championship
     * Tie-breaking criteria:
     * 1. Most points
     * 2. Goal difference (GF - GA)
     * 3. Goals scored (GF)
     * 4. Alphabetical order
     */
    public List<ClassificacaoDTO> calcularClassificacao(String campeonatoId) {
        // 1. Get all teams from championship
        List<Equipe> equipes = equipeRepository.findByCampeonatoIdOrderByNome(campeonatoId);

        if (equipes.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. Get all FINALIZED matches
        List<Partida> partidas = partidaRepository.findByCampeonatoIdOrderByDataHoraAsc(campeonatoId);
        List<Partida> partidasFinalizadas = partidas.stream()
                .filter(p -> p.getStatus() == Partida.StatusPartida.FINALIZADA)
                .collect(Collectors.toList());

        // 3. Create a Map to store stats per team - initialize with zeros
        Map<String, ClassificacaoDTO> classificacaoMap = new HashMap<>();

        for (Equipe equipe : equipes) {
            ClassificacaoDTO dto = new ClassificacaoDTO();
            dto.setEquipeId(equipe.getId());
            dto.setEquipeNome(equipe.getNome());
            dto.setJogos(0);
            dto.setVitorias(0);
            dto.setEmpates(0);
            dto.setDerrotas(0);
            dto.setGolsFeitos(0);
            dto.setGolsSofridos(0);
            dto.setSaldoGols(0);
            dto.setPontos(0);
            classificacaoMap.put(equipe.getId(), dto);
        }

        // 4. Loop through finalized matches and update statistics
        for (Partida partida : partidasFinalizadas) {
            String equipe1Id = partida.getEquipe1().getId();
            String equipe2Id = partida.getEquipe2().getId();

            Integer placarEquipe1 = partida.getPlacarEquipe1() != null ? partida.getPlacarEquipe1() : 0;
            Integer placarEquipe2 = partida.getPlacarEquipe2() != null ? partida.getPlacarEquipe2() : 0;

            ClassificacaoDTO stats1 = classificacaoMap.get(equipe1Id);
            ClassificacaoDTO stats2 = classificacaoMap.get(equipe2Id);

            // Skip if teams not found (shouldn't happen, but for safety)
            if (stats1 == null || stats2 == null) {
                continue;
            }

            // Update games played
            stats1.setJogos(stats1.getJogos() + 1);
            stats2.setJogos(stats2.getJogos() + 1);

            // Update goals
            stats1.setGolsFeitos(stats1.getGolsFeitos() + placarEquipe1);
            stats1.setGolsSofridos(stats1.getGolsSofridos() + placarEquipe2);
            stats2.setGolsFeitos(stats2.getGolsFeitos() + placarEquipe2);
            stats2.setGolsSofridos(stats2.getGolsSofridos() + placarEquipe1);

            // Determine result and update wins/draws/losses
            if (placarEquipe1 > placarEquipe2) {
                // Team 1 wins
                stats1.setVitorias(stats1.getVitorias() + 1);
                stats2.setDerrotas(stats2.getDerrotas() + 1);
            } else if (placarEquipe1 < placarEquipe2) {
                // Team 2 wins
                stats2.setVitorias(stats2.getVitorias() + 1);
                stats1.setDerrotas(stats1.getDerrotas() + 1);
            } else {
                // Draw
                stats1.setEmpates(stats1.getEmpates() + 1);
                stats2.setEmpates(stats2.getEmpates() + 1);
            }

            // Update goal difference and points
            stats1.setSaldoGols(stats1.getGolsFeitos() - stats1.getGolsSofridos());
            stats1.setPontos((stats1.getVitorias() * 3) + stats1.getEmpates());
            stats2.setSaldoGols(stats2.getGolsFeitos() - stats2.getGolsSofridos());
            stats2.setPontos((stats2.getVitorias() * 3) + stats2.getEmpates());
        }

        // 5. Sort by: pontos DESC, saldoGols DESC, golsFeitos DESC, nome ASC
        List<ClassificacaoDTO> classificacaoList = new ArrayList<>(classificacaoMap.values());

        classificacaoList.sort(Comparator
                .comparing(ClassificacaoDTO::getPontos, Comparator.reverseOrder())
                .thenComparing(ClassificacaoDTO::getSaldoGols, Comparator.reverseOrder())
                .thenComparing(ClassificacaoDTO::getGolsFeitos, Comparator.reverseOrder())
                .thenComparing(ClassificacaoDTO::getEquipeNome));

        // 6. Assign positions
        for (int i = 0; i < classificacaoList.size(); i++) {
            classificacaoList.get(i).setPosicao(i + 1);
        }

        return classificacaoList;
    }

    /**
     * Calculates championship statistics
     */
    public EstatisticasDTO calcularEstatisticas(String campeonatoId) {
        EstatisticasDTO estatisticas = new EstatisticasDTO();

        // Count total teams
        List<Equipe> equipes = equipeRepository.findByCampeonatoIdOrderByNome(campeonatoId);
        estatisticas.setTotalEquipes(equipes.size());

        // Get all matches
        List<Partida> todasPartidas = partidaRepository.findByCampeonatoIdOrderByDataHoraAsc(campeonatoId);
        estatisticas.setTotalPartidas(todasPartidas.size());

        // Filter finalized matches
        List<Partida> partidasFinalizadas = todasPartidas.stream()
                .filter(p -> p.getStatus() == Partida.StatusPartida.FINALIZADA)
                .collect(Collectors.toList());
        estatisticas.setPartidasRealizadas(partidasFinalizadas.size());

        // Calculate total goals from finalized matches
        int totalGols = 0;
        for (Partida partida : partidasFinalizadas) {
            Integer placarEquipe1 = partida.getPlacarEquipe1() != null ? partida.getPlacarEquipe1() : 0;
            Integer placarEquipe2 = partida.getPlacarEquipe2() != null ? partida.getPlacarEquipe2() : 0;
            totalGols += placarEquipe1 + placarEquipe2;
        }
        estatisticas.setTotalGols(totalGols);

        // Calculate average goals per match (handle division by zero)
        if (partidasFinalizadas.size() > 0) {
            estatisticas.setMediaGolsPorPartida((double) totalGols / partidasFinalizadas.size());
        } else {
            estatisticas.setMediaGolsPorPartida(0.0);
        }

        // Determine current phase
        String faseAtual = determinarFaseAtual(todasPartidas);
        estatisticas.setFaseAtual(faseAtual);

        return estatisticas;
    }

    /**
     * Helper method to determine the current phase of the championship
     */
    private String determinarFaseAtual(List<Partida> partidas) {
        if (partidas.isEmpty()) {
            return "Sem partidas";
        }

        // Get the most recent match based on status priority
        // Priority: EM_ANDAMENTO > AGENDADA > FINALIZADA
        Optional<Partida> partidaEmAndamento = partidas.stream()
                .filter(p -> p.getStatus() == Partida.StatusPartida.EM_ANDAMENTO)
                .findFirst();

        if (partidaEmAndamento.isPresent()) {
            Partida partida = partidaEmAndamento.get();
            return formatarFase(partida.getFase(), partida.getRodada());
        }

        Optional<Partida> proximaPartida = partidas.stream()
                .filter(p -> p.getStatus() == Partida.StatusPartida.AGENDADA)
                .findFirst();

        if (proximaPartida.isPresent()) {
            Partida partida = proximaPartida.get();
            return formatarFase(partida.getFase(), partida.getRodada());
        }

        // If all matches are completed or no ongoing/scheduled matches, get the last finalized
        Optional<Partida> ultimaPartida = partidas.stream()
                .filter(p -> p.getStatus() == Partida.StatusPartida.FINALIZADA)
                .reduce((first, second) -> second); // Get last element

        if (ultimaPartida.isPresent()) {
            Partida partida = ultimaPartida.get();
            return formatarFase(partida.getFase(), partida.getRodada()) + " (Concluída)";
        }

        return "Fase Inicial";
    }

    /**
     * Helper method to format phase description
     */
    private String formatarFase(String fase, Integer rodada) {
        if (fase != null && !fase.isEmpty()) {
            if (rodada != null) {
                return fase + " - Rodada " + rodada;
            }
            return fase;
        }

        if (rodada != null) {
            return "Rodada " + rodada;
        }

        return "Fase em andamento";
    }
}
