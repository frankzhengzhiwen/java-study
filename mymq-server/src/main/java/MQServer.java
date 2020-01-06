import task.ReceiveTask;
import util.Const;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MQServer {

    private static final ExecutorService RECEIVE_POOL = Executors
            .newCachedThreadPool();

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(Const.SERVER_PORT);
            while(true){
                Socket client = server.accept();
                RECEIVE_POOL.execute(new ReceiveTask(client));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
