package br.com.backend.clientes.application.client;

import org.springframework.stereotype.Component;

import br.com.backend.clientes.domain.client.BrazilianState;
import br.com.backend.clientes.domain.client.Client;
import br.com.backend.clientes.domain.client.Location;
import br.com.backend.clientes.domain.dto.ClientCardResponse;
import br.com.backend.clientes.domain.dto.ClientDetailsResponse;
import br.com.backend.clientes.support.TextUtils;

@Component
public class ClientResponseMapper {

    public ClientCardResponse respostaParaCard(Client client) {
        return new ClientCardResponse(
                client.getId(),
                TextUtils.paraTitulo(client.getNomeCompleto()),
                client.getEmail(),
                formatarCidade(client.getLocation()),
                formatarEstado(client.getLocation()),
                resolverSiglaEstado(client.getLocation()),
                client.getPicture() != null ? client.getPicture().getMedium() : null);
    }

    public ClientDetailsResponse respostaParaDetalhesClientes(Client client) {
        return new ClientDetailsResponse(
                client.getId(),
                TextUtils.paraTitulo(client.getNomeCompleto()),
                client.getGender(),
                client.getEmail(),
                client.getPhone(),
                client.getCell(),
                formatarEndereco(client.getLocation()),
                formatarCidade(client.getLocation()),
                formatarEstado(client.getLocation()),
                resolverSiglaEstado(client.getLocation()),
                formatarCep(client.getLocation()),
                client.getPicture() != null ? client.getPicture().getLarge() : null);
    }

    private String formatarEndereco(Location location) {
        return location == null ? "" : TextUtils.paraTitulo(location.getStreet());
    }

    private String formatarCidade(Location location) {
        return location == null ? "" : TextUtils.paraTitulo(location.getCity());
    }

    private String formatarEstado(Location location) {
        return location == null ? "" : TextUtils.paraTitulo(location.getState());
    }

    private String formatarCep(Location location) {
        return location == null || location.getPostcode() == null
                ? null
                : String.valueOf(location.getPostcode());
    }

    private String resolverSiglaEstado(Location location) {
        if (location == null) {
            return null;
        }

        return BrazilianState.porValor(location.getState())
                .map(BrazilianState::sigla)
                .orElse(null);
    }
}
