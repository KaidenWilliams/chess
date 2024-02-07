package chess;

import java.util.HashSet;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator{
    int[][] bishopMoves = {{1, 1}, {-1, 1}, {-1, -1}, {1, -1}};
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition oldPosition, ChessGame.TeamColor pieceColor) {

        HashSet<ChessMove> possibleMoves = new HashSet<>();

        int currRow = oldPosition.getRow();
        int currCol = oldPosition.getColumn();

        for (int[] move: bishopMoves) {

            int moveRow = currRow + move[0];
            int moveCol = currCol + move[1];

            if (moveRow == 5) {
                System.out.println("ready");
            }

            ChessPosition newPosition = new ChessPosition(moveRow, moveCol);

            while (isValidNoCheck(board, oldPosition, newPosition, pieceColor, possibleMoves)) {
                ChessMove currMove = new ChessMove(oldPosition, newPosition, null);
                possibleMoves.add(currMove);

                moveRow = moveRow + move[0];
                moveCol = moveCol + move[1];
                newPosition = new ChessPosition(moveRow, moveCol);
            }
        }

        return possibleMoves;
    }


    private boolean isValidNoCheck(ChessBoard board, ChessPosition oldPosition, ChessPosition newPosition, ChessGame.TeamColor pieceColor, Collection<ChessMove> possibleMoves) {
        // Checks if in Bounds
        if (board.inBounds(newPosition)) {

            ChessPiece otherPiece = board.getPiece(newPosition);

            // If no piece is there, add
            if (otherPiece == null) {
                return true;
            }
            //If piece is friendly, can't move. If no, list as move then can't move.
            else {
                if (otherPiece.getTeamColor() == pieceColor) {
                    return false;
                }
                else {
                    ChessMove currMove = new ChessMove(oldPosition, newPosition, null);
                    possibleMoves.add(currMove);
                    return false;
                }
            }
        }
        return false;
    }
}
