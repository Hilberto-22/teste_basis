package br.com.backend.clientes.domain.dto;

import java.util.List;

public record PageResponse<T>(
        long total,
        int page,
        int limit,
        List<T> data) {
}
