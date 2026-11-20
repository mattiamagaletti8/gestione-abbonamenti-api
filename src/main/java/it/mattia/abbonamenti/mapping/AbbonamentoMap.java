package it.mattia.abbonamenti.mapping;

import java.util.List;
import it.mattia.abbonamenti.dto.output.AbbonamentoDTO;
import it.mattia.abbonamenti.models.Abbonamento;

public class AbbonamentoMap {
    public static AbbonamentoDTO buildAbbonamentoDTO(Abbonamento abbonamento) {
        return AbbonamentoDTO.builder()
                .id(abbonamento.getId())
                .clienteId(abbonamento.getCliente().getId())
                .nomeCliente(abbonamento.getCliente().getNome())
                .piano(abbonamento.getPiano())
                .prezzoMensile(abbonamento.getPrezzoMensile())
                .attivo(abbonamento.isAttivo())
                .dataInizio(abbonamento.getDataInizio())
                .dataDisattivazione(abbonamento.getDataDisattivazione())
                .build();
    }

    public static List<AbbonamentoDTO> buildAbbonamentoDTOList(List<Abbonamento> abbonamenti) {
        return abbonamenti.stream()
                .map(AbbonamentoMap::buildAbbonamentoDTO)
                .toList();
    }
}
