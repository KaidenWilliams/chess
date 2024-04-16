package server.websocket;

import chess.ChessPosition;

import java.util.HashMap;
import java.util.Map;

public class ChessMoveBuilder {

    public static final Map<Integer, Character> colDict = new HashMap<>() {{
        put(1, 'a');
        put(2, 'b');
        put(3, 'c');
        put(4, 'd');
        put(5, 'e');
        put(6, 'f');
        put(7, 'g');
        put(8, 'h');
    }};

    public static final Map<Integer, Character> rowDict = new HashMap<>() {{
        put(1, '1');
        put(2, '2');
        put(3, '3');
        put(4, '4');
        put(5, '5');
        put(6, '6');
        put(7, '7');
        put(8, '8');
    }};


    public static String getPieceTypeString(chess.ChessPiece.PieceType pieceType) {
        return switch (pieceType) {
            case KING -> "King";
            case QUEEN -> "Queen";
            case BISHOP -> "Bishop";
            case KNIGHT -> "Knight";
            case ROOK -> "Rook";
            case PAWN -> "Pawn";
        };
    }

    public static String getPositionString(ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();

        // Map the row and column to the correct notation
        char colChar = colDict.get(col);
        char rowChar = rowDict.get(row);

        return String.valueOf(colChar) + rowChar;

    }
}
