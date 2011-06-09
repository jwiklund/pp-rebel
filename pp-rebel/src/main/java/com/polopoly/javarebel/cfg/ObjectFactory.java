package com.polopoly.javarebel.cfg;

import javax.xml.bind.annotation.XmlRegistry;


@XmlRegistry
public class ObjectFactory {

    public ObjectFactory() {
    }

    public Configuration createConfiguration() {
        return new Configuration();
    }

    public ContentItem createContentItem() {
        return new ContentItem();
    }

    public LessItem createCssItem() {
        return new LessItem();
    }

    public DirItem createDirItem() {
        return new DirItem();
    }

    public JSItem createJSItem() {
        return new JSItem();
    }
}
