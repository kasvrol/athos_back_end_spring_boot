package br.ufpr.athos.autenticacao.messaging;

import br.ufpr.athos.autenticacao.model.Usuario;
import br.ufpr.athos.autenticacao.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Listener para processar requisições de consulta de usuário via RabbitMQ
 * Responde com informações do usuário (id, nome, email)
 */
@Component
public class UsuarioConsultaListener {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioConsultaListener.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Escuta requisições de consulta de usuário
     * @param event Evento contendo o ID do usuário solicitado
     */
    @RabbitListener(queues = "campeonato.usuario.consulta")
    public void handleUsuarioConsulta(UsuarioConsultaEvent event) {
        logger.info("Recebida requisição de consulta de usuário: {}", event.getUsuarioId());

        try {
            // Buscar usuário no banco de dados
            Usuario usuario = usuarioRepository.findById(event.getUsuarioId()).orElse(null);

            UsuarioResponseEvent response;

            if (usuario != null) {
                // Usuário encontrado - criar resposta com dados
                response = new UsuarioResponseEvent(
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getEmail()
                );
                logger.info("Usuário encontrado: {} ({})", usuario.getNome(), usuario.getId());
            } else {
                // Usuário não encontrado - responder com null/vazio
                response = new UsuarioResponseEvent(event.getUsuarioId(), null, null);
                logger.warn("Usuário não encontrado: {}", event.getUsuarioId());
            }

            // Enviar resposta via RabbitMQ
            rabbitTemplate.convertAndSend(
                "usuario.exchange",
                "usuario.resposta.campeonato",
                response
            );

            logger.info("Resposta enviada para usuário: {}", event.getUsuarioId());

        } catch (Exception e) {
            logger.error("Erro ao processar consulta de usuário: {}", event.getUsuarioId(), e);

            // Enviar resposta de erro
            UsuarioResponseEvent errorResponse = new UsuarioResponseEvent(
                event.getUsuarioId(),
                null,
                null
            );

            rabbitTemplate.convertAndSend(
                "usuario.exchange",
                "usuario.resposta.campeonato",
                errorResponse
            );
        }
    }
}
