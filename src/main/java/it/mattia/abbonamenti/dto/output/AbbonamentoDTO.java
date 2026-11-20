package it.mattia.abbonamenti.dto.output;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AbbonamentoDTO {
    private Long id;
    private Long clienteId;
    private String nomeCliente;
    private String piano;
    private BigDecimal prezzoMensile;
    private boolean attivo;
    private LocalDate dataInizio;
    private LocalDate dataDisattivazione;
}
