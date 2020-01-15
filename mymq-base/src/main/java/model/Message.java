package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import util.MessageType;

import java.util.Date;

/**
 * 服务器接收到的客户端信息（发布消息、订阅消息）
 */
@Getter
@Setter
@ToString
public class Message {

    /**
     * 消息类型：
     * 分pub（发布消息）和sub（订阅消息）两种
     * 类似于生产者消费者模型，但需要先进行订阅，在订阅时间之后
     * 生产的消息，就可以被获取并消费
     */
    private MessageType type;

    /**
     * 消息主题：
     * 根据主题进行消息的生产或消费
     */
    private String topic;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 服务端接收到消息的时间
     */
    @JsonIgnore
    private Date date;

    /**
     * 客户端IP地址
     */
    @JsonIgnore
    private String host;

    /**
     * 客户端端口号
     */
    @JsonIgnore
    private Integer port;
}
