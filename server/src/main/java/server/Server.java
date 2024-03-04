package server;

import spark.*;
import com.google.gson.Gson;
import service.ChessService;

public class Server {

    private final ChessService service;

    public Server(){
        this.service = new ChessService();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

//        Spark.delete("/db", (req, res) -> clear(req, res));
        Spark.post("/user", this::registerUser);
        Spark.post("/db", this::loginUser);
        Spark.delete("/user", this::logoutUser);
        Spark.get("/db", this::listGames);
        Spark.post("/user", this::createGame);
        Spark.put("/db", this::joinGame);
        Spark.delete("/db", this::clearAll);
//        Spark.exception(ResponseException.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

//    public int port() {
//        return Spark.port();
//    }


//    private void exceptionHandler(ResponseException ex, Request req, Response res) {
//        res.status(ex.StatusCode());
//    }


    //1. Register User
    private Object registerUser(Request req, Response res) {
        var id = Integer.parseInt(req.params(":id"));
        var pet = service.getPet(id);
        if (pet != null) {
            service.deletePet(id);
            webSocketHandler.makeNoise(pet.name(), pet.sound());
            res.status(204);
        } else {
            res.status(404);
        }
        return "";
    }

    //2. Login User
//    private Object loginUser(Request req, Response res) {
//
//    }
//
    //3. Logout User
//    private Object logoutUser(Request req, Response res) {
//
//    }
//
    //4. ListGames
//    private Object listGames(Request req, Response res) {
//        res.type("application/json");
//        var list = service.listPets().toArray();
//        return new Gson().toJson(Map.of("pet", list));
//    }
//
    //5. createGame
//    private Object createGame(Request req, Response res) {
//        var pet = new Gson().fromJson(req.body(), Pet.class);
//        pet = service.addPet(pet);
//        webSocketHandler.makeNoise(pet.name(), pet.sound());
//        return new Gson().toJson(pet);
//    }

    //6. joinGame
//    private Object joinGame(Request req, Response res) {
//
//    }

    //7. Clear all DB
//    private Object clearAll(Request req, Response res) {
//        service.deleteAllPets();
//        res.status(204);
//        return "";
//    }



}
