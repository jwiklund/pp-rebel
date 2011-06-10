package com.polopoly.javarebel.model;

import org.zeroturnaround.javarebel.ClassEventListener;

public class ModelReloadListener implements ClassEventListener {

    Class<?> policy;

    public ModelReloadListener() throws ClassNotFoundException
    {
        policy = Class.forName("com.polopoly.cm.policy.Policy");
    }

    @SuppressWarnings("rawtypes")
    public void onClassEvent(int arg0, Class arg1)
    {
        if (policy.isAssignableFrom(arg1)) {
            ModelDomainHandler.reload(arg1);
        }
    }

    public int priority()
    {
        return PRIORITY_DEFAULT;
    }
}
