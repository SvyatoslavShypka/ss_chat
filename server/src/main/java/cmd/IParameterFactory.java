package cmd;

public interface IParameterFactory {

    IParameterProvider getParameters(String[] args);

}
