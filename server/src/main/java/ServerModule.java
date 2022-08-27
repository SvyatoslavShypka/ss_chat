import cmd.IParameterFactory;
import cmd.ParameterFactory;
import com.google.inject.AbstractModule;
import core.connection.ClientDispatcherFactory;
import core.connection.ConnectionManagerFactory;
import core.dispatcher.IClientDispatcherFactory;
import core.connection.IConnectionManagerFactory;
import core.server.IServerThreadFactory;
import core.server.ServerThreadFactory;

public class ServerModule extends AbstractModule {

    @Override
    public void configure() {
//        register factories
        bind(IParameterFactory.class).to(ParameterFactory.class);
        bind(IServerThreadFactory.class).to(ServerThreadFactory.class);
        bind(IConnectionManagerFactory.class).to(ConnectionManagerFactory.class);
        bind(IClientDispatcherFactory.class).to(ClientDispatcherFactory.class);
    }
}
