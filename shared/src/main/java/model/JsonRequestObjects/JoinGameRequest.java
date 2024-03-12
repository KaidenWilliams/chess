package model.JsonRequestObjects;

public record JoinGameRequest(String authToken, RequestBody body) {
    public static record RequestBody(String playerColor, int gameID) {}
}
