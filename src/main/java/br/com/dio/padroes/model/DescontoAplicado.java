package br.com.dio.padroes.model;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public record DescontoAplicado(String descricao, BigDecimal valor) {
}
