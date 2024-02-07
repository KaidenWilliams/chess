package chess;

import java.util.HashSet;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator {
    int[][] kingMoves = {{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}};

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition oldPosition, ChessGame.TeamColor pieceColor) {
        //Stops if friend is there or out of bounds
        //How do I do capturing

        //Do I just store all possible moves and apply them in order, or do loop
        // - problems with loop if I need specific order, dumb test cases bc of that

        HashSet<ChessMove> possibleMoves = new HashSet<>();

        int currRow = oldPosition.getRow();
        int currCol = oldPosition.getColumn();

        for (int[] move : kingMoves) {

            int moveRow = currRow + move[0];
            int moveCol = currCol + move[1];

            ChessPosition newPosition = new ChessPosition(moveRow, moveCol);

            if (isValidNoCheck(board, newPosition, pieceColor)) {
                ChessMove currMove = new ChessMove(oldPosition, newPosition, null);
                possibleMoves.add(currMove);
            }
        }

        return possibleMoves;
    }


    private boolean isValidNoCheck(ChessBoard board, ChessPosition newPosition, ChessGame.TeamColor pieceColor) {
        // Checks if in Bounds
        if (board.inBounds(newPosition)) {

            ChessPiece otherPiece = board.getPiece(newPosition);

            // If no piece is there, add
            if (otherPiece == null) {
                return true;
            }
            //If piece is friendly, can't move. Don't need queen unlimited move logic.
            else {
                if (otherPiece.getTeamColor() != pieceColor) {
                    return true;
                }
            }
        }
        return false;
    }
}