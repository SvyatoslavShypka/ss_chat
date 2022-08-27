package core.connection;

import com.google.inject.Inject;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class ConnectionManager implements IConnectionManager {
// a collection in the class that will contain the connected clients
    private final List<IClient> clients = new ArrayList<>();

//    a threadpool for individual clients
    private final ExecutorService pool;

//    a constant representing the maximum number of actively communicating clients
    final int maxClients;

//    The constructor
    @Inject
    public ConnectionManager(ExecutorService pool, int maxClients) {
        this.pool = pool;
        this.maxClients = maxClients;
    }

//    method to decide whether to put a client in the active client collection or the waiting queue
    private synchronized void insertClientToListOrQueue(Client client) {
        if (clients.size() < maxClients) {
            clients.add(client);
            client.setConnectionClosedListener(() -> {
                clients.remove(client);
            });
            pool.submit(client);
        } else {
            // TODO add the client to the waiting queue
        }
    }

//    The addClient() method only delegates to the insertClientToListOrQueue() method.
    @Override
    public void addClient(Socket socket) throws IOException {
        insertClientToListOrQueue(new Client(socket));
    }

    @Override
    public void onServerStart() {}

//    When the server is stopped, we'll go through all the clients and close the connection with them. Finally, we'll terminate the threadpool itself:
    @Override
    public void onServerStop() {
        for (IClient client : clients) {
            client.close();
        }
        pool.shutdown();
    }

}
