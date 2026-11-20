package it.mattia.abbonamenti.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import it.mattia.abbonamenti.models.Cliente;

@Repository
public interface IClienteRepository extends JpaRepository<Cliente, Long> {
}
