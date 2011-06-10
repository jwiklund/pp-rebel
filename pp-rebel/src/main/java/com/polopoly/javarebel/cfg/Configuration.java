package com.polopoly.javarebel.cfg;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "content" })
@XmlRootElement(name = "pp-rebel")
public class Configuration {

    private static final JAXBContext CONTEXT;
    
    static {
        try {
            CONTEXT = JAXBContext.newInstance(Configuration.class, ContentItem.class, LessItem.class, DirItem.class, FilterItem.class);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Item {
        public final String type;
        public final String path;
        public final String value;
        public Item(String type, String path, String value)
        {
            this.type = type;
            this.path = path;
            this.value = value;
        }
    }

    public static class Filter {
        public final String className;
        public final String filterName;
        public final List<Item> items;
        public Filter(String className, String filterName, List<Item> items)
        {
            this.className = className;
            this.filterName = filterName;
            this.items = items;
        }        
    }

    public Configuration() {
        content = new ArrayList<Object>();
    }

    public Configuration(List<? extends Object> content) {
        this.content = new ArrayList<Object>(content);
    }

    @XmlElementRefs({
        @XmlElementRef(name = "content", type = ContentItem.class),
        @XmlElementRef(name = "filter", type = FilterItem.class)
    })
    private List<Object> content;
    @XmlAttribute(name = "patchFilters")
    @XmlSchemaType(name = "anySimpleType")
    private String patchFilters;
    @XmlTransient
    private Map<String, List<Item>> contentIndex;
    @XmlTransient
    private Map<String, List<Item>> filterIndex;

    public static Configuration parse(Reader reader) throws JAXBException
    {
        Configuration result = (Configuration) CONTEXT.createUnmarshaller().unmarshal(reader);
        result.index();
        return result;
    }

    private void index()
    {
        contentIndex = new HashMap<String, List<Item>>();
        filterIndex = new HashMap<String, List<Item>>();
        for (Object item : content) {
            if (item instanceof ContentItem) {
                List<Item> result = new ArrayList<Item>();
                for (BaseItem el : ((ContentItem) item).items) {
                    result.add(new Item(el.type(), el.path(), el.value()));
                }
                contentIndex.put(((ContentItem) item).externalid, result);                
            } else if (item instanceof FilterItem) {
                List<Item> result = new ArrayList<Item>();
                FilterItem filter = (FilterItem) item;
                for (BaseItem el : filter.items) {
                    result.add(new Item(el.type(), el.path(), el.value()));
                }
                filterIndex.put(filter.filterName, result);
            } else {
                throw new RuntimeException("Unexpected type " + item.getClass());
            }
        }
    }

    public List<Item> getContentFiles(String externalid)
    {
        return contentIndex.get(externalid);
    }

    public List<Item> getFilterFiles(String filtername)
    {
        return filterIndex.get(filtername);
    }

    public boolean hasFilterFiles() {
        return !filterIndex.isEmpty();
    }

    public boolean enableFilterProcessing()
    {
        return "true".equals(patchFilters);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((content == null) ? 0 : content.hashCode());
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
        Configuration other = (Configuration) obj;
        if (content == null) {
            if (other.content != null)
                return false;
        } else if (!content.equals(other.content))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "Configuration [content=" + content + "]";
    }
}
