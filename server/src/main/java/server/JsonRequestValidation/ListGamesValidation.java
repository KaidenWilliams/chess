package server.JsonRequestValidation;


import server.JsonRequestObjects.ListGamesRequest;

public class ListGamesValidation {

    public void validate(ListGamesRequest listGamesRecord){

        if (listGamesRecord == null || listGamesRecord.authToken() == null) {
            throw new IllegalArgumentException("Error: bad request");
        }

    }
}
