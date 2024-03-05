package service;
import dataAccess.*;
import model.*;
import server.JsonRequestObjects.RegisterRequest;

import java.util.UUID;


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
    public String registerUser(RegisterRequest user) throws DataAccessException{

        UserModel userExisting = userDAO.getRowByUsername(user.username());

        if (userExisting != null) {
            throw new DataAccessException("Error: already taken", 403);
        }
        else {
            userDAO.create(new UserModel(user.username(), user.password(), user.email()));
            AuthModel authRow = authDAO.create(new AuthModel(UUID.randomUUID().toString(), user.username()));
            return authRow.authToken();
        }
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
