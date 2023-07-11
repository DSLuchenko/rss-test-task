package com.dsluchenko.app.util;

import java.io.IOException;

import java.util.logging.*;

import static java.lang.System.currentTimeMillis;


public abstract class FileLogger {
    private static FileHandler fileHandler;

    public static Logger getFileLogger(String name) {
        fileHandler = getFileHandler();
        Logger logger = Logger.getLogger(name);
        logger.addHandler(fileHandler);
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.FINEST);
        return logger;
    }

    private static FileHandler getFileHandler() {
        if (fileHandler == null) {
            try {
                fileHandler = new FileHandler("./parser_" + currentTimeMillis() + ".log");
                fileHandler.setFormatter(new SimpleFormatter());
                fileHandler.setLevel(Level.FINEST);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fileHandler;
    }

}
