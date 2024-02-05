package chess;

import java.util.Collection;

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


    // TODO - Maybe make this default teamTurn WHite?
    public ChessGame() {

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
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = myBoard.getPiece(startPosition);
        Collection<ChessMove> possibleMoves = piece.pieceMoves(myBoard, startPosition);

        // Will see if its this easy, probably need to do more than isInCheck()
        // - I need to actually apply the move then check, I'm currently just checking w/o changing anything

        ChessBoard newBoard = new ChessBoard(myBoard);
        for (ChessMove move: possibleMoves) {

            if (isInCheckTakesBoard(piece.getTeamColor(), newBoard)) {
                possibleMoves.remove(move);
            }
        }
        return possibleMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform\
     * @throws InvalidMoveException if move is invalid
     */


    // Don't know if I just try to make move and do try catch, or if I actually check if valid
    // Don't think I need to do my own valid checking, we will see
    // If I need to add own checking I will add those functions, potential to move
    public void makeMove(ChessMove move) throws InvalidMoveException {

        try {
            ChessPiece piece = myBoard.getPiece(move.getStartPosition());
            myBoard.removePiece(move.getStartPosition());
            myBoard.addPiece(move.getEndPosition(), piece);
            changeTeamTurn();
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

    //This will be expensive operation, don't really know how to do
    //
    public boolean isInCheck(TeamColor teamColor) {

    }

    // Used for testing valid moves, takes copy board so tested moves aren't applied to actual board
    // Just like isInCheck, just need to replace myBoard with newBoard
    public boolean isInCheckTakesBoard(TeamColor teamColor, ChessBoard newboard) {

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
        if (validMoves(KING).length != 0){
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
        if (validMoves(KING).length != 0){
            return false;
        }
        return true;
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
