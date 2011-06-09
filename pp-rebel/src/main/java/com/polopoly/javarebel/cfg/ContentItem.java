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
@XmlRootElement(name = "content")
class ContentItem {

    @XmlElementRefs({
        @XmlElementRef(name = "js", type = JSItem.class),
        @XmlElementRef(name = "dir", type = DirItem.class),
        @XmlElementRef(name = "css", type = LessItem.class)
    })
    public List<BaseItem> items;
    
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    public String externalid;

    public ContentItem() {
        items = new ArrayList<BaseItem>();
    }
    
    public ContentItem(String externalid) {
        this();
        this.externalid = externalid;
    }

    public ContentItem(String externalid, BaseItem item) {
        this(externalid);
        items.add(item);
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

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ContentItem other = (ContentItem) obj;
        if (externalid == null) {
            if (other.externalid != null)
                return false;
        } else if (!externalid.equals(other.externalid))
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
        return "ContentItem [items=" + items + ", externalid=" + externalid + "]";
    }
}
