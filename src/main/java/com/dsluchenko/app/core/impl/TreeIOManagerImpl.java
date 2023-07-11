package com.dsluchenko.app.core.impl;

import com.dsluchenko.app.core.io.TreeIOManager;
import com.dsluchenko.app.util.FileLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.logging.Logger;

public final class TreeIOManagerImpl implements TreeIOManager<Node> {
    private static final Logger logger = FileLogger.getFileLogger(TreeIOManagerImpl.class.getName());

    private void writeTree(Node node, int level, Path path) throws IOException {
        logger.finest("node: " + node + "\n" +
                "level: " + level + "\n" +
                "path: " + path);


        if (node != null) {
            if (node instanceof RootNode) {
                Files.writeString(path, (getIndent(level++) + node), StandardOpenOption.APPEND);
                for (Node n : ((RootNode) node).getValue()) {
                    if (n instanceof RootNode) {
                        writeTree(n, level, path);
                    } else if (n instanceof LeafNode) {
                        Files.writeString(path, (getIndent(level) + (LeafNode) n), StandardOpenOption.APPEND);
                    }
                }
            } else if (node instanceof LeafNode) {
                Files.writeString(path, (getIndent(level) + (LeafNode) node), StandardOpenOption.APPEND);
            }
        }
    }

    public void writeToFile(Path pathOutput, Node root) {
        logger.finest("pathOutput :" + pathOutput + "\n" +
                "root :" + root);
        try {
            if (!Files.exists(pathOutput)) {
                Files.createFile(pathOutput);
                logger.info("создан файл :" + pathOutput);
            } else {
                Files.writeString(pathOutput, "");
                logger.info("Файл уже имеется, содержимое файла очищено.");
            }
            int level = 0;
            logger.info("Началась запись в файл: " + pathOutput);
            writeTree(root, level, pathOutput);
            logger.info("Файл записан успешно: " + pathOutput);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getIndent(int level) {
        final String indent = " ";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level * 2; i++) {
            sb.append(indent);
        }
        return sb.toString();
    }
}
