package com.polopoly.javarebel.fs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.tools.shell.Global;

public class JSFS implements FS {

    File pack;
    JSConfig config = new JSConfig();
    
    public JSFS(File pack)
    {
        this.pack = pack;
    }

    public Object[] getFileInfo(String path) throws IOException
    {
        if (path == null) {
            return null;
        }
        int index = path.lastIndexOf('/');
        String dir = index == -1 ? "/" : path.substring(0, index);
        String name = path.substring(index+1);
        boolean isDirectory = false;
        long lastModified = getLastModified(path);
        long size = getFileContent(path).length;
        return new Object[] { dir, name, isDirectory, lastModified, size } ;
    }

    public boolean exportFile(String path, OutputStream out) throws IOException
    {
        byte[] file = getFileContent(path);
        if (file == null) {
            return false;
        }
        out.write(file);
        return true;
    }
    
    private long getLastModified(String path) throws IOException
    {
        long lastModified = pack.lastModified();
        for (File file : getFiles(path)) {
            long thisModified = file.lastModified();
            if (thisModified > lastModified) {
                lastModified = thisModified;
            }
        }
        return lastModified;
    }
    
    private byte[] getFileContent(String path) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        boolean first = true;
        for (File file : getFiles(path)) {
            if (first) {
                first = false;
            } else {
                baos.write((int) '\n');
            }
            FSUtil.pipe(new FileInputStream(file), baos);
        }
        return baos.toByteArray();
    }

    public File[] getFiles(String path) throws IOException 
    {
        return config.getFiles(pack, path);
    }

    private static class JSConfig {
        private Scriptable scope;

        public JSConfig() {
            Context cx = Context.enter();
            cx.setOptimizationLevel(9);
            Global global = new Global();
            global.init(cx);          
            scope = cx.initStandardObjects(global);
            Context.exit();
        }

        public File[] getFiles(File source, String uri) throws IOException
        {
            String absolute = source.getParentFile().getAbsolutePath();
            int start = 0;
            if (uri.startsWith("/")) { 
                start = 1;
            }
            int end = uri.length();
            if (uri.endsWith(".js")) {
                end = end - 3;
            }
            String name = uri.substring(start, end);
            List<String> files = new ArrayList<String>();
            synchronized (this) {
                Context cx = Context.enter();
                cx.evaluateReader(scope, new FileReader(source), source.getName(), 0, null);
                NativeArray deps = (NativeArray) cx.evaluateString(scope, "deps['"+name+"']", "getFiles(" + name + ")", 1, null);
                for (int i = 0 ; i < deps.getLength() ; i++) {
                    files.add((String) deps.get(i, scope));
                }
                Context.exit();
            }
            File[] result = new File[files.size()];
            for (int i = 0 ; i < result.length ; i++) {
                result[i] = new File(absolute + "/" + files.get(i) + ".js");
            }
            return result;
        }
    }
}
