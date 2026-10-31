package br.com.dio.padroes.frete;

import br.com.dio.padroes.model.ModalidadeFrete;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class FreteSedex implements FreteStrategy {

    private static final BigDecimal TAXA_BASE = new BigDecimal("18.00");
    private static final BigDecimal POR_KG = new BigDecimal("4.00");

    @Override
    public ModalidadeFrete modalidade() {
        return ModalidadeFrete.SEDEX;
    }

    @Override
    public CotacaoFrete cotar(Regiao regiao, BigDecimal pesoKg) {
        BigDecimal valor = TAXA_BASE.add(POR_KG.multiply(pesoKg))
                .multiply(regiao.fatorCusto())
                .setScale(2, RoundingMode.HALF_UP);
        return new CotacaoFrete(modalidade(), valor, 1 + regiao.diasExtras());
    }
}
