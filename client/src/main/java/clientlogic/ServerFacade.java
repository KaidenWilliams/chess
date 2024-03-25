package clientlogic;


import com.google.gson.Gson;
import exceptionclient.ClientException;
import model.JsonRequestObjects.*;
import model.JsonResponseObjects.*;

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
        return this.makeRequest("POST", path, req, null, RegisterResponse.class);
    }

    public LoginResponse loginUser(LoginRequest req) throws ClientException {
        var path = "/session";
        return this.makeRequest("POST", path, req, null, LoginResponse.class);
    }

    public void logoutUser(String authorization) throws ClientException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, authorization, null);
    }

    public ListGamesResponse listGames(String authorization) throws ClientException {
        var path = "/game";
        return this.makeRequest("GET", path, null, authorization, ListGamesResponse.class);
    }

    public CreateGameResponse createGame(CreateGameRequest.RequestBody req, String authorization) throws ClientException {
        var path = "/game";
        return this.makeRequest("POST", path, req, authorization, CreateGameResponse.class);
    }

    public void joinGame(JoinGameRequest.RequestBody req, String authorization) throws ClientException {
        var path = "/game";
        this.makeRequest("PUT", path, req, authorization, null);
    }




    private <T> T makeRequest(String method, String path, Object request, String authorization, Class<T> responseClass) throws ClientException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, authorization, http);
            http.connect();

             if (isSuccessful(http)){
                 return readBodySuccess(http, responseClass);
             }
             else {
                readBodyError(http);
            }

        } catch (ClientException e) {
            throw e;
        }
        catch (Exception ex) {
            throw new ClientException(ex.getMessage(), 500);
        }
        return null;
    }

    private static boolean isSuccessful(HttpURLConnection http) throws IOException {
        var status = http.getResponseCode();
        return status / 100 == 2;
    }


    private static void writeBody(Object request, String authorization, HttpURLConnection http) throws ClientException {

        if (authorization != null) {
            http.setRequestProperty("Authorization", authorization);
        }

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

    private static <T> T readBodySuccess(HttpURLConnection http, Class<T> responseClass) throws ClientException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);

                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
            catch (IOException e) {
                throw new ClientException(e.getMessage(), 500);
            }
        }
        return response;
    }

    private static void readBodyError(HttpURLConnection http) throws ClientException {
        ExceptionResponse response;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getErrorStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                response = new Gson().fromJson(reader, ExceptionResponse.class);
                throw new ClientException(response.message(), http.getResponseCode());
            }
            catch (IOException e) {
                throw new ClientException(e.getMessage(), 500);
            }
        }
    }




}


