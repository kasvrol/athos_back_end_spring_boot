package br.ufpr.athos.campeonato.messaging;

import br.ufpr.athos.campeonato.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Listener for user information responses from ms-autenticacao
 * Uses CompletableFuture to handle asynchronous RPC-style communication
 */
@Component
public class UsuarioResponseListener {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioResponseListener.class);

    // Map to store pending requests: usuarioId -> CompletableFuture<UsuarioResponseEvent>
    private final ConcurrentHashMap<String, CompletableFuture<UsuarioResponseEvent>> pendingRequests =
            new ConcurrentHashMap<>();

    /**
     * Listens for user information responses
     */
    @RabbitListener(queues = RabbitMQConfig.USUARIO_RESPONSE_QUEUE)
    public void handleUsuarioResponse(UsuarioResponseEvent response) {
        logger.info("Received user info response for usuarioId: {}", response.getUsuarioId());

        CompletableFuture<UsuarioResponseEvent> future = pendingRequests.remove(response.getUsuarioId());

        if (future != null) {
            future.complete(response);
            logger.debug("Completed future for usuarioId: {}", response.getUsuarioId());
        } else {
            logger.warn("Received response for unknown usuarioId: {}", response.getUsuarioId());
        }
    }

    /**
     * Registers a pending request for user information
     *
     * @param usuarioId The user ID to wait for
     * @return CompletableFuture that will be completed when response arrives
     */
    public CompletableFuture<UsuarioResponseEvent> registerPendingRequest(String usuarioId) {
        CompletableFuture<UsuarioResponseEvent> future = new CompletableFuture<>();
        pendingRequests.put(usuarioId, future);
        logger.debug("Registered pending request for usuarioId: {}", usuarioId);
        return future;
    }

    /**
     * Cancels a pending request (cleanup on timeout)
     *
     * @param usuarioId The user ID to cancel
     */
    public void cancelPendingRequest(String usuarioId) {
        CompletableFuture<UsuarioResponseEvent> future = pendingRequests.remove(usuarioId);
        if (future != null && !future.isDone()) {
            future.cancel(true);
            logger.debug("Cancelled pending request for usuarioId: {}", usuarioId);
        }
    }
}
