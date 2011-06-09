package com.polopoly.javarebel.staticfiles;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletResponse;

public class LazyOutputStream extends OutputStream {

    private final ServletResponse response;
    private OutputStream backer;

    public LazyOutputStream(ServletResponse response)
    {
        this.response = response;
    }

    public void write(int b) throws IOException
    {
        initBacker();
        backer.write(b);
    }

    public void write(byte[] b) throws IOException
    {
        initBacker();
        backer.write(b);
    }

    public void write(byte[] b, int off, int len) throws IOException
    {
        initBacker();
        backer.write(b, off, len);
    }

    public void flush() throws IOException
    {
        initBacker();
        backer.flush();
    }

    public void close() throws IOException
    {
        initBacker();
        backer.close();
    }

    private void initBacker() throws IOException
    {
        if (backer == null) {
            backer = response.getOutputStream();
        }
    }
}
