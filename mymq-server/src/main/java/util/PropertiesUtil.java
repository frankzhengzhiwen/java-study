package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    private static final String PROPERTIES_PATH = "conf.properties";

    private static volatile Properties PROPERTIES;

    private PropertiesUtil(){

    }

    /**
     * 单例模式获取属性，不需要提供外部使用（private）
     * @return
     * @throws IOException
     */
    private static Properties getProperties() throws IOException {
        if(PROPERTIES == null){
            synchronized (PropertiesUtil.class){
                if(PROPERTIES == null){
                    InputStream is = PropertiesUtil.class.getClassLoader()
                            .getResourceAsStream(PROPERTIES_PATH);
                    PROPERTIES = new Properties();
                    PROPERTIES.load(is);
                }
            }
        }
        return PROPERTIES;
    }

    /**
     * 根据properties文件的键获取对应的值
     * @param key 键
     * @return value 值
     * @throws IOException
     */
    public static String get(String key) {
        try {
            return getProperties().getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("读取properties配置文件错误");
        }
    }
}
