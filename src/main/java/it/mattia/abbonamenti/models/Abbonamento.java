package it.mattia.abbonamenti.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "abbonamenti")
public class Abbonamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false, length = 100)
    private String piano;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prezzoMensile;

    @Column(nullable = false)
    private boolean attivo;

    @Column(nullable = false)
    private LocalDate dataInizio;

    private LocalDate dataDisattivazione;
}
