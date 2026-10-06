package br.com.autoescola.api.adapter.out.integration.viacep;

public class ViaCepNotFoundException extends RuntimeException {
    public ViaCepNotFoundException(String message) {
        super(message);
    }
}
