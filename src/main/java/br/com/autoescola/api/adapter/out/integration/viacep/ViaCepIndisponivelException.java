package br.com.autoescola.api.adapter.out.integration.viacep;

public class ViaCepIndisponivelException extends RuntimeException {
    public ViaCepIndisponivelException(String message, Throwable cause) {
        super(message, cause);
    }
}
