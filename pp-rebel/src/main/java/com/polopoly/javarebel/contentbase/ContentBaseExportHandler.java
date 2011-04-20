package com.polopoly.javarebel.contentbase;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.polopoly.javarebel.fs.FS;
import com.polopoly.javarebel.fs.FSProvider;
import com.polopoly.javarebel.fs.PolopolyFSProvider;

public class ContentBaseExportHandler {

    public static FSProvider provider = new PolopolyFSProvider();
    
    public static boolean exportFileHandled(String externalid, String path, OutputStream out) throws IOException
    {
        if (externalid == null) {
            return false;
        }
        FS fs = provider.getFS(externalid);
        if (fs == null) {
            return false;
        }
        return fs.exportFile(path, out);
    }

    public static Object[] fileInfoHandler(String externalid, String path) throws IOException
    {
        if (externalid == null) {
            return null;
        }
        FS fs = provider.getFS(externalid);
        if (fs == null) {
            return null;
        }
        return fs.getFileInfo(path);
    }

    /*
     * Output
     * Mapping int[] (+ => result, - => left + 1)
     * Results Object[]
     * Left String[]
     */
    public static Object[] fileInfosHandler(String externalid, String paths[]) throws IOException
    {
        if (externalid == null) {
            return null;
        }
        FS fs = provider.getFS(externalid);
        if (fs == null) {
            return null;
        }
        int[] mapping = new int[paths.length];
        List<Object[]> results = new ArrayList<Object[]>();
        List<String> left = new ArrayList<String>();
        
        for (int i = 0 ; i < paths.length ; i++) {
            Object[] res = fs.getFileInfo(paths[i]);
            if (res == null) {
                mapping[i] = -(left.size() + 1);
                left.add(paths[i]);
            } else {
                mapping[i] = results.size();
                results.add(res);
            }
        }
        return new Object[] { mapping, results.toArray(new Object[results.size()]), left.toArray(new String[left.size()]) };
    }
}
