package com.polopoly.javarebel.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.polopoly.javarebel.fs.FSUtil;

public class JettyTestFilterFilter extends JettyTestIntermediateFilter {
    
    public void init(FilterConfig filterConfig) throws ServletException
    {
        System.out.println("JettyTestFilter.name " + filterConfig.getFilterName());
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        if (((HttpServletRequest) request).getRequestURI().endsWith("somethings.css")) {
            FSUtil.pipe(JettyTestFilterFilter.class.getResourceAsStream("ex/hello.css"), response.getOutputStream());
        } else {
            chain.doFilter(request, response);
        }
    }

    public void destroy()
    {
    }
}
