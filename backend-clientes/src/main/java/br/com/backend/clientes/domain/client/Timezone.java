package br.com.backend.clientes.domain.client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Timezone {
    private String offset;
    private String description;
}
