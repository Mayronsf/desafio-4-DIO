package br.com.dio.padroes.service;

import br.com.dio.padroes.desconto.CadeiaDescontos;
import br.com.dio.padroes.desconto.ContextoDesconto;
import br.com.dio.padroes.evento.StatusPedidoAlteradoEvent;
import br.com.dio.padroes.exception.RecursoNaoEncontradoException;
import br.com.dio.padroes.frete.CalculadoraFrete;
import br.com.dio.padroes.frete.CotacaoFrete;
import br.com.dio.padroes.model.Cliente;
import br.com.dio.padroes.model.HistoricoStatus;
import br.com.dio.padroes.model.Pedido;
import br.com.dio.padroes.model.StatusPedido;
import br.com.dio.padroes.repository.HistoricoStatusRepository;
import br.com.dio.padroes.repository.PedidoRepository;
import br.com.dio.padroes.web.dto.PedidoRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Facade: um único ponto de entrada para criar e movimentar pedidos.
 * Por trás dele trabalham cliente/ViaCEP, Strategy de frete, cadeia de descontos,
 * Builder do pedido e publicação de eventos — o controller não conhece nada disso.
 */
@Service
public class PedidoFacade {

    private final PedidoRepository pedidoRepository;
    private final HistoricoStatusRepository historicoRepository;
    private final ClienteService clienteService;
    private final CalculadoraFrete calculadoraFrete;
    private final CadeiaDescontos cadeiaDescontos;
    private final ApplicationEventPublisher eventos;

    public PedidoFacade(PedidoRepository pedidoRepository,
                        HistoricoStatusRepository historicoRepository,
                        ClienteService clienteService,
                        CalculadoraFrete calculadoraFrete,
                        CadeiaDescontos cadeiaDescontos,
                        ApplicationEventPublisher eventos) {
        this.pedidoRepository = pedidoRepository;
        this.historicoRepository = historicoRepository;
        this.clienteService = clienteService;
        this.calculadoraFrete = calculadoraFrete;
        this.cadeiaDescontos = cadeiaDescontos;
        this.eventos = eventos;
    }

    @Transactional
    public Pedido criar(PedidoRequest request) {
        Cliente cliente = clienteService.buscar(request.clienteId());

        CotacaoFrete frete = calculadoraFrete.cotar(request.modalidadeFrete(), cliente.getEndereco(), request.pesoKg());

        boolean primeiraCompra = !pedidoRepository.existsByClienteIdAndStatusNot(cliente.getId(), StatusPedido.CANCELADO);
        ContextoDesconto descontos = cadeiaDescontos.aplicar(
                new ContextoDesconto(request.valorProdutos(), frete.valor(), request.cupom(), primeiraCompra));

        Pedido pedido = pedidoRepository.save(Pedido.builder()
                .cliente(cliente)
                .produtos(request.valorProdutos(), request.pesoKg())
                .frete(frete.modalidade(), frete.valor(), frete.prazoDias())
                .cupom(descontos.getCupom())
                .descontos(descontos.getDescontos(), descontos.total())
                .build());

        publicar(pedido, null);
        return pedido;
    }

    @Transactional
    public Pedido alterarStatus(Long id, StatusPedido novo) {
        Pedido pedido = buscar(id);
        StatusPedido anterior = pedido.alterarStatus(novo);
        pedidoRepository.save(pedido);
        publicar(pedido, anterior);
        return pedido;
    }

    @Transactional(readOnly = true)
    public Pedido buscar(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public List<Pedido> listar(Long clienteId) {
        return clienteId == null ? pedidoRepository.findAll() : pedidoRepository.findByClienteId(clienteId);
    }

    @Transactional(readOnly = true)
    public List<HistoricoStatus> historico(Long id) {
        buscar(id);
        return historicoRepository.findByPedidoIdOrderByIdAsc(id);
    }

    public List<CotacaoFrete> simularFrete(Long clienteId, BigDecimal pesoKg) {
        return calculadoraFrete.simular(clienteService.buscar(clienteId).getEndereco(), pesoKg);
    }

    private void publicar(Pedido pedido, StatusPedido anterior) {
        eventos.publishEvent(new StatusPedidoAlteradoEvent(
                pedido.getId(), pedido.getCliente().getEmail(), anterior, pedido.getStatus()));
    }
}
