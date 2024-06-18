package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static chess.ChessPiece.PieceType.*;

public class ChessGameBuilder {
    private static final String[] COLUMNS = {"a", "b", "c", "d", "e", "f", "g", "h"};
    private static final String[] ROWS = {"1", "2", "3", "4", "5", "6", "7", "8"};


    public static String printBoard(ChessPiece[][] squares, String color) {
        boolean isWhiteView = color == null || color.equalsIgnoreCase("white");
        return printBoardString(squares, isWhiteView);
    }

    private static String printBoardString(ChessPiece[][] squares, boolean isWhiteView) {
        StringBuilder boardString = new StringBuilder(EscapeSequences.ERASE_SCREEN);
        printColumnLabels(boardString, isWhiteView);
        boardString.append("\n");

        for (int row = 0; row < 8; row++) {
            int rowIndex = isWhiteView ? 7 - row : row;
            printRowLabelRight(boardString, ROWS[rowIndex]);

            for (int col = 0; col < 8; col++) {
                int columnIndex = isWhiteView ? col : 7 - col;
                String squareColor = getSquareColor(row, columnIndex);
                String piece = getPieceString(squares[rowIndex][columnIndex]);
                boardString.append(squareColor)
                        .append(piece)
                        .append(EscapeSequences.RESET_COLOR);
            }

            printRowLabelLeft(boardString, ROWS[rowIndex]);
            boardString.append("\n");
        }

        printColumnLabels(boardString, isWhiteView);
        boardString.append("\n");

        return boardString.toString();
    }


    public static String printBoardWithHighlights(ChessPiece[][] squares, String color, HashSet<ChessPosition> legalMoves) {
        boolean isWhiteView = color == null || color.equalsIgnoreCase("white");

        StringBuilder boardString = new StringBuilder(EscapeSequences.ERASE_SCREEN);
        printColumnLabels(boardString, isWhiteView);
        boardString.append("\n");

        for (int row = 0; row < 8; row++) {
            int rowIndex = isWhiteView ? 7 - row : row;
            printRowLabelRight(boardString, ROWS[rowIndex]);

            for (int col = 0; col < 8; col++) {
                int columnIndex = isWhiteView ? col : 7 - col;
                String squareColor = getSquareColor(row, columnIndex);

                // Check if the current square is a legal move
                if (legalMoves.contains(new ChessPosition(rowIndex + 1, columnIndex + 1))) {
                    squareColor = EscapeSequences.HIGHLIGHT_SQUARE_COLOR;
                }

                String piece = getPieceString(squares[rowIndex][columnIndex]);
                boardString.append(squareColor)
                        .append(piece)
                        .append(EscapeSequences.RESET_COLOR);
            }

            printRowLabelLeft(boardString, ROWS[rowIndex]);
            boardString.append("\n");
        }

        printColumnLabels(boardString, isWhiteView);
        boardString.append("\n");

        return boardString.toString();
    }



    private static String getPieceString(ChessPiece piece) {
        if (piece == null) {
            return EscapeSequences.EMPTY;
        }

        return switch (piece.getPieceType()) {
            case KING -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_KING : EscapeSequences.BLACK_KING;
            case QUEEN -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_QUEEN : EscapeSequences.BLACK_QUEEN;
            case BISHOP -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_BISHOP : EscapeSequences.BLACK_BISHOP;
            case KNIGHT -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_KNIGHT : EscapeSequences.BLACK_KNIGHT;
            case ROOK -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_ROOK : EscapeSequences.BLACK_ROOK;
            case PAWN -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_PAWN : EscapeSequences.BLACK_PAWN;
        };
    }

    private static void printColumnLabels(StringBuilder boardString, boolean isWhiteView) {
        boardString.append(EscapeSequences.ROWLABELPADDING).append(EscapeSequences.ROWLABELSPACING);
        for (int i = 0; i < COLUMNS.length; i++) {
            int columnIndex = isWhiteView ? i : COLUMNS.length - 1 - i;
            boardString.append(EscapeSequences.SET_TEXT_COLOR_WHITE)
                    .append(COLUMNS[columnIndex])
                    .append(EscapeSequences.ROWLABELSPACING)
                    .append(EscapeSequences.RESET_TEXT_COLOR);
        }
        boardString.append(EscapeSequences.ROWLABELSPACING);
    }

    private static void printRowLabelRight(StringBuilder boardString, String rowLabel) {
        boardString.append(EscapeSequences.SET_TEXT_COLOR_WHITE)
                .append(rowLabel)
                .append(" ");
    }

    private static void printRowLabelLeft(StringBuilder boardString, String rowLabel) {
        boardString.append(EscapeSequences.SET_TEXT_COLOR_WHITE)
                .append(" ")
                .append(rowLabel);
    }

    private static String getSquareColor(int row, int col) {
        boolean isLightSquare = (row + col) % 2 == 0;
        return isLightSquare
                ? EscapeSequences.LIGHT_SQUARE_COLOR
                : EscapeSequences.DARK_SQUARE_COLOR;
    }



    public static final Map<Character, Integer> colDict = new HashMap<>() {{
        put('a', 1);
        put('b', 2);
        put('c', 3);
        put('d', 4);
        put('e', 5);
        put('f', 6);
        put('g', 7);
        put('h', 8);
    }};

    public static final Map<Character, Integer> rowDict = new HashMap<>() {{
        put('1', 1);
        put('2', 2);
        put('3', 3);
        put('4', 4);
        put('5', 5);
        put('6', 6);
        put('7', 7);
        put('8', 8);
    }};

    public static final Map<Character, ChessPiece.PieceType> pieceDict = new HashMap<>() {{
        put('q', QUEEN);
        put('r', ROOK);
        put('k', KNIGHT);
        put('b', BISHOP);
    }};



    public static final String leaveString =
            """
            Leaving the Chess Game.
            You are in the main menu.
            """;


    public static final String resignString =

            """
            Are you sure you want to resign? You will forfeit the game.
            Type "confirm" to confirm the resign, or "cancel" to cancel the resign
            """;

    public static final String observerResignString =

            """
            You cannot resign. You are observing the game. If you wish to stop observing, type "leave"
            """;

    public static final String cancelString =

            """
            You have successfully cancelled your resignation
            """;


    public static final String moveFailString =

            """
            You cannot make a move for this piece, as it is not your turn.
            """;

    public static final String highlightFailString =

            """
            You cannot highlight moves for this piece, as it is not its color's turn.
            """;

    public static final String gameOverString =

            """
            The game is over. No other actions can be performed.
            """;


    public static final String helpString =
            """
            Commands:
            < move <COL_START><ROW_START>-<COL_END><ROW_END> -- to move a piece. See "syntax" for info.
            < resign -- to forfeit the chess game. You will be asked to confirm or cancel.
            < redraw -- to draw the board again
            < highlight <COL><ROW> -- to highlight a piece on the board who's turn it is to move.
            < leave -- to leave the chess game window
            < syntax -- to get more information about the chess move syntax
            < help -- to get a list of possible commands
            """;

    public static final String syntaxString =

            """
            Move Syntax:
            -Provide the starting column and row, followed by a dash "-", then the destination column and row.
            -For pawn promotion, use "=" after the move, followed by the promotion piece.
            -Only move pieces on your turn, and make sure it is a legal chess move
                        
            -Regular Move:
                <COL_START><ROW_START>-<COL_END><ROW_END>
                ex. "e2-e4"
            -Promotion Move:
                <COL_START><ROW_START>-<COL_END><ROW_END>=<PROMOTION_PIECE>
                ex. "f7-f8=q"
            -Col Options
                "a", "b", "c", "d", "e", "f", "g", "h"
            -Row Options
                "1", "2", "3", "4", "5", "6", "7", "8"
            -Promotion_Piece options
                "q" (Queen), "r" (Rook), "b" (Bishop), "k" (Knight)
            """;

    public static final String defaultString =
            """
            Your input was invalid. The following valid commands are shown below
            
            Commands:
            < move <COL_START><ROW_START>-<COL_END><ROW_END> -- to move a piece. See "syntax" for info.
            < resign -- to forfeit the chess game. You will be asked to confirm or cancel.
            < redraw -- to draw the board again
            < highlight <COL><ROW> -- to highlight a piece on the board who's turn it is to move.
            < leave -- to leave the chess game window
            < syntax -- to get more information about the chess move syntax
            < help -- to get a list of possible commands
            """;
}