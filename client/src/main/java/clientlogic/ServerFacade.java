package clientlogic;


import com.google.gson.Gson;
import exceptionclient.ClientException;
import model.JsonRequestObjects.LoginRequest;
import model.JsonRequestObjects.RegisterRequest;
import model.JsonResponseObjects.ExceptionResponse;
import model.JsonResponseObjects.LoginResponse;
import model.JsonResponseObjects.RegisterResponse;

import java.io.*;
import java.net.*;

public class ServerFacade {

    // Takes input from Client, converts it into appropriate HTTP Request, and sends to actual server

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }



//     Spark.post("/user", this::registerUser);
//        Spark.post("/session", this::loginUser);
//        Spark.delete("/session", this::logoutUser);
//        Spark.get("/game", this::listGames);
//        Spark.post("/game", this::createGame);
//        Spark.put("/game", this::joinGame);
//        Spark.delete("/db", this::clearAll);
//        Spark.exception(Exception.class, this::exceptionHandler);


    public RegisterResponse registerUser(RegisterRequest req) throws ClientException {
        var path = "/user";
        return this.makeRequest("POST", path, req, RegisterResponse.class);
    }

    public LoginResponse loginUser(LoginRequest req) throws ClientException {
        var path = "/session";
        return this.makeRequest("POST", path, req, LoginResponse.class);
    }

//
//    public void deletePet(int id) throws ResponseException {
//        var path = String.format("/pet/%s", id);
//        this.makeRequest("DELETE", path, null, null);
//    }
//
//    public void deleteAllPets() throws ResponseException {
//        var path = "/pet";
//        this.makeRequest("DELETE", path, null, null);
//    }
//
//    public Pet[] listPets() throws ResponseException {
//        var path = "/pet";
//        record listPetResponse(Pet[] pet) {
//        }
//        var response = this.makeRequest("GET", path, null, listPetResponse.class);
//        return response.pet();
//    }
//
    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ClientException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
//            throwIfNotSuccessful(http);
            // readBody should be able to throw exceptions if needed,
            // - throwIfNotSuccessful intercepted my ExceptionResponses
            return readBody(http, responseClass);
        } catch (ClientException e) {
            throw e;
        }
        catch (Exception ex) {
            throw new ClientException(ex.getMessage(), 500);
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws ClientException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
            catch (IOException e) {
                throw new ClientException(e.getMessage(), 500);
            }
        }
    }

//    private static void throwIfNotSuccessful(HttpURLConnection http) throws ClientException {
//        try {
//            var status = http.getResponseCode();
//            if (!isSuccessful(status)) {
//                throw new ClientException("Error: Response not valid", status);
//            }
//        } catch (IOException e) {
//            throw new ClientException(e.getMessage(), 500);
//        }
//    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws ClientException {
        T response = null;
        if (http.getContentLength() < 0) {
            // Fails here to get proper message because it sees error message, not happy.

            // Make sure to dispose of resources properly, make two methods maybe
            // For error one use IsSuccesful, or use finally throw exception, you can't let errors like 404 sneak by

            InputStream stream;
            int responseCode = http.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                stream = http.getInputStream();
            } else {
                stream = http.getErrorStream();
            }


            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);

                    // IntelliJ sorcery how does this even work
                    if (response instanceof ExceptionResponse exceptionResponse) {
                        throw new ClientException(exceptionResponse.message(), http.getResponseCode());
                    }

                }
            }
            catch (IOException e) {
                throw new ClientException(e.getMessage(), 500);
            }
        }
        return response;
    }


//    private static boolean isSuccessful(int status) {
//        return status / 100 == 2;
//    }

}


