import cmd.IParameterFactory;
import cmd.IParameterProvider;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import core.server.IServerThread;
import core.server.IServerThreadFactory;

import java.io.IOException;
import java.util.Scanner;

public class Server {

    private final IParameterFactory parameterFactory;

    private final IServerThreadFactory serverThreadFactory;

    @Inject
    public Server(IParameterFactory parameterFactory, IServerThreadFactory serverThreadFactory) {
        this.parameterFactory = parameterFactory;
        this.serverThreadFactory = serverThreadFactory;
    }

    private void run(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);  // Create a Scanner object

        final IParameterProvider parameters = parameterFactory.getParameters(args);
        final IServerThread serverThread = serverThreadFactory.getServerThread(parameters);
        serverThread.start();

        while(true) {
            final String input = scanner.nextLine();
            if ("exit".equals(input)) {
                break;
            }
        }
        serverThread.shutdown();
//        System.out.println("Maximum number of clients: " + parameters.getInteger(CmdParser.CLIENTS));
    }

    public static void main(String[] args) throws Exception {

        final Injector injector = Guice.createInjector(new ServerModule());
        Server server = injector.getInstance(Server.class);
        server.run(args);

    }
}
