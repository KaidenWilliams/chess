package chess;

import java.util.ArrayList;
import java.util.Collection;

// Will we need to do double moves and en passant? We shall see
// THis is horrible code, I don't really care though
// Will figure this out later

//1. Can always move {1, 0} unless enemy is blocking or OFB
//2. If OldPosition is {2, x}, can either move {1, 0} or {2, 0}
//3. If enemy is {1, -1} or {1, 1} from position, can move those ways
//4. If position is {8, x}, needs a promote piece

//5. En passant (probably will be in game state like castling). If enemy pawn on either side {same, 1}
//   or {same, -1} last pieceMove was {2, 0}, can move to {enemy, enemy-1 (behind enemy)

public class PawnMovesCalculator implements PieceMovesCalculator{
    ArrayList<ChessMove> possibleMoves = new ArrayList<>();
//    @Override
//    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition oldPosition, ChessGame.TeamColor pieceColor) {
//
//        int currRow = oldPosition.getRow();
//        int currCol = oldPosition.getColumn();
//
//        //1. Can always move {1, 0} unless enemy is blocking or OFB
//
//        int moveRow = currRow + 1;
//        int moveCol = currCol;
//        ChessPosition newPosition = new ChessPosition(moveRow, moveCol);
//
//        if (isValidNoCheck(board, oldPosition, newPosition, pieceColor)){
//            ChessMove currMove = new ChessMove(oldPosition, newPosition, null);
//            possibleMoves.add(currMove);
//        }
//
//        //2. If OldPosition is {2, x}, can either move {1, 0} or {2, 0}
//
//        if (currRow == 2) {
//            moveRow = currRow + 2;
//            moveCol = currCol;
//            newPosition = new ChessPosition(moveRow, moveCol);
//
//            if (isValidNoCheck(board, oldPosition, newPosition, pieceColor)){
//                ChessMove currMove = new ChessMove(oldPosition, newPosition, null);
//                possibleMoves.add(currMove);
//            }
//        }
//
//        //3. If enemy is {1, -1} or {1, 1} from position, can move those ways
//        moveRow = currRow + 1;
//        moveCol = currCol - 1;
//        newPosition = new ChessPosition(moveRow, moveCol);
//
//        ChessPiece otherPiece = board.getPiece(newPosition);
//        if (board.inBounds(newPosition)) {
//            if (otherPiece != null) {
//                if (otherPiece.getTeamColor() != pieceColor) {
//                    ChessMove currMove = new ChessMove(oldPosition, newPosition, null);
//                    possibleMoves.add(currMove);
//                }
//            }
//        }
//
//
//        moveRow = currRow + 1;
//        moveCol = currCol + 1;
//
//        newPosition = new ChessPosition(moveRow, moveCol);
//
//        otherPiece = board.getPiece(newPosition);
//        if (board.inBounds(newPosition)) {
//            if (otherPiece != null) {
//                if (otherPiece.getTeamColor() != pieceColor) {
//                    ChessMove currMove = new ChessMove(oldPosition, newPosition, null);
//                    possibleMoves.add(currMove);
//                }
//            }
//        }
//
//        //4. If position is {8, x}, needs a promote piece
//
//
//        return possibleMoves;
//    }
//
//
//    private boolean isValidNoCheck(ChessBoard board, ChessPosition oldPosition, ChessPosition newPosition, ChessGame.TeamColor pieceColor) {
//        // Checks if in Bounds
//        if (board.inBounds(newPosition)) {
//
//            ChessPiece otherPiece = board.getPiece(newPosition);
//
//            // If no piece is there, add
//            if (otherPiece == null) {
//                return true;
//            }
//            //If piece is friendly, can't move. If no, list as move then can't move.
//            else {
//                if (otherPiece.getTeamColor() == pieceColor) {
//                    return false;
//                }
//            }
//        }
//        return false;
//    }





        @Override
        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition oldPosition, ChessGame.TeamColor pieceColor) {
            int currRow = oldPosition.getRow();
            int currCol = oldPosition.getColumn();
            int direction = (pieceColor == ChessGame.TeamColor.WHITE) ? 1 : -1;

            addMove(board, oldPosition, currRow + direction, currCol, pieceColor);

            if (currRow == (pieceColor == ChessGame.TeamColor.WHITE ? 2 : 7)) {
                addMove(board, oldPosition, currRow + 2 * direction, currCol, pieceColor);
            }

            addMoveEnemy(board, oldPosition, currRow + direction, currCol - 1, pieceColor);
            addMoveEnemy(board, oldPosition, currRow + direction, currCol + 1, pieceColor);

            // Add logic for position {8, x}, needing a promote piece if necessary

            return possibleMoves;
        }

        private void addMove(ChessBoard board, ChessPosition oldPosition, int moveRow, int moveCol, ChessGame.TeamColor pieceColor) {
            ChessPosition newPosition = new ChessPosition(moveRow, moveCol);

            if (board.inBounds(newPosition)) {
                ChessPiece otherPiece = board.getPiece(newPosition);

                if (otherPiece == null || otherPiece.getTeamColor() != pieceColor) {
                    ChessMove currMove = new ChessMove(oldPosition, newPosition, null);
                    possibleMoves.add(currMove);
                }
            }
        }

        private void addMoveEnemy(ChessBoard board, ChessPosition oldPosition, int moveRow, int moveCol, ChessGame.TeamColor pieceColor) {
            ChessPosition newPosition = new ChessPosition(moveRow, moveCol);

            if (board.inBounds(newPosition)) {
                ChessPiece otherPiece = board.getPiece(newPosition);

                if (otherPiece != null && otherPiece.getTeamColor() != pieceColor) {
                    addMove(board, oldPosition, moveRow, moveCol, pieceColor);
                }
            }
        }
    }