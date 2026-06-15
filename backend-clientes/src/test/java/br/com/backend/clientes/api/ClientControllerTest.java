package br.com.backend.clientes.api;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "clients.source-url=classpath:clients-test.json")
@AutoConfigureMockMvc
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveListarClientesComFiltrosEPaginacao() throws Exception {
        mockMvc.perform(get("/api/clients")
                        .queryParam("page", "1")
                        .queryParam("limit", "2")
                        .queryParam("state", "SC")
                        .queryParam("name", "ale"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.limit").value(2))
                .andExpect(jsonPath("$.data[0].name").value("Alejandra Rodrigues"))
                .andExpect(jsonPath("$.data[0].stateCode").value("SC"))
                .andExpect(jsonPath("$.data[0].city").value("Umuarama"));
    }

    @Test
    void deveRetornarDetalhesDoClientePorId() throws Exception {
        String id = gerarId("ana.souza@example.com", "2018-02-10T10:00:00Z");

        mockMvc.perform(get("/api/clients/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Ana Souza"))
                .andExpect(jsonPath("$.stateCode").value("SP"))
                .andExpect(jsonPath("$.postcode").value("10101"));
    }

    @Test
    void deveRetornarBadRequestQuandoAPaginacaoForInvalida() throws Exception {
        mockMvc.perform(get("/api/clients")
                        .queryParam("page", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("O parametro 'page' deve ser maior ou igual a 1."));
    }

    @Test
    void deveRetornarListagemVaziaQuandoNenhumClienteCorresponderAosFiltros() throws Exception {
        mockMvc.perform(get("/api/clients")
                        .queryParam("state", "XX")
                        .queryParam("name", "inexistente"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(0))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void deveRetornarNotFoundQuandoBuscarClientePorIdInexistente() throws Exception {
        String idInexistente = UUID.randomUUID().toString();

        mockMvc.perform(get("/api/clients/{id}", idInexistente))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cliente com id '" + idInexistente + "' nao foi encontrado."));
    }

    @Test
    void deveRetornarBadRequestQuandoOParametroPageForMenorQue1() throws Exception {
        mockMvc.perform(get("/api/clients")
                        .queryParam("page", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("O parametro 'page' deve ser maior ou igual a 1."));
    }

    private String gerarId(String email, String registeredDate) {
        String semente = email + "|" + registeredDate;
        return UUID.nameUUIDFromBytes(semente.getBytes(StandardCharsets.UTF_8)).toString();
    }

    public static String gerarIdParaTeste(String email, String registeredDate) {
        String semente = email + "|" + registeredDate;
        return UUID.nameUUIDFromBytes(semente.getBytes(StandardCharsets.UTF_8)).toString();
    }
}
