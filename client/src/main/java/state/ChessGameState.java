package state;

import clientlogic.ServerFacade;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ChessGameState extends AState {

    protected static Map<String, Function<String[], String>> _commandMethods = new HashMap<>();

    public ChessGameState(ServerFacade serverFacade, StateNotifier observer) {
        super(serverFacade, observer);
//        _commandMethods.put("help", this::Logout);
//        _commandMethods.put("logout", this::CreateGame);
//        _commandMethods.put("help", this::ListGames);
//        _commandMethods.put("logout", this::JoinGame);
//        _commandMethods.put("help", this::JoinObserver);
    }

    @Override
    Map<String, Function<String[], String>> getCommandMethods() {
        return null;
    }

    @Override
    String DefaultCommand(String[] params) {
        return null;
    }
}
