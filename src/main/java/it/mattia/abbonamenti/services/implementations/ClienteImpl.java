package it.mattia.abbonamenti.services.implementations;

import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.mattia.abbonamenti.dto.input.ClienteReq;
import it.mattia.abbonamenti.dto.output.ClienteDTO;
import it.mattia.abbonamenti.exceptions.ApiException;
import it.mattia.abbonamenti.mapping.ClienteMap;
import it.mattia.abbonamenti.models.Cliente;
import it.mattia.abbonamenti.repositories.IClienteRepository;
import it.mattia.abbonamenti.services.interfaces.IClienteServices;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ClienteImpl implements IClienteServices {
    private final IClienteRepository repC;

    @Transactional
    @Override
    public ClienteDTO create(ClienteReq req) {
        Cliente cliente = new Cliente();
        cliente.setNome(req.getNome().trim());
        cliente.setEmail(req.getEmail().trim().toLowerCase(Locale.ROOT));
        return ClienteMap.buildClienteDTO(repC.save(cliente));
    }

    @Override
    public List<ClienteDTO> list() {
        return ClienteMap.buildClienteDTOList(repC.findAll());
    }

    @Override
    public ClienteDTO getById(Long id) {
        Cliente cliente = repC.findById(id)
                .orElseThrow(() -> new ApiException("Cliente non trovato"));
        return ClienteMap.buildClienteDTO(cliente);
    }
}
