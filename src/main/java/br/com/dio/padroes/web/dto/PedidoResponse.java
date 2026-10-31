package br.com.dio.padroes.web.dto;

import br.com.dio.padroes.model.DescontoAplicado;
import br.com.dio.padroes.model.Endereco;
import br.com.dio.padroes.model.ModalidadeFrete;
import br.com.dio.padroes.model.Pedido;
import br.com.dio.padroes.model.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(
        Long id,
        Long clienteId,
        Endereco enderecoEntrega,
        BigDecimal valorProdutos,
        BigDecimal pesoKg,
        ModalidadeFrete modalidadeFrete,
        BigDecimal valorFrete,
        int prazoEntregaDias,
        String cupom,
        List<DescontoAplicado> descontos,
        BigDecimal valorDesconto,
        BigDecimal valorTotal,
        StatusPedido status,
        LocalDateTime criadoEm) {

    public static PedidoResponse de(Pedido pedido) {
        return new PedidoResponse(
                pedido.getId(),
                pedido.getCliente().getId(),
                pedido.getEnderecoEntrega(),
                pedido.getValorProdutos(),
                pedido.getPesoKg(),
                pedido.getModalidadeFrete(),
                pedido.getValorFrete(),
                pedido.getPrazoEntregaDias(),
                pedido.getCupom(),
                pedido.getDescontos(),
                pedido.getValorDesconto(),
                pedido.getValorTotal(),
                pedido.getStatus(),
                pedido.getCriadoEm());
    }
}
