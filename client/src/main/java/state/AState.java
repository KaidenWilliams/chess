package state;

import chess.ChessGame;
import clientlogic.ServerFacade;
import clientlogic.websocket.WebSocketFacade;
import exceptionclient.ClientException;

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
            return DefaultCommand(params);
        }
    }

    abstract Map<String, Function<String[], String>> getCommandMethods();

    abstract String DefaultCommand(String[] params);

}
