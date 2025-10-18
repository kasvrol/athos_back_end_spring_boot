package br.ufpr.athos.pagamento.controller;

import br.ufpr.athos.pagamento.dto.PagamentoRequestDTO;
import br.ufpr.athos.pagamento.dto.PagamentoResponseDTO;
import br.ufpr.athos.pagamento.service.PagamentoService;
import com.stripe.exception.StripeException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @PostMapping
    public ResponseEntity<PagamentoResponseDTO> criarPagamento(@Valid @RequestBody PagamentoRequestDTO request) {
        try {
            PagamentoResponseDTO pagamento = pagamentoService.criarPagamento(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(pagamento);
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{pagamentoId}")
    public ResponseEntity<PagamentoResponseDTO> buscarPagamento(@PathVariable String pagamentoId) {
        try {
            PagamentoResponseDTO pagamento = pagamentoService.buscarPagamento(pagamentoId);
            return ResponseEntity.ok(pagamento);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PagamentoResponseDTO>> listarPagamentosPorUsuario(@PathVariable String usuarioId) {
        List<PagamentoResponseDTO> pagamentos = pagamentoService.listarPagamentosPorUsuario(usuarioId);
        return ResponseEntity.ok(pagamentos);
    }

    @GetMapping("/evento/{eventoId}")
    public ResponseEntity<List<PagamentoResponseDTO>> listarPagamentosPorEvento(@PathVariable String eventoId) {
        List<PagamentoResponseDTO> pagamentos = pagamentoService.listarPagamentosPorEvento(eventoId);
        return ResponseEntity.ok(pagamentos);
    }

    @PostMapping("/confirmar/{paymentIntentId}")
    public ResponseEntity<PagamentoResponseDTO> confirmarPagamento(@PathVariable String paymentIntentId) {
        try {
            PagamentoResponseDTO pagamento = pagamentoService.confirmarPagamento(paymentIntentId);
            return ResponseEntity.ok(pagamento);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
