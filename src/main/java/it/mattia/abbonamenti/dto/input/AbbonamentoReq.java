package it.mattia.abbonamenti.dto.input;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbbonamentoReq {
    @NotNull
    @Positive
    private Long clienteId;

    @NotBlank
    @Size(max = 100)
    private String piano;

    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 8, fraction = 2)
    private BigDecimal prezzoMensile;
}
