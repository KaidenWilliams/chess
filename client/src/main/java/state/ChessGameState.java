package state;

import clientlogic.ServerFacade;

import java.util.Map;
import java.util.function.Function;

public class ChessGameState extends AState {

    public ChessGameState(ServerFacade serverFacade) {
        super(serverFacade);
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
