package it.mattia.abbonamenti.services.implementations;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.mattia.abbonamenti.dto.input.AbbonamentoReq;
import it.mattia.abbonamenti.dto.output.AbbonamentoDTO;
import it.mattia.abbonamenti.dto.output.TotaleMensileDTO;
import it.mattia.abbonamenti.exceptions.ApiException;
import it.mattia.abbonamenti.mapping.AbbonamentoMap;
import it.mattia.abbonamenti.models.Abbonamento;
import it.mattia.abbonamenti.models.Cliente;
import it.mattia.abbonamenti.repositories.IAbbonamentoRepository;
import it.mattia.abbonamenti.repositories.IClienteRepository;
import it.mattia.abbonamenti.services.interfaces.IAbbonamentoServices;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AbbonamentoImpl implements IAbbonamentoServices {
    private final IAbbonamentoRepository repA;
    private final IClienteRepository repC;

    @Transactional
    @Override
    public AbbonamentoDTO create(AbbonamentoReq req) {
        Cliente cliente = repC.findById(req.getClienteId())
                .orElseThrow(() -> new ApiException("Cliente non trovato"));

        Abbonamento abbonamento = new Abbonamento();
        abbonamento.setCliente(cliente);
        abbonamento.setPiano(req.getPiano().trim());
        abbonamento.setPrezzoMensile(req.getPrezzoMensile());
        abbonamento.setAttivo(true);
        abbonamento.setDataInizio(LocalDate.now());
        return AbbonamentoMap.buildAbbonamentoDTO(repA.save(abbonamento));
    }

    @Override
    public List<AbbonamentoDTO> list(Boolean attivo) {
        List<Abbonamento> abbonamenti;
        if (attivo == null) {
            abbonamenti = repA.findAll();
        } else {
            abbonamenti = repA.findByAttivo(attivo);
        }
        return AbbonamentoMap.buildAbbonamentoDTOList(abbonamenti);
    }

    @Override
    public AbbonamentoDTO getById(Long id) {
        Abbonamento abbonamento = repA.findById(id)
                .orElseThrow(() -> new ApiException("Abbonamento non trovato"));
        return AbbonamentoMap.buildAbbonamentoDTO(abbonamento);
    }

    @Transactional
    @Override
    public AbbonamentoDTO disattiva(Long id) {
        Abbonamento abbonamento = repA.findById(id)
                .orElseThrow(() -> new ApiException("Abbonamento non trovato"));

        if (abbonamento.isAttivo()) {
            abbonamento.setAttivo(false);
            abbonamento.setDataDisattivazione(LocalDate.now());
        }
        return AbbonamentoMap.buildAbbonamentoDTO(abbonamento);
    }

    @Override
    public TotaleMensileDTO totale() {
        return TotaleMensileDTO.builder()
                .totale(repA.totaleAttivi())
                .valuta("EUR")
                .build();
    }
}
