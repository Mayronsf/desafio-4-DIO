package br.com.dio.padroes.repository;

import br.com.dio.padroes.model.Pedido;
import br.com.dio.padroes.model.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    boolean existsByClienteIdAndStatusNot(Long clienteId, StatusPedido status);

    List<Pedido> findByClienteId(Long clienteId);
}
