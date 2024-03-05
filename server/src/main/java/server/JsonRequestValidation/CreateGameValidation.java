package server.JsonRequestValidation;

import server.JsonRequestObjects.CreateGameRequest;

public class CreateGameValidation {

    public void validate(CreateGameRequest createGameRecord){

        if (createGameRecord == null || createGameRecord.authToken() == null || createGameRecord.body() == null || createGameRecord.body().gameName() == null) {
            throw new IllegalArgumentException("Error: bad request");
        }

    }

}
