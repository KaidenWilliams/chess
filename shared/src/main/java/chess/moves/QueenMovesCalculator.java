package chess.moves;

import chess.*;

import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator {
    int[][] queenMoves = {{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}};
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition oldPosition, ChessGame.TeamColor pieceColor) {

        return SharedCalculatorMethods.pieceMoves(board, oldPosition, pieceColor, queenMoves);
    }
}