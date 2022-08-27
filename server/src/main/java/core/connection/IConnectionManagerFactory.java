package core.connection;

public interface IConnectionManagerFactory {

    IConnectionManager getConnectionManager(int maxClients, int waitingQueueSize);

}
