package com.polopoly.javarebel.fs;


public interface FSProvider {

    public FS getContentFS(String externalid);
    public FS getFilterFS(String filtername);
}
