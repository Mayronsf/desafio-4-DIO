package br.com.dio.padroes.evento;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class NotificacaoClienteListener {

    private static final Logger log = LoggerFactory.getLogger(NotificacaoClienteListener.class);

    // Só avisa o cliente depois do commit, para nunca notificar algo que foi desfeito
    @TransactionalEventListener
    public void notificar(StatusPedidoAlteradoEvent evento) {
        log.info("[e-mail para {}] Seu pedido #{} agora está {}",
                evento.emailCliente(), evento.pedidoId(), evento.statusNovo());
    }
}
