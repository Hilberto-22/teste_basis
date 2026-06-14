package br.com.backend.clientes.resources;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.backend.clientes.application.client.ClientService;
import br.com.backend.clientes.domain.dto.ClientCardResponse;
import br.com.backend.clientes.domain.dto.ClientDetailsResponse;
import br.com.backend.clientes.domain.dto.PageResponse;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService servicoCliente;

    public ClientController(ClientService servicoCliente) {
        this.servicoCliente = servicoCliente;
    }

    @GetMapping
    public PageResponse<ClientCardResponse> listarClientes(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String name) {
        return servicoCliente.listarClientes(page, limit, state, name);
    }

    @GetMapping("/{id}")
    public ClientDetailsResponse buscarClientePorId(@PathVariable String id) {
        return servicoCliente.buscarClientePorId(id);
    }
}
