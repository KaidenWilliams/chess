package model.customSerializers;

import com.google.gson.*;
import chess.*;

import java.lang.reflect.Type;

public class ChessPositionSerializer implements JsonSerializer<ChessPosition>, JsonDeserializer<ChessPosition> {

    @Override
    public JsonElement serialize(ChessPosition src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.addProperty("row", src.getRow());
        result.addProperty("col", src.getCol());
        return result;
    }

    @Override
    public ChessPosition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        int row = jsonObject.get("row").getAsInt();
        int col = jsonObject.get("col").getAsInt();
        return new ChessPosition(row, col);
    }
}