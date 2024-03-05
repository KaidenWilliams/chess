package server.JsonRequestValidation;

import server.JsonRequestObjects.RegisterRequest;

public class RegisterValidation {
    public void validate(RegisterRequest registerRecord){

        if (registerRecord == null || registerRecord.username() == null || registerRecord.password() == null || registerRecord.email() == null) {
            throw new IllegalArgumentException("Error: bad request");
        }

    }

}
