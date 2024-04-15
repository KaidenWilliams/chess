package model.customSerializers;
import chess.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonRegistrar {
    private static final Gson chessGameGSON = new GsonBuilder()
            .registerTypeAdapter(ChessPosition.class, new ChessPositionSerializer())
            .registerTypeAdapter(ChessPiece.class, new ChessPieceSerializer())
            .registerTypeAdapter(ChessMove.class, new ChessMoveSerializer())
            .registerTypeAdapter(ChessBoard.class, new ChessBoardSerializer())
            .registerTypeAdapter(ChessGame.class, new ChessGameSerializer())
            .create();

    public static Gson getChessGameGson() {
        return chessGameGSON;
    }
}
