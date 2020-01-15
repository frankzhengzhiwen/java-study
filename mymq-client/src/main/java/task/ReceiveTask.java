package task;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import mail.SendMailText;
import model.Message;
import model.Result;
import util.Const;
import util.JSONUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReceiveTask implements Runnable {

    private BufferedReader in;

    public ReceiveTask(Socket client){
        try {
            in = new BufferedReader(new InputStreamReader(
                    client.getInputStream(), Const.CHARSET));
        } catch (IOException e) {
            throw new RuntimeException("获取socket输入流错误", e);
        }
    }

    @Override
    public void run() {
        try {
            StringBuilder sb = new StringBuilder();
            while(true) {
                sb.delete(0, sb.length());
                // 按行读取
                String line;
                // while((line=in.readLine()) != null){
                // 一直读取到结束符
                while (!Const.MESSAGE_EOF.equals(line = in.readLine())) {
                    sb.append(line);
                    //                    if(!Const.MESSAGE_EOF.equals(line)){
                    //                        sb.append(line);
                    //                        continue;
                    //                    }
                }
                ObjectMapper mapper = JSONUtil.getMapper();

                Result<Message> recv = mapper.readValue(sb.toString(),
                        new TypeReference<Result<Message>>(){/**/});
                Message message = recv.getData();
                if(message == null){
                    System.out.println("响应消息："+recv.getMessage()
                            +(recv.getSuccess()?"":"，堆栈信息为：\n"+recv.getStackTrace()));
                }else{
                    System.out.println("接收到消息，即将发送邮件："+message.getContent());
                    SendMailText.send(SendMailText.RECIPIENT_ADDRESS,
                            "赶紧夸夸我吧，我很可爱哦", message.getContent());
                }
//                System.out.println("即将发布一个邮件消息，请输入收件人邮箱：");
//                System.out.println("请输入要发布的邮件标题：");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
