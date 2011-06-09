package com.polopoly.javarebel.cfg;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "value"
})
@XmlRootElement(name = "less")
class LessItem implements BaseItem
{
    @XmlValue
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    private String value;
    @XmlAttribute
    @XmlSchemaType(name = "anySimpleType")
    private String path;
    public LessItem() {
    }
    public LessItem(String value) {
        this.value = value;
    }
    public LessItem(String path, String value)
    {
        this.path = path;
        this.value = value;
    }

    public String type()
    {
        return "css";
    }
    public String path()
    {
        return path;
    }
    public String value()
    {
        return value;
    }
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
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
        LessItem other = (LessItem) obj;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }
    @Override
    public String toString()
    {
        return "CssItem [value=" + value + ", path=" + path + "]";
    }
}
