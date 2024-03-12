package model.JsonRequestValidation;

import model.JsonRequestObjects.LoginRequest;

public class LoginValidation {

    public static void validate(LoginRequest loginRequestRecord){

        if (loginRequestRecord == null || loginRequestRecord.username() == null || loginRequestRecord.password() == null) {
            throw new IllegalArgumentException("Error: bad request");
        }

    }
}
