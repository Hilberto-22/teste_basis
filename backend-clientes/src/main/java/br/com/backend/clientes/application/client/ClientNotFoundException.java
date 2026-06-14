package br.com.backend.clientes.application.client;

public class ClientNotFoundException extends RuntimeException {

    /**
     * Cria a excecao usada quando um cliente nao e encontrado pelo ID solicitado.
     */
    public ClientNotFoundException(String id) {
        super("Cliente com id '%s' nao foi encontrado.".formatted(id));
    }
}
