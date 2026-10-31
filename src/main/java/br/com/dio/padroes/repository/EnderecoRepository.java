package br.com.dio.padroes.repository;

import br.com.dio.padroes.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, String> {
}
