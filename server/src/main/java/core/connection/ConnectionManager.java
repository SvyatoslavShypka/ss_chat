package core.connection;

import com.google.inject.Inject;
import core.dispatcher.IClientDispatcher;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class ConnectionManager implements IConnectionManager {

    // a collection in the class that will contain the connected clients
    private final List<IClient> clients = new ArrayList<>();

    private final IClientDispatcher clientDispatcher;

//    a threadpool for individual clients
    private final ExecutorService pool;

//    a constant representing the maximum number of actively communicating clients
    final int maxClients;

//    The constructor
    @Inject
    public ConnectionManager(IClientDispatcher clientDispatcher, ExecutorService pool, int maxClients) {
        this.clientDispatcher = clientDispatcher;
        this.pool = pool;
        this.maxClients = maxClients;
    }

//    method to decide whether to put a client in the active client collection or the waiting queue
    private synchronized void insertClientToListOrQueue(Client client) {
        if (clients.size() < maxClients) {
            clients.add(client);
//          changed
/*
            client.setConnectionClosedListener(() -> {
                clients.remove(client);
            });
*/
//            the server tries to retrieve a client from the queue and add it to the active clients
            client.setConnectionClosedListener(() -> {
                    clients.remove(client);
            if (clientDispatcher.hasClientInQueue()) {
                this.insertClientToListOrQueue(clientDispatcher.getClientFromQueue());
            }
});
            pool.submit(client);
        } else {
            // DoneTODO add the client to the waiting queue
//            Here we use the return value of the addClientToQueue() method, which is true if it added the client to the queue successfully.  If the queue is
//            full, the method returns false and we disconnect the client.
            if (!clientDispatcher.addClientToQueue(client)) {
                client.close();
            }

        }
    }

//    The addClient() method only delegates to the insertClientToListOrQueue() method.
    @Override
    public void addClient(Socket socket) throws IOException {
        insertClientToListOrQueue(new Client(socket));
    }

    @Override
    public void onServerStart() {
        clientDispatcher.start();
    }

//    When the server is stopped, we'll go through all the clients and close the connection with them. Finally, we'll terminate the threadpool itself:
    @Override
    public void onServerStop() {
        for (IClient client : clients) {
            client.close();
        }
        pool.shutdown();
//        not sure about position
//        set the interupt variable and wake up the thread. After a while, the dispatcher thread terminates.
//        terminate the dispatcher
        clientDispatcher.shutdown();
    }

}
