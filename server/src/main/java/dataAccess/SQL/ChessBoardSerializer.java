package dataAccess.SQL;

import com.google.gson.*;
import chess.*;

import java.lang.reflect.Type;

public class ChessBoardSerializer implements JsonSerializer<ChessBoard>, JsonDeserializer<ChessBoard> {
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

        // Serialize the row and column values
        result.addProperty("whiteRow", src.whiteKingPosition.getRow());
        result.addProperty("whiteColumn", src.whiteKingPosition.getColumn());
        result.addProperty("blackRow", src.blackKingPosition.getRow());
        result.addProperty("blackColumn", src.blackKingPosition.getColumn());

        return result;
    }

    @Override
    public ChessBoard deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ChessBoard board = new ChessBoard();
        JsonObject jsonObject = json.getAsJsonObject();

        int whiteRow = jsonObject.get("whiteRow").getAsInt();
        int whiteColumn = jsonObject.get("whiteColumn").getAsInt();
        int blackRow = jsonObject.get("blackRow").getAsInt();
        int blackColumn = jsonObject.get("blackColumn").getAsInt();

        board.whiteKingPosition = new ChessPosition(whiteRow, whiteColumn);
        board.blackKingPosition = new ChessPosition(blackRow, blackColumn);


        JsonArray jsonArray = json.getAsJsonArray();
        for (int i = 0; i < 8; i++) {
            JsonArray rowArray = jsonArray.get(i).getAsJsonArray();
            for (int j = 0; j < 8; j++) {
                JsonElement pieceJson = rowArray.get(j);
                ChessPiece piece = context.deserialize(pieceJson, ChessPiece.class);
                board.addPieceIndexes(i, j, piece);
            }
        }
        return board;
    }
}