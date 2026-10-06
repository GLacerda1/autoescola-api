package br.com.autoescola.api.exception.type;

public class InstrutorNotFoundException extends RuntimeException {
    public InstrutorNotFoundException(String message) {
        super(message);
    }
}