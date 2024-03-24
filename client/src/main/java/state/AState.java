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
        ThrowingFunctionDumb<String[], String> commandMethod = getCommandMethods().get(commandName);
        if (commandMethod != null) {
            return commandMethod.apply(params);
        } else {
            return DefaultCommand(params);
        }
    }

    abstract Map<String, ThrowingFunctionDumb<String[], String>> getCommandMethods();

    abstract String DefaultCommand(String[] params);

}
