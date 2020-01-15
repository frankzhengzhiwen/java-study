package service;

import model.Broker;
import model.Message;
import model.Result;
import util.MessageType;

import java.net.Socket;
import java.util.Date;

public class MessageService {

    public static Result handle(Socket client, Message message) {
        Result result;

        // 以服务端时间为准
        message.setDate(new Date());
        // 设置客户端IP和端口号
        message.setHost(client.getInetAddress().getHostAddress());
        message.setPort(client.getPort());
        // 发布消息：保存到阻塞队列，且在此时间前订阅的客户端都发送该消息
        try {
            if(MessageType.PUBLISH.equals(message.getType())){
                Broker.publish(message);
                // 订阅消息，需要保存客户端信息及订阅的主题
            }else if(MessageType.SUBSCRIBE.equals(message.getType())){
                Broker.subscribe(client, message);
                // 取消订阅消息
            }else if(MessageType.UNSUBSCRIBE.equals(message.getType())){
                Broker.unsubscribe(client, message);
            }
            result = Result.buildOK();
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.buildError(e);
        }

        return result;
    }

    public static void consume(Socket client) {
        Broker.consume(client);
    }
}
