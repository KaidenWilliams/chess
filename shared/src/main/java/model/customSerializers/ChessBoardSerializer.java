package model.customSerializers;

import com.google.gson.*;
import chess.*;

import java.lang.reflect.Type;

public class ChessBoardSerializer implements JsonSerializer<ChessBoard>, JsonDeserializer<ChessBoard> {
    @Override
    public JsonElement serialize(ChessBoard src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();

        // Serialize the board squares
        JsonArray squaresArray = new JsonArray();
        for (ChessPiece[] row : src.getSquares()) {
            JsonArray rowArray = new JsonArray();
            for (ChessPiece piece : row) {
                JsonElement pieceJson = context.serialize(piece, ChessPiece.class);
                rowArray.add(pieceJson);
            }
            squaresArray.add(rowArray);
        }
        result.add("squares", squaresArray);

        // Serialize the king positions
        result.add("whiteKingPosition", context.serialize(src.whiteKingPosition, ChessPosition.class));
        result.add("blackKingPosition", context.serialize(src.blackKingPosition, ChessPosition.class));

        return result;
    }

    @Override
    public ChessBoard deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        ChessBoard board = new ChessBoard();

        // Deserialize the king positions
        board.whiteKingPosition = context.deserialize(jsonObject.get("whiteKingPosition"), ChessPosition.class);
        board.blackKingPosition = context.deserialize(jsonObject.get("blackKingPosition"), ChessPosition.class);

        // Deserialize the board squares
        JsonArray squaresArray = jsonObject.getAsJsonArray("squares");
        for (int i = 0; i < 8; i++) {
            JsonArray rowArray = squaresArray.get(i).getAsJsonArray();
            for (int j = 0; j < 8; j++) {
                JsonElement pieceJson = rowArray.get(j);
                ChessPiece piece = context.deserialize(pieceJson, ChessPiece.class);
                board.addPieceIndexes(i, j, piece);
            }
        }

        return board;
    }
}