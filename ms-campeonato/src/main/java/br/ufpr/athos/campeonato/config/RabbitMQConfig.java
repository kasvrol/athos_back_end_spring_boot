package br.ufpr.athos.campeonato.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Campeonato events exchange and routing keys
    public static final String EXCHANGE_NAME = "campeonato.events";
    public static final String ROUTING_KEY_CAMPEONATO_CRIADO = "campeonato.criado";
    public static final String ROUTING_KEY_CAMPEONATO_STATUS = "campeonato.status.alterado";
    public static final String ROUTING_KEY_EQUIPE_INSCRITA = "campeonato.equipe.inscrita";
    public static final String ROUTING_KEY_PARTIDAS_GERADAS = "campeonato.partidas.geradas";
    public static final String ROUTING_KEY_PARTIDA_FINALIZADA = "campeonato.partida.finalizada";

    // Usuario exchange and queue configuration for user lookup
    public static final String USUARIO_EXCHANGE = "usuario.exchange";
    public static final String USUARIO_CONSULTA_QUEUE = "campeonato.usuario.consulta";
    public static final String USUARIO_CONSULTA_ROUTING_KEY = "usuario.consultar";
    public static final String USUARIO_RESPONSE_QUEUE = "campeonato.usuario.response";
    public static final String USUARIO_RESPONSE_ROUTING_KEY = "usuario.resposta.campeonato";

    @Bean
    public TopicExchange campeonatoExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public TopicExchange usuarioExchange() {
        return new TopicExchange(USUARIO_EXCHANGE);
    }

    @Bean
    public Queue usuarioConsultaQueue() {
        return new Queue(USUARIO_CONSULTA_QUEUE, true);
    }

    @Bean
    public Queue usuarioResponseQueue() {
        return new Queue(USUARIO_RESPONSE_QUEUE, true);
    }

    @Bean
    public Binding usuarioConsultaBinding() {
        return BindingBuilder
                .bind(usuarioConsultaQueue())
                .to(usuarioExchange())
                .with(USUARIO_CONSULTA_ROUTING_KEY);
    }

    @Bean
    public Binding usuarioResponseBinding() {
        return BindingBuilder
                .bind(usuarioResponseQueue())
                .to(usuarioExchange())
                .with(USUARIO_RESPONSE_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
