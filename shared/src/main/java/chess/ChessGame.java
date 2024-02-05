package chess;

import java.util.ArrayList;
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
    private ChessTeamTracker whiteTeamTracker;
    private ChessTeamTracker blackTeamTracker;


    public ChessGame() {
        teamTurn = TeamColor.WHITE;
        whiteTeamTracker = new ChessTeamTracker(TeamColor.WHITE);
        blackTeamTracker = new ChessTeamTracker(TeamColor.BLACK);
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
        ChessBoard newBoard = new ChessBoard(myBoard);

        for (ChessMove move: possibleMoves) {

            newBoard.addPiece(move.getEndPosition(), piece);
            if (isInCheckTakesBoard(piece.getTeamColor(), newBoard)) {
                possibleMoves.remove(move);
            }
            newBoard.removePiece(move.getEndPosition());
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
    // This code is absolutely not readable, definetly need to define local variables at top
    public void makeMove(ChessMove move) throws InvalidMoveException {

        try {
            ChessPosition startPosition = move.getStartPosition();
            ChessPosition endPosition = move.getEndPosition();
            ChessPiece piece = myBoard.getPiece(startPosition);
            assert (piece != null);
            TeamColor color = piece.getTeamColor();
            assert(getTeamTurn() == color);
            assert(piece.pieceMoves(myBoard, startPosition).contains(move));
            myBoard.removePiece(startPosition);
            myBoard.addPiece(endPosition, piece);
            if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                ChessTeamTracker tracker = (color == TeamColor.WHITE ? whiteTeamTracker : blackTeamTracker);
                tracker.setKingPosition(endPosition);
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

    //This will be expensive operation, don't really know how to do
    //
    public boolean isInCheck(TeamColor teamColor) {
        return isInCheckTakesBoard(teamColor, myBoard);
    }

    // Used for testing valid moves, takes copy board so tested moves aren't applied to actual board
    // Just like isInCheck, just need to replace myBoard with newBoard
    public boolean isInCheckTakesBoard(TeamColor teamColor, ChessBoard newBoard) {
        ChessPiece[][] newSquares = newBoard.getSquares();

        for (int i = 0; i < newSquares.length; i++) {
            for (int j = 0; j < newSquares[i].length; j++) {
                ChessPosition currPosition = new ChessPosition(i+1, j+1);
                ChessPiece currPiece = newBoard.getPiece(currPosition);
                if (currPiece == null){
                    continue;
                }
                if (currPiece.getTeamColor() != teamColor) {

                    ArrayList<ChessPosition> endPositionList = currPiece.pieceMoves(newBoard, currPosition).stream()
                            .map(ChessMove::getEndPosition)
                            .collect(Collectors.toCollection(ArrayList::new));

                    if (endPositionList.contains(getKingPosition(teamColor))) {
                        return true;
                    }
                }
            }
        }
        return false;
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
        if (!validMoves(getKingPosition(teamColor)).isEmpty()){
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
        if (!validMoves(getKingPosition(teamColor)).isEmpty()){
            return false;
        }
        return true;
    }

    public ChessPosition getKingPosition(ChessGame.TeamColor teamColor) {
        ChessTeamTracker tracker = (teamColor == TeamColor.WHITE ? whiteTeamTracker : blackTeamTracker);
        return tracker.getKingPosition();
    }

    //TODO EN PASSANT and CASTLING if I have the time
//    public void castling
//
//    public void enPassant()

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
