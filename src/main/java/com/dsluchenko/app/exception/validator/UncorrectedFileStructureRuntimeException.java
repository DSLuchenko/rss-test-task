package com.dsluchenko.app.exception.validator;

public class UncorrectedFileStructureRuntimeException extends RuntimeException {
    public UncorrectedFileStructureRuntimeException(String errorMessage) {
        super(errorMessage);
    }
}
