package br.com.dio.padroes.web.dto;

import br.com.dio.padroes.model.ModalidadeFrete;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PedidoRequest(
        @NotNull Long clienteId,
        @NotNull @DecimalMin("0.01") BigDecimal valorProdutos,
        @NotNull @DecimalMin("0.01") BigDecimal pesoKg,
        @NotNull ModalidadeFrete modalidadeFrete,
        String cupom) {
}
