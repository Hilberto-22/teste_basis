package br.com.backend.clientes.domain.dto;

public record ClientDetailsResponse(
        String id,
        String name,
        String gender,
        String email,
        String phone,
        String cell,
        String address,
        String city,
        String state,
        String stateCode,
        String postcode,
        String pictureUrl) {
}
