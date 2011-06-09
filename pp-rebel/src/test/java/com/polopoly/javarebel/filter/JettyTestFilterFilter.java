package com.polopoly.javarebel.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class JettyTestFilterFilter implements Filter {
    
    static final long serialVersionUID = 2L;
    
    public void init(FilterConfig filterConfig) throws ServletException
    {
        System.out.println(hashCode() + " " + filterConfig.getFilterName());
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        chain.doFilter(request, response);
    }

    public void destroy()
    {
    }
}
