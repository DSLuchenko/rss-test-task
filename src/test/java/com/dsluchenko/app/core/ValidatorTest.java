package com.dsluchenko.app.core;

import com.dsluchenko.app.core.impl.StringValidatorImpl;
import com.dsluchenko.app.core.validator.StringValidator;

import com.dsluchenko.app.exception.validator.*;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTest {
    private final StringValidator validator = new StringValidatorImpl();

    @Test
    public void checkNodeName_NameIsNullOrEmptyOrBlank() {
        assertThrows(UncorrectedNodeNameRuntimeException.class, () -> validator.checkNodeName(null));
        assertThrows(UncorrectedNodeNameRuntimeException.class, () -> validator.checkNodeName(""));
        assertThrows(UncorrectedNodeNameRuntimeException.class, () -> validator.checkNodeName(" "));

    }

    @Test
    public void checkNodeName_StartsWithDigit() {
        assertThrows(UncorrectedNodeNameRuntimeException.class, () -> validator.checkNodeName("1qwerty"));
        assertThrows(UncorrectedNodeNameRuntimeException.class, () -> validator.checkNodeName("0_qwerty"));
    }

    @Test
    public void checkNodeName_WithUncorrectedSymbols() {
        assertThrows(UncorrectedNodeNameRuntimeException.class, () -> validator.checkNodeName("q1wer*ty"));
        assertThrows(UncorrectedNodeNameRuntimeException.class, () -> validator.checkNodeName("q_qwerty-"));
    }

    @Test
    public void checkNodeName_Corrected() {
        assertDoesNotThrow(() -> validator.checkNodeName("q1werty"));
        assertDoesNotThrow(() -> validator.checkNodeName("q_1werty"));
        assertDoesNotThrow(() -> validator.checkNodeName("_qwerty"));
        assertDoesNotThrow(() -> validator.checkNodeName("q_123121"));
        assertDoesNotThrow(() -> validator.checkNodeName("_123121q"));
    }

    @Test
    public void checkInputString_InputStringNullOrEmptyOrBlank() {
        assertThrows(UncorrectedFileStructureRuntimeException.class, () -> validator.checkInputData(null));
        assertThrows(UncorrectedFileStructureRuntimeException.class, () -> validator.checkInputData(""));
        assertThrows(UncorrectedFileStructureRuntimeException.class, () -> validator.checkInputData(" "));
    }

    @Test
    public void checkInputString_WithoutSplitCharEqualsSign() {
        assertThrows(UncorrectedFileStructureRuntimeException.class, () -> validator.checkInputData("type \"tetrahedron\" "));
        assertThrows(UncorrectedFileStructureRuntimeException.class, () -> validator.checkInputData("node is value_123"));
        assertThrows(UncorrectedFileStructureRuntimeException.class, () -> validator.checkInputData("node {}"));
        assertThrows(UncorrectedFileStructureRuntimeException.class, () -> validator.checkInputData("_node_name {"));
    }

    @Test
    public void checkInputString_Corrected() {
        assertDoesNotThrow(() -> validator.checkInputData("s hape = { "));
        assertDoesNotThrow(() -> validator.checkInputData("type = \"tetrahedron\" "));
        assertDoesNotThrow(() -> validator.checkInputData("   vertices = {"));
        assertDoesNotThrow(() -> validator.checkInputData("point = { x = \"1\" y = \"0\" z = \"0\" } "));
        assertDoesNotThrow(() -> validator.checkInputData("color = { r = \"0xFF\" g = \"0x00\" b = \"0x80\" alpha = \"0x80\" }"));
        assertDoesNotThrow(() -> validator.checkInputData("   }   "));
    }

    @Test
    public void checkNodeValue_ValueIsNullOrEmptyOrBlank() {
        assertThrows(UncorrectedNodeValueRuntimeException.class, () -> validator.checkNodeValue(null));
        assertThrows(UncorrectedNodeValueRuntimeException.class, () -> validator.checkNodeValue(""));
        assertThrows(UncorrectedNodeValueRuntimeException.class, () -> validator.checkNodeValue(" "));
    }

    @Test
    public void checkNodeValue_ValueUncorrectedStartSymbol() {
        assertThrows(UncorrectedNodeValueRuntimeException.class, () -> validator.checkNodeValue("="));
        assertThrows(UncorrectedNodeValueRuntimeException.class, () -> validator.checkNodeValue("qwerty"));
        assertThrows(UncorrectedNodeValueRuntimeException.class, () -> validator.checkNodeValue("\"1qwerty"));
    }

    @Test
    public void checkNodeValue_Corrected() {
        assertDoesNotThrow(() -> validator.checkNodeValue("{name=\"sds\"}"));
        assertDoesNotThrow(() -> validator.checkNodeValue("\"121sadas\""));
        assertDoesNotThrow(() -> validator.checkNodeValue("{"));
    }
}