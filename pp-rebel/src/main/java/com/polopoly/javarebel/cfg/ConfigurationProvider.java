package com.polopoly.javarebel.cfg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.xml.bind.JAXBException;

import org.zeroturnaround.javarebel.LoggerFactory;

import com.polopoly.javarebel.fs.FSUtil;

public class ConfigurationProvider {

    public static ConfigurationProvider instance() {
        return Instance.instance;
    }

    private static class Instance {
        public static ConfigurationProvider instance = new ConfigurationProvider();
    }

    boolean exists = true;
    File configFile;
    Cfg cfg = new Cfg(null, -1);
    long lastCheck = -1;
    
    public ConfigurationProvider()
    {
        String polopoly = FSUtil.getPolopoly();
        if (polopoly == null) {
            LoggerFactory.getInstance().echo("pp-rebel.WARNING: No PP_HOME environment defined, Polopoly Content Rebel Plugin will be disabled");
            return;
        }
        configFile = new File(polopoly + "/pp-rebel.xml");
    }
    
    public static class Cfg {
        public final Configuration configuration;
        public final long lastModified;
        public Cfg(Configuration configuration, long lastModified)
        {
            this.configuration = configuration;
            this.lastModified = lastModified;
        }
    }
    
    public synchronized Cfg getConfiguration()
    {
        if (configFile == null) {
            return null;
        }
        if ((System.currentTimeMillis() - lastCheck) < 1000) {
            if (cfg.configuration != null) {
                return cfg;
            }
        }
        lastCheck = System.currentTimeMillis();
        if ((!configFile.exists() || configFile.isDirectory()) && exists) {
            LoggerFactory.getInstance().echo("pp-rebel.ERROR: " + configFile.getAbsolutePath() + " does not exist");
            exists = false;
        }
        if (!exists) {
            return cfg;
        }
        long currentLastModified = configFile.lastModified();
        if (currentLastModified > cfg.lastModified) {
            try {
                Configuration config = Configuration.parse(new FileReader(configFile));
                cfg = new Cfg(config, currentLastModified);
            } catch (FileNotFoundException e) {
                LoggerFactory.getInstance().error(e);
            } catch (JAXBException e) {
                LoggerFactory.getInstance().error(e);
                cfg = new Cfg(cfg.configuration, currentLastModified);
            }
        }
        return cfg;
    }
}
