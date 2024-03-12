package model.JsonResponseObjects;
import java.util.List;

public record ListGamesResponse(List<Game> games) {
    public record Game(int gameID, String whiteUsername, String blackUsername, String gameName) {
    }
}
