package server.JsonRequestValidation;

import server.JsonRequestObjects.RegisterRequest;

public class RegisterValidation {
    public void validate(RegisterRequest registerRecord){

        if (registerRecord == null) {
            throw new IllegalArgumentException("Error: bad request");
        }

        if (registerRecord.username() == null ) {
            throw new IllegalArgumentException("Error: bad request");
        }

        // Check if password is null or not a String
        if (registerRecord.password() == null) {
            throw new IllegalArgumentException("Error: bad request");
        }

        // Check if email is null or not a String
        if (registerRecord.email() == null) {
            throw new IllegalArgumentException("Error: bad request");
        }

    }

}
