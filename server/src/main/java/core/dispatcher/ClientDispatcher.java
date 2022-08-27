package core.dispatcher;

import core.connection.Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class ClientDispatcher extends Thread implements IClientDispatcher {

    private static final int SLEEP_TIME = 5000;

//    The interrupt variable will control the thread. As long as it's false, the thread will run.  The thread will wait at the semaphore until
//    there are clients in the queue. Once a connected client queues, the thread will pass through the semaphore and do its job.
//    Once all the clients are removed from the queue, the thread is put back to sleep.
    private boolean interrupt = false;
//    The semaphore will manage the queue.
    private final Semaphore semaphore = new Semaphore(0);
//    Two collections follow. waitingQueue will keep the clients and clientsToRemove will contain clients that have closed the connection and must be removed
//    from the queue. The waitingQueueSize variable contains the maximum number of queued clients.
    private final Queue<Client> waitingQueue = new ConcurrentLinkedQueue<>();
    private final Collection<Client> clientsToRemove = new ArrayList<>();
//    The waitingQueueSize variable contains the maximum number of queued clients.
    private final int waitingQueueSize;

    public ClientDispatcher(int waitingQueueSize) {
        this.waitingQueueSize = waitingQueueSize;
    }

    @Override
    public boolean hasClientInQueue() {
        return !waitingQueue.isEmpty();
    }

    @Override
    public Client getClientFromQueue() {
        return waitingQueue.poll();
    }

//    First, we must determine if the queue can handle another client. If it does, the semaphore is released and true returned,
//    otherwise false is returned and nothing more happens.
    @Override
    public boolean addClientToQueue(Client client) {
        if (waitingQueue.size() < waitingQueueSize) {
            waitingQueue.add(client);
            semaphore.release();
            return true;
        }

        return false;
    }

    @Override
    public void run() {
        while(!interrupt) {
            while(waitingQueue.isEmpty() && !interrupt) {
                try {
                    semaphore.acquire();
                } catch (InterruptedException ignored) {}
            }

            if (interrupt) {
                clientsToRemove.addAll(waitingQueue);
            } else {
                final int count = waitingQueue.size();
// was forEach
                waitingQueue.iterator().forEachRemaining(client -> {
                    try {
// client.writer was
/*
                        client.writer.write(("count: " + count + "\n").getBytes());
                        client.writer.flush();
*/
                        client.writer.write(("count: " + count + "\n").getBytes());
                        client.writer.flush();
                    } catch (IOException e) {
                        clientsToRemove.add(client);
                    }
                });
            }

            waitingQueue.removeAll(clientsToRemove);
            for (Client client : clientsToRemove) {
                client.close();
            }
            clientsToRemove.clear();

            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException ignored) {}
        }
    }

//In this method we do three things:
//
//set the interupt variable to true
//release the semaphore
//wait for the thread to terminate
//Releasing the semaphore triggers the dispatcher thread. By setting the interrupt variable to true, all clients
// from the queue will be added to the list of clients to disconnect. After the connection with the client is  closed
// and the client removed from the queue, the infinite loop condition no longer applies and the thread terminates safely.
    @Override
    public void shutdown() {
        interrupt = true;
        semaphore.release();
        try {
            join();
        } catch (InterruptedException ignored) { }

    }
}
