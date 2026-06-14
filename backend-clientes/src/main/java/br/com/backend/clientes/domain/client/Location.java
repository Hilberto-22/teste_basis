package br.com.backend.clientes.domain.client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Location {
    private String street;
    private String city;
    private String state;
    private Integer postcode;
    private Coordinates coordinates;
    private Timezone timezone;

}
