package core.connection;

import core.dispatcher.ClientDispatcher;
import core.dispatcher.IClientDispatcher;
import core.dispatcher.IClientDispatcherFactory;

public class ClientDispatcherFactory implements IClientDispatcherFactory {
    @Override
    public IClientDispatcher getClientDispatcher(int waitingQueueSize) {
        return new ClientDispatcher(waitingQueueSize);
    }
}
