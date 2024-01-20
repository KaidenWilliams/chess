package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        //Starts one up from king, then moves in counterclockwise circle
        //Stops if friend is there or out of bounds
        //How do I do capturing

        //Do I just store all possible moves and apply them in order, or do loop
        // - problems with loop if I need specific order

        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        int row = position.getRow();
        int col = position.getColumn();

        for (int rowMove = 1; rowMove >= -1; rowMove--) {
            for (int colMove = 0;)
        }


    }
}
