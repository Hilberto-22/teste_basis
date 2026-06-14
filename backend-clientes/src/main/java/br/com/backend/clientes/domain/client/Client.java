package br.com.backend.clientes.domain.client;

import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Client {

    private String id;
    private String gender;
    private Name name;
    private Location location;
    private String email;
    private DateInfo dob;
    private DateInfo registered;
    private String phone;
    private String cell;
    private Picture picture;

    /**
     * Junta nome e sobrenome do cliente em uma unica string, tratando campos ausentes.
     */
    public String getNomeCompleto() {
        if (name == null) {
            return "";
        }

        String primeiroNome = StringUtils.hasText(name.getFirst()) ? name.getFirst().trim() : "";
        String sobrenome = StringUtils.hasText(name.getLast()) ? name.getLast().trim() : "";

        return (primeiroNome + " " + sobrenome).trim();
    }
}
