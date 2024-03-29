package service;
import dataAccess.*;
import model.*;
import model.models.AuthModel;
import model.models.GameModel;
import model.models.UserModel;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import model.JsonRequestObjects.*;

import java.util.Collection;
import java.util.UUID;


public class Service {

    private final IAuthDAO authDAO;
    private final IGameDAO gameDAO;
    private final IUserDAO userDAO;

    public Service(IDataAccess dataAccess){
        this.authDAO = dataAccess.getAuthDAO();
        this.gameDAO = dataAccess.getGameDAO();
        this.userDAO= dataAccess.getUserDAO();
    }

    //1. Register User
    public AuthModel registerUser(RegisterRequest user) throws DataAccessException {

        UserModel userExisting = userDAO.getRowByUsername(user.username());

        if (userExisting != null) {
            throw new DataAccessException("Error: User already taken", 403);
        }
        else {

            String encryptedPassword = new BCryptPasswordEncoder().encode(user.password());

            userDAO.create(new UserModel(user.username(), encryptedPassword, user.email()));
            return authDAO.create(new AuthModel(UUID.randomUUID().toString(), user.username()));
        }
    }

    //2. Login User
    public AuthModel loginUser(LoginRequest user) throws DataAccessException {

        UserModel userExisting = userDAO.getRowByUsername(user.username());
        if (userExisting == null) {
            throw new DataAccessException("Error: incorrect login", 401);
        }

        String hashedPassword = userExisting.password();
        boolean authenticated = new BCryptPasswordEncoder().matches(user.password(), hashedPassword);
        if (!authenticated) {
            throw new DataAccessException("Error: incorrect login", 401);
        }
        else {
            return authDAO.create(new AuthModel(UUID.randomUUID().toString(), user.username()));
        }
    }

    //3. Logout User
    public void logoutUser(LogoutRequest user) throws DataAccessException {

       Object userExisting = authDAO.deleteRowByAuthtoken(user.authToken());

        if (userExisting == null) {
            throw new DataAccessException("Error: unauthorized", 401);
        }
    }

    //4. ListGames
    public Collection<GameModel> listGames(ListGamesRequest listGames) throws DataAccessException {
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
             return gameDAO.create(new GameModel(0, null, null, createGame.body().gameName(), null));
        }
    }

    //6. joinGame
    public void joinGame(JoinGameRequest joinGame) throws DataAccessException {
        AuthModel userExisting = authDAO.getRowByAuthtoken(joinGame.authToken());
        if (userExisting == null) {
            throw new DataAccessException("Error: unauthorized", 401);
        }

        GameModel oldGame = gameDAO.getRowByGameID(joinGame.body().gameID());
        if (oldGame == null ) {
            throw new DataAccessException("Error: bad request", 400);
        }

        String color = joinGame.body().playerColor();
        if (color == null){
            return;
        }

        if (!color.equalsIgnoreCase("BLACK") && !color.equalsIgnoreCase("WHITE")) {
            throw new DataAccessException("Error: bad request", 400);
        }

        GameModel updatedGame = gameDAO.updateUsername(oldGame, userExisting.username(), color);
        if (updatedGame == null) {
            throw new DataAccessException("Error: User already taken", 403);
        }
    }

    //7. Clear all DB
    public void deleteAll() throws DataAccessException {
        authDAO.deleteAll();
        gameDAO.deleteAll();
        userDAO.deleteAll();
    }
//



}
