package com.polopoly.javarebel.cfg;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "content" })
@XmlRootElement(name = "pp-rebel")
public class Configuration {

    private static final JAXBContext CONTEXT;
    
    static {
        try {
            CONTEXT = JAXBContext.newInstance(Configuration.class, ConfigurationItem.class);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static class Item {
        public final String type;
        public final String value;
        public Item(String type, String value)
        {
            this.type = type;
            this.value = value;
        }
    }

    @XmlElement(required = true)
    private List<ConfigurationItem> content;
    @XmlTransient
    private Map<String, List<Item>> index;

    public List<ConfigurationItem> getContent()
    {
        return content;
    }

    public Configuration(List<ConfigurationItem> content)
    {
        this.content = content;
    }
    
    public Configuration()
    {
        this.content = new ArrayList<ConfigurationItem>();
    }

    public static Configuration parse(Reader reader) throws JAXBException
    {
        Configuration result = (Configuration) CONTEXT.createUnmarshaller().unmarshal(reader);
        result.index();
        return result;
    }

    private void index()
    {
        index = new HashMap<String, List<Item>>();
        for (ConfigurationItem item : content) {
            List<Item> result = new ArrayList<Item>();
            for (JAXBElement<String> el : item.items) {
                result.add(new Item(el.getName().getLocalPart(), el.getValue()));
            }
            index.put(item.externalid, result);
        }
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

    public List<Item> get(String externalid)
    {
        return index.get(externalid);
    }
}
