package com.polopoly.javarebel.filter;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class JettyTestFilterServlet extends GenericServlet
{
    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException
    {
        res.getOutputStream().println("Hello World! from " + ((HttpServletRequest) req).getRequestURI());
    }
}
