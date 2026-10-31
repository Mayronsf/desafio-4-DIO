package br.com.dio.padroes.integracao;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

/**
 * Proxy: o Spring gera a implementação desta interface em tempo de execução
 * (HTTP Interface), escondendo todos os detalhes da chamada HTTP ao ViaCEP.
 */
public interface ViaCepClient {

    @GetExchange("/{cep}/json/")
    ViaCepResponse consultar(@PathVariable String cep);
}
