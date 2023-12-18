package com.roberta.alugueltrajes.exceptions;

public class NaoEncontradoException extends Exception {

    public NaoEncontradoException() {
        super();
    }

    public NaoEncontradoException(String message) {
        super(message);
    }

    public NaoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }

    public NaoEncontradoException(Throwable cause) {
        super(cause);
    }
}