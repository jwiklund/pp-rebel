package com.polopoly.javarebel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FSUtil {
    
    public static String getPolopoly() {
        return System.getProperty("PP_HOME");
    }

    public static void pipe(InputStream read, OutputStream write) throws IOException
    {
        byte[] bytes = new byte[1024];
        int count;
        while ((count = read.read(bytes)) > 0) {
            write.write(bytes, 0, count);
        }
        write.flush();
        write.close();
        read.close();
    }
}
