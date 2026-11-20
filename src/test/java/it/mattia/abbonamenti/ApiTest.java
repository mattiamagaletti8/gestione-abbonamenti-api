package it.mattia.abbonamenti;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test") @Transactional
class ApiTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    private long cliente() throws Exception {
        var result = mvc.perform(post("/api/clienti").contentType("application/json")
                .content("{\"nome\":\"Cliente Test\",\"email\":\"test@example.com\"}"))
                .andExpect(status().isCreated()).andReturn();
        return json.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }
    @Test void totalePersistenzaDisattivazione() throws Exception {
        long c = cliente();
        var result = mvc.perform(post("/api/abbonamenti").contentType("application/json")
                .content("{\"clienteId\":"+c+",\"piano\":\"Mensile\",\"prezzoMensile\":19.90}"))
                .andExpect(status().isCreated()).andReturn();
        long a = json.readTree(result.getResponse().getContentAsString()).get("id").asLong();
        mvc.perform(get("/api/abbonamenti/totale-mensile")).andExpect(jsonPath("$.totale").value(19.90));
        mvc.perform(patch("/api/abbonamenti/"+a+"/disattiva")).andExpect(status().isOk()).andExpect(jsonPath("$.attivo").value(false));
        mvc.perform(patch("/api/abbonamenti/"+a+"/disattiva")).andExpect(status().isOk());
        mvc.perform(get("/api/abbonamenti?attivo=true")).andExpect(jsonPath("$.length()").value(0));
        mvc.perform(get("/api/abbonamenti/totale-mensile")).andExpect(jsonPath("$.totale").value(0));
    }
    @Test void rifiutaPrezzoNegativoEdEmailErrata() throws Exception {
        mvc.perform(post("/api/clienti").contentType("application/json").content("{\"nome\":\"\",\"email\":\"no\"}"))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/api/abbonamenti").contentType("application/json").content("{\"clienteId\":1,\"piano\":\"Base\",\"prezzoMensile\":-1}"))
                .andExpect(status().isBadRequest());
    }
    @Test void riferimentiInesistenti() throws Exception {
        mvc.perform(get("/api/abbonamenti/999999")).andExpect(status().isNotFound());
        mvc.perform(post("/api/abbonamenti").contentType("application/json").content("{\"clienteId\":999999,\"piano\":\"Base\",\"prezzoMensile\":10}"))
                .andExpect(status().isNotFound());
    }
    @Test void rifiutaEmailDuplicata() throws Exception {
        cliente();
        mvc.perform(post("/api/clienti").contentType("application/json").content("{\"nome\":\"Altro\",\"email\":\"test@example.com\"}"))
                .andExpect(status().isConflict());
    }
}
