package task;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Message;
import util.Const;
import util.MessageType;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SendTask implements Runnable {

    private PrintWriter out;

    public SendTask(Socket client){
        try {
            out = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException("获取socket输出流错误", e);
        }
    }

    @Override
    public void run() {
        try {
            Scanner scanner = new Scanner(System.in);
            while(true){
                System.out.println("请输入发送类型【1订阅。2取消订阅。3发布】");
                Integer nType = Integer.parseInt(scanner.nextLine());
                MessageType type;
                switch (nType){
                    case 1:// 订阅和取消订阅
                        type = MessageType.SUBSCRIBE;
                        System.out.println("请输入要订阅的主题：");
                        break;
                    case 2:
                        type = MessageType.UNSUBSCRIBE;
                        System.out.println("请输入要取消订阅的主题：");
                        break;
                    case 3:
                        type = MessageType.PUBLISH;
                        System.out.println("请输入要发布的主题：");
                        break;
                    default:{
                        System.out.println("输入错误，请重新输入");
                        continue;
                    }
                }
                // 输入主题
                String topic = scanner.nextLine();
                Message message = new Message();
                message.setType(type);
                message.setTopic(topic);

                if(nType == 3){
                    System.out.println("请输入要发布的消息内容：");
                    String content = scanner.nextLine();
                    message.setContent(content);
                }

                //先向消息队列发送消息
                String send = new ObjectMapper().writeValueAsString(message);
                System.out.println("发送数据："+send);
                out.println(send);
                //消息结束符
                out.println(Const.MESSAGE_EOF);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
