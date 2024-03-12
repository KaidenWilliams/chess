package model.JsonRequestValidation;


import model.JsonRequestObjects.ListGamesRequest;

public class ListGamesValidation {

    public static void validate(ListGamesRequest listGamesRecord){

        if (listGamesRecord == null || listGamesRecord.authToken() == null) {
            throw new IllegalArgumentException("Error: bad request");
        }

    }
}
