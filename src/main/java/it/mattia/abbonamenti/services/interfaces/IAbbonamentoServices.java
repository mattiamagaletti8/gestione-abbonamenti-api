package it.mattia.abbonamenti.services.interfaces;

import java.util.List;
import it.mattia.abbonamenti.dto.input.AbbonamentoReq;
import it.mattia.abbonamenti.dto.output.AbbonamentoDTO;
import it.mattia.abbonamenti.dto.output.TotaleMensileDTO;

public interface IAbbonamentoServices {
    AbbonamentoDTO create(AbbonamentoReq req);
    List<AbbonamentoDTO> list(Boolean attivo);
    AbbonamentoDTO getById(Long id);
    AbbonamentoDTO disattiva(Long id);
    TotaleMensileDTO totale();
}
