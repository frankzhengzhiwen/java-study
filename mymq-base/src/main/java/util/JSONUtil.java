package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import model.Message;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JSONUtil {

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
    public static ObjectMapper getMapper(){
        if(MAPPER == null){
            synchronized (JSONUtil.class){
                if(MAPPER == null){
                    MAPPER = new ObjectMapper();
                    MAPPER.setDateFormat(new SimpleDateFormat(Const.DATE_PATTERN));
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
    public static <T> T deserialize(String str, Class<T> clazz) {
        try {
            return getMapper().readValue(str, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("JSON反序列化错误", e);
        }
    }

    /**
     * 序列化Java对象为JSON字符串
     * @param object Java对象
     * @return
     * @throws JsonProcessingException
     */
    public static String serialize(Object object) {
        try {
            return getMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("JSON序列化错误", e);
        }
    }

    public static void main(String[] args) throws JsonProcessingException {
        Message message = new Message();
        message.setType(MessageType.valueOf("publish".toUpperCase()));
        message.setHost("127.0.0.1");
        message.setPort(9999);
        message.setTopic("order");
        message.setDate(new Date());
        message.setContent("卖出一台笔记本电脑");
        System.out.println(serialize(message));
    }
}
