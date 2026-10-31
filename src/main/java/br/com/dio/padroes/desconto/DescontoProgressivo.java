package br.com.dio.padroes.desconto;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Order(2)
public class DescontoProgressivo extends RegraDesconto {

    private static final BigDecimal VALOR_MINIMO = new BigDecimal("500.00");
    private static final BigDecimal PERCENTUAL = new BigDecimal("0.05");

    @Override
    protected void processar(ContextoDesconto contexto) {
        if (contexto.getValorProdutos().compareTo(VALOR_MINIMO) >= 0) {
            contexto.registrar("Compra acima de R$ 500 (5%)", contexto.getValorProdutos().multiply(PERCENTUAL));
        }
    }
}
