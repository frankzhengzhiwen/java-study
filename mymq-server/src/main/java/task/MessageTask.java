package task;

import model.Message;
import model.Result;
import service.MessageService;
import util.Const;
import util.JSONUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MessageTask implements Runnable {

    private Socket client;

    public MessageTask(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(client.getInputStream(), Const.CHARSET));
            PrintWriter out = new PrintWriter(
                    client.getOutputStream(), true);
            // 按行读取
            String line;
            StringBuilder sb = new StringBuilder();
            while((line=in.readLine()) != null){
                System.out.println("recv:"+line);
//                while(!Const.MESSAGE_EOF.equals(line=in.readLine())){
                // 一直读取到结束符
                if(!Const.MESSAGE_EOF.equals(line)){
                    sb.append(line);
                    continue;
                }
                Message message = JSONUtil.deserialize(sb.toString(), Message.class);
                // 处理消息
                Result result = MessageService.handle(client, message);
                // 输出处理结果
                out.println(JSONUtil.serialize(result));
                out.println(Const.MESSAGE_EOF);
                // 重置字符串缓存，并回到循环进行阻塞等待下条消息
                sb.delete(0, sb.length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
