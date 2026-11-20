package it.mattia.abbonamenti.controllers;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import it.mattia.abbonamenti.dto.output.ErroreDTO;
import it.mattia.abbonamenti.exceptions.ApiException;

@RestControllerAdvice
public class ExceptionManager {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroreDTO> validazione(MethodArgumentNotValidException e) {
        List<String> dettagli = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .toList();
        return ResponseEntity.badRequest()
                .body(ErroreDTO.builder().stato(400).messaggio("Dati non validi").dettagli(dettagli).build());
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErroreDTO> nonTrovato(ApiException e) {
        return ResponseEntity.status(404)
                .body(ErroreDTO.builder().stato(404).messaggio(e.getMessage()).dettagli(List.of()).build());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroreDTO> conflitto() {
        return ResponseEntity.status(409)
                .body(ErroreDTO.builder().stato(409)
                        .messaggio("Dati in conflitto: verifica che l'email non sia gia' presente")
                        .dettagli(List.of()).build());
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErroreDTO> formato() {
        return ResponseEntity.badRequest()
                .body(ErroreDTO.builder().stato(400).messaggio("Formato della richiesta non valido")
                        .dettagli(List.of()).build());
    }
}
