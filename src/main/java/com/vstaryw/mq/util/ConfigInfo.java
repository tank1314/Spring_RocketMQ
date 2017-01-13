package com.vstaryw.mq.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Sumail on 2017/1/12.
 */
public class ConfigInfo {

    private static Properties props ;

    static {
        try {
            ClassPathResource resource = new ClassPathResource("properties/config.properties");
            props=  PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {

        }
    }

    public static String getStrValue(String key){
        return props.getProperty(key,null);
    }

    public static String getStrValue(String key,String defaultValue){
        return props.getProperty(key,defaultValue);
    }

    public static int getIntValue(String key){
        return Integer.parseInt(props.get(key).toString());
    }
}
