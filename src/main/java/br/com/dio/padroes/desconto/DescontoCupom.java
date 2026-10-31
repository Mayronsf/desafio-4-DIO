package br.com.dio.padroes.desconto;

import br.com.dio.padroes.exception.RegraNegocioException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Order(3)
public class DescontoCupom extends RegraDesconto {

    public static final String DIO10 = "DIO10";
    public static final String FRETEGRATIS = "FRETEGRATIS";

    @Override
    protected void processar(ContextoDesconto contexto) {
        String cupom = contexto.getCupom();
        if (cupom == null) {
            return;
        }
        switch (cupom) {
            case DIO10 -> contexto.registrar("Cupom DIO10 (10%)", contexto.getValorProdutos().multiply(new BigDecimal("0.10")));
            case FRETEGRATIS -> contexto.registrar("Cupom FRETEGRATIS", contexto.getValorFrete());
            // Cupom inválido interrompe a cadeia: o pedido não deve ser criado
            default -> throw new RegraNegocioException("Cupom inválido: " + cupom);
        }
    }
}
