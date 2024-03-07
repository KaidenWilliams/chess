package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataAccess.DataAccessException;
import dataAccess.IDataAccess;
import dataAccess.SQL.SQLDataAccess;
import model.AuthModel;
import server.JsonRequestObjects.*;
import server.JsonRequestValidation.*;
import server.JsonResponseObjects.*;
import spark.*;
import service.Service;
import model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Server {

    private final Service service;


    public Server() {
        this.service = createService();
        System.out.println("Successfully Initialized Server");
    }

    // Best I could think of, TODO
    public Service createService() {
        try {
            return new Service(SQLDataAccess.getInstance());
        }
        catch (DataAccessException e) {
            System.out.println("Error initializing server. Try Again: \n" + e.getMessage());
            return null;
        }
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::loginUser);
        Spark.delete("/session", this::logoutUser);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.delete("/db", this::clearAll);
        Spark.exception(Exception.class, this::exceptionHandler);

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
    private Object registerUser(Request req, Response res) throws DataAccessException {

        RegisterRequest user = new Gson().fromJson(req.body(), RegisterRequest.class);
        RegisterValidation.validate(user);

        AuthModel auth = service.registerUser(user);

        RegisterResponse registerResponse = new RegisterResponse(auth.username(), auth.authToken());

        res.status(200);

        return new Gson().toJson(registerResponse);
    }

    //2. Login User
    private Object loginUser(Request req, Response res) throws DataAccessException {
        LoginRequest user = new Gson().fromJson(req.body(), LoginRequest.class);
        LoginValidation.validate(user);

        AuthModel auth = service.loginUser(user);

        LoginResponse registerResponse = new LoginResponse(auth.username(), auth.authToken());

        res.status(200);

        return new Gson().toJson(registerResponse);
    }


    //3. Logout User
    private Object logoutUser(Request req, Response res) throws DataAccessException {

        // Probably won't work, we will see
        LogoutRequest user = new LogoutRequest(req.headers("Authorization"));
        LogoutValidation.validate(user);

        service.logoutUser(user);

        res.status(200);
        res.type("application/json");
        return "{}";

    }


    //4. ListGames
    private Object listGames(Request req, Response res) throws DataAccessException {

        ListGamesRequest user = new ListGamesRequest(req.headers("Authorization"));
        ListGamesValidation.validate(user);

        Collection<GameModel> listGameModel = service.listGames(user);


        if (listGameModel != null ) {
            List<ListGamesResponse.Game> games = listGameModel.stream()
                    .map(gameModel -> new ListGamesResponse.Game(
                            gameModel.gameID(),
                            gameModel.whiteUsername(),
                            gameModel.blackUsername(),
                            gameModel.gameName()
                    ))
                    .collect(Collectors.toList());
            ListGamesResponse listGames = new ListGamesResponse(games);

            res.status(200);
            return new Gson().toJson(listGames);
        }
        else {
            List<ListGamesResponse.Game> games = new ArrayList<>();
            ListGamesResponse listGames = new ListGamesResponse(games);
            res.status(200);
            return new Gson().toJson(listGames);
        }

    }


    //5. createGame
    private Object createGame(Request req, Response res) throws DataAccessException {

        CreateGameRequest user = new CreateGameRequest(req.headers("Authorization"), (new Gson().fromJson(req.body(), CreateGameRequest.RequestBody.class)));
        CreateGameValidation.validate(user);

        GameModel newGame = service.createGame(user);

        CreateGameResponse createGameResponse = new CreateGameResponse(newGame.gameID());

        res.status(200);

        return new Gson().toJson(createGameResponse);

    }




    //6. joinGame
    private Object joinGame(Request req, Response res) throws DataAccessException {

        JoinGameRequest user = new JoinGameRequest(req.headers("Authorization"), (new Gson().fromJson(req.body(), JoinGameRequest.RequestBody.class)));
        JoinGameValidation.validate(user);

        service.joinGame(user);

        res.status(200);
        res.type("application/json");
        return "{}";
    }


    //7. Clear all DB
    private Object clearAll(Request req, Response res) throws DataAccessException {
        service.deleteAll();
        res.status(200);
        res.type("application/json");
        return "{}";
    }

    //8. Exception Handler, very useful
    private void exceptionHandler(Exception ex, Request req, Response res) {
        if (ex instanceof DataAccessException) {
            res.status(((DataAccessException) ex).getStatusCode());
            ExceptionResponse dataException = new ExceptionResponse(ex.getMessage());
            res.body(new Gson().toJson(dataException));
        }
        else if (ex instanceof JsonSyntaxException || ex instanceof IllegalArgumentException) {
            res.status(400);
            ExceptionResponse formatException = new ExceptionResponse("Error: bad request");
            res.body(new Gson().toJson(formatException));
        }
        else {
            res.status(500);
            ExceptionResponse generalException = new ExceptionResponse(ex.getMessage());
            res.body(new Gson().toJson(generalException));
        }
    }


}
