package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator {
    // List of moves a king can make
//    private final int[][] kingMoves = {{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}};

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor pieceColor) {
        //Stops if friend is there or out of bounds
        //How do I do capturing

        //Do I just store all possible moves and apply them in order, or do loop
        // - problems with loop if I need specific order, dumb test cases bc of that

        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        int[][] kingMoves = {{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}};

        int currRow = position.getRow();
        int currCol = position.getColumn();

        for (int[] move: kingMoves) {

            ChessPosition newPosition = new ChessPosition(currRow + move[0], currCol + move[1]);

            // Checks if in Bounds
            if (board.inBounds(newPosition)) {

                // Checks if friendly piece is there
                ChessPiece otherPiece = board.getPiece(newPosition);

                if (otherPiece == null) {
                    ChessMove currMove = new ChessMove(position, newPosition, null);
                    possibleMoves.add(currMove);
                } else {
                    if (otherPiece.getTeamColor() != pieceColor) {
                        ChessMove currMove = new ChessMove(position, newPosition, null);
                        possibleMoves.add(currMove);
                    }
                }
            }
        }

        return possibleMoves;
    }

}