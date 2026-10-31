package br.com.dio.padroes.repository;

import br.com.dio.padroes.model.HistoricoStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoStatusRepository extends JpaRepository<HistoricoStatus, Long> {

    List<HistoricoStatus> findByPedidoIdOrderByIdAsc(Long pedidoId);
}
