package br.com.dio.padroes.desconto;

import br.com.dio.padroes.model.DescontoAplicado;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto que percorre a cadeia de descontos. Cada regra lê os dados do pedido
 * e registra aqui o desconto que concedeu.
 */
public class ContextoDesconto {

    private final BigDecimal valorProdutos;
    private final BigDecimal valorFrete;
    private final String cupom;
    private final boolean primeiraCompra;
    private final List<DescontoAplicado> descontos = new ArrayList<>();

    public ContextoDesconto(BigDecimal valorProdutos, BigDecimal valorFrete, String cupom, boolean primeiraCompra) {
        this.valorProdutos = valorProdutos;
        this.valorFrete = valorFrete;
        this.cupom = cupom == null || cupom.isBlank() ? null : cupom.trim().toUpperCase();
        this.primeiraCompra = primeiraCompra;
    }

    public void registrar(String descricao, BigDecimal valor) {
        if (valor.signum() > 0) {
            descontos.add(new DescontoAplicado(descricao, valor.setScale(2, RoundingMode.HALF_UP)));
        }
    }

    /** Soma dos descontos, nunca maior que o valor do pedido. */
    public BigDecimal total() {
        BigDecimal soma = descontos.stream()
                .map(DescontoAplicado::valor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return soma.min(valorProdutos.add(valorFrete)).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getValorProdutos() {
        return valorProdutos;
    }

    public BigDecimal getValorFrete() {
        return valorFrete;
    }

    public String getCupom() {
        return cupom;
    }

    public boolean isPrimeiraCompra() {
        return primeiraCompra;
    }

    public List<DescontoAplicado> getDescontos() {
        return List.copyOf(descontos);
    }
}
