package br.com.backend.clientes.domain.dto;

public record ClientCardResponse(
        String id,
        String name,
        String email,
        String city,
        String state,
        String stateCode,
        String pictureUrl) {
}
