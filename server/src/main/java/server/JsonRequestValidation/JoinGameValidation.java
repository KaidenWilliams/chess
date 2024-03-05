package server.JsonRequestValidation;

import chess.ChessGame;
import server.JsonRequestObjects.JoinGameRequest;

public class JoinGameValidation {

    public void validate(JoinGameRequest joinGameRecord) {

        if (joinGameRecord == null || joinGameRecord.authToken() == null || joinGameRecord.body() == null) {
            throw new IllegalArgumentException("Error: bad request");
        }

        if (!isValidColor(joinGameRecord.body().playerColor())) {
            throw new IllegalArgumentException("Error: bad request");
        }
    }
    private boolean isValidColor(String color) {
        return color == null || color.equalsIgnoreCase("WHITE") || color.equalsIgnoreCase("BLACK");
    }

}
