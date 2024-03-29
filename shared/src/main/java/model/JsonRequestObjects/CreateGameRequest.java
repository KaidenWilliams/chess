package model.JsonRequestObjects;

public record CreateGameRequest(String authToken, RequestBody body) {
    public record RequestBody(String gameName) {}
}
