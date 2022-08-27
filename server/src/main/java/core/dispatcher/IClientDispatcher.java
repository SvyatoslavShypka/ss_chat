package core.dispatcher;

import core.IThreadControl;
import core.connection.Client;

//The interface inherits from IThreadControl so that we can control the thread in which the dispatcher will run.
public interface IClientDispatcher extends IThreadControl {

    boolean hasClientInQueue();
    Client getClientFromQueue();
    boolean addClientToQueue(Client client);

}