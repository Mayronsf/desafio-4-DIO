package br.com.dio.padroes.service;

import br.com.dio.padroes.exception.RecursoNaoEncontradoException;
import br.com.dio.padroes.exception.RegraNegocioException;
import br.com.dio.padroes.integracao.ViaCepClient;
import br.com.dio.padroes.integracao.ViaCepResponse;
import br.com.dio.padroes.model.Endereco;
import br.com.dio.padroes.repository.EnderecoRepository;
import org.springframework.stereotype.Service;

@Service
public class EnderecoService {

    private final EnderecoRepository repository;
    private final ViaCepClient viaCep;

    public EnderecoService(EnderecoRepository repository, ViaCepClient viaCep) {
        this.repository = repository;
        this.viaCep = viaCep;
    }

    /**
     * Procura o CEP no banco e só consulta o ViaCEP se ainda não conhecer o endereço.
     * O resto do sistema usa este método sem saber de onde o endereço veio.
     */
    public Endereco buscarPorCep(String cep) {
        String cepLimpo = normalizar(cep);
        return repository.findById(cepLimpo).orElseGet(() -> consultarViaCep(cepLimpo));
    }

    private Endereco consultarViaCep(String cep) {
        ViaCepResponse resposta = viaCep.consultar(cep);
        if (resposta == null || resposta.naoEncontrado()) {
            throw new RecursoNaoEncontradoException("CEP não encontrado: " + cep);
        }
        // Adapter: converte o formato do ViaCEP para a nossa entidade
        return repository.save(new Endereco(
                cep, resposta.logradouro(), resposta.bairro(), resposta.localidade(), resposta.uf()));
    }

    private static String normalizar(String cep) {
        String digitos = cep == null ? "" : cep.replaceAll("\\D", "");
        if (digitos.length() != 8) {
            throw new RegraNegocioException("CEP deve ter 8 dígitos: " + cep);
        }
        return digitos;
    }
}
