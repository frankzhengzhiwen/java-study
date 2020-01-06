package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 服务器接收到的客户端信息（发布消息、订阅消息）
 */
@Getter
@Setter
@ToString
public class Message implements Comparable<Message> {

    /**
     * 消息类型：发布
     */
    public static final String PUB = "pub";

    /**
     * 消息类型：订阅
     */
    public static final String SUB = "sub";

    /**
     * 消息类型：
     * 分pub（发布消息）和sub（订阅消息）两种
     * 类似于生产者消费者模型，但需要先进行订阅，在订阅时间之后
     * 生产的消息，就可以被获取并消费
     */
    private String type;

    /**
     * 消息主题：
     * 根据主题进行消息的生产或消费
     */
    private String topic;

    /**
     * 消息的标签：
     * 可以根据标签进行该主题下的消息过滤，再消费
     */
    private Set<String> tags = new HashSet<>();

    /**
     * 消息是否需要持久化（保存到本地磁盘）（发布消息时可配置）
     */
    private Boolean persistence;

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
    private String ipAddress;

    /**
     * 客户端端口号
     */
    @JsonIgnore
    private Integer port;

    @Override
    public int compareTo(Message o) {
        if(topic.equals(o.topic)){
            return Long.compare(date.getTime(), o.date.getTime());
        }
        return topic.compareTo(o.topic);
    }
}
