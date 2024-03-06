package chess.moves;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

class SharedCalculatorMethods {

    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition oldPosition, ChessGame.TeamColor pieceColor, int[][] pieceMoves) {

        HashSet<ChessMove> possibleMoves = new HashSet<>();

        int currRow = oldPosition.getRow();
        int currCol = oldPosition.getColumn();

        for (int[] move: pieceMoves) {

            int moveRow = currRow + move[0];
            int moveCol = currCol + move[1];

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

    static boolean isValidNoCheck(ChessBoard board, ChessPosition oldPosition, ChessPosition newPosition, ChessGame.TeamColor pieceColor, Collection<ChessMove> possibleMoves) {
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
