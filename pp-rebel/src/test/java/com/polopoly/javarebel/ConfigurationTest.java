package com.polopoly.javarebel;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.polopoly.javarebel.cfg.Configuration;
import com.polopoly.javarebel.cfg.ConfigurationItem;

@RunWith(Parameterized.class)
public class ConfigurationTest {

    private final String xml;
    private final Configuration config;

    @Parameters
    public static List<Object[]> parameters() {
        List<Object[]> list = new ArrayList<Object[]>();
        list.add(new Object[] { "<pp-rebel></pp-rebel>", new Configuration(Collections.<ConfigurationItem>emptyList()) });
        list.add(new Object[] { "<pp-rebel><content externalid=\"example\"></content></pp-rebel>",
                                new Configuration(Arrays.asList(new ConfigurationItem("example"))) });
        list.add(new Object[] { "<pp-rebel><content externalid=\"example\"><dir>path</dir></content></pp-rebel>",
                                new Configuration(Arrays.asList(new ConfigurationItem("example", "dir", "path"))) });
        list.add(new Object[] { "<pp-rebel><content externalid=\"example\"><js>path</js></content></pp-rebel>",
                                new Configuration(Arrays.asList(new ConfigurationItem("example", "js", "path"))) });
        list.add(new Object[] { "<pp-rebel><content externalid=\"example1\"><dir>path1</dir></content>" +
                                          "<content externalid=\"example2\"><less>path2</less></content></pp-rebel>",
                                new Configuration(Arrays.asList(new ConfigurationItem("example1", "dir", "path1"),
                                                                new ConfigurationItem("example2", "less", "path2"))) });
        return list;
    }
    
    public ConfigurationTest(String xml, Configuration config) {
        this.xml = xml;
        this.config = config;
    }

    @Test
    public void verify_parsing() throws Exception
    {
        assertEquals(config, Configuration.parse(new StringReader(xml))); 
    }
}
