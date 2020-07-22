package com.shizy.ftd.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {

    /**
     * 例：获取resources下的config.properties
     * <p>
     * PropertiesUtils.getPropertiesInProject("config.properties");
     */
    public static Properties getPropertiesInProject(String path) {
        Properties properties = null;
        try {
            properties = new Properties();
            InputStream in = PropertiesUtils.class.getClassLoader().getResourceAsStream(path);
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * 例：获取工程同路径或发布jar包同路径下的config.properties
     * <p>
     * PropertiesUtils.getPropertiesInDir(System.getProperty("user.dir") + "/config.properties");
     */
    public static Properties getPropertiesInDir(String path) {
        Properties properties = null;
        try {
            properties = new Properties();
            InputStream in = new BufferedInputStream(new FileInputStream(path));
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

}
