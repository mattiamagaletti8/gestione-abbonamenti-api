package it.mattia.abbonamenti.services.interfaces;

import java.util.List;
import it.mattia.abbonamenti.dto.input.ClienteReq;
import it.mattia.abbonamenti.dto.output.ClienteDTO;

public interface IClienteServices {
    ClienteDTO create(ClienteReq req);
    List<ClienteDTO> list();
    ClienteDTO getById(Long id);
}
