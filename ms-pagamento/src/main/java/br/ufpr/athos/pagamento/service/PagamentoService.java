package br.ufpr.athos.pagamento.service;

import br.ufpr.athos.pagamento.config.RabbitMQConfig;
import br.ufpr.athos.pagamento.dto.PagamentoRequestDTO;
import br.ufpr.athos.pagamento.dto.PagamentoResponseDTO;
import br.ufpr.athos.pagamento.event.PagamentoEvent;
import br.ufpr.athos.pagamento.model.Pagamento;
import br.ufpr.athos.pagamento.repository.PagamentoRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PagamentoService {

    private static final Logger logger = LoggerFactory.getLogger(PagamentoService.class);

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public PagamentoResponseDTO criarPagamento(PagamentoRequestDTO request) throws StripeException {
        // Verificar se já existe pagamento pendente para este evento
        Optional<Pagamento> existing = pagamentoRepository.findByUsuarioIdAndEventoId(
            request.getUsuarioId(),
            request.getEventoId()
        );

        if (existing.isPresent() && existing.get().getStatus() == Pagamento.StatusPagamento.CONFIRMADO) {
            throw new RuntimeException("Pagamento já foi confirmado para este evento");
        }

        // Criar PaymentIntent no Stripe
        long valorEmCentavos = request.getValor().multiply(java.math.BigDecimal.valueOf(100)).longValue();

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
            .setAmount(valorEmCentavos)
            .setCurrency("brl")
            .setAutomaticPaymentMethods(
                PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                    .setEnabled(true)
                    .build()
            )
            .putMetadata("usuario_id", request.getUsuarioId())
            .putMetadata("evento_id", request.getEventoId())
            .putMetadata("evento_nome", request.getEventoNome())
            .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        // Criar registro no banco
        Pagamento pagamento = new Pagamento();
        pagamento.setUsuarioId(request.getUsuarioId());
        pagamento.setEventoId(request.getEventoId());
        pagamento.setEventoNome(request.getEventoNome());
        pagamento.setValor(request.getValor());
        pagamento.setStatus(Pagamento.StatusPagamento.PENDENTE);
        pagamento.setStripePaymentIntentId(paymentIntent.getId());
        pagamento.setStripeClientSecret(paymentIntent.getClientSecret());

        Pagamento salvo = pagamentoRepository.save(pagamento);

        // Publicar evento
        publicarEventoPagamento(salvo, "PENDENTE");

        return new PagamentoResponseDTO(salvo);
    }

    public PagamentoResponseDTO confirmarPagamento(String paymentIntentId) {
        Pagamento pagamento = pagamentoRepository.findByStripePaymentIntentId(paymentIntentId)
            .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

        pagamento.setStatus(Pagamento.StatusPagamento.CONFIRMADO);
        pagamento.setDataConfirmacao(LocalDateTime.now());

        Pagamento atualizado = pagamentoRepository.save(pagamento);

        // Publicar evento
        publicarEventoPagamento(atualizado, "CONFIRMADO");

        return new PagamentoResponseDTO(atualizado);
    }

    public PagamentoResponseDTO falharPagamento(String paymentIntentId) {
        Pagamento pagamento = pagamentoRepository.findByStripePaymentIntentId(paymentIntentId)
            .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

        pagamento.setStatus(Pagamento.StatusPagamento.FALHOU);
        Pagamento atualizado = pagamentoRepository.save(pagamento);

        return new PagamentoResponseDTO(atualizado);
    }

    private void publicarEventoPagamento(Pagamento pagamento, String status) {
        try {
            PagamentoEvent event = new PagamentoEvent(
                pagamento.getId(),
                pagamento.getUsuarioId(),
                pagamento.getEventoId(),
                pagamento.getEventoNome(),
                pagamento.getValor(),
                status
            );

            String routingKey = status.equals("CONFIRMADO") ?
                RabbitMQConfig.PAGAMENTO_CONFIRMADO_KEY :
                RabbitMQConfig.PAGAMENTO_PENDENTE_KEY;

            rabbitTemplate.convertAndSend(
                RabbitMQConfig.PAGAMENTO_EXCHANGE,
                routingKey,
                event
            );

            logger.info("Evento de pagamento publicado: {} - {}", pagamento.getId(), status);
        } catch (Exception e) {
            logger.error("Erro ao publicar evento de pagamento: {}", e.getMessage(), e);
        }
    }

    public List<PagamentoResponseDTO> listarPagamentosPorUsuario(String usuarioId) {
        return pagamentoRepository.findByUsuarioIdOrderByDataCriacaoDesc(usuarioId)
                .stream()
                .map(PagamentoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<PagamentoResponseDTO> listarPagamentosPorEvento(String eventoId) {
        return pagamentoRepository.findByEventoIdOrderByDataCriacaoDesc(eventoId)
                .stream()
                .map(PagamentoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public PagamentoResponseDTO buscarPagamento(String pagamentoId) {
        Pagamento pagamento = pagamentoRepository.findById(pagamentoId)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));
        return new PagamentoResponseDTO(pagamento);
    }
}
