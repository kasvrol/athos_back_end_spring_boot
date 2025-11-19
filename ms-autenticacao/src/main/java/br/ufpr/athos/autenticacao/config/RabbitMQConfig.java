package br.ufpr.athos.autenticacao.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do RabbitMQ para o microsserviço de autenticação
 * Define exchanges, filas e bindings para comunicação com outros microserviços
 */
@Configuration
public class RabbitMQConfig {

    // Exchange para eventos de usuário
    public static final String USUARIO_EXCHANGE = "usuario.exchange";

    // Fila para receber consultas de usuário de outros microserviços
    public static final String USUARIO_CONSULTA_QUEUE = "campeonato.usuario.consulta";

    // Routing keys
    public static final String USUARIO_CONSULTAR_KEY = "usuario.consultar";
    public static final String USUARIO_RESPOSTA_KEY = "usuario.resposta.campeonato";

    /**
     * Exchange principal para eventos de usuário
     */
    @Bean
    public TopicExchange usuarioExchange() {
        return new TopicExchange(USUARIO_EXCHANGE);
    }

    /**
     * Fila para receber requisições de consulta de usuário
     */
    @Bean
    public Queue usuarioConsultaQueue() {
        return new Queue(USUARIO_CONSULTA_QUEUE, true);
    }

    /**
     * Binding: conecta a fila de consulta ao exchange
     */
    @Bean
    public Binding usuarioConsultaBinding(Queue usuarioConsultaQueue, TopicExchange usuarioExchange) {
        return BindingBuilder
                .bind(usuarioConsultaQueue)
                .to(usuarioExchange)
                .with(USUARIO_CONSULTAR_KEY);
    }

    /**
     * Conversor de mensagens para JSON
     */
    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Template do RabbitMQ configurado com conversor JSON
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
