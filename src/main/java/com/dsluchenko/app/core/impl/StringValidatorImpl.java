package com.dsluchenko.app.core.impl;

import com.dsluchenko.app.core.validator.StringValidator;

import com.dsluchenko.app.exception.validator.*;
import com.dsluchenko.app.util.FileLogger;
import com.dsluchenko.app.util.Separator;

import java.util.logging.Logger;

public final class StringValidatorImpl implements StringValidator {
    private static final Logger logger = FileLogger.getFileLogger(StringValidatorImpl.class.getName());

    public void checkInputData(final String inputData) {
        logger.finest("inputData: " + inputData);

        if (inputData == null ||
                inputData.isBlank() ||
                inputData.isEmpty()) {
            throw new UncorrectedFileStructureRuntimeException("Некорректная структура файла");
        }
        String trimmedString = inputData.trim();
        int splitIndex = trimmedString.indexOf("=");

        if (!(trimmedString.length() == 1 && trimmedString.codePointAt(0) == Separator.END_LIST_DEC_CODE) //проверяем если эта строка отвечает за закрытие блока
                && splitIndex == -1 //если нет хотя бы одного символа равенства, то строка некорректна
        ) {
            throw new UncorrectedFileStructureRuntimeException("В строке отсутствует символ равенства: \n" + inputData);
        }

    }

    public void checkNodeName(final String name) {
        logger.finest("name: " + name);

        if (name == null ||
                name.isBlank() ||
                name.isEmpty()) {
            throw new UncorrectedNodeNameRuntimeException("Имя узла не может быть пустым");
        }

        String trimmedName = name.trim();

        //имя узла не может начинаться с цифры
        if (Character.isDigit(trimmedName.charAt(0))) {
            throw new UncorrectedNodeNameRuntimeException("Имя узла не может начинаться с цифры:\n" + name);
        }

        for (byte b : trimmedName.getBytes()) {
            if (!Character.isLetterOrDigit(b) & b != Separator.LOW_LINE_DEC_CODE) {
                throw new UncorrectedNodeNameRuntimeException("Имя узла строка из букв, цифр, и символа '_', начинающаяся не с цифры:\n" + name);
            }
        }
    }

    public void checkNodeValue(final String value) {
        logger.finest("value: " + value);

        if (value == null ||
                value.isBlank() ||
                value.isEmpty()) {
            throw new UncorrectedNodeValueRuntimeException("Значение узла не может быть пустым");
        }
        String trimmedValue = value.trim();

        if (trimmedValue.codePointAt(0) != Separator.DOUBLE_QUOTATION_MARK_DEC_CODE &&
                trimmedValue.codePointAt(0) != Separator.START_LIST_DEC_CODE) {
            throw new UncorrectedNodeValueRuntimeException("Некорректная структура значения узла: \n" + value);
        }
        if (trimmedValue.codePointAt(0) == Separator.DOUBLE_QUOTATION_MARK_DEC_CODE &&
                trimmedValue.substring(1).indexOf((char) Separator.DOUBLE_QUOTATION_MARK_DEC_CODE) == -1) {
            throw new UncorrectedNodeValueRuntimeException("На найден символ окончания значения узла [\"]: \n" + value);
        }
    }
}
