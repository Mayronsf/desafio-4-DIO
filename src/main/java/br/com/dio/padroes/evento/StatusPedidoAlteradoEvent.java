package br.com.dio.padroes.evento;

import br.com.dio.padroes.model.StatusPedido;

/**
 * Observer: evento publicado sempre que um pedido muda de status.
 * Quem publica não conhece quem escuta; cada listener reage por conta própria.
 */
public record StatusPedidoAlteradoEvent(
        Long pedidoId,
        String emailCliente,
        StatusPedido statusAnterior,
        StatusPedido statusNovo) {
}
