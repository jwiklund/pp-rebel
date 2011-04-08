package com.polopoly.javarebel.fs;

import java.io.IOException;
import java.io.OutputStream;

public interface FS {

    Object[] getFileInfo(String path) throws IOException;
    boolean exportFile(String path, OutputStream out) throws IOException;
}
