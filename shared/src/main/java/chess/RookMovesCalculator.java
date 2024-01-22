package chess;

import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition oldPosition, ChessGame.TeamColor pieceColor) {
        return null;
    }

    private boolean isValidNoCheck(ChessBoard board, ChessPosition oldPosition, ChessPosition newPosition, ChessGame.TeamColor pieceColor) {
        return false;
    }
}
