package chess.moves;

import chess.*;

import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {
    int[][] bishopMoves = {{1, 1}, {-1, 1}, {-1, -1}, {1, -1}};

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition oldPosition, ChessGame.TeamColor pieceColor) {

        return SharedCalculatorMethods.pieceMoves(board, oldPosition, pieceColor, bishopMoves);
    }
}