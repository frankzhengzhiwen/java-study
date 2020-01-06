package struct;

import model.Message;

import java.util.concurrent.PriorityBlockingQueue;

public class MessageQueue extends PriorityBlockingQueue<Message> {

    @Override
    public void put(Message message) {
        super.put(message);
    }

    @Override
    public Message take() throws InterruptedException {
        return super.take();
    }

//    public Map<Message> take(String topic)
}
