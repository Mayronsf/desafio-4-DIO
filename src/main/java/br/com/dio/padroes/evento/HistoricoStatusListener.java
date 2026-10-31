package br.com.dio.padroes.evento;

import br.com.dio.padroes.model.HistoricoStatus;
import br.com.dio.padroes.repository.HistoricoStatusRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class HistoricoStatusListener {

    private final HistoricoStatusRepository repository;

    public HistoricoStatusListener(HistoricoStatusRepository repository) {
        this.repository = repository;
    }

    // Roda na mesma transação de quem publicou: se a mudança falhar, o histórico também é desfeito
    @EventListener
    public void registrar(StatusPedidoAlteradoEvent evento) {
        repository.save(new HistoricoStatus(
                evento.pedidoId(), evento.statusAnterior(), evento.statusNovo(), LocalDateTime.now()));
    }
}
