package core.writer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class WriterThread extends Thread implements IWriterThread {

//    The principle of the semaphore here is the same as in the previous lesson. The thread will sleep at the semaphore until it receives a signal to work.
    private final Semaphore semaphore = new Semaphore(0);
//    messageQueue contains the messages to be sent.
    private final Queue<QueueTuple> messageQueue = new ConcurrentLinkedQueue<>();
//    The working variable indicates whether the thread is working or sleeping at the semaphore.
    private boolean working = false;
//    The interrupt variable has the same purpose as in the previous lessons, when we created classes that inherit from the Thread class.
    private boolean interrupt = false;

//    Nested class in the WriterThread class because we won't use it anywhere else
    private static final class QueueTuple {
        final Object message;
        final ObjectOutputStream writer;

        private QueueTuple(ObjectOutputStream writer, Object message) {
            this.message = message;
            this.writer = writer;
        }
    }

    public WriterThread() {
        super("WriterThread");
    }

//    Sending a message will be nothing but adding a message to the message queue, and if the thread isn't working, we'll wake it up
//    and set the working variable to true:
    @Override
    public void sendMessage(ObjectOutputStream writer, Object message) {
//        OutputStream was changed on writer
        messageQueue.add(new QueueTuple(writer, message));
        if (!working) {
            working = true;
            semaphore.release();
        }
    }

    @Override
    public void shutdown() {
        interrupt = true;
        semaphore.release();
        try {
            join();
        } catch (InterruptedException ignored) {}
    }

    @Override
    public void run() {
        while(!interrupt) {
            while(messageQueue.isEmpty() && !interrupt) {
                try {
                    semaphore.acquire();
                } catch (InterruptedException ignored) {}
            }
            working = true;
            while(!messageQueue.isEmpty()) {
                final QueueTuple entry = messageQueue.poll();
                try {
//                    writeLine was changed on writeObject
                    entry.writer.writeObject(entry.message);
                    entry.writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            working = false;
        }
    }

}
