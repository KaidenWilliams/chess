package server.JsonRequestObjects;

public record CreateGameRequest(String authToken, RequestBody body) {
    public static record RequestBody(String gameName) {}
}
