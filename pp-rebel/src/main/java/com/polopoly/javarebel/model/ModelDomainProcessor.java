package com.polopoly.javarebel.model;

import org.zeroturnaround.bundled.javassist.ClassPool;
import org.zeroturnaround.bundled.javassist.CtClass;
import org.zeroturnaround.bundled.javassist.CtConstructor;
import org.zeroturnaround.bundled.javassist.CtMethod;
import org.zeroturnaround.bundled.javassist.bytecode.Descriptor;
import org.zeroturnaround.javarebel.LoggerFactory;
import org.zeroturnaround.javarebel.integration.support.JavassistClassBytecodeProcessor;

public class ModelDomainProcessor extends JavassistClassBytecodeProcessor {

    @Override
    public void process(ClassPool cp, ClassLoader cl, CtClass ctClass) throws Exception
    {
        ctClass.addInterface(cp.get(ModelDomainReloader.class.getName()));
        CtMethod reload = new CtMethod(cp.get("void"), "generated$reload", new CtClass[] { cp.get("java.lang.String") }, ctClass);
        reload.setBody("{" +
                       "  synchronized (_modelTypes) {" +
                       "    _modelTypes.remove($1);" +
                       "  }" +
                       "  org.zeroturnaround.javarebel.LoggerFactory.getInstance().echo(\"pp-rebel: removed \" + $1);" +
                       "  policyModelCache.clear();" +
        		"}");
        ctClass.addMethod(reload);
        String loadDesc = Descriptor.ofMethod(cp.get("com.polopoly.model.ModelType"), new CtClass[] {
            cp.get("java.lang.String"),
            cp.get("com.polopoly.cm.ContentId")
        });
        CtMethod method = ctClass.getMethod("loadModelType", loadDesc);
        method.insertAfter("com.polopoly.javarebel.model.ModelDomainHandler.handleLoadModelType($1, $_);");
        String createDesc = Descriptor.ofConstructor(new CtClass[] {
            cp.get("com.polopoly.cm.policy.PolicyCMServer"),
            cp.get("java.lang.String"),
            cp.get("boolean"),
            cp.get("int")
        });
        CtConstructor constructor = ctClass.getConstructor(createDesc);
        constructor.insertAfter("com.polopoly.javarebel.model.ModelDomainHandler.handleModelDomainCreated($0);");
        LoggerFactory.getInstance().echo("pp-rebel: patched " + ctClass.getName() + " to support ModelType reload of Policy classes");
    }
}
