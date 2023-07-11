package com.dsluchenko.app.core.validator;

public interface Validator<T> {
    void checkInputData(final T inputData);

}
