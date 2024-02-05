package chess;

// Lot's of different ways to do this, don't know whic
// This is for king and in check, castling, and en passant
// 1. Should I just set flags/very specific structures for just these 3
// 2. Should I make big data structure with two parts. LL/Stack for every piece and general one
// - Need to be able to work backwards, for every current position figure out what previous positions/moves were
// - Needs to track location and piece (AT this point doesn't this make having a chessboard irrelevant?)
// 3.
public class ChessTeamTracker {

    private boolean canCastleQueenSide;
    private boolean canCastleKingSide;
    private ChessPosition kingPosition;

    public ChessTeamTracker(ChessGame.TeamColor teamColor) {
        if (teamColor == ChessGame.TeamColor.WHITE) {
            kingPosition = new ChessPosition(0,4);
        }
        else {
            kingPosition = new ChessPosition(7,4);
        }
        canCastleQueenSide = true;
        canCastleKingSide = false;

    }

    public boolean isCanCastleKingSide() {
        return canCastleKingSide;
    }
    public void setCanCastleKingSide(boolean canCastleKingSide) {
        this.canCastleKingSide = canCastleKingSide;
    }
    public boolean isCanCastleQueenSide() {
        return canCastleQueenSide;
    }
    public void setCanCastleQueenSide(boolean canCastleKingSide) {
        this.canCastleQueenSide = canCastleQueenSide;
    }

    public ChessPosition getKingPosition() {
        return kingPosition;
    }
    public void setKingPosition(ChessPosition newKingPosition) {
        this.kingPosition = newKingPosition;
    }

}
