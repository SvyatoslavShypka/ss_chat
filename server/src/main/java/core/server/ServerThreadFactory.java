package core.server;

import cmd.CmdParser;
import cmd.IParameterProvider;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import core.connection.IConnectionManagerFactory;
import core.dispatcher.ClientDispatcher;

import java.io.IOException;

@Singleton
public class ServerThreadFactory implements IServerThreadFactory{

    // Default port
    private static final int DEFAULT_SERVER_PORT = 15378;
    // Default maximum number of clients
    private static final int DEFAULT_MAX_CLIENTS = 3;
    // Default waiting queue size
    private static final int DEFAULT_WAITING_QUEUE_SIZE = 1;

    private final IConnectionManagerFactory connectionManagerFactory;

    @Inject
    public ServerThreadFactory(IConnectionManagerFactory connectionManagerFactory) {
        this.connectionManagerFactory = connectionManagerFactory;
    }

    @Override
    public IServerThread getServerThread(IParameterProvider parameters) throws IOException {
        final int port = parameters.getInteger(CmdParser.PORT, DEFAULT_SERVER_PORT);
        final int maxClients = parameters.getInteger(CmdParser.CLIENTS, DEFAULT_MAX_CLIENTS);
        final int waitingQueueSize = parameters.getInteger(CmdParser.MAX_WAITING_QUEUE, DEFAULT_WAITING_QUEUE_SIZE);
//added iClientDispatcher with 20 pcs as waitingQueueSize
        return new ServerThread(connectionManagerFactory.getConnectionManager(new ClientDispatcher(20), maxClients,
                waitingQueueSize), port);
    }
}
