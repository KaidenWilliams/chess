package model.JsonRequestValidation;

import model.JsonRequestObjects.LogoutRequest;

public class LogoutValidation {

    public static void validate(LogoutRequest logoutRequestRecord){

        if (logoutRequestRecord == null || logoutRequestRecord.authToken() == null) {
            throw new IllegalArgumentException("Error: bad request");
        }

    }
}
