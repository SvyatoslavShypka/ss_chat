import cmd.IParameterFactory;
import cmd.ParameterFactory;
import com.google.inject.AbstractModule;
import core.connection.ConnectionManagerFactory;
import core.connection.IConnectionManagerFactory;
import core.server.IServerThreadFactory;
import core.server.ServerThreadFactory;

public class ServerModule extends AbstractModule {

    @Override
    public void configure() {
        bind(IParameterFactory.class).to(ParameterFactory.class);
        bind(IServerThreadFactory.class).to(ServerThreadFactory.class);
//        register the factory
        bind(IConnectionManagerFactory.class).to(ConnectionManagerFactory.class);
    }
}
