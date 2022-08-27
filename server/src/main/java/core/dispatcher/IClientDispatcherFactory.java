package core.dispatcher;

import core.dispatcher.IClientDispatcher;

public interface IClientDispatcherFactory {

    IClientDispatcher getClientDispatcher(int waitingQueueSize);

}
