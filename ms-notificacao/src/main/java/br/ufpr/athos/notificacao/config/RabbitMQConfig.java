package br.ufpr.athos.notificacao.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Exchanges
    public static final String EVENTO_EXCHANGE = "evento.exchange";
    public static final String PAGAMENTO_EXCHANGE = "pagamento.exchange";
    public static final String CAMPEONATO_EXCHANGE = "campeonato.exchange";
    public static final String AVALIACAO_EXCHANGE = "avaliacao.exchange";

    // Queues
    public static final String NOTIFICACAO_EVENTO_QUEUE = "notificacao.evento.queue";
    public static final String NOTIFICACAO_PAGAMENTO_QUEUE = "notificacao.pagamento.queue";
    public static final String NOTIFICACAO_CAMPEONATO_QUEUE = "notificacao.campeonato.queue";
    public static final String NOTIFICACAO_AVALIACAO_QUEUE = "notificacao.avaliacao.queue";

    // Routing Keys
    public static final String EVENTO_CRIADO_KEY = "evento.criado";
    public static final String EVENTO_ATUALIZADO_KEY = "evento.atualizado";
    public static final String EVENTO_CANCELADO_KEY = "evento.cancelado";
    public static final String INSCRICAO_CONFIRMADA_KEY = "inscricao.confirmada";
    public static final String INSCRICAO_CANCELADA_KEY = "inscricao.cancelada";
    public static final String VAGA_DISPONIVEL_KEY = "vaga.disponivel";

    public static final String PAGAMENTO_CONFIRMADO_KEY = "pagamento.confirmado";
    public static final String PAGAMENTO_PENDENTE_KEY = "pagamento.pendente";

    public static final String CAMPEONATO_INICIO_KEY = "campeonato.inicio";
    public static final String PARTIDA_AGENDADA_KEY = "partida.agendada";

    public static final String AVALIACAO_RECEBIDA_KEY = "avaliacao.recebida";

    // Message Converter
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    // ========== EVENTO EXCHANGE E QUEUES ==========
    @Bean
    public TopicExchange eventoExchange() {
        return new TopicExchange(EVENTO_EXCHANGE);
    }

    @Bean
    public Queue notificacaoEventoQueue() {
        return new Queue(NOTIFICACAO_EVENTO_QUEUE, true);
    }

    @Bean
    public Binding eventoBinding(Queue notificacaoEventoQueue, TopicExchange eventoExchange) {
        return BindingBuilder.bind(notificacaoEventoQueue)
                .to(eventoExchange)
                .with("evento.*");
    }

    @Bean
    public Binding inscricaoBinding(Queue notificacaoEventoQueue, TopicExchange eventoExchange) {
        return BindingBuilder.bind(notificacaoEventoQueue)
                .to(eventoExchange)
                .with("inscricao.*");
    }

    @Bean
    public Binding vagaBinding(Queue notificacaoEventoQueue, TopicExchange eventoExchange) {
        return BindingBuilder.bind(notificacaoEventoQueue)
                .to(eventoExchange)
                .with("vaga.*");
    }

    // ========== PAGAMENTO EXCHANGE E QUEUES ==========
    @Bean
    public TopicExchange pagamentoExchange() {
        return new TopicExchange(PAGAMENTO_EXCHANGE);
    }

    @Bean
    public Queue notificacaoPagamentoQueue() {
        return new Queue(NOTIFICACAO_PAGAMENTO_QUEUE, true);
    }

    @Bean
    public Binding pagamentoBinding(Queue notificacaoPagamentoQueue, TopicExchange pagamentoExchange) {
        return BindingBuilder.bind(notificacaoPagamentoQueue)
                .to(pagamentoExchange)
                .with("pagamento.*");
    }

    // ========== CAMPEONATO EXCHANGE E QUEUES ==========
    @Bean
    public TopicExchange campeonatoExchange() {
        return new TopicExchange(CAMPEONATO_EXCHANGE);
    }

    @Bean
    public Queue notificacaoCampeonatoQueue() {
        return new Queue(NOTIFICACAO_CAMPEONATO_QUEUE, true);
    }

    @Bean
    public Binding campeonatoBinding(Queue notificacaoCampeonatoQueue, TopicExchange campeonatoExchange) {
        return BindingBuilder.bind(notificacaoCampeonatoQueue)
                .to(campeonatoExchange)
                .with("campeonato.*");
    }

    @Bean
    public Binding partidaBinding(Queue notificacaoCampeonatoQueue, TopicExchange campeonatoExchange) {
        return BindingBuilder.bind(notificacaoCampeonatoQueue)
                .to(campeonatoExchange)
                .with("partida.*");
    }

    // ========== AVALIACAO EXCHANGE E QUEUES ==========
    @Bean
    public TopicExchange avaliacaoExchange() {
        return new TopicExchange(AVALIACAO_EXCHANGE);
    }

    @Bean
    public Queue notificacaoAvaliacaoQueue() {
        return new Queue(NOTIFICACAO_AVALIACAO_QUEUE, true);
    }

    @Bean
    public Binding avaliacaoBinding(Queue notificacaoAvaliacaoQueue, TopicExchange avaliacaoExchange) {
        return BindingBuilder.bind(notificacaoAvaliacaoQueue)
                .to(avaliacaoExchange)
                .with("avaliacao.*");
    }
}
