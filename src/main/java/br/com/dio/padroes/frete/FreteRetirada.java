package br.com.dio.padroes.frete;

import br.com.dio.padroes.model.ModalidadeFrete;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FreteRetirada implements FreteStrategy {

    @Override
    public ModalidadeFrete modalidade() {
        return ModalidadeFrete.RETIRADA;
    }

    @Override
    public CotacaoFrete cotar(Regiao regiao, BigDecimal pesoKg) {
        // Retirada na loja: sem custo, fica disponível no dia seguinte
        return new CotacaoFrete(modalidade(), new BigDecimal("0.00"), 1);
    }
}
