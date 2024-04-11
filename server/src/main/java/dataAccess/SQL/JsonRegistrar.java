package dataAccess.SQL;
import chess.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.customSerializers.ChessBoardSerializer;
import model.customSerializers.ChessGameSerializer;
import model.customSerializers.ChessPieceSerializer;

public class JsonRegistrar {
    private static final Gson chessGameGSON = new GsonBuilder()
            .registerTypeAdapter(ChessPiece.class, new ChessPieceSerializer())
            .registerTypeAdapter(ChessBoard.class, new ChessBoardSerializer())
            .registerTypeAdapter(ChessGame.class, new ChessGameSerializer())
            .create();

    public static Gson getChessGameGson() {
        return chessGameGSON;
    }
}
