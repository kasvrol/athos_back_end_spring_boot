package br.ufpr.athos.pagamento.repository;

import br.ufpr.athos.pagamento.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, String> {

    List<Pagamento> findByUsuarioIdOrderByDataCriacaoDesc(String usuarioId);

    List<Pagamento> findByEventoIdOrderByDataCriacaoDesc(String eventoId);

    Optional<Pagamento> findByStripePaymentIntentId(String stripePaymentIntentId);

    Optional<Pagamento> findByUsuarioIdAndEventoId(String usuarioId, String eventoId);
}
