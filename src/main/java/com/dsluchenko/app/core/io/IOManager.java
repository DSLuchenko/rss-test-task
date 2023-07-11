package com.dsluchenko.app.core.io;

import java.nio.file.Path;

public interface IOManager<T> {
    void writeToFile(Path pathOutput, T object);
}
