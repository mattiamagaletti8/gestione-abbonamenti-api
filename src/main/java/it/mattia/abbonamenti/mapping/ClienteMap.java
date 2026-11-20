package it.mattia.abbonamenti.mapping;

import java.util.List;
import it.mattia.abbonamenti.dto.output.ClienteDTO;
import it.mattia.abbonamenti.models.Cliente;

public class ClienteMap {
    public static ClienteDTO buildClienteDTO(Cliente cliente) {
        return ClienteDTO.builder()
                .id(cliente.getId())
                .nome(cliente.getNome())
                .email(cliente.getEmail())
                .build();
    }

    public static List<ClienteDTO> buildClienteDTOList(List<Cliente> clienti) {
        return clienti.stream()
                .map(ClienteMap::buildClienteDTO)
                .toList();
    }
}
