package it.mattia.abbonamenti.dto.output;

import java.math.BigDecimal;
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
public class TotaleMensileDTO {
    private BigDecimal totale;
    private String valuta;
}
