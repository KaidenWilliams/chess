package model.customSerializers;
import chess.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonRegistrar {
    private static final Gson chessGameGSON = new GsonBuilder()
            .registerTypeAdapter(ChessPiece.class, new ChessPieceSerializer())
            .registerTypeAdapter(ChessBoard.class, new ChessBoardSerializer())
            .registerTypeAdapter(ChessGame.class, new ChessGameSerializer())
            .registerTypeAdapter(ChessPosition.class, new ChessPositionSerializer())
            .create();

    public static Gson getChessGameGson() {
        return chessGameGSON;
    }
}
