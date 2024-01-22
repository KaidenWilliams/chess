package chess;

import java.util.ArrayList;
import java.util.Collection;

// Will we need to do double moves and en passant? We shall see

//1. Can always move {1, 0} unless enemy is blocking or OFB
//2. If OldPosition is {2, x}, can either move {1, 0} or {2, 0}
//3. If enemy is {1, -1} or {1, 1} from position, can move those ways
//4. If position is {8, x}, needs a promote piece

//5. En passant (probably will be in game state like castling). If enemy pawn on either side {same, 1}
//   or {same, -1} last pieceMove was {2, 0}, can move to {enemy, enemy-1 (behind enemy)

public class PawnMovesCalculator implements PieceMovesCalculator{
    ArrayList<ChessMove> possibleMoves = new ArrayList<>();
    int[][] pawnMoves = {{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}};
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition oldPosition, ChessGame.TeamColor pieceColor) {

        int currRow = oldPosition.getRow();
        int currCol = oldPosition.getColumn();

        //1. Can always move {1, 0} unless enemy is blocking or OFB

        int moveRow = currRow + 1;
        int moveCol = currCol;

        ChessPosition newPosition = new ChessPosition(moveRow, moveCol);

        if (isValidNoCheck(board, oldPosition, newPosition, pieceColor)){
            ChessMove currMove = new ChessMove(oldPosition, newPosition, null);
            possibleMoves.add(currMove);
        }

        //2. If OldPosition is {2, x}, can either move {1, 0} or {2, 0}
        //3. If enemy is {1, -1} or {1, 1} from position, can move those ways
        //4. If position is {8, x}, needs a promote piece


        return possibleMoves;
    }


    private boolean isValidNoCheck(ChessBoard board, ChessPosition oldPosition, ChessPosition newPosition, ChessGame.TeamColor pieceColor) {
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
            }
        }
        return false;
    }
}