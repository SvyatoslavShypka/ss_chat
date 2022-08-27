package core.connection;

import core.dispatcher.IClientDispatcher;

public interface IConnectionManagerFactory {

    IConnectionManager getConnectionManager(IClientDispatcher clientDispatcher, int maxClients, int waitingQueueSize);

}
