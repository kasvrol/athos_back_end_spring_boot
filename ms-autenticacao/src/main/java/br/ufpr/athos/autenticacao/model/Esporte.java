package br.ufpr.athos.autenticacao.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

@Document(collection = "esportes")
public class Esporte {

    @Id
    private String id;

    @Indexed(unique = true)
    private String nome;

    public Esporte() {}

    public Esporte(String nome) {
        this.nome = nome;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
