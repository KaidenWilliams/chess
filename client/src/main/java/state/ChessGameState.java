package state;

import clientlogic.ServerFacade;
import ui.ChessGameBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ChessGameState extends AState {

    protected static Map<String, Function<String[], String>> _commandMethods = new HashMap<>();

    public ChessGameState(ServerFacade serverFacade, StateNotifier observer) {
        super(serverFacade, observer);
        _commandMethods.put("exit", this::Exit);
        _commandMethods.put("help", this::Help);
    }


    private String Exit(String[] params) {
        _observer.ChangeStateLoggedIn();
        return ChessGameBuilder.exitString;
    }

    private String Help(String[] params) {
        return ChessGameBuilder.helpString;
    }


    @Override
    Map<String, Function<String[], String>> getCommandMethods() {
        return _commandMethods;
    }

    @Override
    String DefaultCommand(String[] params) {
        return ChessGameBuilder.getBothBoards();
    }
}
