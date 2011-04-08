package com.polopoly.javarebel.cfg;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


@XmlRegistry
public class ObjectFactory {

    private final static QName _Javascript_QNAME = new QName("", "javascript");
    private final static QName _Dir_QNAME = new QName("", "dir");
    private final static QName _Less_QNAME = new QName("", "less");

    public ObjectFactory() {
    }

    public Configuration createConfiguration() {
        return new Configuration();
    }

    public ConfigurationItem createConfigurationItem() {
        return new ConfigurationItem();
    }

    @XmlElementDecl(namespace = "", name = "javascript")
    public JAXBElement<String> createJavascript(String value) {
        return new JAXBElement<String>(_Javascript_QNAME, String.class, null, value);
    }

    @XmlElementDecl(namespace = "", name = "dir")
    public JAXBElement<String> createDir(String value) {
        return new JAXBElement<String>(_Dir_QNAME, String.class, null, value);
    }

    @XmlElementDecl(namespace = "", name = "less")
    public JAXBElement<String> createLess(String value) {
        return new JAXBElement<String>(_Less_QNAME, String.class, null, value);
    }
}
