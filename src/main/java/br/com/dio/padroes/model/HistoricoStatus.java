package br.com.dio.padroes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class HistoricoStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long pedidoId;

    @Enumerated(EnumType.STRING)
    private StatusPedido statusAnterior;

    @Enumerated(EnumType.STRING)
    private StatusPedido statusNovo;

    private LocalDateTime alteradoEm;

    protected HistoricoStatus() {
    }

    public HistoricoStatus(Long pedidoId, StatusPedido statusAnterior, StatusPedido statusNovo, LocalDateTime alteradoEm) {
        this.pedidoId = pedidoId;
        this.statusAnterior = statusAnterior;
        this.statusNovo = statusNovo;
        this.alteradoEm = alteradoEm;
    }

    public Long getId() {
        return id;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public StatusPedido getStatusAnterior() {
        return statusAnterior;
    }

    public StatusPedido getStatusNovo() {
        return statusNovo;
    }

    public LocalDateTime getAlteradoEm() {
        return alteradoEm;
    }
}
