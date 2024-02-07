package chess;

import java.util.HashSet;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    // TODO - Implement data structure to store moves

    private ChessBoard myBoard;
    private TeamColor teamTurn;


    public ChessGame() {
        teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Changes teamTurn from one to another, uses current teamTurn
     */
    public void changeTeamTurn() {
        if (teamTurn == TeamColor.WHITE) {
            teamTurn = TeamColor.BLACK;
        } else {
            teamTurn = TeamColor.WHITE;
        }
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */

    // - for some reason blocking doesn't work, idk why
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = myBoard.getPiece(startPosition);
        Collection<ChessMove> possibleMoves = piece.pieceMoves(myBoard, startPosition);
        HashSet<ChessMove> validMoves = new HashSet<>();
        ChessBoard newBoard = new ChessBoard(myBoard);

        Collection<ChessPosition> endPositionList;

        for (ChessMove move: possibleMoves) {

            // Edge case for capturing and bringing out of check, need to implement check for that
            ChessPiece endPiece = newBoard.getPiece(move.getEndPosition());
            if (newBoard.getPiece(move.getEndPosition()) != null) {
                newBoard.removePiece(move.getStartPosition());
                newBoard.addPiece(move.getEndPosition(), piece);
                endPositionList = getEnemyPositions(piece.getTeamColor(), newBoard);

                if (!isInCheckTakesBoard(piece.getTeamColor(), endPositionList, newBoard)) {
                    validMoves.add(move);
                }

                newBoard.addPiece(move.getEndPosition(), endPiece);
                newBoard.addPiece(move.getStartPosition(), piece);
            }

            else {
                newBoard.removePiece(move.getStartPosition());
                newBoard.addPiece(move.getEndPosition(), piece);
                endPositionList = getEnemyPositions(piece.getTeamColor(), newBoard);

                if (!isInCheckTakesBoard(piece.getTeamColor(), endPositionList, newBoard)) {
                    validMoves.add(move);
                }

                newBoard.removePiece(move.getEndPosition());
                newBoard.addPiece(move.getStartPosition(), piece);
            }
        }
        return validMoves;
    }



//    public Collection<ChessMove> validMovesTakesMove(ChessMove move) {
//        ChessPiece piece = myBoard.getPiece(move.getStartPosition());
//        Collection<ChessMove> possibleMoves = new HashSet<>();
//        possibleMoves.add(move);
//        ChessBoard newBoard = new ChessBoard(myBoard);
//        Collection<ChessPosition> endPositionList = getEnemyPositions(piece.getTeamColor(), newBoard);
//
//        newBoard.addPiece(move.getEndPosition(), piece);
//        if (isInCheckTakesBoard(piece.getTeamColor(), endPositionList)) {
//            possibleMoves.remove(move);
//        }
//        newBoard.removePiece(move.getEndPosition());
//
//        return possibleMoves;
//    }



    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform\
     * @throws InvalidMoveException if move is invalid
     */


    // Don't know if I just try to make move and do try catch, or if I actually check if valid
    // Don't think I need to do my own valid checking, we will see
    // If I need to add own checking I will add those functions, potential to move
    // This code is absolutely not readable, definetly need to define local variables at top
    public void makeMove(ChessMove move) throws InvalidMoveException {

        try {
            ChessPosition startPosition = move.getStartPosition();
            ChessPosition endPosition = move.getEndPosition();
            ChessPiece piece = myBoard.getPiece(startPosition);
            assert (piece != null);
            TeamColor color = piece.getTeamColor();
            assert(getTeamTurn() == color);

            assert(validMoves(startPosition).contains(move));

            myBoard.removePiece(startPosition);
            if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                ChessTeamTracker tracker = (color == TeamColor.WHITE ? myBoard.getWhiteTeamTracker() : myBoard.getBlackTeamTracker());
                tracker.setKingPosition(endPosition);
            }
            if (piece.getPieceType() == ChessPiece.PieceType.PAWN && move.getPromotionPiece() != null) {
                myBoard.addPiece(endPosition, new ChessPiece(color, move.getPromotionPiece()));
            }
            else {
                myBoard.addPiece(endPosition, piece);
            }
            changeTeamTurn();
        }
        catch(AssertionError ae) {
            throw new InvalidMoveException("Could not make move. Error: " +  ae.getMessage());
        }
        catch(Exception e) {
            throw new InvalidMoveException("Could not make move. Error: " +  e.getMessage());
        }

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */

    //
    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessPosition> endPositionList = getEnemyPositions(teamColor, myBoard);
        return isInCheckTakesBoard(teamColor, endPositionList, myBoard);
    }

    // Used for testing valid moves, takes copy board so tested moves aren't applied to actual board
    // Just like isInCheck, just need to replace myBoard with newBoard
    // Need to call boards get king method
    public boolean isInCheckTakesBoard(TeamColor teamColor, Collection<ChessPosition> endPositionList, ChessBoard newBoard) {

        return endPositionList.contains(getKingPosition(teamColor, newBoard));
    }

    public Collection<ChessPosition> getEnemyPositions(TeamColor teamColor, ChessBoard newBoard) {
        ChessPiece[][] newSquares = newBoard.getSquares();
        HashSet<ChessPosition> endPositionList = new HashSet<>();

        for (int i = 0; i < newSquares.length; i++) {
            for (int j = 0; j < newSquares[i].length; j++) {
                ChessPosition currPosition = new ChessPosition(i + 1, j + 1);
                ChessPiece currPiece = newBoard.getPiece(currPosition);
                if (currPiece == null) {
                    continue;
                }
                if (currPiece.getTeamColor() != teamColor) {

                    // Edge case with pawns, where they can attack is different
                    int direction = (currPiece.getTeamColor() == ChessGame.TeamColor.WHITE) ? 1 : -1;
                    if (currPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
                        endPositionList.add(new ChessPosition(currPosition.getRow() + direction, currPosition.getColumn()-1));
                        endPositionList.add(new ChessPosition(currPosition.getRow() + direction, currPosition.getColumn()+1));
                    }

                    else {
                        endPositionList.addAll(currPiece.pieceMoves(newBoard, currPosition).stream()
                                .map(ChessMove::getEndPosition)
                                .collect(Collectors.toCollection(HashSet::new)));
                    }
                }
            }
        }
        return endPositionList;
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (getTeamTurn() != teamColor) {
            return false;
        }
        if (!isInCheck(teamColor)) {
            return false;
        }
        if (!validMoves(getKingPosition(teamColor, myBoard)).isEmpty()){
            return false;
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     * Different from Checkmate in that isInCheck is false
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (getTeamTurn() != teamColor) {
            return false;
        }
        if (isInCheck(teamColor)) {
            return false;
        }
        if (!validMoves(getKingPosition(teamColor, myBoard)).isEmpty()){
            return false;
        }
        return true;
    }

    public ChessPosition getKingPosition(ChessGame.TeamColor teamColor, ChessBoard board) {
        ChessTeamTracker tracker = (teamColor == TeamColor.WHITE ? board.getWhiteTeamTracker() : board.getBlackTeamTracker());
        ChessPosition kingPosition = tracker.getKingPosition();
        ChessPiece kingPiece = board.getPiece(kingPosition);
        if (kingPiece != null && kingPiece.getPieceType() == ChessPiece.PieceType.KING && kingPiece.getTeamColor() == teamColor) {
            return kingPosition;
        }

        ChessPiece[][] squares = board.getSquares();

        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[i].length; j++) {
                ChessPosition currPosition = new ChessPosition(i + 1, j + 1);
                ChessPiece currPiece = board.getPiece(currPosition);
                if (currPiece == null) {
                    continue;
                }
                if (currPiece.getPieceType() == ChessPiece.PieceType.KING && currPiece.getTeamColor() == teamColor) {
                    return currPosition;
                }
            }
        }
        return null;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        myBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return myBoard;
    }

}
