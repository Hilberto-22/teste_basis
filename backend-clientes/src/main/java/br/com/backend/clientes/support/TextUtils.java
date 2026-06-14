package br.com.backend.clientes.support;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

public final class TextUtils {

    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");

    private TextUtils() {
    }

    /**
     * Remove acentos, espacos excedentes e diferencas de caixa para facilitar comparacoes.
     */
    public static String normalizarTexto(String value) {
        if (!StringUtils.hasText(value)) {
            return "";
        }

        String textoSemAcentos = Normalizer.normalize(value.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");

        return textoSemAcentos
                .replaceAll("\\s+", " ")
                .toLowerCase(Locale.ROOT);
    }

    public static String paraTitulo(String value) {
        if (!StringUtils.hasText(value)) {
            return "";
        }

        return Arrays.stream(value.trim().replaceAll("\\s+", " ").split(" "))
                .map(TextUtils::capitalizar)
                .collect(Collectors.joining(" "));
    }

    private static String capitalizar(String value) {
        if (value.isBlank()) {
            return value;
        }

        String primeiraLetra = value.substring(0, 1).toUpperCase(PT_BR);
        String restante = value.substring(1).toLowerCase(PT_BR);

        return primeiraLetra + restante;
    }
}
