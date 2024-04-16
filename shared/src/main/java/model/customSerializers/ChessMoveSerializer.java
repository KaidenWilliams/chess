package model.customSerializers;
import chess.*;

import com.google.gson.*;
import java.lang.reflect.Type;


public class ChessMoveSerializer implements JsonSerializer<ChessMove>, JsonDeserializer<ChessMove> {
    @Override
    public JsonElement serialize(ChessMove src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("startPosition", context.serialize(src.getStartPosition(), ChessPosition.class));
        result.add("endPosition", context.serialize(src.getEndPosition(), ChessPosition.class));

        ChessPiece.PieceType promotionPiece = src.getPromotionPiece();
        if (promotionPiece != null) {
            result.addProperty("promotionPiece", promotionPiece.name());
        } else {
            result.addProperty("promotionPiece", (String) null);
        }

        return result;
    }

    @Override
    public ChessMove deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        ChessPosition startPosition = context.deserialize(jsonObject.get("startPosition"), ChessPosition.class);
        ChessPosition endPosition = context.deserialize(jsonObject.get("endPosition"), ChessPosition.class);

        JsonElement promotionPieceElement = jsonObject.get("promotionPiece");
        ChessPiece.PieceType promotionPiece = null;
        if (promotionPieceElement != null && !promotionPieceElement.isJsonNull()) {
            promotionPiece = ChessPiece.PieceType.valueOf(promotionPieceElement.getAsString());
        }

        return new ChessMove(startPosition, endPosition, promotionPiece);
    }
}