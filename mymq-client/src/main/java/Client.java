import task.ReceiveTask;
import task.SendTask;
import util.Const;

import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        try {
            // Socket客户端连接
            Socket client = new Socket();
            client.bind(new InetSocketAddress(
                    Const.SERVER_PORT+Integer.parseInt(args[0])));
            client.connect(new InetSocketAddress(Const.SERVER_HOST, Const.SERVER_PORT));

            // 发送数据
            new Thread(new SendTask(client)).start();
            // 接收数据
            new Thread(new ReceiveTask(client)).start();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
