package com.polopoly.javarebel.cfg;

import java.util.ArrayList;
import java.util.List;

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

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "items"
})
@XmlRootElement(name = "filter")
class FilterItem {

    @XmlElementRefs({
        @XmlElementRef(name = "js", type = JSItem.class),
        @XmlElementRef(name = "dir", type = DirItem.class),
        @XmlElementRef(name = "css", type = LessItem.class)
    })
    public List<BaseItem> items = new ArrayList<BaseItem>();
    
    @XmlAttribute(required = true, name="class")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    public String className;

    @XmlAttribute(required = true, name="name")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    public String filterName;

    public FilterItem() {
        items = new ArrayList<BaseItem>();
    }
    public FilterItem(String className, String filterName, BaseItem item)
    {
        this();
        this.className = className;
        this.filterName = filterName;
        items.add(item);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((className == null) ? 0 : className.hashCode());
        result = prime * result + ((filterName == null) ? 0 : filterName.hashCode());
        result = prime * result + ((items == null) ? 0 : items.hashCode());
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
        FilterItem other = (FilterItem) obj;
        if (className == null) {
            if (other.className != null)
                return false;
        } else if (!className.equals(other.className))
            return false;
        if (filterName == null) {
            if (other.filterName != null)
                return false;
        } else if (!filterName.equals(other.filterName))
            return false;
        if (items == null) {
            if (other.items != null)
                return false;
        } else if (!items.equals(other.items))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "FilterItem [items=" + items + ", className=" + className + ", filterName=" + filterName + "]";
    }
}
