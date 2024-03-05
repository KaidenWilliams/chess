package service;
import dataAccess.*;
import model.*;
import server.JsonRequestObjects.*;

import java.util.List;
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
    public AuthModel registerUser(RegisterRequest user) throws DataAccessException {

        UserModel userExisting = userDAO.getRowByUsername(user.username());

        if (userExisting != null) {
            throw new DataAccessException("Error: already taken", 403);
        }
        else {
            userDAO.create(new UserModel(user.username(), user.password(), user.email()));
            return authDAO.create(new AuthModel(UUID.randomUUID().toString(), user.username()));
        }
    }

    //2. Login User
    public AuthModel loginUser(LoginRequest user) throws DataAccessException {
        UserModel userExisting = userDAO.getRowByUsernameAndPassword(user.username(), user.password());

        if (userExisting == null) {
            throw new DataAccessException("Error: already taken", 401);
        }
        else {
            return authDAO.create(new AuthModel(UUID.randomUUID().toString(), user.username()));
        }
    }

    //3. Logout User
    public void logoutUser(LogoutRequest user) throws DataAccessException {

       AuthModel userExisting = authDAO.deleteRowByAuthtoken(user.authToken());

        if (userExisting == null) {
            throw new DataAccessException("Error: unauthorized", 401);
        }
    }

    //4. ListGames
    public List<GameModel> listGames(ListGamesRequest listGames) throws DataAccessException {
        AuthModel userExisting = authDAO.getRowByAuthtoken(listGames.authToken());

        if (userExisting == null) {
            throw new DataAccessException("Error: unauthorized", 401);
        }
        else {
            return gameDAO.listAll();
        }
    }


    //5. createGame

    public GameModel createGame(CreateGameRequest createGame) throws DataAccessException {
         AuthModel userExisting = authDAO.getRowByAuthtoken(createGame.authToken());

         if (userExisting == null) {
             throw new DataAccessException("Error: unauthorized", 401);
         }
        else {
             return gameDAO.create(new GameModel(gameDAO.getGameId(), null, null, createGame.body().gameName(), null));
        }
    }

    //6. joinGame
//    public Object joinGame(JoinGameRequest joinGame) throws DataAccessException {
//       UserModel userExisting = userDAO.getRowByUsername(user.username());
//
//        if (userExisting != null) {
//            throw new DataAccessException("Error: already taken", 403);
//        }
//        else {
//            userDAO.create(new UserModel(user.username(), user.password(), user.email()));
//            AuthModel authRow = authDAO.create(new AuthModel(UUID.randomUUID().toString(), user.username()));
//            return authRow.authToken();
//        }
//    }

    //7. Clear all DB
    public void deleteAll() {
        authDAO.deleteAll();
        gameDAO.deleteAll();
        userDAO.deleteAll();
    }
//



}
