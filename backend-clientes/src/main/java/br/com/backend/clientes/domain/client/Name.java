package br.com.backend.clientes.domain.client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Name {
    private String title;
    private String first;
    private String last;

    public String getNomeCompleto() {
        return first + " " + last;
    }
}
