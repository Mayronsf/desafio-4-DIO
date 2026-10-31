package br.com.dio.padroes.desconto;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Order(1)
public class DescontoPrimeiraCompra extends RegraDesconto {

    private static final BigDecimal PERCENTUAL = new BigDecimal("0.10");

    @Override
    protected void processar(ContextoDesconto contexto) {
        if (contexto.isPrimeiraCompra()) {
            contexto.registrar("Primeira compra (10%)", contexto.getValorProdutos().multiply(PERCENTUAL));
        }
    }
}
