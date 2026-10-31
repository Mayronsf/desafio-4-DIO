package br.com.dio.padroes.web.dto;

import br.com.dio.padroes.model.StatusPedido;
import jakarta.validation.constraints.NotNull;

public record AlterarStatusRequest(@NotNull StatusPedido status) {
}
