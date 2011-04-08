package com.polopoly.javarebel.fs;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class JSFS implements FS {

    public JSFS(File path)
    {
    }

    public Object[] getFileInfo(String path) throws IOException
    {
        return null;
    }

    public boolean exportFile(String path, OutputStream out) throws IOException
    {
        return false;
    }

}
