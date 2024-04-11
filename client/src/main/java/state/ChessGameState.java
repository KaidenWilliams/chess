package state;

import chess.*;
import clientlogic.ServerFacade;
import ui.ChessGameBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import ui.EscapeSequences;

import static ui.ChessGameBuilder.*;
import static ui.SharedBuilder.*;


public class ChessGameState extends AState {

    protected static Map<String, Function<String[], String>> _commandMethods = new HashMap<>();
    private final String _color = EscapeSequences.SET_TEXT_COLOR_BLUE;
    private boolean _resign = false;
    private ChessPiece[][] board;



//1. Join_Player: Integer gameID, ChessGame.TeamColor playerColor
//2. Join_Observer: Integer gameID
//5. Resign: Integer gameID



//1. Load_Game: game(any type, needs to be called game) - sends current game state to client
//2. Error: String errorMessage - error message sent to client when it sends invalid command, message must include word Error
//3. Notification: Message - informs a player when another player has made an action



//1. help: displays text informing user what actions they can take (all of them below)
//2. Redraw chess board: just print out chess board again, don't know why we have it
//5. resign: prompt user to confirm they want to resign. If they do, forfeits game. 2 - part grrr. just have resign-count, it gets put to 1, reset otherwise? Or bite bullet and make scanner.
//6. highlight legal moves. allows user to inpput what piece they want to highlight legal moves. current pieces squares, all legal moves highlighted



    public ChessGameState(ServerFacade serverFacade, StateNotifier observer) {
        super(serverFacade, observer);
        _commandMethods.put("redraw", this::Redraw);
        _commandMethods.put("leave", this::Leave);
//        _commandMethods.put("move", this::Move);
//        _commandMethods.put("highlight", this::Highlight);
        _commandMethods.put("resign", this::Resign);
        _commandMethods.put("cancel", this::Cancel);
//        _commandMethods.put("confirm", this::Confirm);
        _commandMethods.put("syntax", this::Syntax);
        _commandMethods.put("help", this::Help);

        var chessBoard = new ChessBoard();
        chessBoard.resetBoard();

    }



    private String Redraw(String[] params) {

        return DrawBoard(board);
    }

//    private String wsJoin(String[] params) {
//
//        // leave: transitions back to post-login UI, no DB changes
//        // Leave: Integer gameID
//        // TODO server facade method with gameID, notifies everyone else that user left
//        // - Gameid used to select connection to remove from
//
//        _observer.ChangeStateLoggedIn();
//        _gameColor = null;
//        return setStringColor(_color, ChessGameBuilder.leaveString);
//    }
//

    private String Leave(String[] params) {

        // leave: transitions back to post-login UI, no DB changes
        // Leave: Integer gameID
        // TODO server facade method with gameID, notifies everyone else that user left
        // - Gameid used to select connection to remove from

        _observer.ChangeStateLoggedIn();
        _gameColor = null;
        return setStringColor(_color, ChessGameBuilder.leaveString);
    }


    private String Move(String[] params) {

        // make move: makes move, new board printed out
        // Make_Move: Integer gameID, ChessMove move
        // makes move, updates DB, draws Board

        if (params == null || params.length != 1) {
            return setStringColor(_color, getErrorStringSyntax("move"));
        }

        try {
            String move = params[0];

            if (move.length() == 5 || move.length() == 7) {

                Integer colFrom = colDict.get(move.charAt(0));
                Integer rowFrom = colDict.get(move.charAt(1));

                Integer colTo = colDict.get(move.charAt(3));
                Integer rowTo = colDict.get(move.charAt(4));

                ChessPiece.PieceType promotionPiece = null;

                if (colFrom == null || rowFrom == null || colTo == null || rowTo == null || move.charAt(2) != '-') {
                    return setStringColor(_color, getErrorStringSyntax("move"));
                }

                if (move.length() == 7) {
                    promotionPiece = pieceDict.get(move.charAt(6));

                    if (promotionPiece == null || move.charAt(5) != '=') {
                        return setStringColor(_color, getErrorStringSyntax("move"));
                    }
                }
                ChessMove chessMove = new ChessMove(new ChessPosition(rowFrom, colFrom), new ChessPosition(rowTo, colTo), promotionPiece);
            }

            else {
                return setStringColor(_color, getErrorStringSyntax("move"));
            }

            return DrawBoard(board);

        }
        catch (Exception ex) {
            return setStringColor(_color, getErrorStringRequest(ex.getMessage(), "move"));
        }
    }




//
//    private String Highlight(String[] params) {
//         if (params == null || params.length != 1) {
//            return setStringColor(_color, getErrorStringSyntax("create"));
//        }
//        try {
//            var req = new CreateGameRequest.RequestBody(params[0]);
//            CreateGameResponse res = _serverFacade.createGame(req, _authToken);
//
//            return setStringColor(_color, LoggedInBuilder.getCreateGameString(params[0]));
//        }
//        catch (ClientException e) {
//            return setStringColor(_color, getErrorStringRequest(e.toString(), "list"));
//        }
//    }
//
    private String Resign(String[] params) {
        _resign = true;
        return setStringColor(_color, resignString);
    }


//    private String Confirm(String[] params) {
//
//            _gameColor = null;
//                set team turn to null, update DB
//        _observer.ChangeStateLoggedIn();
//        return setStringColor(_color, ChessGameBuilder.confirmString);
//    }
//
        private String Cancel(String[] params) {
            if (!_resign) {
                return setStringColor(_color, defaultString);
            }
          _resign = false;

        return setStringColor(_color, cancelString);
    }


    private String Syntax(String[] params) {
        return setStringColor(_color, syntaxString);
    }

    private String Help(String[] params) {
        return setStringColor(_color, helpString);
    }


    private String DrawBoard(ChessPiece[][] chessBoard) {


//        var position = new ChessPosition(3, 1);
//        var piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
//
//        chessBoard.addPiece(position, piece);
//        board = chessBoard.getSquares();

//        return DrawBoard(board);

        String bothBoards = ChessGameBuilder.printBoard(chessBoard,"white") + "\n\n" + ChessGameBuilder.printBoard(chessBoard,"black");
        return bothBoards;

//        return printBoard(chessBoard, _gameColor);
    }

    @Override
    String DefaultCommand(String[] params) {
        return setStringColor(_color, defaultString);
    }


    @Override
    Map<String, Function<String[], String>> getCommandMethods() {
        return _commandMethods;
    }

}
