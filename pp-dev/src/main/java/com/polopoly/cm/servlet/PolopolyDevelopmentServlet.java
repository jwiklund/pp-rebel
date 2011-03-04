package com.polopoly.cm.servlet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.tools.shell.Global;

import com.asual.lesscss.LessEngine;
import com.asual.lesscss.LessResource;
import com.asual.lesscss.LessServlet;
import com.asual.lesscss.Resource;
import com.asual.lesscss.ResourceNotFoundException;
import com.asual.lesscss.ResourceUtils;
import com.asual.lesscss.ScriptResource;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

public class PolopolyDevelopmentServlet extends LessServlet 
{
    private String cssbase = null;
    private String jsbase = null;
    private LessEngine engine;
    private JSConfig config;
    
    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        if (config.getInitParameter("cssbase") != null) {
            cssbase = config.getInitParameter("cssbase");
        }
        if (config.getInitParameter("jsbase") != null) {
            jsbase = config.getInitParameter("jsbase");
        }
        this.engine = new LessEngine();
        this.config = new JSConfig();
    }
    
    @Override
    protected Resource getResource(String uri) throws ResourceNotFoundException
    {
        String mimeType = getResourceMimeType(uri);
        if (!resources.containsKey(uri)) {
            if ("text/css".equals(mimeType)) {
                resources.put(uri, new DevelopmentLessResource(engine, getServletContext(), uri, charset, cache, compress));
                return resources.get(uri);
            } else if ("text/javascript".equals(mimeType)) {
                resources.put(uri, new DevelopmentScriptResource(config, getServletContext(), uri, charset, cache, compress));
                return resources.get(uri);
            } else {
                return super.getResource(uri);
            }
        } else {
            return resources.get(uri);
        }
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
    
    private class DevelopmentScriptResource extends ScriptResource {

        private JSConfig config;
        private String uri;
        
        public DevelopmentScriptResource(JSConfig config, ServletContext servletContext, String uri, String charset, boolean cache, boolean compress) 
            throws ResourceNotFoundException
        {
            super(servletContext, uri, charset, cache, compress);
            this.uri = uri;
            this.config = config;
        }
        
        @Override
        protected File getFile(String path)
        {
            if (jsbase != null) {
                File file = new File(jsbase);
                if (file.exists() && file.isDirectory()) {
                    return new File(file.getAbsolutePath() + "/package.js");
                }
            }
            return super.getFile(path);
        }
        
        @Override
        public byte[] getContent() throws IOException
        {
            long thisModified = getLastModified();
            if (content == null || lastModified == null || (content != null && lastModified < thisModified)) {
                lastModified = thisModified;
                List<byte[]> contents = new ArrayList<byte[]>();
                int size = 0;
                for (File file : getFiles()) {
                    byte[] fileContent = ResourceUtils.readTextFile(file, charset);
                    contents.add(fileContent);
                    size = size + fileContent.length;
                }
                byte[] content = new byte[size];
                int pointer = 0;
                for (byte[] bytes : contents) {
                    System.arraycopy(bytes, 0, content, pointer, bytes.length);
                    pointer = pointer + bytes.length;
                }
                if (compress) {
                    this.content = compress(content);
                } else {
                    this.content = content;
                }
            }
            return content;
        }
        
        private byte[] compress(byte[] content) throws UnsupportedEncodingException, IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Reader in = new InputStreamReader(new ByteArrayInputStream(new String(content, charset).getBytes(charset)), charset);
            Writer out = new OutputStreamWriter(baos, charset);
            JavaScriptCompressor compressor = new JavaScriptCompressor(in, new ErrorReporter() {
                public void warning(String message, String sourceName,
                        int line, String lineSource, int lineOffset) {
                    logger.error("Message: " + message + (lineSource != null ? ", Line: " + line + ", Column: " + lineOffset + ", Source: " + lineSource : "") + ".");
                }
                public void error(String message, String sourceName,
                        int line, String lineSource, int lineOffset) {
                    logger.error("Message: " + message + (lineSource != null ? ", Line: " + line + ", Column: " + lineOffset + ", Source: " + lineSource : "") + ".");
                }
                public EvaluatorException runtimeError(String message, String sourceName,
                        int line, String lineSource, int lineOffset) {
                    error(message, sourceName, line, lineSource, lineOffset);
                    return new EvaluatorException(message);
                }
            });
            in.close();
            compressor.compress(out, -1, true, false, true, false);
            out.flush();
            content = baos.toByteArray();
            out.close();
            return content;
        }
        
        @Override
        public long getLastModified() throws IOException
        {
            if (lastModified == null || !cache) {
                long thisLastModified = getFile("package.js").lastModified();
                for (File file : getFiles()) {
                    long thisModified = file.lastModified();
                    if (thisModified > thisLastModified) {
                        thisLastModified = thisModified;
                    }
                }
                if (cache) {
                    lastModified = thisLastModified;
                }
                return thisLastModified;
            }
            return lastModified;
        }
        
        public File[] getFiles() throws IOException 
        {
            return config.getFiles(getFile("package.js"), uri);
        }
    }
    
    private class DevelopmentLessResource extends LessResource {

        public DevelopmentLessResource(LessEngine engine, ServletContext servletContext, String uri, String charset, boolean cache, boolean compress) 
            throws ResourceNotFoundException
        {
            super(engine, servletContext, uri, charset, cache, compress);
        }
        
        @Override
        protected File getFile(String path)
        {
            if (cssbase != null) {
                File file = new File(cssbase);
                if (file.exists() && file.isDirectory()) {
                    return new File(file.getAbsolutePath() + path.replaceAll(".css", ".less"));
                }
            }
            return super.getFile(path);
        }
                
        @Override
        public long getLastModified() throws IOException
        {
            return super.getLastModified();
        }
    }
}
