package br.ufpr.athos.notificacao.listener;

import br.ufpr.athos.notificacao.config.RabbitMQConfig;
import br.ufpr.athos.notificacao.event.PagamentoEvent;
import br.ufpr.athos.notificacao.model.Notificacao;
import br.ufpr.athos.notificacao.service.NotificacaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PagamentoListener {

    private static final Logger logger = LoggerFactory.getLogger(PagamentoListener.class);

    @Autowired
    private NotificacaoService notificacaoService;

    @RabbitListener(queues = RabbitMQConfig.NOTIFICACAO_PAGAMENTO_QUEUE)
    public void processarPagamentoEvent(PagamentoEvent event) {
        logger.info("Recebido evento de pagamento: {} para usuário {}",
                    event.getStatus(), event.getUsuarioId());

        try {
            String titulo = "";
            String mensagem = "";
            Notificacao.TipoNotificacao tipo = null;

            switch (event.getStatus()) {
                case "CONFIRMADO":
                    titulo = "Pagamento confirmado!";
                    mensagem = String.format("Seu pagamento de R$ %.2f para o evento '%s' foi confirmado com sucesso.",
                                           event.getValor(), event.getEventoNome());
                    tipo = Notificacao.TipoNotificacao.PAGAMENTO_CONFIRMADO;
                    break;

                case "PENDENTE":
                    titulo = "Pagamento pendente";
                    mensagem = String.format("Seu pagamento de R$ %.2f para o evento '%s' está pendente de confirmação.",
                                           event.getValor(), event.getEventoNome());
                    tipo = Notificacao.TipoNotificacao.PAGAMENTO_PENDENTE;
                    break;

                default:
                    logger.warn("Status de pagamento desconhecido: {}", event.getStatus());
                    return;
            }

            notificacaoService.criarNotificacao(
                event.getUsuarioId(),
                titulo,
                mensagem,
                tipo,
                event.getEventoId()
            );

            logger.info("Notificação de pagamento criada para usuário {}", event.getUsuarioId());

        } catch (Exception e) {
            logger.error("Erro ao processar evento de pagamento: {}", e.getMessage(), e);
        }
    }
}
