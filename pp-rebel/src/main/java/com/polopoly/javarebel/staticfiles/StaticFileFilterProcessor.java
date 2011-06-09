package com.polopoly.javarebel.staticfiles;

import org.zeroturnaround.bundled.javassist.CannotCompileException;
import org.zeroturnaround.bundled.javassist.ClassPool;
import org.zeroturnaround.bundled.javassist.CtClass;
import org.zeroturnaround.bundled.javassist.CtField;
import org.zeroturnaround.bundled.javassist.CtMethod;
import org.zeroturnaround.bundled.javassist.Modifier;
import org.zeroturnaround.bundled.javassist.NotFoundException;
import org.zeroturnaround.bundled.javassist.bytecode.Descriptor;
import org.zeroturnaround.javarebel.Configuration;
import org.zeroturnaround.javarebel.ConfigurationFactory;
import org.zeroturnaround.javarebel.LoggerFactory;
import org.zeroturnaround.javarebel.Resource;
import org.zeroturnaround.javarebel.integration.support.JavassistClassBytecodeProcessor;

public class StaticFileFilterProcessor extends JavassistClassBytecodeProcessor {

    private static final String FILTER_NAME = "generated$filterName";

    @Override
    public void process(ClassPool cp, ClassLoader cl, CtClass ctClass) throws Exception
    {
        Configuration cfg = ConfigurationFactory.getInstance();
        Resource rs = cfg.getClassResource(cl, ctClass.getName());
        if (rs == null) {
            return;
        }
        if (!cfg.isManagedClass(cl, ctClass.getName(), rs)) {
            return;
        }
        if (Modifier.isAbstract(ctClass.getModifiers())) {
            return ;
        }
        if (!isFilter(ctClass)) {
            return ;
        }
        addNameField(cp, cl, ctClass);
        patchInit(cp, cl, ctClass);
        patchFilter(cp, cl, ctClass);
        LoggerFactory.getInstance().echo("pp-rebel: patched " + ctClass.getName() + " to (possibly) handle static files");
    }

    private void addNameField(ClassPool cp, ClassLoader cl, CtClass ctClass) throws CannotCompileException, NotFoundException
    {
        CtField nameField = new CtField(cp.get("java.lang.String"), FILTER_NAME, ctClass);
        nameField.setModifiers(Modifier.PRIVATE);
        ctClass.addField(nameField);
    }

    private void patchInit(ClassPool cp, ClassLoader cl, CtClass ctClass) throws NotFoundException, CannotCompileException
    {
        CtMethod init = ctClass.getMethod("init", Descriptor.ofMethod(cp.get("void"), new CtClass[] { cp.get("javax.servlet.FilterConfig") }));
        init.insertBefore("{ " + FILTER_NAME + " = $1.getFilterName() ; }");
    }

    private void patchFilter(ClassPool cp, ClassLoader cl, CtClass ctClass) throws NotFoundException, CannotCompileException
    {
        CtMethod filter = ctClass.getMethod("doFilter", Descriptor.ofMethod(cp.get("void"), new CtClass[] { 
            cp.get("javax.servlet.ServletRequest"),
            cp.get("javax.servlet.ServletResponse"),
            cp.get("javax.servlet.FilterChain")
        }));
        filter.insertBefore("if (com.polopoly.javarebel.staticfiles.StaticFileFilterHandler.doFilterHandled(" + FILTER_NAME + ", $1, $2)) { return ; }");
    }

    private boolean isFilter(CtClass ctClass) throws NotFoundException
    {
        for (CtClass intrf : ctClass.getInterfaces()) {
            if ("javax.servlet.Filter".equals(intrf.getName())) {
                return true;
            }
        }
        if (ctClass.getSuperclass() != null) {
            return isFilter(ctClass.getSuperclass()) && Modifier.isAbstract(ctClass.getSuperclass().getModifiers());
        }
        return false;
    }
}
