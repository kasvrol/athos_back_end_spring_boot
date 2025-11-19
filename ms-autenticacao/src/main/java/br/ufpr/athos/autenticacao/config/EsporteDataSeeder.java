package br.ufpr.athos.autenticacao.config;

import br.ufpr.athos.autenticacao.model.Esporte;
import br.ufpr.athos.autenticacao.repository.EsporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class EsporteDataSeeder implements CommandLineRunner {

    @Autowired
    private EsporteRepository repository;

    @Override
    public void run(String... args) throws Exception {
        // Only seed if database is empty
        if (repository.count() == 0) {
            List<String> esportes = Arrays.asList(
                "Futebol",
                "Futsal",
                "Basquete",
                "Vôlei",
                "Vôlei de Praia",
                "Handebol",
                "Tênis",
                "Tênis de Mesa",
                "Badminton",
                "Natação",
                "Atletismo",
                "Ciclismo",
                "Corrida",
                "Caminhada",
                "Skate",
                "Surf",
                "Artes Marciais",
                "Judô",
                "Karatê",
                "Taekwondo",
                "Jiu-Jitsu",
                "Boxe",
                "Muay Thai",
                "CrossFit",
                "Musculação",
                "Yoga",
                "Pilates",
                "Funcional",
                "Rugby",
                "Beisebol",
                "Softball",
                "Cricket",
                "Golfe",
                "Boliche",
                "Escalada",
                "Outros"
            );

            esportes.forEach(nome -> repository.save(new Esporte(nome)));

            System.out.println("✅ Database seeded with " + esportes.size() + " sports");
        }
    }
}
