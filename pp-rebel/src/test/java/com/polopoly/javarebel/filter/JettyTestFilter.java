package com.polopoly.javarebel.filter;

import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;

public class JettyTestFilter {

    /*
     * -noverify -javaagent:$JREBLE_HOME/jrebel.jar
     * -DPP_HOME=${resource_loc:/pp-rebel/src/test/java/com/polopoly/javarebel/filter/ex}
     * -Drebel.plugins=${resource_loc:/pp-rebel/target/pp-rebel-1.0-SNAPSHOT-jar-with-dependencies.jar}
     * -Drebel.pp-rebel=true
     */
    public static void main(String[] args) throws Exception
    {
        Context context = new Context();
        context.addServlet(JettyTestFilterServlet.class, "/*");
        context.addFilter(JettyTestFilterFilter.class, "/*", Handler.DEFAULT);
        Server server = new Server(9090);
        server.addHandler(context);
        server.start();
        server.join();
    }
}
