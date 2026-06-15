package br.com.backend.clientes.application.client;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.com.backend.clientes.api.ClientNotFoundException;
import br.com.backend.clientes.domain.client.BrazilianState;
import br.com.backend.clientes.domain.client.Client;
import br.com.backend.clientes.domain.dto.ClientCardResponse;
import br.com.backend.clientes.domain.dto.ClientDetailsResponse;
import br.com.backend.clientes.domain.dto.PageResponse;
import br.com.backend.clientes.infrastructure.client.ClientJsonGateway;
import br.com.backend.clientes.support.TextUtils;

@Service
public class ClientService {

    private static final int PAGINA_PADRAO = 1;
    private static final int LIMITE_PADRAO = 9;

    private final ClientJsonGateway gatewayJsonCliente;
    private final ClientResponseMapper mapeadorRespostaCliente;

    
    public ClientService(ClientJsonGateway gatewayJsonCliente, ClientResponseMapper mapeadorRespostaCliente) {
        this.gatewayJsonCliente = gatewayJsonCliente;
        this.mapeadorRespostaCliente = mapeadorRespostaCliente;
    }

    public PageResponse<ClientCardResponse> listarClientes(Integer page, Integer limit, String state, String name) {
        int paginaResolvida = page == null ? PAGINA_PADRAO : page;
        int limiteResolvido = limit == null ? LIMITE_PADRAO : limit;

        validarPaginacao(paginaResolvida, limiteResolvido);

        List<Client> clientesFiltrados = gatewayJsonCliente.buscarTodos()
                .stream()
                .filter(client -> correspondeEstado(client, state))
                .sorted((c1, c2) -> c1.getNomeCompleto().compareToIgnoreCase(c2.getNomeCompleto()))
                .filter(client -> correspondeNome(client, name))
                .toList();

        List<ClientCardResponse> dados = clientesFiltrados.stream()
                .skip((long) (paginaResolvida - 1) * limiteResolvido)
                .limit(limiteResolvido)
                .map(mapeadorRespostaCliente::respostaParaCard)
                .toList();

        return new PageResponse<>(clientesFiltrados.size(), paginaResolvida, limiteResolvido, dados);
    }

    public ClientDetailsResponse buscarClientePorId(String id) {
        var cliente = gatewayJsonCliente.buscarPorId(id)
                .orElseThrow(() -> new ClientNotFoundException(id));

        return mapeadorRespostaCliente.respostaParaDetalhesClientes(cliente);
    }
    
    private void validarPaginacao(int page, int limit) {
        if (page < 1) {
            throw new IllegalArgumentException("O parametro 'page' deve ser maior ou igual a 1.");
        }

        if (limit < 1) {
            throw new IllegalArgumentException("O parametro 'limit' deve ser maior ou igual a 1.");
        }
    }

    private boolean correspondeEstado(Client client, String state) {
        if (!StringUtils.hasText(state)) {
            return true;
        }

        String consultaNormalizada = TextUtils.normalizarTexto(state);
        String estadoClienteNormalizado = TextUtils.normalizarTexto(client.getLocation().getState());
        String siglaEstadoCliente = BrazilianState.porValor(client.getLocation().getState())
                .map(BrazilianState::sigla)
                .orElse("");

        return estadoClienteNormalizado.equals(consultaNormalizada)
                || siglaEstadoCliente.equalsIgnoreCase(state.trim());
    }

    private boolean correspondeNome(Client client, String name) {
        if (!StringUtils.hasText(name)) {
            return true;
        }

        String consultaNormalizada = TextUtils.normalizarTexto(name);
        String nomeClienteNormalizado = TextUtils.normalizarTexto(client.getNomeCompleto());

        return nomeClienteNormalizado.contains(consultaNormalizada);
    }
}
