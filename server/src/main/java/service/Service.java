package service;
import chess.ChessBoard;
import chess.ChessGame;
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

    // Need to implement websocket make move methods, don't know if they even need to be here
    // Just need to call makeMove on ChessBoard, then update GameDAO, then send that new version to everyone in connection



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
             ChessGame newGame = new ChessGame();
             ChessBoard chessBoard = new ChessBoard();
             chessBoard.resetBoard();
             newGame.setChessBoard(chessBoard);

             return gameDAO.create(new GameModel(0, null, null, createGame.body().gameName(), newGame));
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





    public String getUsername(String authToken) throws DataAccessException {
        try {
            AuthModel auth = authDAO.getRowByAuthtoken(authToken);
            return auth.username();
        } catch (Exception ex) {
            throw new DataAccessException("Error: Could not Find User. Are you logged in?", 500);
        }
    }


    public boolean verifyUsername(int gameId, String username, ChessGame.TeamColor color) throws DataAccessException {

        try {
            GameModel game = gameDAO.getRowByGameID(gameId);
            String checkUsername = null;

            switch (color) {
                case ChessGame.TeamColor.WHITE -> checkUsername = game.whiteUsername();
                case ChessGame.TeamColor.BLACK -> checkUsername = game.blackUsername();
            }

            return checkUsername.equals(username);

        } catch (Exception ex) {
            throw new DataAccessException("Error: Could not Find Chess Game. Does it exist?", 500);
        }
    }


    // TODO make sure I do strings vs ChessGame right when working with ChessGame.
    // - Also, when making new game, create a blank ChessGame

    public ChessGame getChessGame(int gameId) throws DataAccessException {
        GameModel game = gameDAO.getRowByGameID(gameId);
        return game.chessGame();
    }


    public void updateChessGame(int gameId, String game) throws DataAccessException {
        try {
            gameDAO.updateChessGame(gameId, game);
        } catch (Exception ex) {
            throw new DataAccessException("Error: Could not Update Chess Game. Does it exist?", 500);
        }
    }

    public void removePlayer(int gameId, ChessGame.TeamColor color) throws DataAccessException {
        try {
            gameDAO.deleteUsernameFromGame(gameId, color);
        } catch (Exception ex) {
            throw new DataAccessException("Error: Could not Update Chess Game. Does it exist?", 500);
        }
    }





    //2. Service method to get ChessGame by gameId from game table
    // - a.used to make sure correct user in black/white
    // - b. used to update Chess Game

    //3. Service method to updateChessgame by gameId from game table



//    When sending a Notification that refers to one of the clients,
//    the message should use the Clients username. (E.g., Bob left the game).



}
