package com.polopoly.javarebel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.polopoly.javarebel.cfg.Configuration.Item;

public class PolopolyFS implements FS {

    List<FS> systems = new ArrayList<FS>();
    
    public PolopolyFS(List<Item> items)
    {
        String pp = FSUtil.getPolopoly();
        if (pp == null) {
            return ;
        }
        for (Item item : items) {
            systems.add(new DirFS(new File(pp + "/" + item.value)));
        }
    }
    
    public boolean exportFile(String path, OutputStream out) throws IOException
    {
        for (FS fs : systems) {
            if (fs.exportFile(path, out)) {
                return true;
            }
        }
        return false;
    }

    public Object[] getFileInfo(String path) throws IOException
    {
        for (FS fs : systems) {
            Object[] res = fs.getFileInfo(path);
            if (res != null) {
                return res;
            }
        }
        return null;
    }

    public static class DirFS implements FS {
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
}
