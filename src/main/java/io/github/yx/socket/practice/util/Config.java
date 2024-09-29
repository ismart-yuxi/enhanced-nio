package io.github.yx.socket.practice.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Config类用于加载和获取配置文件中的属性。
 */
public class Config {
    private static Properties properties = new Properties();

    static {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 根据键获取字符串类型的配置属性。
     *
     * @param key 配置属性的键
     * @return 配置属性的值
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * 根据键获取整数类型的配置属性。
     *
     * @param key 配置属性的键
     * @return 配置属性的整数值
     */
    public static int getIntProperty(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }
}
