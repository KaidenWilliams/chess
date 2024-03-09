package dataAccess.SQL;
import chess.*;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ChessGameSerializer implements JsonSerializer<ChessGame>, JsonDeserializer<ChessGame> {
    @Override
    public JsonElement serialize(ChessGame src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.addProperty("teamTurn", src.getTeamTurn().toString());
        result.add("myBoard", context.serialize(src.getChessBoard(), ChessBoard.class));
        return result;
    }

    @Override
    public ChessGame deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String teamTurnStr = jsonObject.get("teamTurn").getAsString();
        JsonElement boardJson = jsonObject.get("myBoard");

        ChessGame.TeamColor teamTurn = ChessGame.TeamColor.valueOf(teamTurnStr);
        ChessBoard board = context.deserialize(boardJson, ChessBoard.class);

        ChessGame game = new ChessGame();
        game.setChessBoard(board);
        game.setTeamTurn(teamTurn);

        return game;
    }
}