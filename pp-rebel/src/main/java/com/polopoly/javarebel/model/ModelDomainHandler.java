package com.polopoly.javarebel.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.zeroturnaround.javarebel.LoggerFactory;

public class ModelDomainHandler {

    static ModelDomainReloader reloader;
    static ConcurrentHashMap<String, Set<String>> policyClassToModelName = new ConcurrentHashMap<String, Set<String>>();

    public static void handleModelDomainCreated(ModelDomainReloader domain)
    {
        if (reloader != null) {
            LoggerFactory.getInstance().echo("pp-rebel.WARNING: More than one domain in class loader, only tracking last");            
        }
        reloader = domain;
    }

    public static void handleLoadModelType(String name, Object mt)
    {
        if (mt == null) {
            return;
        }
        try {
            Object templateModel = getTemplateModel(mt);
            if (templateModel == null) {
                return;
            }
            String policyClassName = getPolicyClass(templateModel);
            if (policyClassName == null) {
                return;
            }
            Class<?> policyClass = Class.forName(policyClassName);
            while (policyClass != null) {
                addPolicyNameMapping(policyClass.getName(), name);
                for (Class<?> intrf : policyClass.getInterfaces()) {
                    addPolicyNameMapping(intrf.getName(), name);
                }
                policyClass = policyClass.getSuperclass();
            }
        } catch (Exception e) {
            LoggerFactory.getInstance().echo("pp-rebel.ERROR: Failed to handle load model type (model type reloading will be disabled for '"+name+"')");
            LoggerFactory.getInstance().errorEcho(e);
        }
    }

    private static void addPolicyNameMapping(String policyClass, String name)
    {
        Set<String> previous = policyClassToModelName.putIfAbsent(policyClass, new HashSet<String>(Arrays.asList(name)));
        if (previous != null) {
            synchronized (previous) {
                previous.add(name);
            }
        }
    }
    
    private static Object getTemplateModel(Object mt) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        Method method = mt.getClass().getMethod("getStaticAttribute", new Class<?>[]{String.class});
        return method.invoke(mt, "templateModel");
    }

    private static String getPolicyClass(Object tm) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        Method method = tm.getClass().getMethod("getPolicyClassName", new Class<?>[0]);;
        return (String) method.invoke(tm);
    }

    public static void handleReload(String name) {
        if (reloader != null) {
            reloader.generated$reload(name);
        }
    }

    public static void reload(Class<?> clazz)
    {
        if (reloader != null) {
            Set<String> templates = policyClassToModelName.get(clazz.getName());
            if (templates != null) {
                List<String> copy = null;
                synchronized (templates) {
                    copy = new ArrayList<String>(templates);
                }
                for (String name : copy) {
                    reloader.generated$reload(name);
                }
            }
        }
    }
}
