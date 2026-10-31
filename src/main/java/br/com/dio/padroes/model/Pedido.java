package br.com.dio.padroes.model;

import br.com.dio.padroes.exception.RegraNegocioException;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Cliente cliente;

    @ManyToOne
    private Endereco enderecoEntrega;

    private BigDecimal valorProdutos;
    private BigDecimal pesoKg;

    @Enumerated(EnumType.STRING)
    private ModalidadeFrete modalidadeFrete;

    private BigDecimal valorFrete;
    private int prazoEntregaDias;
    private String cupom;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "pedido_desconto")
    private List<DescontoAplicado> descontos = new ArrayList<>();

    private BigDecimal valorDesconto;
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    private LocalDateTime criadoEm;

    protected Pedido() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public StatusPedido alterarStatus(StatusPedido novo) {
        if (!status.podeIrPara(novo)) {
            throw new RegraNegocioException(
                    "Não é possível mudar o pedido de %s para %s. Permitidos: %s".formatted(status, novo, status.proximos()));
        }
        StatusPedido anterior = status;
        status = novo;
        return anterior;
    }

    public Long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Endereco getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public BigDecimal getValorProdutos() {
        return valorProdutos;
    }

    public BigDecimal getPesoKg() {
        return pesoKg;
    }

    public ModalidadeFrete getModalidadeFrete() {
        return modalidadeFrete;
    }

    public BigDecimal getValorFrete() {
        return valorFrete;
    }

    public int getPrazoEntregaDias() {
        return prazoEntregaDias;
    }

    public String getCupom() {
        return cupom;
    }

    public List<DescontoAplicado> getDescontos() {
        return List.copyOf(descontos);
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    /**
     * Builder: monta o pedido passo a passo e só entrega um objeto
     * consistente, já com o total calculado e o status inicial.
     */
    public static class Builder {

        private final Pedido pedido = new Pedido();

        private Builder() {
        }

        public Builder cliente(Cliente cliente) {
            pedido.cliente = cliente;
            pedido.enderecoEntrega = cliente.getEndereco();
            return this;
        }

        public Builder produtos(BigDecimal valor, BigDecimal pesoKg) {
            pedido.valorProdutos = valor;
            pedido.pesoKg = pesoKg;
            return this;
        }

        public Builder frete(ModalidadeFrete modalidade, BigDecimal valor, int prazoDias) {
            pedido.modalidadeFrete = modalidade;
            pedido.valorFrete = valor;
            pedido.prazoEntregaDias = prazoDias;
            return this;
        }

        public Builder cupom(String cupom) {
            pedido.cupom = cupom;
            return this;
        }

        public Builder descontos(List<DescontoAplicado> descontos, BigDecimal total) {
            pedido.descontos = new ArrayList<>(descontos);
            pedido.valorDesconto = total;
            return this;
        }

        public Pedido build() {
            if (pedido.cliente == null || pedido.valorProdutos == null || pedido.modalidadeFrete == null) {
                throw new IllegalStateException("Pedido precisa de cliente, produtos e frete");
            }
            if (pedido.valorDesconto == null) {
                pedido.valorDesconto = BigDecimal.ZERO;
            }
            pedido.valorTotal = pedido.valorProdutos.add(pedido.valorFrete).subtract(pedido.valorDesconto);
            pedido.status = StatusPedido.CRIADO;
            pedido.criadoEm = LocalDateTime.now();
            return pedido;
        }
    }
}
