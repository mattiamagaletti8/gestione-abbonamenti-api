package it.mattia.abbonamenti.controllers;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import it.mattia.abbonamenti.dto.input.AbbonamentoReq;
import it.mattia.abbonamenti.dto.output.AbbonamentoDTO;
import it.mattia.abbonamenti.dto.output.TotaleMensileDTO;
import it.mattia.abbonamenti.services.interfaces.IAbbonamentoServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/abbonamenti")
public class AbbonamentoController {
    private final IAbbonamentoServices abbS;

    @PostMapping
    public ResponseEntity<AbbonamentoDTO> create(@Valid @RequestBody AbbonamentoReq req) {
        AbbonamentoDTO abbonamento = abbS.create(req);
        return ResponseEntity.created(URI.create("/api/abbonamenti/" + abbonamento.getId()))
                .body(abbonamento);
    }

    @GetMapping
    public ResponseEntity<List<AbbonamentoDTO>> list(@RequestParam(required = false) Boolean attivo) {
        return ResponseEntity.ok(abbS.list(attivo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AbbonamentoDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(abbS.getById(id));
    }

    @PatchMapping("/{id}/disattiva")
    public ResponseEntity<AbbonamentoDTO> disattiva(@PathVariable Long id) {
        return ResponseEntity.ok(abbS.disattiva(id));
    }

    @GetMapping("/totale-mensile")
    public ResponseEntity<TotaleMensileDTO> totale() {
        return ResponseEntity.ok(abbS.totale());
    }
}
