import com.fasterxml.jackson.databind.ObjectMapper;
import model.Message;
import model.Result;
import util.Const;
import util.JSONUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class MQClient {

    public static Message testMessage(){
        Message message = new Message();
        message.setContent("卖出一部华为mate30");
        Set<String> tags = new HashSet<>();
        tags.add("手机");
        tags.add("华为");
        tags.add("huawei");
        tags.add("mate30");
        message.setTags(tags);
        return message;
    }

    public static void main(String[] args) {
        try {
            // Socket客户端连接
            Socket client = new Socket();
            client.bind(new InetSocketAddress(Const.CLIENT_PORT));
            client.connect(new InetSocketAddress(Const.SERVER_HOST, Const.SERVER_PORT));

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    client.getInputStream(), Const.CHARSET));

            PrintWriter out = new PrintWriter(client.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);
            while(scanner.hasNext()){
                String type = scanner.nextLine();
                Message message = testMessage();
                message.setType(type);
                //先向消息队列发送消息
                String send = new ObjectMapper().writeValueAsString(message);
                out.println(send);
                //消息结束符
                out.println(Const.MESSAGE_EOF);

                //再从消息队列获取一条消息
//                String recv = in.readLine();
//                System.out.println("receive:"+recv);

                // 按行读取
                String line;
                StringBuilder sb = new StringBuilder();
//                while((line=in.readLine()) != null){
                // 一直读取到结束符
                while(!Const.MESSAGE_EOF.equals(line=in.readLine())){
                    sb.append(line);
//                    if(!Const.MESSAGE_EOF.equals(line)){
//                        sb.append(line);
//                        continue;
//                    }
                }
                Result recv = JSONUtil.deserialize(sb.toString(), Result.class);
                System.out.println("接收到消息："+recv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
