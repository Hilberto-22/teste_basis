package br.com.backend.clientes.infrastructure.client;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import br.com.backend.clientes.domain.client.Client;
import br.com.backend.clientes.domain.client.ClientApiResponse;
import jakarta.annotation.PostConstruct;
import tools.jackson.databind.ObjectMapper;

@Component
public class ClientJsonGateway {

    private final String urlFonte;
    private final ObjectMapper mapeadorObjetos;
    private final ResourceLoader carregadorRecursos;
    private final RestTemplate clienteRest = new RestTemplate();

    private List<Client> clientes = new ArrayList<>();

    public ClientJsonGateway(
            @Value("${clients.source-url}") String urlFonte,
            ObjectMapper mapeadorObjetos,
            ResourceLoader carregadorRecursos) {
        this.urlFonte = urlFonte;
        this.mapeadorObjetos = mapeadorObjetos;
        this.carregadorRecursos = carregadorRecursos;
    }

    @PostConstruct
    public void carregarClientes() {
        ClientApiResponse resposta = lerFonte();
        List<Client> clientesCarregados = Optional.ofNullable(resposta.results())
                .orElseGet(List::of)
                .stream()
                .peek(this::atribuirId)
                .toList();

        this.clientes = clientesCarregados;
    }

    public List<Client> buscarTodos() {
        return clientes;
    }

    public Optional<Client> buscarPorId(String id) {
        return clientes.stream()
                .filter(client -> client.getId().equals(id))
                .findFirst();
    }

    private ClientApiResponse lerFonte() {
        try {
            if (ehLocalizacaoDeRecurso(urlFonte)) {
                Resource recurso = carregadorRecursos.getResource(urlFonte);

                try (InputStream fluxoEntrada = recurso.getInputStream()) {
                    return mapeadorObjetos.readValue(fluxoEntrada, ClientApiResponse.class);
                }
            }

            String conteudo = clienteRest.getForObject(urlFonte, String.class);

            if (!StringUtils.hasText(conteudo)) {
                throw new IllegalStateException("A fonte configurada de clientes retornou um conteudo vazio.");
            }

            return mapeadorObjetos.readValue(conteudo, ClientApiResponse.class);
        } catch (IOException | RestClientException excecao) {
            throw new IllegalStateException("Nao foi possivel carregar o conjunto de clientes.", excecao);
        }
    }

    private boolean ehLocalizacaoDeRecurso(String valor) {
        return StringUtils.hasText(valor)
                && (valor.startsWith("classpath:") || valor.startsWith("file:"));
    }

    private void atribuirId(Client client) {
        String random = client.getEmail() + "|" + client.getRegistered().getDate();
        String idGerado = UUID.nameUUIDFromBytes(random.getBytes(StandardCharsets.UTF_8)).toString();
        client.setId(idGerado);
    }
}
