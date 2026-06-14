package br.com.backend.clientes.domain.client;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import br.com.backend.clientes.support.TextUtils;

public enum BrazilianState {
    AC("acre"),
    AL("alagoas"),
    AP("amapa"),
    AM("amazonas"),
    BA("bahia"),
    CE("ceara"),
    DF("distrito federal"),
    ES("espirito santo"),
    GO("goias"),
    MA("maranhao"),
    MT("mato grosso"),
    MS("mato grosso do sul"),
    MG("minas gerais"),
    PA("para"),
    PB("paraiba"),
    PR("parana"),
    PE("pernambuco"),
    PI("piaui"),
    RJ("rio de janeiro"),
    RN("rio grande do norte"),
    RS("rio grande do sul"),
    RO("rondonia"),
    RR("roraima"),
    SC("santa catarina"),
    SP("sao paulo"),
    SE("sergipe"),
    TO("tocantins");

    private static final Map<String, BrazilianState> POR_NOME = Arrays.stream(values())
            .collect(Collectors.toMap(BrazilianState::nomeNormalizado, Function.identity()));

    private final String nomeNormalizado;

    BrazilianState(String nomeNormalizado) {
        this.nomeNormalizado = nomeNormalizado;
    }

    public String sigla() {
        return name();
    }

    public static Optional<BrazilianState> porValor(String value) {
        String valorNormalizado = TextUtils.normalizarTexto(value);

        if (valorNormalizado.length() == 2) {
            try {
                return Optional.of(BrazilianState.valueOf(valorNormalizado.toUpperCase()));
            } catch (IllegalArgumentException ignored) {
                return Optional.empty();
            }
        }

        return Optional.ofNullable(POR_NOME.get(valorNormalizado));
    }

    private String nomeNormalizado() {
        return nomeNormalizado;
    }
}
