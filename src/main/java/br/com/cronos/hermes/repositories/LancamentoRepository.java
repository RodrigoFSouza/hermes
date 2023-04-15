package br.com.cronos.hermes.repositories;

import br.com.cronos.hermes.entities.Lancamento;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, UUID> {
}
