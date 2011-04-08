package com.polopoly.javarebel.cfg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "items"
})
@XmlRootElement(name = "content")
public class ConfigurationItem {

    @XmlElementRefs({
        @XmlElementRef(name = "js", type = JAXBElement.class),
        @XmlElementRef(name = "less", type = JAXBElement.class),
        @XmlElementRef(name = "dir", type = JAXBElement.class)
    })
    protected List<JAXBElement<String>> items;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String externalid;
    
    public ConfigurationItem()
    {
        this.externalid = null;
        this.items = new ArrayList<JAXBElement<String>>();
    }
    
    public ConfigurationItem(String externalid)
    {
        this.externalid = externalid;
        this.items = new ArrayList<JAXBElement<String>>();
    }
    
    public ConfigurationItem(String externalid, String type, String path)
    {
        this.externalid = externalid;
        this.items = new ArrayList<JAXBElement<String>>();
        items.add(new JAXBElement<String>(new QName("", type), String.class, path));
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((externalid == null) ? 0 : externalid.hashCode());
        result = prime * result + ((items == null) ? 0 : items.hashCode());
        return result;
    }
    
    public List<JAXBElement<String>> getItems()
    {
        return items;
    }

    public String getExternalid()
    {
        return externalid;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ConfigurationItem other = (ConfigurationItem) obj;
        if (externalid == null) {
            if (other.externalid != null)
                return false;
        } else if (!externalid.equals(other.externalid))
            return false;
        if (items == null) {
            if (other.items != null)
                return false;
        } else {
            if (items.size() != other.items.size()) {
                return false;
            }
            Iterator<JAXBElement<String>> first = items.iterator();
            Iterator<JAXBElement<String>> second = other.items.iterator();
            while (first.hasNext()) {
                JAXBElement<String> firstitem = first.next();
                JAXBElement<String> seconditem = second.next();
                if (!firstitem.getName().equals(seconditem.getName())) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (JAXBElement<String> el : items) {
            if (first) { first = false ; }
            else { sb.append(", "); }
            sb.append(el.getName() + "=" + el.getValue());
        }
        return "Content [externalid=" + externalid + ", items=[" + sb + "]]";
    }
}
