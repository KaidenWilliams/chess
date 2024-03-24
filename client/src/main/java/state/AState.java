package state;

import clientlogic.ServerFacade;
import exceptionclient.ClientException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class AState {

    protected String _authToken;
    protected ServerFacade _serverFacade;


    public AState(ServerFacade serverFacade){
        _serverFacade = serverFacade;
    }

    public String eval(String commandName, String[] params) throws ClientException {
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
