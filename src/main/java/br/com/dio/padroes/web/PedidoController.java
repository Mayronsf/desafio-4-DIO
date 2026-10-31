package br.com.dio.padroes.web;

import br.com.dio.padroes.model.HistoricoStatus;
import br.com.dio.padroes.service.PedidoFacade;
import br.com.dio.padroes.web.dto.AlterarStatusRequest;
import br.com.dio.padroes.web.dto.PedidoRequest;
import br.com.dio.padroes.web.dto.PedidoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Pedidos")
@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoFacade pedidoFacade;

    public PedidoController(PedidoFacade pedidoFacade) {
        this.pedidoFacade = pedidoFacade;
    }

    @GetMapping
    public List<PedidoResponse> listar(@RequestParam(required = false) Long clienteId) {
        return pedidoFacade.listar(clienteId).stream().map(PedidoResponse::de).toList();
    }

    @GetMapping("/{id}")
    public PedidoResponse buscar(@PathVariable Long id) {
        return PedidoResponse.de(pedidoFacade.buscar(id));
    }

    @Operation(summary = "Cria um pedido calculando frete e descontos (cupons: DIO10, FRETEGRATIS)")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponse criar(@Valid @RequestBody PedidoRequest request) {
        return PedidoResponse.de(pedidoFacade.criar(request));
    }

    @Operation(summary = "Avança o status do pedido (CRIADO → PAGO → ENVIADO → ENTREGUE, ou CANCELADO)")
    @PatchMapping("/{id}/status")
    public PedidoResponse alterarStatus(@PathVariable Long id, @Valid @RequestBody AlterarStatusRequest request) {
        return PedidoResponse.de(pedidoFacade.alterarStatus(id, request.status()));
    }

    @GetMapping("/{id}/historico")
    public List<HistoricoStatus> historico(@PathVariable Long id) {
        return pedidoFacade.historico(id);
    }
}
