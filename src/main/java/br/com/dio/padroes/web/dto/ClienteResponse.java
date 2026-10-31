package br.com.dio.padroes.web.dto;

import br.com.dio.padroes.model.Cliente;
import br.com.dio.padroes.model.Endereco;

public record ClienteResponse(Long id, String nome, String email, Endereco endereco) {

    public static ClienteResponse de(Cliente cliente) {
        return new ClienteResponse(cliente.getId(), cliente.getNome(), cliente.getEmail(), cliente.getEndereco());
    }
}
