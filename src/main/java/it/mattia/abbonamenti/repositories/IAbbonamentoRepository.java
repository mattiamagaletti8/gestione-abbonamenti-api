package it.mattia.abbonamenti.repositories;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import it.mattia.abbonamenti.models.Abbonamento;

@Repository
public interface IAbbonamentoRepository extends JpaRepository<Abbonamento, Long> {
    List<Abbonamento> findByAttivo(boolean attivo);

    @Query("select coalesce(sum(a.prezzoMensile), 0) from Abbonamento a where a.attivo = true")
    BigDecimal totaleAttivi();
}
