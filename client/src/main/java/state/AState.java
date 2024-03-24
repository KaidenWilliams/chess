package state;

import clientlogic.ServerFacade;
import exceptionclient.ClientException;

import java.util.Map;
import java.util.function.Function;

public abstract class AState {

    // TODO will see how authtoken works, if everyone shares it
    protected static String _authToken;
    protected static ServerFacade _serverFacade;
    protected static StateNotifier _observer;


    public AState(ServerFacade serverFacade, StateNotifier observer){
        _serverFacade = serverFacade;
        _observer = observer;
    }

    public String eval(String commandName, String[] params)  {
        Function<String[], String> commandMethod = getCommandMethods().get(commandName);
        if (commandMethod != null) {
            return commandMethod.apply(params);
        } else {
            return DefaultCommand(params);
        }
    }

    abstract Map<String, Function<String[], String>> getCommandMethods();

    abstract String DefaultCommand(String[] params);

}
