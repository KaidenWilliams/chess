package chess.moves;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

//5. En passant (probably will be in game state like castling). If enemy pawn on either side {same, 1}
//   or {same, -1} last pieceMove was {2, 0}, can move to {enemy, enemy-1 (behind enemy)

public class PawnMovesCalculator implements PieceMovesCalculator {
    ChessPiece.PieceType[] promotionPieces = {ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.ROOK, ChessPiece.PieceType.QUEEN};
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition oldPosition, ChessGame.TeamColor pieceColor) {

        HashSet<ChessMove> possibleMoves = new HashSet<>();

        int currRow = oldPosition.getRow();
        int currCol = oldPosition.getColumn();

        int direction = (pieceColor == ChessGame.TeamColor.WHITE) ? 1 : -1;
        int startRow = (pieceColor == ChessGame.TeamColor.WHITE) ? 2 : 7;
        int promoteRow = (pieceColor == ChessGame.TeamColor.WHITE) ? 8 : 1;


        if (addMove(board, oldPosition, currRow + direction, currCol, promoteRow, possibleMoves)) {
            if (currRow == startRow) {
                addMove(board, oldPosition, currRow + 2 * direction, currCol, promoteRow, possibleMoves);
            }
        }

        addMoveEnemy(board, oldPosition, currRow + direction, currCol - 1, pieceColor, promoteRow, possibleMoves);
        addMoveEnemy(board, oldPosition, currRow + direction, currCol + 1, pieceColor, promoteRow, possibleMoves);

        return possibleMoves;
    }

    private boolean addMove(ChessBoard board, ChessPosition oldPosition, int moveRow, int moveCol, int promoteRow, Collection<ChessMove> possibleMoves) {
        ChessPosition newPosition = new ChessPosition(moveRow, moveCol);

        if (board.inBounds(newPosition)) {
            ChessPiece otherPiece = board.getPiece(newPosition);

            if (otherPiece == null) {
                if (moveRow == promoteRow) {
                    addPiece(oldPosition, newPosition, this.promotionPieces, possibleMoves);
                }
                else {
                    addPiece(oldPosition, newPosition, null, possibleMoves);
                }
                return true;
            }
        }
        return false;
    }

    private void addMoveEnemy(ChessBoard board, ChessPosition oldPosition, int moveRow, int moveCol, ChessGame.TeamColor pieceColor, int promoteRow, Collection<ChessMove> possibleMoves) {
        ChessPosition newPosition = new ChessPosition(moveRow, moveCol);

        if (board.inBounds(newPosition)) {
            ChessPiece otherPiece = board.getPiece(newPosition);

            if (otherPiece != null && otherPiece.getTeamColor() != pieceColor) {
                if (moveRow == promoteRow) {
                    addPiece(oldPosition, newPosition, this.promotionPieces, possibleMoves);
                }
                else {
                    addPiece(oldPosition, newPosition, null, possibleMoves);
                }
            }
        }
    }
    private void addPiece(ChessPosition oldPosition, ChessPosition newPosition, ChessPiece.PieceType[] promotionPieces, Collection<ChessMove> possibleMoves) {
        if (promotionPieces == null) {
            ChessMove currMove = new ChessMove(oldPosition, newPosition, null);
            possibleMoves.add(currMove);
        }
        else {
            for (ChessPiece.PieceType promotionPiece : promotionPieces) {
                ChessMove currMove = new ChessMove(oldPosition, newPosition, promotionPiece);
                possibleMoves.add(currMove);
            }
        }
    }
}