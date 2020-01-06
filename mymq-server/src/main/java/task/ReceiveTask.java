package task;

import exception.BaseException;
import model.Broker;
import model.Message;
import model.Result;
import util.Const;
import util.JSONUtil;

import java.io.*;
import java.net.Socket;

public class ReceiveTask implements Runnable {

    private Socket client;

    public ReceiveTask(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            InputStream is = client.getInputStream();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(is, Const.CHARSET));
            PrintWriter pw = new PrintWriter(
                    client.getOutputStream(), true);
            try{
                // 按行读取
                String line;
                StringBuilder sb = new StringBuilder();
                while((line=in.readLine()) != null){
//                while(!Const.MESSAGE_EOF.equals(line=in.readLine())){
                    // 一直读取到结束符
                    if(!Const.MESSAGE_EOF.equals(line)){
                        sb.append(line);
                        continue;
                    }
                    Message message = JSONUtil.deserialize(sb.toString(), Message.class);
                    System.out.println("接收到消息："+message);
                    // 处理消息
                    Result result = Broker.handle(client, message);
                    // 输出处理结果
                    pw.println(JSONUtil.serialize(result));
                    pw.println(Const.MESSAGE_EOF);
                    // 重置字符串缓存，并回到循环进行阻塞等待下条消息
                    sb.delete(0, sb.length());
                }
            } catch (Exception e) {
                e.printStackTrace();
                // 包装异常信息
                Result result = wrapError(e);
                // 返回错误信息
                pw.println(JSONUtil.serialize(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 异常包装为处理结果
     * @param e
     * @return
     */
    private Result wrapError(Exception e) {
        // 设置错误信息
        Result result = new Result();
        result.setSuccess(false);
        if(e instanceof BaseException){
            result.setCode(((BaseException)e).getCode());
            result.setMessage(((BaseException)e).getMessage());
        }else{
            result.setCode("600");
            result.setMessage("未知错误");
        }
        // 设置异常堆栈信息
        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter(sw);
        e.printStackTrace(writer);
        result.setStackTrace(sw.toString());
        return result;
    }
}
