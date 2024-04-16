package state;

import chess.*;
import ui.ChessGameBuilder;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import ui.EscapeSequences;

import static ui.ChessGameBuilder.*;
import static ui.SharedBuilder.*;


public class ChessGameState extends AState {

    protected static Map<String, Function<String[], String>> _commandMethods = new HashMap<>();
    private final String _color = EscapeSequences.SET_TEXT_COLOR_BLUE;
    private boolean _resign = false;
    public ChessGame game;



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



    public ChessGameState(ClientContext context) {
        super(context);

        _commandMethods.put("redraw", this::Redraw);
        _commandMethods.put("leave", this::Leave);
        _commandMethods.put("move", this::Move);
        _commandMethods.put("highlight", this::Highlight);
        _commandMethods.put("resign", this::Resign);
        _commandMethods.put("cancel", this::Cancel);
        _commandMethods.put("confirm", this::Confirm);
        _commandMethods.put("syntax", this::Syntax);
        _commandMethods.put("help", this::Help);

    }



    private String Redraw(String[] params) {

       return DrawBoard(game.getBoard().getSquares());
    }


    private String Move(String[] params) {

        // make move: makes move, new board printed out
        // Make_Move: Integer gameID, ChessMove move
        // makes move, updates DB, draws Board

        if (params == null || params.length != 1) {
            return getErrorStringSyntax("move");
        }

        if (game.isGameOver()) {
            return setStringColor(_color, gameOverString);
        }

        try {
            String move = params[0];
            ChessMove chessMove;

            if (move.length() == 5 || move.length() == 7) {

                Integer colFrom = colDict.get(move.charAt(0));
                Integer rowFrom = rowDict.get(move.charAt(1));

                Integer colTo = colDict.get(move.charAt(3));
                Integer rowTo = rowDict.get(move.charAt(4));

                ChessPiece.PieceType promotionPiece = null;

                if (colFrom == null || rowFrom == null || colTo == null || rowTo == null || move.charAt(2) != '-') {
                    return getErrorStringSyntax("move");
                }

                if (move.length() == 7) {
                    promotionPiece = pieceDict.get(move.charAt(6));

                    if (promotionPiece == null || move.charAt(5) != '=') {
                        return getErrorStringSyntax("move");
                    }
                }

                ChessPosition currPosition = new ChessPosition(rowFrom, colFrom);
                ChessBoard currBoard = game.getBoard();
                ChessPiece currPiece = currBoard.getPiece(currPosition);

                if (currPiece.getTeamColor() != game.getTeamTurn()) {
                    return setStringColor(_color, highlightFailString);
                }

                chessMove = new ChessMove(new ChessPosition(rowFrom, colFrom), new ChessPosition(rowTo, colTo), promotionPiece);
                context.webSocketFacade.MakeMove(chessMove);
                return "";
            }

            else {
                return getErrorStringSyntax("move");
            }

        }
        catch (Exception ex) {
            return getErrorStringRequest(ex.getMessage(), "move");
        }
    }


    private String Highlight(String[] params) {
         if (params == null || params.length != 1) {
            return getErrorStringSyntax("highlight");
        }
         if (game.isGameOver()) {
             return setStringColor(_color, gameOverString);
         }

        try {

            String move = params[0];

            if (move.length() != 2){
                return getErrorStringSyntax("highlight");
            }

            Integer col = colDict.get(move.charAt(0));
            Integer row = rowDict.get(move.charAt(1));

            if (col == null || row == null) {
                return getErrorStringSyntax("highlight");
            }

            ChessPosition currPosition = new ChessPosition(row, col);
            ChessBoard currBoard = game.getBoard();
            ChessPiece currPiece = currBoard.getPiece(currPosition);

            if (currPiece.getTeamColor() != game.getTeamTurn()) {
                return setStringColor(_color, highlightFailString);
            }

            Collection<ChessMove> moves = game.validMoves(currPosition);

            HashSet<ChessPosition> endPositions = moves.stream()
                    .map(ChessMove::getEndPosition)
                    .collect(Collectors.toCollection(HashSet::new));
            endPositions.add(currPosition);


            return HighlightBoard(game.getBoard().getSquares(), endPositions);


        }
        catch (Exception e) {
            return getErrorStringRequest(e.toString(), "highlight");
        }

    }



    private String Resign(String[] params) {

        if (context.gameColor == null) {
            return setStringColor(_color, observerResignString);
        }
        else {
            _resign = true;
            return setStringColor(_color, resignString);
        }
    }


    private String Leave(String[] params) {

        // leave: transitions back to post-login UI, no DB changes
        // Leave: Integer gameID
        // TODO server facade method with gameID, notifies everyone else that user left
        // - Gameid used to select connection to remove from
        try {
            context.webSocketFacade.Leave();

            context.gameColor = null;
            context.webSocketFacade.session.close();
            context.webSocketFacade = null;
            context.observer.ChangeStateLoggedIn();

            return setStringColor(_color, ChessGameBuilder.leaveString);
        }
        catch (Exception ex) {
            return getErrorStringRequest(ex.getMessage(), "leave");
        }
    }


    private String Confirm(String[] params) {
        if (!_resign) {
            return setStringColor(_color, defaultString);
        }
        else {
            try {
                context.webSocketFacade.Resign();
                return "";
            } catch (Exception ex) {
                return getErrorStringRequest(ex.getMessage(), "confirm");
            }
        }
    }


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

    public ChessGame getGame() {
        return game;
    }

    public void setGame(ChessGame newGame) {
        game = newGame;
    }

    public String DrawBoard(ChessPiece[][] board) {
        return ChessGameBuilder.printBoard(board, context.gameColor);
    }

    public String HighlightBoard(ChessPiece[][] board, HashSet<ChessPosition> moves) {
        return ChessGameBuilder.printBoardWithHighlights(board, context.gameColor, moves);
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
