package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataAccess.DataAccessException;
import dataAccess.Memory.MemoryDataAccess;
import server.JsonRequestObjects.RegisterRequest;
import server.JsonResponseObjects.ExceptionResponse;
import server.JsonResponseObjects.RegisterResponse;
import spark.*;
import service.ChessService;
import model.*;

public class ChessServer {

    private final ChessService service;


    public ChessServer(){
        this.service = new ChessService(new MemoryDataAccess());
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

//        Spark.delete("/db", (req, res) -> clear(req, res));
        Spark.post("/user", this::registerUser);
//        Spark.post("/db", this::loginUser);
//        Spark.delete("/user", this::logoutUser);
//        Spark.get("/db", this::listGames);
//        Spark.post("/user", this::createGame);
//        Spark.put("/db", this::joinGame);
//        Spark.delete("/db", this::clearAll);
//        Spark.exception(ResponseException.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return port();
    }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public int port() {
        return Spark.port();
    }


    //1. Register User
    // [Post] /user {username, pasword, email}
    //	- register new user
    //	- Success: [200] { "username":"", "authToken":"" }
    //	- Failure: [400] { "message": "Error: bad request" }
    //	- Failure: [403] { "message": "Error: already taken" }
    //	- Failure: [500] { "message": "Error: description" }
    private Object registerUser(Request req, Response res) {

        try {
            RegisterRequest user = new Gson().fromJson(req.body(), RegisterRequest.class);

            String authToken = service.registerUser(user);

            res.status(200);
            return new Gson().toJson(authToken, RegisterResponse.class);

        }
        catch (DataAccessException e) {
            res.status(e.getStatusCode());
            return new Gson().toJson(e.getMessage(), ExceptionResponse.class);
        }
        catch (JsonSyntaxException e) {
            res.status(400);
            return new Gson().toJson("Error: bad request", ExceptionResponse.class);
        }
        catch (Exception e){
            res.status(500);
            return new Gson().toJson(e.getMessage(), ExceptionResponse.class);
        }

    }

    //2. Login User
    //Login: [Post] /session {username, pasword}
    //	- logs in existing user (returns new authToken)
    //	- Sucess: [200] { "username":"", "authToken":"" }
    //	- Failure: [401] { "message": "Error: unauthorized" }
    //	- Failure: [500] { "message": "Error: description" }
//    private Object loginUser(Request req, Response res) {

//      service.loginUser()
//    }


    //3. Logout User
    // Logout: [Delete] /session authToken
    //	- Logs out user represented by authToken
    //	- Header: authorization: <authToken>
    //	- Success: 200
    //  - Failure: [401] { "message": "Error: unauthorized" }
    //	- Failure: [500] { "message": "Error: description" }
//    private Object logoutUser(Request req, Response res) {
//
//        service.logoutUser()
//
//    }




    //4. ListGames
    // List Games: [Get] /game authToken
    //	- gives a list of all games
    //	- Header: authorization: <authToken>
    //	- Success: [200] { "games": [{"gameID": 1234, "whiteUsername":", "blackUsername":"", "gameName:""} ]}
    //  - Failure: [401] { "message": "Error: unauthorized" }
    //  - Failure: [500] { "message": "Error: description" }
//    private Object listGames(Request req, Response res) {
//        res.type("application/json");
//        var list = service.listPets().toArray();
//        return new Gson().toJson(Map.of("pet", list));

//        service.listGames()
//    }
//


    //5. createGame
    //  Create Game: [Post] /game authToken {gameName}
    //  - Creates a new game
    //  - Header: authorization: <authToken>
    //  - Success:  [200] { "gameID": 1234 }
    //  - Failure: [400] { "message": "Error: bad request" }
    //  - Failure: [401] { "message": "Error: unauthorized" }
    //  - Failure: [500] { "message": "Error: description" }
//    private Object createGame(Request req, Response res) {
//        var pet = new Gson().fromJson(req.body(), Pet.class);
//        pet = service.addPet(pet);
//        webSocketHandler.makeNoise(pet.name(), pet.sound());
//        return new Gson().toJson(pet);

//        service.createGame()
//    }




    //6. joinGame
    //  Join Game: [Put] /game authToken {ClientColor, gameID}
    //  - Verifies that the specified game exists, and, if a color is specified, adds the caller as the requested color to the game. If 	no color is specified the user is joined as an observer. This 	request is idempotent.
    //  - Header: authorization: <authToken>
    //  - Success: 200
    //  - Failure: [400] { "message": "Error: bad request" }
    //  - Failure: [401] { "message": "Error: unauthorized" }
    //  - Failure: [500] { "message": "Error: description" }
//    private Object joinGame(Request req, Response res) {
//
//      service.joinGame();
//    }



    //7. Clear all DB
    //  Clear Application: [Delete] /db
    //  - clears db, removes users, games, authTokens
    //  - Success: 200
    //  - Failure: 500 {"message": "Error: description" }
//    private Object clearAll(Request req, Response res) {
//        service.deleteAllPets();
//        res.status(204);
//        return "";

//          service.clearAll()
//    }



    //8. Exception Hander, will do later
//    private void exceptionHandler(ResponseException ex, Request req, Response res) {
//        res.status(ex.StatusCode());
//    }


}
