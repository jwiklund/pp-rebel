package com.polopoly.javarebel.fs;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.zeroturnaround.javarebel.LoggerFactory;

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
            File path = new File(pp + "/" + item.value);
            if ("dir".equals(item.type)) {
                systems.add(new DirFS(path));
            } else if ("less".equals(item.type)) {
                systems.add(new LessFS(path));
            } else if ("js".equals(item.type)) {
                systems.add(new JSFS(path));
            } else {
                LoggerFactory.getInstance().echo("pp-rebel unknown configuration type '" + item.type + "' is ignored");
            }
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
}
