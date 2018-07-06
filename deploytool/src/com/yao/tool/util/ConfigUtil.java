package com.yao.tool.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class ConfigUtil {
    public static String getValue(String key)
            throws IOException {
        String fileName = System.getProperty("user.dir") + File.separator + "config.properties";
        File file = new File(fileName);
        if (file.exists()) {
            Properties properties = new Properties();
            String value = "";
            FileInputStream inputFile = new FileInputStream(fileName);
            properties.load(inputFile);
            inputFile.close();
            if (properties.containsKey(key)) {
                value = properties.getProperty(key);
                return value;
            }
            return value;
        }


        return "";
    }
}