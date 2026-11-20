package it.mattia.abbonamenti.dto.output;

import java.util.List;
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
public class ErroreDTO {
    private int stato;
    private String messaggio;
    private List<String> dettagli;
}
