package state;

import chess.ChessGame;
import clientlogic.ServerFacade;
import clientlogic.websocket.WebSocketFacade;
import exceptionclient.ClientException;

import java.util.Map;
import java.util.function.Function;

public abstract class AState {

    // Every state will need websocket messages fuctionality, just way I built it
    // Pass webSocketFacade into each of them as well, call facade methods

    // Will need to probably initialize WebSocketFacade upon entering chess game, only time it
    // - is used (I think)

    // also in chess game need to implement move, lots of other endpoints, might have to split
    // - it into multiple classes or something

    protected static String _authToken;
    protected static String _username;

    protected static String _gameColor;
    protected static ServerFacade _serverFacade;
    protected static WebSocketFacade _webSocketFacade;
    protected static String _URL;
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

    public void setURL(String url) {
        _URL = url;
    }

    abstract Map<String, Function<String[], String>> getCommandMethods();

    abstract String DefaultCommand(String[] params);

}
