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
            result.addProperty("promotionPiece", promotionPiece.toString());
        } else {
            result.addProperty("promotionPiece", "");
        }

        return result;
    }

    @Override
    public ChessMove deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        ChessPosition startPosition = context.deserialize(jsonObject.get("startPosition"), ChessPosition.class);
        ChessPosition endPosition = context.deserialize(jsonObject.get("endPosition"), ChessPosition.class);

        String promotionPieceStr = jsonObject.has("promotionPiece") && !jsonObject.get("promotionPiece").isJsonNull()
                ? jsonObject.get("promotionPiece").getAsString()
                : null;

        ChessPiece.PieceType promotionPiece = promotionPieceStr != null
                ? ChessPiece.PieceType.valueOf(promotionPieceStr)
                : null;

        return new ChessMove(startPosition, endPosition, promotionPiece);
    }
}