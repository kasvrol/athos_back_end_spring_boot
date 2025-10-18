package br.ufpr.athos.notificacao.listener;

import br.ufpr.athos.notificacao.config.RabbitMQConfig;
import br.ufpr.athos.notificacao.event.EventoEvent;
import br.ufpr.athos.notificacao.model.Notificacao;
import br.ufpr.athos.notificacao.service.NotificacaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventoListener {

    private static final Logger logger = LoggerFactory.getLogger(EventoListener.class);

    @Autowired
    private NotificacaoService notificacaoService;

    @RabbitListener(queues = RabbitMQConfig.NOTIFICACAO_EVENTO_QUEUE)
    public void processarEventoEvent(EventoEvent event) {
        logger.info("Recebido evento: {} para evento {} e usuário {}",
                    event.getTipo(), event.getEventoId(), event.getUsuarioId());

        try {
            String titulo = "";
            String mensagem = "";
            Notificacao.TipoNotificacao tipo = null;

            switch (event.getTipo()) {
                case "INSCRICAO_CONFIRMADA":
                    titulo = "Inscrição confirmada!";
                    mensagem = String.format("Sua inscrição no evento '%s' foi confirmada com sucesso.", event.getEventoNome());
                    tipo = Notificacao.TipoNotificacao.INSCRICAO_CONFIRMADA;
                    break;

                case "INSCRICAO_CANCELADA":
                    titulo = "Inscrição cancelada";
                    mensagem = String.format("Sua inscrição no evento '%s' foi cancelada.", event.getEventoNome());
                    tipo = Notificacao.TipoNotificacao.INSCRICAO_CANCELADA;
                    break;

                case "VAGA_DISPONIVEL":
                    titulo = "Nova vaga disponível!";
                    mensagem = String.format("Uma nova vaga está disponível no evento '%s'.", event.getEventoNome());
                    tipo = Notificacao.TipoNotificacao.VAGA_DISPONIVEL;
                    break;

                case "EVENTO_CANCELADO":
                    titulo = "Evento cancelado";
                    mensagem = String.format("O evento '%s' foi cancelado. %s",
                                           event.getEventoNome(),
                                           event.getMensagemAdicional() != null ? event.getMensagemAdicional() : "");
                    tipo = Notificacao.TipoNotificacao.EVENTO_CANCELADO;
                    break;

                case "EVENTO_ATUALIZADO":
                    titulo = "Evento atualizado";
                    mensagem = String.format("O evento '%s' foi atualizado. %s",
                                           event.getEventoNome(),
                                           event.getMensagemAdicional() != null ? event.getMensagemAdicional() : "Confira as mudanças!");
                    tipo = Notificacao.TipoNotificacao.EVENTO_ATUALIZADO;
                    break;

                default:
                    logger.warn("Tipo de evento desconhecido: {}", event.getTipo());
                    return;
            }

            notificacaoService.criarNotificacao(
                event.getUsuarioId(),
                titulo,
                mensagem,
                tipo,
                event.getEventoId()
            );

            logger.info("Notificação criada com sucesso para usuário {}", event.getUsuarioId());

        } catch (Exception e) {
            logger.error("Erro ao processar evento: {}", e.getMessage(), e);
        }
    }
}
