package model.customSerializers;
import chess.*;


import com.google.gson.*;
import java.lang.reflect.Type;

public class ChessPieceSerializer implements JsonSerializer<ChessPiece>, JsonDeserializer<ChessPiece> {
    @Override
    public JsonElement serialize(ChessPiece src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.addProperty("pieceColor", src.pieceColor.toString());
        result.addProperty("type", src.type.toString());
        return result;
    }

    @Override
    public ChessPiece deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String pieceColorStr = jsonObject.get("pieceColor").getAsString();
        String typeStr = jsonObject.get("type").getAsString();
        ChessGame.TeamColor pieceColor = ChessGame.TeamColor.valueOf(pieceColorStr);
        ChessPiece.PieceType type = ChessPiece.PieceType.valueOf(typeStr);
        return new ChessPiece(pieceColor, type);
    }
}
