package com.polopoly.javarebel.fs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;


public class DirFS implements FS {
    File base;
    public DirFS(File base) {
        this.base = base;
    }

    public boolean exportFile(String path, OutputStream out) throws IOException
    {
        if (base == null) {
            return false;
        }
        File file = new File(base.getAbsolutePath() + "/" + path);
        if (!file.exists() || !file.isFile()) {
            return false;
        }
        FileInputStream fis = new FileInputStream(file);
        try {
            FSUtil.pipe(fis, out);
        } finally {
            fis.close();
        }
        return true;
    }

    public Object[] getFileInfo(String path) throws IOException
    {
        if (base == null) {
            return null;
        }
        File file = new File(base.getAbsoluteFile() + "/" + path);
        if (!file.exists()) {
            return null;
        }
        int index = path.lastIndexOf('/');
        String dir = index == -1 ? "/" : path.substring(0, index);
        String name = path.substring(index+1);
        boolean isDirectory = file.isDirectory();
        long lastModified = file.lastModified();
        long size = file.length();
        return new Object[] { dir, name, isDirectory, lastModified, size } ;
    }
}