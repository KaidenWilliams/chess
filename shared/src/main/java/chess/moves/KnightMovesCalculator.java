package chess.moves;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovesCalculator implements PieceMovesCalculator {
    int[][] knightMoves = {{2, 1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {-1, -2}, {1, -2}, {2, -1}};

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition oldPosition, ChessGame.TeamColor pieceColor) {

        HashSet<ChessMove> possibleMoves = new HashSet<>();

        int currRow = oldPosition.getRow();
        int currCol = oldPosition.getColumn();

        for (int[] move: knightMoves) {

            int moveRow = currRow + move[0];
            int moveCol = currCol + move[1];

            ChessPosition newPosition = new ChessPosition(moveRow, moveCol);

            if (SharedCalculatorMethods.isValidNoCheck(board, oldPosition, newPosition, pieceColor, possibleMoves)) {
                ChessMove currMove = new ChessMove(oldPosition, newPosition, null);
                possibleMoves.add(currMove);
            }
        }

        return possibleMoves;
    }


}