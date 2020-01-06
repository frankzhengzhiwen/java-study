package model;

import java.net.Socket;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class Broker {

    private static final BlockingQueue WORK_QUEUE2
            = new LinkedBlockingQueue();
    /**
     * 主题
     */
    private static final Map<String, Message> MESSAGE_MAP
            = new ConcurrentHashMap<>();

    public static Result handle(Socket client, Message message) {
        Result result = new Result();
        result.setSuccess(true);
        result.setCode("200");
        result.setMessage("OK");

        // 以服务端时间为准
        message.setDate(new Date());
        // 设置客户端IP和端口号
        message.setIpAddress(client.getInetAddress().getHostAddress());
        message.setPort(client.getPort());
        // 发布消息：保存到阻塞队列，且在此时间前订阅的客户端都发送该消息
        if(Message.PUB.equals(message.getType())){

        // 订阅消息，需要保存客户端信息及订阅的主题
        }else if(Message.SUB.equals(message.getType())){

        }

        return result;
    }

//    private static final BlockingQueue
}
