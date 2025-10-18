package br.ufpr.athos.notificacao.listener;

import br.ufpr.athos.notificacao.config.RabbitMQConfig;
import br.ufpr.athos.notificacao.event.AvaliacaoEvent;
import br.ufpr.athos.notificacao.model.Notificacao;
import br.ufpr.athos.notificacao.service.NotificacaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AvaliacaoListener {

    private static final Logger logger = LoggerFactory.getLogger(AvaliacaoListener.class);

    @Autowired
    private NotificacaoService notificacaoService;

    @RabbitListener(queues = RabbitMQConfig.NOTIFICACAO_AVALIACAO_QUEUE)
    public void processarAvaliacaoEvent(AvaliacaoEvent event) {
        logger.info("Recebida avaliação de {} para {}",
                    event.getAvaliadorNome(), event.getAvaliadoId());

        try {
            String titulo = "Você recebeu uma avaliação!";
            String mensagem = String.format("%s avaliou você com %d estrelas no evento '%s'.",
                                          event.getAvaliadorNome(), event.getNota(), event.getEventoNome());

            notificacaoService.criarNotificacao(
                event.getAvaliadoId(),
                titulo,
                mensagem,
                Notificacao.TipoNotificacao.AVALIACAO_RECEBIDA,
                event.getEventoId()
            );

            logger.info("Notificação de avaliação criada para usuário {}", event.getAvaliadoId());

        } catch (Exception e) {
            logger.error("Erro ao processar evento de avaliação: {}", e.getMessage(), e);
        }
    }
}
