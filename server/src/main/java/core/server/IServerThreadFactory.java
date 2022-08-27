package core.server;

import cmd.IParameterProvider;

import java.io.IOException;

public interface IServerThreadFactory {

    IServerThread getServerThread(IParameterProvider parameters) throws IOException;

}
