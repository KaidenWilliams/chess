package service;
import dataAccess.*;
import spark.Request;
import spark.Response;


public class ChessService {

    private final IAuthDAO authDAO;
    private final IGameDAO gameDAO;
    private final IUserDAO userDAO;

    public ChessService(IDataAccess dataAccess){
        this.authDAO = dataAccess.getAuthDAO();
        this.gameDAO = dataAccess.getGameDAO();
        this.userDAO= dataAccess.getUserDAO();
    }

    //1. Register User
    public Object registerUser(Request req, Response res) {
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
//    public Object loginUser(Request req, Response res) {
//
//    }
//
    //3. Logout User
//    public Object logoutUser(Request req, Response res) {
//
//    }
//
    //4. ListGames
//    public Object listGames(Request req, Response res) {
//        res.type("application/json");
//        var list = service.listPets().toArray();
//        return new Gson().toJson(Map.of("pet", list));
//    }
//
    //5. createGame
//    public Object createGame(Request req, Response res) {
//        var pet = new Gson().fromJson(req.body(), Pet.class);
//        pet = service.addPet(pet);
//        webSocketHandler.makeNoise(pet.name(), pet.sound());
//        return new Gson().toJson(pet);
//    }

    //6. joinGame
//    public Object joinGame(Request req, Response res) {
//
//    }

    //7. Clear all DB
//    public void deleteAllPets() throws ResponseException {
//        authDao.deleteAll();
//        gameDao.deleteAll();
//        userDao.deleteAll();
//
//    }
//



}
