package com.dsluchenko.app.core.validator;

public interface StringValidator extends Validator<String> {
    void checkNodeName(final String name);

    void checkNodeValue(final String value);
}
