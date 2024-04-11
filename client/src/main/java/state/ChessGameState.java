package state;

import clientlogic.ServerFacade;
import ui.ChessGameBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static ui.SharedBuilder.*;
import ui.EscapeSequences;


public class ChessGameState extends AState {

    protected static Map<String, Function<String[], String>> _commandMethods = new HashMap<>();

    private final String _color = EscapeSequences.SET_TEXT_COLOR_BLUE;

    private boolean _resign = false;


//1. help: displays text informing user what actions they can take (all of them below)
//2. Redraw chess board: just print out chess board again, don't know why we have it
//3. leave: transistions back to post-login UI, no DB changes
//4. make move: makes move, new board printed out / updated
//5. resign: prompt user to confirm they want to resign. If they do, forfeits game. 2 - part grrr. just have resign-count, it gets put to 1, reset otherwise? Or bite bullet and make scanner.
//6. highlight legal moves. allows user to inpput what piece they want to highlight legal moves. current pieces squares, all legal moves highlighted



    public ChessGameState(ServerFacade serverFacade, StateNotifier observer) {
        super(serverFacade, observer);
//        _commandMethods.put("redraw", this::Redraw);
//        _commandMethods.put("leave", this::Leave);
//        _commandMethods.put("move", this::Move);
//        _commandMethods.put("highlight", this::Highlight);
//        _commandMethods.put("resign", this::Resign);
//        _commandMethods.put("cancel", this::cancel);
//        _commandMethods.put("stay", this::Stay);
//        _commandMethods.put("help", this::Help);
    }


//    private String Redraw(String[] params) {
//        String bothBoards = ChessGameBuilder.printBoard("white") + "\n\n" + ChessGameBuilder.printBoard("black");
//        return bothBoards;
//    }
//
//    private String Leave(String[] params) {
//        _observer.ChangeStateLoggedIn();
//        _gameColor = null;
//        return setStringColor(_color, ChessGameBuilder.exitString);
//    }
//
//    private String Move(String[] params) {
//        return setStringColor(_color, ChessGameBuilder.helpString);
//    }
//
//    private String Highlight(String[] params) {
//        return setStringColor(_color, ChessGameBuilder.helpString);
//    }
//
//    private String Resign(String[] params) {
//        _resign = true;
//        return setStringColor(_color, ChessGameBuilder.resignString);
//    }
//
//    private String Confirm(String[] params) {
//
//        _observer.ChangeStateLoggedIn();
//        return setStringColor(_color, ChessGameBuilder.confirmString);
//    }
//
//        private String Cancel(String[] params) {
//
//
//        _observer.ChangeStateLoggedIn();
//        return setStringColor(_color, ChessGameBuilder.exitString);
//    }
//
//    private String Help(String[] params) {
//        return setStringColor(_color, ChessGameBuilder.helpString);
//    }


    @Override
    String DefaultCommand(String[] params) {
        String bothBoards = ChessGameBuilder.printBoard("white") + "\n\n" + ChessGameBuilder.printBoard("black");
        return bothBoards;
    }

    @Override
    Map<String, Function<String[], String>> getCommandMethods() {
        return _commandMethods;
    }

}
