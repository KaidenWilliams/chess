package server.JsonRequestValidation;

import server.JsonRequestObjects.LoginRequest;

public class LoginValidation {

    public void validate(LoginRequest loginRequestRecord){

        if (loginRequestRecord == null || loginRequestRecord.username() == null || loginRequestRecord.password() == null) {
            throw new IllegalArgumentException("Error: bad request");
        }

    }
}
