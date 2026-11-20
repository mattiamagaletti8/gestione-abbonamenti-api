package it.mattia.abbonamenti.controllers;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import it.mattia.abbonamenti.dto.input.ClienteReq;
import it.mattia.abbonamenti.dto.output.ClienteDTO;
import it.mattia.abbonamenti.services.interfaces.IClienteServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/clienti")
public class ClienteController {
    private final IClienteServices cliS;

    @PostMapping
    public ResponseEntity<ClienteDTO> create(@Valid @RequestBody ClienteReq req) {
        ClienteDTO cliente = cliS.create(req);
        return ResponseEntity.created(URI.create("/api/clienti/" + cliente.getId()))
                .body(cliente);
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> list() {
        return ResponseEntity.ok(cliS.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(cliS.getById(id));
    }
}
