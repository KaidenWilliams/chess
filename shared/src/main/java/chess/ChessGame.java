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

    private ChessBoard myBoard;
    private TeamColor teamTurn;
    private boolean gameOver;


    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    public ChessGame() {
        teamTurn = TeamColor.WHITE;
    }


    public ChessBoard getChessBoard() {return myBoard;}

    public void setChessBoard(ChessBoard newBoard) {myBoard = newBoard;}

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
        gameOver = false;
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

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
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

            ChessPosition startPosition = move.getStartPosition();
            ChessPosition endPosition = move.getEndPosition();
            ChessPiece piece = myBoard.getPiece(startPosition);
            if (piece == null) {
                throw new InvalidMoveException("Null Piece");
            }
            TeamColor color = piece.getTeamColor();

            if (getTeamTurn() != color) {
                throw new InvalidMoveException("Wrong Color Turn");
            }

            if (!validMoves(startPosition).contains(move)) {
                throw new InvalidMoveException("Not a valid move");
            }

            myBoard.removePiece(startPosition);
            if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                if (color == TeamColor.WHITE) {
                    myBoard.whiteKingPosition = new ChessPosition(endPosition.getRow(), endPosition.getColumn());
                }
                else {
                    myBoard.blackKingPosition = new ChessPosition(endPosition.getRow(), endPosition.getColumn());
                }
            }
            if (piece.getPieceType() == ChessPiece.PieceType.PAWN && move.getPromotionPiece() != null) {
                myBoard.addPiece(endPosition, new ChessPiece(color, move.getPromotionPiece()));
            }
            else {
                myBoard.addPiece(endPosition, piece);
            }
            changeTeamTurn();

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

        if (areValidMoves(teamColor)) return false;

        return isInCheck(teamColor);
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

        return !areValidMoves(teamColor);
    }


    private boolean areValidMoves(TeamColor teamColor) {
        ChessPiece[][] squares = myBoard.getSquares();
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[i].length; j++) {
                ChessPosition currPosition = new ChessPosition(i + 1, j + 1);
                ChessPiece currPiece = myBoard.getPiece(currPosition);
                if (currPiece != null && currPiece.getTeamColor() == teamColor) {
                    if (!validMoves(currPosition).isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public ChessPosition getKingPosition(ChessGame.TeamColor teamColor, ChessBoard board) {
        ChessPosition kingPosition = (teamColor == TeamColor.WHITE ? board.whiteKingPosition : board.blackKingPosition);
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
