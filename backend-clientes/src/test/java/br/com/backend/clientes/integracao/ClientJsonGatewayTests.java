package br.com.backend.clientes.integracao;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;

import br.com.backend.clientes.domain.client.Client;
import br.com.backend.clientes.infrastructure.client.ClientJsonGateway;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest(properties = "clients.source-url=classpath:clients-test.json")
class ClientJsonGatewayTests {
    
    @Autowired
    private ClientJsonGateway gateway;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    @DisplayName("Deve carregar clientes com sucesso a partir de um recurso JSON")
    void deveCarregarClientesComSucesso() {
        gateway.carregarClientes();
        List<Client> clientes = gateway.buscarTodos();

        assertNotNull(clientes);
        assertFalse(clientes.isEmpty());
    }

    @Test
    @DisplayName("Deve buscar um cliente por ID corretamente após o carregamento")
    void deveBuscarClientePorId() {
        gateway.carregarClientes();
        List<Client> todos = gateway.buscarTodos();
        String idExistente = todos.get(0).getId();

        Optional<Client> clienteEncontrado = gateway.buscarPorId(idExistente);

        assertTrue(clienteEncontrado.isPresent());
        assertEquals(idExistente, clienteEncontrado.get().getId());
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar um ID inexistente")
    void deveRetornarVazioParaIdInexistente() {
        gateway.carregarClientes();
        Optional<Client> clienteEncontrado = gateway.buscarPorId("id-nao-existente");

        assertTrue(clienteEncontrado.isEmpty());
    }

    @Test
    @DisplayName("Deve lançar exceção quando a URL da fonte for inválida ou inexistente")
    void deveLancarExcecaoParaFonteInvalida() {
        ClientJsonGateway gatewayInvalido = new ClientJsonGateway("classpath:arquivo-fantasma.json", objectMapper, resourceLoader);
        
        assertThrows(IllegalStateException.class, gatewayInvalido::carregarClientes);
    }

}
