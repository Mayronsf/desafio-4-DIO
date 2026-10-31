package br.com.dio.padroes.model;

import java.util.Set;

/**
 * State: cada status sabe para quais status o pedido pode seguir,
 * então a regra de transição fica no próprio estado e não espalhada em ifs.
 */
public enum StatusPedido {

    CRIADO {
        @Override
        public Set<StatusPedido> proximos() {
            return Set.of(PAGO, CANCELADO);
        }
    },
    PAGO {
        @Override
        public Set<StatusPedido> proximos() {
            return Set.of(ENVIADO, CANCELADO);
        }
    },
    ENVIADO {
        @Override
        public Set<StatusPedido> proximos() {
            return Set.of(ENTREGUE);
        }
    },
    ENTREGUE,
    CANCELADO;

    public Set<StatusPedido> proximos() {
        return Set.of();
    }

    public boolean podeIrPara(StatusPedido novo) {
        return proximos().contains(novo);
    }
}
