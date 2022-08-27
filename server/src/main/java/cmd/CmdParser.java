package cmd;

import java.util.HashMap;
import java.util.Map;

public class CmdParser implements IParameterProvider {

    public static final String PORT = "port";
    public static final String CLIENTS = "clients";
    public static final String MAX_WAITING_QUEUE = "max_waiting_queue";

    private final Map<String, String> map = new HashMap<>();

    public CmdParser(String[] args) {
        for (String arg : args) {
            arg = arg.replace("-", "");
            String[] section = arg.split(",");
            for (String sec : section) {
            String[] raw = arg.split("=");
            map.put(raw[0], raw[1]);
            }
        }
        System.out.println(map);
    }

    @Override
    public String getString(String key, String def) {
        final String s = map.get(key);
        return s == null ? def : s;
    }

    @Override
    public int getInteger(String key, int def) {
        final String s = map.get(key);
        return s == null ? def : Integer.parseInt(s);
    }
}
