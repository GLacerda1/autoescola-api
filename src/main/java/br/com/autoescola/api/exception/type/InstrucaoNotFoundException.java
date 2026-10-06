package br.com.autoescola.api.exception.type;

public class InstrucaoNotFoundException extends RuntimeException {
    public InstrucaoNotFoundException(String message) {
        super(message);
    }
}
