package br.com.dio.padroes.frete;

import br.com.dio.padroes.model.ModalidadeFrete;

import java.math.BigDecimal;

public record CotacaoFrete(ModalidadeFrete modalidade, BigDecimal valor, int prazoDias) {
}
