package br.com.dio.padroes.frete;

import br.com.dio.padroes.model.ModalidadeFrete;

import java.math.BigDecimal;

/**
 * Strategy: cada modalidade de frete tem seu próprio algoritmo de cálculo.
 * Para criar uma nova modalidade basta implementar esta interface e anotar com @Component.
 */
public interface FreteStrategy {

    ModalidadeFrete modalidade();

    CotacaoFrete cotar(Regiao regiao, BigDecimal pesoKg);
}
