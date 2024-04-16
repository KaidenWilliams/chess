package state;

import java.util.Map;
import java.util.function.Function;

public abstract class AState {


    protected final ClientContext context;

    public AState(ClientContext context){
        this.context = context;
    }


    public String eval(String commandName, String[] params)  {
        Function<String[], String> commandMethod = getCommandMethods().get(commandName);
        if (commandMethod != null) {
            return commandMethod.apply(params);
        } else {
            return defaultCommand(params);
        }
    }

    abstract Map<String, Function<String[], String>> getCommandMethods();

    abstract String defaultCommand(String[] params);

}
