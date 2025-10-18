package br.ufpr.athos.notificacao.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "notificacoes")
public class Notificacao {

    @Id
    private String id;

    private String usuarioId;
    private String titulo;
    private String mensagem;
    private TipoNotificacao tipo;
    private Boolean lida;
    private LocalDateTime dataCriacao;
    private String eventoRelacionadoId;  // ID do evento, campeonato, etc. que gerou a notificação

    public Notificacao() {
        this.lida = false;
        this.dataCriacao = LocalDateTime.now();
    }

    public Notificacao(String usuarioId, String titulo, String mensagem, TipoNotificacao tipo) {
        this();
        this.usuarioId = usuarioId;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.tipo = tipo;
    }

    public Notificacao(String usuarioId, String titulo, String mensagem, TipoNotificacao tipo, String eventoRelacionadoId) {
        this(usuarioId, titulo, mensagem, tipo);
        this.eventoRelacionadoId = eventoRelacionadoId;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public TipoNotificacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoNotificacao tipo) {
        this.tipo = tipo;
    }

    public Boolean getLida() {
        return lida;
    }

    public void setLida(Boolean lida) {
        this.lida = lida;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getEventoRelacionadoId() {
        return eventoRelacionadoId;
    }

    public void setEventoRelacionadoId(String eventoRelacionadoId) {
        this.eventoRelacionadoId = eventoRelacionadoId;
    }

    public enum TipoNotificacao {
        INSCRICAO_CONFIRMADA,
        INSCRICAO_CANCELADA,
        VAGA_DISPONIVEL,
        EVENTO_CANCELADO,
        EVENTO_ATUALIZADO,
        PAGAMENTO_CONFIRMADO,
        PAGAMENTO_PENDENTE,
        AVALIACAO_RECEBIDA,
        CAMPEONATO_INICIO,
        PARTIDA_AGENDADA,
        RECOMENDACAO
    }
}
