package task;

import service.MessageService;

import java.net.Socket;

public class ConsumeTask implements Runnable {

    private Socket client;

    public ConsumeTask(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        MessageService.consume(client);
//        try {
//            Result result = new Result();
//            result.setSuccess(true);
//            result.setCode("haha");
//            result.setMessage("哈哈");
//            result.setData("message，多个哦");
//            PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
//            pw.println(JSONUtil.serialize(result));
//            pw.println(Const.MESSAGE_EOF);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
