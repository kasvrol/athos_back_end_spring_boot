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

    public static final String EXCHANGE_NAME = "campeonato.events";
    public static final String ROUTING_KEY_CAMPEONATO_CRIADO = "campeonato.criado";
    public static final String ROUTING_KEY_CAMPEONATO_STATUS = "campeonato.status.alterado";
    public static final String ROUTING_KEY_EQUIPE_INSCRITA = "campeonato.equipe.inscrita";
    public static final String ROUTING_KEY_PARTIDAS_GERADAS = "campeonato.partidas.geradas";
    public static final String ROUTING_KEY_PARTIDA_FINALIZADA = "campeonato.partida.finalizada";

    @Bean
    public TopicExchange campeonatoExchange() {
        return new TopicExchange(EXCHANGE_NAME);
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
