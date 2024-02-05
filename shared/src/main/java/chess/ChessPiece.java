package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {


    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;
    private final PieceMovesCalculator movesCalculator;


    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;

        switch(type) {
            case KING -> movesCalculator = new KingMovesCalculator();
            case QUEEN -> movesCalculator = new QueenMovesCalculator();
            case BISHOP -> movesCalculator = new BishopMovesCalculator();
            case KNIGHT -> movesCalculator = new KnightMovesCalculator();
            case ROOK -> movesCalculator = new RookMovesCalculator();
            case PAWN -> movesCalculator = new PawnMovesCalculator();
            default -> movesCalculator = null;
        }
    }

    public ChessPiece(ChessPiece copyPiece) {
        this.pieceColor = copyPiece.pieceColor;
        this.type = copyPiece.type;
        this.movesCalculator = copyPiece.movesCalculator;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // Have to check if opponent is there and not out of bounds, maybe make 2 functions?
        // I will return array list of ChessMoves
        // Does order matter? We will see.
        return movesCalculator.pieceMoves(board, myPosition, pieceColor);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }
    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }
}