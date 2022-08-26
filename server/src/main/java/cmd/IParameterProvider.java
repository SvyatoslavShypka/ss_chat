package cmd;

public interface IParameterProvider {

    String DEFAULT_STRING = "";
    int DEFAULT_INTEGER = -1;

    default String getString(String key) {
        return getString(key, DEFAULT_STRING);
    }

    String getString(String key, String def);

    default int getInteger(String key) {
        return getInteger(key, DEFAULT_INTEGER);
    }

    int getInteger(String key, int def);
}
