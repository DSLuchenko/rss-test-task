package com.dsluchenko.app.exception.validator;

public class UncorrectedNodeValueRuntimeException extends RuntimeException {
    public UncorrectedNodeValueRuntimeException(String errorMessage) {
        super(errorMessage);
    }
}
