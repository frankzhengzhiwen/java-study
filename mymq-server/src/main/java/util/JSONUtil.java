package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class JSONUtil {

    /**
     * 日期格式
     */
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * JSON处理类
     */
    private static volatile ObjectMapper MAPPER;

    private JSONUtil(){

    }

    /**
     * 单例模式获取JSON处理类
     * @return
     */
    private static ObjectMapper getMapper(){
        if(MAPPER == null){
            synchronized (JSONUtil.class){
                if(MAPPER == null){
                    MAPPER = new ObjectMapper();
                    MAPPER.setDateFormat(new SimpleDateFormat(DATE_PATTERN));
//		jsonMapper.setSerializationInclusion(Include.NON_NULL);
                    MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
                }
            }
        }
        return MAPPER;
    }

    /**
     * 反序列化：解析JSON字符串
     * @param str JSON字符串
     * @param clazz 需要解析为哪个Java类型
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T deserialize(String str, Class<T> clazz) throws IOException {
        return getMapper().readValue(str, clazz);
    }

    /**
     * 序列化Java对象为JSON字符串
     * @param object Java对象
     * @return
     * @throws JsonProcessingException
     */
    public static String serialize(Object object) throws JsonProcessingException {
        return getMapper().writeValueAsString(object);
    }
}
