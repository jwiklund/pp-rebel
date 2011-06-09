package com.polopoly.javarebel.staticfiles;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.polopoly.javarebel.fs.FS;
import com.polopoly.javarebel.fs.PolopolyFSProvider;

public class StaticFileFilterHandler {

    public static boolean doFilterHandled(String filterName, ServletRequest request, ServletResponse response) throws IOException
    {
        if (filterName == null) {
            return false;
        }
        FS fs = PolopolyFSProvider.instance().getFilterFS(filterName);
        if (fs == null) {
            return false;
        }
        return fs.exportFile(((HttpServletRequest) request).getRequestURI(), response.getOutputStream());
    }
}
