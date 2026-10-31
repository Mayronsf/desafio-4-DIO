package br.com.dio.padroes.web;

import br.com.dio.padroes.frete.CotacaoFrete;
import br.com.dio.padroes.service.ClienteService;
import br.com.dio.padroes.service.PedidoFacade;
import br.com.dio.padroes.web.dto.ClienteRequest;
import br.com.dio.padroes.web.dto.ClienteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Clientes")
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final PedidoFacade pedidoFacade;

    public ClienteController(ClienteService clienteService, PedidoFacade pedidoFacade) {
        this.clienteService = clienteService;
        this.pedidoFacade = pedidoFacade;
    }

    @GetMapping
    public List<ClienteResponse> listar() {
        return clienteService.listar().stream().map(ClienteResponse::de).toList();
    }

    @GetMapping("/{id}")
    public ClienteResponse buscar(@PathVariable Long id) {
        return ClienteResponse.de(clienteService.buscar(id));
    }

    @Operation(summary = "Cadastra um cliente; o endereço é preenchido pelo ViaCEP")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse criar(@Valid @RequestBody ClienteRequest request) {
        return ClienteResponse.de(clienteService.criar(request));
    }

    @PutMapping("/{id}")
    public ClienteResponse atualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequest request) {
        return ClienteResponse.de(clienteService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        clienteService.remover(id);
    }

    @Operation(summary = "Compara o frete de todas as modalidades para o endereço do cliente")
    @GetMapping("/{id}/frete")
    public List<CotacaoFrete> simularFrete(@PathVariable Long id, @RequestParam BigDecimal pesoKg) {
        return pedidoFacade.simularFrete(id, pesoKg);
    }
}
