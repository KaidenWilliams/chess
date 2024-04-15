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
        result.addProperty("promotionPiece", src.getPromotionPiece().toString());
        return result;
    }

    @Override
    public ChessMove deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        ChessPosition startPosition = context.deserialize(jsonObject.get("startPosition"), ChessPosition.class);
        ChessPosition endPosition = context.deserialize(jsonObject.get("endPosition"), ChessPosition.class);
        String promotionPieceStr = jsonObject.get("promotionPiece").getAsString();
        ChessPiece.PieceType promotionPiece = ChessPiece.PieceType.valueOf(promotionPieceStr);
        return new ChessMove(startPosition, endPosition, promotionPiece);
    }
}