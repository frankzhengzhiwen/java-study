import task.ConsumeTask;
import task.MessageTask;
import util.Const;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final ExecutorService pool = Executors
            .newCachedThreadPool();

    private volatile boolean running;

    private ServerThread thread;

    public void start(){
        if(running)
            return;
        running = true;
        thread = new ServerThread();
        thread.start();
    }

    @SuppressWarnings("deprecation")
    public void stop(){
        thread.stop();
        pool.shutdown();
    }

    class ServerThread extends Thread{

        @Override
        public void run(){
            try {
                ServerSocket server = new ServerSocket(Const.SERVER_PORT);
                while(true){
                    Socket client = server.accept();
                    pool.execute(new MessageTask(client));
                    pool.execute(new ConsumeTask(client));
                }
            } catch (IOException e) {
                e.printStackTrace();
                Server.this.stop();
            }
        }
    }

    public static void main(String[] args) {
        new Server().start();
    }
}
