package com.polopoly.javarebel.cfg;

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

@RunWith(Parameterized.class)
public class ConfigurationTest {

    private final String xml;
    private final Configuration config;

    @Parameters
    public static List<Object[]> parameters() {
        List<Object[]> list = new ArrayList<Object[]>();
        list.add(new Object[] { "<pp-rebel></pp-rebel>", new Configuration(Collections.emptyList()) });
        list.add(new Object[] { "<pp-rebel><content externalid=\"example\"></content></pp-rebel>",
                                new Configuration(Arrays.asList(new ContentItem("example"))) });
        list.add(new Object[] { "<pp-rebel><content externalid=\"example\"><dir>path</dir></content></pp-rebel>",
                                new Configuration(Arrays.asList(new ContentItem("example", new DirItem("path")))) });
        list.add(new Object[] { "<pp-rebel><content externalid=\"example\"><js>path</js></content></pp-rebel>",
                                new Configuration(Arrays.asList(new ContentItem("example", new JSItem("path")))) });
        list.add(new Object[] { "<pp-rebel><content externalid=\"example1\"><dir>path1</dir></content>" +
                                          "<content externalid=\"example2\"><less>path2</less></content></pp-rebel>",
                                new Configuration(Arrays.asList(new ContentItem("example1", new DirItem("path1")),
                                                                new ContentItem("example2", new LessItem("path2")))) });
        list.add(new Object[] { "<pp-rebel><content externalid=\"example\"><dir path=\"/path\">value</dir></content></pp-rebel>",
                                new Configuration(Arrays.asList(new ContentItem("example", new DirItem("/path", "value")))) });
        list.add(new Object[] { "<pp-rebel><filter class=\"some.class\" name=\"some filter\"><dir>aha</dir></filter></pp-rebel>",
                                new Configuration(Arrays.asList(new FilterItem("some.class", "some filter", new DirItem("aha")))) });
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
