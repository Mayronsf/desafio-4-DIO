package br.com.dio.padroes.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StatusPedidoTest {

    @Test
    void fluxoNormalDoPedido() {
        assertThat(StatusPedido.CRIADO.podeIrPara(StatusPedido.PAGO)).isTrue();
        assertThat(StatusPedido.PAGO.podeIrPara(StatusPedido.ENVIADO)).isTrue();
        assertThat(StatusPedido.ENVIADO.podeIrPara(StatusPedido.ENTREGUE)).isTrue();
    }

    @Test
    void naoPulaEtapasNemCancelaDepoisDeEnviado() {
        assertThat(StatusPedido.CRIADO.podeIrPara(StatusPedido.ENVIADO)).isFalse();
        assertThat(StatusPedido.ENVIADO.podeIrPara(StatusPedido.CANCELADO)).isFalse();
    }

    @Test
    void estadosFinaisNaoTemSaida() {
        assertThat(StatusPedido.ENTREGUE.proximos()).isEmpty();
        assertThat(StatusPedido.CANCELADO.proximos()).isEmpty();
    }
}
