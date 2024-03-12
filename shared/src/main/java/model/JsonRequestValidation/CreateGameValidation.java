package model.JsonRequestValidation;

import model.JsonRequestObjects.CreateGameRequest;

public class CreateGameValidation {

    public static void validate(CreateGameRequest createGameRecord){

        if (createGameRecord == null || createGameRecord.authToken() == null || createGameRecord.body() == null || createGameRecord.body().gameName() == null) {
            throw new IllegalArgumentException("Error: bad request");
        }

    }

}
