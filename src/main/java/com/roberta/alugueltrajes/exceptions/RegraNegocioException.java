package com.roberta.alugueltrajes.exceptions;

public class RegraNegocioException extends Exception {
    public RegraNegocioException() {
        super();
    }

    public RegraNegocioException(String message) {
        super(message);
    }

    public RegraNegocioException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegraNegocioException(Throwable cause) {
        super(cause);
    }
}
