package com.dsluchenko.app.core.impl;

import com.dsluchenko.app.core.*;
import com.dsluchenko.app.core.io.TreeIOManager;
import com.dsluchenko.app.core.validator.StringValidator;
import com.dsluchenko.app.exception.parser.UncorrectedInputParametersRuntimeException;
import com.dsluchenko.app.exception.validator.*;
import com.dsluchenko.app.util.FileLogger;
import com.dsluchenko.app.util.Separator;

import java.io.BufferedReader;
import java.nio.file.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;


public final class ParserImpl implements Parser {
    private static final Logger logger = FileLogger.getFileLogger(ParserImpl.class.getName());
    private final TreeIOManager<Node> ioManager;
    private final StringValidator validator;
    private int id;

    public ParserImpl() {
        this.ioManager = new TreeIOManagerImpl();
        this.validator = new StringValidatorImpl();
        this.id = 0;
    }


    public void run(String[] inputParameters) {
        logger.finest("inputParameters: " + Arrays.toString(inputParameters));

        if (inputParameters.length < 2) {
            logger.log(Level.SEVERE, "Ошибка входных параметров", new UncorrectedInputParametersRuntimeException());
            System.out.println("Передайте первым аргументом название входного файла, вторым название выходного файла");
        } else {
            final String inputFileName = inputParameters[0];
            final String outPutFileName = inputParameters[1];

            logger.info("Считано имя входного файла: " + inputFileName);
            logger.info("Считано имя выходного файла: " + outPutFileName);

            Path pathInput, pathOutput;
            try {
                pathInput = Path.of(inputFileName);
                Node root = this.parse(pathInput);

                pathOutput = Path.of(outPutFileName);

                ioManager.writeToFile(pathOutput, root);
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Ошибка доступа к файлу", ex);
                System.out.println("Ошибка в данных");
            }
            System.out.println("Программа завершила работу");
        }
    }

    private Node parse(final Path PathInput) {
        logger.finest("PathInput :" + PathInput);

        Node root = null;
        RootNode currentParrent = null;
        String nodeName, nodeValue, stringFromFile;

        try (BufferedReader reader = Files.newBufferedReader(PathInput)) {
            logger.info("Началась загрузка файла в память: " + PathInput.getFileName());
            while (true) {
                stringFromFile = reader.readLine();
                if (stringFromFile == null ||
                        stringFromFile.isEmpty() ||
                        stringFromFile.isBlank()) {
                    break;
                }
                stringFromFile = stringFromFile.trim();
                validator.checkInputData(stringFromFile);
                if (!nodeValueIsEndList(stringFromFile)) {
                    //при первой итерации создаем корень всего дерева
                    if (root == null) {
                        nodeName = getNodeName(stringFromFile);
                        nodeValue = substringFromEqualsSignToEnd(stringFromFile);
                        if (!nodeValueIsStartList(nodeValue)) {
                            root = LeafNode.createLeaf(id++, currentParrent, nodeName, getOneSimpleNodeValue(nodeValue));
                            continue;
                        } else {
                            root = RootNode.createRoot(id++, nodeName);
                            currentParrent = (RootNode) root;
                        }
                        //переходим на новую строку
                        stringFromFile = "";
                    }
                    if (currentParrent == null) {
                        throw new UncorrectedFileStructureRuntimeException("Некорректный файл. Некоторые узлы не относятся к корню дерева");
                    }
                    //считываем все значения списка перемещая курсор
                    //по считываемой строке на длину считанного значение
                    //пока строка не будет пустой
                    while (!stringFromFile.isEmpty()) {
                        stringFromFile = stringFromFile.trim();
                        nodeName = getNodeName(stringFromFile);
                        nodeValue = substringFromEqualsSignToEnd(stringFromFile);
                        if (!nodeValueIsStartList(nodeValue)) {
                            nodeValue = getOneSimpleNodeValue(nodeValue);
                            LeafNode leaf = LeafNode.createLeaf(id++, currentParrent, nodeName, nodeValue);
                            currentParrent
                                    .getValue()
                                    .add(leaf);
                            stringFromFile = substringFromCurrentValue(stringFromFile, nodeValue);
                        } else {
                            //создаем родительский узел
                            RootNode newParrentNode = RootNode.createParrent(id++, currentParrent, nodeName);
                            currentParrent
                                    .getValue()
                                    .add(newParrentNode);
                            currentParrent = newParrentNode;
                            stringFromFile = substringFromCurrentValue(stringFromFile, nodeValue);
                        }
                        // если вся строка обработана и остался символ закрытия списка
                        // переключаемся на предыдущего родителя
                        if (nodeValueIsEndList(stringFromFile)) {
                            currentParrent = currentParrent.getParrent();
                            break;
                        }
                    }
                } else {
                    //вся строка это символ окончания списка
                    currentParrent = currentParrent.getParrent();
                }
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Ошибка в данных", ex);
            System.out.println("Ошибка в данных");
        }

        logger.info("Файл загружен в память, количество элементов :" + this.id);
        return root;
    }

    private String substringFromEqualsSignToEnd(final String stringFromFile) {
        logger.finest("stringFromFile: " + stringFromFile);

        String nodeValue = stringFromFile.substring(stringFromFile.indexOf(Separator.EQUALS_SIGN_DEC_CODE) + 1).trim();
        validator.checkNodeValue(nodeValue);

        logger.finest("return: " + nodeValue);
        return nodeValue;
    }

    private String getNodeName(final String stringFromFile) {
        logger.finest("stringFromFile: " + stringFromFile);

        String nodeName = stringFromFile.substring(0, stringFromFile.indexOf(Separator.EQUALS_SIGN_DEC_CODE)).trim();
        validator.checkNodeName(nodeName);

        logger.finest("return: " + nodeName);
        return nodeName;
    }

    private String substringFromCurrentValue(final String stringFromFile, final String nodeValue) {
        logger.finest("stringFromFile: " + stringFromFile + "\n" +
                "nodeValue: " + nodeValue);

        String stringFromCurrentValue;
        if (nodeValue.codePointAt(0) == Separator.START_LIST_DEC_CODE) {
            stringFromCurrentValue = nodeValue.substring(
                    nodeValue.indexOf(Separator.START_LIST_DEC_CODE) + 1).trim();
        } else {

            int indexStartValue = stringFromFile.indexOf(
                    nodeValue +
                            (char) Separator.DOUBLE_QUOTATION_MARK_DEC_CODE);
            int indexEndValue = nodeValue.length() + 1;
            stringFromCurrentValue = stringFromFile
                    .substring(indexStartValue + indexEndValue).trim();
        }
        logger.finest("return: " + stringFromCurrentValue);
        return stringFromCurrentValue;
    }

    private String getOneSimpleNodeValue(final String stringFromEqualsSignToEnd) {
        logger.finest("stringFromEqualsSignToEnd: " + stringFromEqualsSignToEnd);

        int indexOfSymbolEndValue = stringFromEqualsSignToEnd
                .substring(1)
                .indexOf(Separator.DOUBLE_QUOTATION_MARK_DEC_CODE);
        String nodeValue = stringFromEqualsSignToEnd
                .substring(1, indexOfSymbolEndValue + 1).trim();

        logger.finest("return: " + nodeValue);

        return nodeValue;
    }

    private boolean nodeValueIsStartList(final String nodeValue) {
        logger.finest("nodeValue: " + nodeValue);

        boolean isStartList = nodeValue.codePointAt(0) == Separator.START_LIST_DEC_CODE;

        logger.finest("return: " + isStartList);

        return isStartList;
    }

    private boolean nodeValueIsEndList(final String nodeValue) {
        logger.finest("nodeValue: " + nodeValue);

        boolean isEndList = nodeValue.length() == 1 &&
                nodeValue.codePointAt(0) == Separator.END_LIST_DEC_CODE;

        logger.finest("return: " + isEndList);

        return isEndList;
    }
}
