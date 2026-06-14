package br.com.backend.clientes.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.backend.clientes.application.client.ClientNotFoundException;

@RestControllerAdvice
public class ApiExceptionHandler {

    /**
     * Traduz a excecao de cliente nao encontrado para uma resposta HTTP 404 padronizada.
     */
    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> tratarClienteNaoEncontrado(ClientNotFoundException exception) {
        return montarResposta(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    /**
     * Traduz erros de validacao ou parametros invalidos para uma resposta HTTP 400.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> tratarRequisicaoInvalida(IllegalArgumentException exception) {
        return montarResposta(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    /**
     * Monta o corpo padrao de erro da API com timestamp, status e mensagem.
     */
    private ResponseEntity<ApiErrorResponse> montarResposta(HttpStatus status, String message) {
        ApiErrorResponse resposta = new ApiErrorResponse(
                java.time.Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message);

        return ResponseEntity.status(status).body(resposta);
    }
}
