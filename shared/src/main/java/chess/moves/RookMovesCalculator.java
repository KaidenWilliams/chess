package chess.moves;

import chess.*;

import java.util.Collection;

// I don't know when I will do Castling, kinda worried about it

public class RookMovesCalculator implements PieceMovesCalculator {
    int[][] rookMoves = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition oldPosition, ChessGame.TeamColor pieceColor) {
        return SharedCalculatorMethods.pieceMoves(board, oldPosition, pieceColor, rookMoves);
    }
}