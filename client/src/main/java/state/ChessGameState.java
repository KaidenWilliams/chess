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

    protected static final Map<String, Function<String[], String>> _commandMethods = new HashMap<>();
    private static final String _color = EscapeSequences.SET_TEXT_COLOR_BLUE;
    private boolean resign = false;
    public ChessGame game;


    public ChessGameState(ClientContext context) {
        super(context);

        _commandMethods.put("redraw", this::redraw);
        _commandMethods.put("leave", this::leave);
        _commandMethods.put("move", this::move);
        _commandMethods.put("highlight", this::highlight);
        _commandMethods.put("resign", this::resign);
        _commandMethods.put("cancel", this::cancel);
        _commandMethods.put("confirm", this::confirm);
        _commandMethods.put("syntax", this::syntax);
        _commandMethods.put("help", this::help);

    }



    private String redraw(String[] params) {

       return drawBoard(game.getBoard().getSquares());
    }


    private String move(String[] params) {

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
                    return setStringColor(_color, moveFailString);
                }

                chessMove = new ChessMove(new ChessPosition(rowFrom, colFrom), new ChessPosition(rowTo, colTo), promotionPiece);
                context.webSocketFacade.makeMove(chessMove);
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


    private String highlight(String[] params) {
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


            return highlightBoard(game.getBoard().getSquares(), endPositions);


        }
        catch (Exception e) {
            return getErrorStringRequest(e.toString(), "highlight");
        }

    }


    private String resign(String[] params) {

        if (context.gameColor == null) {
            return setStringColor(_color, observerResignString);
        }
        else {
            resign = true;
            return setStringColor(_color, resignString);
        }
    }


    private String leave(String[] params) {

        try {
            context.webSocketFacade.leave();

            context.gameColor = null;
            context.webSocketFacade.session.close();
            context.webSocketFacade = null;
            context.observer.changeStateLoggedIn();

            return setStringColor(_color, ChessGameBuilder.leaveString);
        }
        catch (Exception ex) {
            return getErrorStringRequest(ex.getMessage(), "leave");
        }
    }


    private String confirm(String[] params) {
        if (!resign) {
            return setStringColor(_color, defaultString);
        }
        else {
            try {
                context.webSocketFacade.resign();
                return "";
            } catch (Exception ex) {
                return getErrorStringRequest(ex.getMessage(), "confirm");
            }
        }
    }


    private String cancel(String[] params) {
        if (!resign) {
            return setStringColor(_color, defaultString);
        }

        resign = false;
        return setStringColor(_color, cancelString);
    }


    private String syntax(String[] params) {
        return setStringColor(_color, syntaxString);
    }

    private String help(String[] params) {
        return setStringColor(_color, helpString);
    }

    public ChessGame getGame() {
        return game;
    }

    public void setGame(ChessGame newGame) {
        game = newGame;
    }

    public String drawBoard(ChessPiece[][] board) {
        return ChessGameBuilder.printBoard(board, context.gameColor);
    }

    public String highlightBoard(ChessPiece[][] board, HashSet<ChessPosition> moves) {
        return ChessGameBuilder.printBoardWithHighlights(board, context.gameColor, moves);
    }



    @Override
    String defaultCommand(String[] params) {
        return setStringColor(_color, defaultString);
    }


    @Override
    Map<String, Function<String[], String>> getCommandMethods() {
        return _commandMethods;
    }

}
