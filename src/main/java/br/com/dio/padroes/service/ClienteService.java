package br.com.dio.padroes.service;

import br.com.dio.padroes.exception.RecursoNaoEncontradoException;
import br.com.dio.padroes.model.Cliente;
import br.com.dio.padroes.model.Endereco;
import br.com.dio.padroes.repository.ClienteRepository;
import br.com.dio.padroes.web.dto.ClienteRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository repository;
    private final EnderecoService enderecoService;

    public ClienteService(ClienteRepository repository, EnderecoService enderecoService) {
        this.repository = repository;
        this.enderecoService = enderecoService;
    }

    public List<Cliente> listar() {
        return repository.findAll();
    }

    public Cliente buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado: " + id));
    }

    @Transactional
    public Cliente criar(ClienteRequest request) {
        Endereco endereco = enderecoService.buscarPorCep(request.cep());
        return repository.save(new Cliente(request.nome(), request.email(), endereco));
    }

    @Transactional
    public Cliente atualizar(Long id, ClienteRequest request) {
        Cliente cliente = buscar(id);
        Endereco endereco = enderecoService.buscarPorCep(request.cep());
        cliente.atualizar(request.nome(), request.email(), endereco);
        return repository.save(cliente);
    }

    @Transactional
    public void remover(Long id) {
        repository.delete(buscar(id));
    }
}
