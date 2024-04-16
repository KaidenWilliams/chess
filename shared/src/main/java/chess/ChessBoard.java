package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] squares;
    public ChessPosition whiteKingPosition;
    public ChessPosition blackKingPosition;
    public ChessBoard() {
        squares = new ChessPiece[8][8];
        whiteKingPosition = new ChessPosition(1,5);
        blackKingPosition = new ChessPosition(8,5);
    }

    // I don't want to use clone
    public ChessBoard(ChessBoard copyBoard) {
        this.whiteKingPosition = new ChessPosition(copyBoard.whiteKingPosition.getRow(), copyBoard.whiteKingPosition.getColumn());
        this.blackKingPosition = new ChessPosition(copyBoard.blackKingPosition.getRow(), copyBoard.blackKingPosition.getColumn());
        this.squares = new ChessPiece[8][8];
        for (int i = 0; i < copyBoard.squares.length; i++) {
            for (int j = 0; j < copyBoard.squares[i].length; j++) {
                if (copyBoard.squares[i][j] != null) {
                    this.squares[i][j] = new ChessPiece(copyBoard.squares[i][j]);
                }
            }
        }
    }


    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getArrayRow()][position.getArrayColumn()] = piece;
    }
    public void removePiece(ChessPosition position) {
        squares[position.getArrayRow()][position.getArrayColumn()] = null;
    }

    public void addPieceIndexes(int row, int column, ChessPiece piece) {
        squares[row][column] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getArrayRow()][position.getArrayColumn()];
    }

    public ChessPiece[][] getSquares() {
        return squares;
    }


    //Difference between array (0 indexing) and board (1 indexing)
    public boolean inBounds(ChessPosition position) {
        int x = position.getArrayRow();
        int y = position.getArrayColumn();

        return x >= 0 && x < squares.length && y >= 0 && y < squares[0].length;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */

    // Make new chessboard, then put pieces down in normal order
    public void resetBoard() {
        this.squares = new ChessPiece[8][8];

        initializePieces(0, ChessGame.TeamColor.WHITE);
        initializePawns(1, ChessGame.TeamColor.WHITE);
        initializePawns(6, ChessGame.TeamColor.BLACK);
        initializePieces(7, ChessGame.TeamColor.BLACK);
    }

    private void initializePieces(int row, ChessGame.TeamColor teamColor) {
        squares[row][0] = new ChessPiece(teamColor, ChessPiece.PieceType.ROOK);
        squares[row][1] = new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT);
        squares[row][2] = new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP);
        squares[row][3] = new ChessPiece(teamColor, ChessPiece.PieceType.QUEEN);
        squares[row][4] = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        squares[row][5] = new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP);
        squares[row][6] = new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT);
        squares[row][7] = new ChessPiece(teamColor, ChessPiece.PieceType.ROOK);
    }

    private void initializePawns(int row, ChessGame.TeamColor teamColor) {
        for (int j = 0; j < squares[row].length; j++) {
            squares[row][j] = new ChessPiece(teamColor, ChessPiece.PieceType.PAWN);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);

    }


    @Override
    public String toString() {
        return "ChessBoard{" +
                "squares=" + Arrays.deepToString(squares) +
                '}';
    }
}
