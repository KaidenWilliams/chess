package state;

import clientlogic.ServerFacade;
import ui.ChessGameBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import ui.SharedBuilder;
import ui.EscapeSequences;

public class ChessGameState extends AState {

    protected static Map<String, Function<String[], String>> _commandMethods = new HashMap<>();

    private final String _color = EscapeSequences.SET_TEXT_COLOR_BLUE;

    public ChessGameState(ServerFacade serverFacade, StateNotifier observer) {
        super(serverFacade, observer);
        _commandMethods.put("exit", this::Exit);
        _commandMethods.put("help", this::Help);
    }


    private String Exit(String[] params) {
        _observer.ChangeStateLoggedIn();
        return SharedBuilder.setStringColor(_color, ChessGameBuilder.exitString);
    }

    private String Help(String[] params) {
        return SharedBuilder.setStringColor(_color, ChessGameBuilder.helpString);
    }


    @Override
    Map<String, Function<String[], String>> getCommandMethods() {
        return _commandMethods;
    }

    @Override
    String DefaultCommand(String[] params) {
        String bothBoards = ChessGameBuilder.printBoard("white") + "\n\n" + ChessGameBuilder.printBoard("black");
        return bothBoards;
    }
}
