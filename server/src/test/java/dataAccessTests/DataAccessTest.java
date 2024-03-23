package dataAccessTests;

import chess.ChessGame;
import model.DataAccessException;
import dataAccess.DatabaseManager;
import model.models.AuthModel;
import model.models.GameModel;
import model.models.UserModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dataAccess.SQL.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataAccessTest {

    @BeforeEach
    void clearBefore() throws DataAccessException {

       new SQLDataAccess();
    }
    @AfterEach
    void clearAfter() throws DataAccessException {
        new SQLAuthDAO().deleteAll();
        new SQLGameDAO().deleteAll();
        new SQLUserDAO().deleteAll();

        try (var conn = DatabaseManager.getConnection()) {
            DatabaseManager.deleteDatabase(conn);
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()), 500);
        }
    }


    @Test
    void createAuthSuccess() throws DataAccessException {

        AuthModel testModel = new SQLAuthDAO().create(new AuthModel("testToken0", "testUsername0"));
        assertNotNull(testModel);

    }
    @Test
    void createAuthFailure() throws DataAccessException {
        new SQLAuthDAO().create(new AuthModel("testToken0", "testUsername0"));
        assertThrows(DataAccessException.class, () -> new SQLAuthDAO().create(new AuthModel("testToken0", "testUsername0")));
    }

    @Test
    void createGameSuccess() throws DataAccessException {

        GameModel testModel = new SQLGameDAO().create(new GameModel(0, "testUsername2", "testUsername3", "testGameName0", null));
        assertNotNull(testModel);

    }
    @Test
    void createGameFailure() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> new SQLGameDAO().create(new GameModel(0, "testUsername2", "testUsername3", null, null)));
    }

    @Test
    void createUserSuccess() throws DataAccessException {

        UserModel testModel =   new SQLUserDAO().create(new UserModel("testUsername4", "testPassword0", "testEmail0"));
        assertNotNull(testModel);

    }
    @Test
    void createUserFailure() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> new SQLUserDAO().create(new UserModel(null, "testPassword0", "testEmail0")));
    }


    @Test
    void deleteRowByAuthTokenSuccess() throws DataAccessException {
        String token = "validToken";
        String username = "testUser";
        AuthModel authModel = new AuthModel(token, username);
        new SQLAuthDAO().create(authModel);

        Object deletedToken = new SQLAuthDAO().deleteRowByAuthtoken(token);

        assertNotNull(deletedToken);
        assertEquals(token, deletedToken);
    }
    @Test
    void deleteRowByAuthTokenFailure() throws DataAccessException {
        String invalidToken = "invalidToken";

        Object deletedToken = new SQLAuthDAO().deleteRowByAuthtoken(invalidToken);

        assertNull(deletedToken);
    }

    @Test
    void getRowByAuthTokenSuccess() throws DataAccessException {

        String token = "validToken";
        String username = "testUser";
        AuthModel authModel = new AuthModel(token, username);
        new SQLAuthDAO().create(authModel);

        AuthModel retrievedAuthModel = new SQLAuthDAO().getRowByAuthtoken(token);

        assertNotNull(retrievedAuthModel);
        assertEquals(token, retrievedAuthModel.authToken());
        assertEquals(username, retrievedAuthModel.username());
    }
    @Test
    void getRowByAuthTokenFailure() throws DataAccessException {
        String invalidToken = "invalidToken";

        AuthModel retrievedAuthModel = new SQLAuthDAO().getRowByAuthtoken(invalidToken);

        assertNull(retrievedAuthModel);
    }



    @Test
    void listAllSuccess() throws DataAccessException {
        GameModel game1 = new GameModel(0, null, "game1", "gamename5", new ChessGame());
        GameModel game2 = new GameModel(0, null, "game1", "gamename4", new ChessGame());
        new SQLGameDAO().create(game1);
        new SQLGameDAO().create(game2);

        Collection<GameModel> games = new SQLGameDAO().listAll();

        assertNotNull(games);
        assertEquals(2, games.size());
    }

    @Test
    void listAllFailure() throws DataAccessException {
        Collection<GameModel> games = new SQLGameDAO().listAll();

        assertNull(games);
    }

    @Test
    void getRowByGameIdSuccess() throws DataAccessException {
        GameModel game = new GameModel(0, null, "game1", "gamename3", new ChessGame());
        GameModel createdGame = new SQLGameDAO().create(game);

        GameModel retrievedGame = new SQLGameDAO().getRowByGameID(createdGame.gameID());

        assertNotNull(retrievedGame);
        assertEquals(createdGame.gameID(), retrievedGame.gameID());
        assertEquals(createdGame.whiteUsername(), retrievedGame.whiteUsername());
        assertEquals(createdGame.blackUsername(), retrievedGame.blackUsername());
    }

    @Test
    void getRowByGameIdFailure() throws DataAccessException {
        GameModel retrievedGame = new SQLGameDAO().getRowByGameID(1);

        assertNull(retrievedGame);
    }

    @Test
    void updateUsernameSuccess() throws DataAccessException {
        GameModel game = new GameModel(0, null, "game1", "gamename1", new ChessGame());
        GameModel createdGame = new SQLGameDAO().create(game);
        String newUsername = "newPlayer";

        GameModel updatedGame = new SQLGameDAO().updateUsername(createdGame, newUsername, "WHITE");

        assertNotNull(updatedGame);
        assertEquals(createdGame.gameID(), updatedGame.gameID());
    }

    @Test
    void updateUsernameFailure() throws DataAccessException {
        GameModel game = new GameModel(0, null, "game1", "gamename2", new ChessGame());
        GameModel testGame = new GameModel(0, "beans", "game7", "gamename23", new ChessGame());
        String newUsername = "newPlayer";

        assertNull(new SQLGameDAO().updateUsername(testGame, newUsername, "WHITE"));
    }


    @Test
    void getRowByUsernameSuccess() throws DataAccessException {

        String username = "testUser";
        String password = "password123";
        String email = "test@example.com";
        UserModel userModel = new UserModel(username, password, email);
        new SQLUserDAO().create(userModel);

        UserModel retrievedUser = new SQLUserDAO().getRowByUsername(username);

        assertNotNull(retrievedUser);
        assertEquals(username, retrievedUser.username());
        assertEquals(password, retrievedUser.password());
        assertEquals(email, retrievedUser.email());
    }

    @Test
    void testGetRowByUsernameNegative() throws DataAccessException {
        String invalidUsername = "invalidUser";

        UserModel retrievedUser = new SQLUserDAO().getRowByUsername(invalidUsername);

        assertNull(retrievedUser);
    }



    @Test
    void deleteAuthSuccess() throws DataAccessException, SQLException {

        new SQLAuthDAO().create(new AuthModel("testToken0", "testUsername0"));
        new SQLAuthDAO().create(new AuthModel("testToken1", "testUsername1"));

        new SQLAuthDAO().deleteAll();

        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM auth";
            ResultSet rs = new SQLAuthDAO().executeQuery(conn, statement);

            assertFalse(rs.next());
        }
    }

    @Test
    void deleteGameSuccess() throws DataAccessException, SQLException {

        new SQLGameDAO().create(new GameModel(0, "testUsername2", "testUsername3", "testGameName0", null));

        new SQLGameDAO().deleteAll();

        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM game";
            ResultSet rs = new SQLGameDAO().executeQuery(conn, statement);

            assertFalse(rs.next());
        }
    }

    @Test
    void deleteUserSuccess() throws DataAccessException, SQLException {

        new SQLUserDAO().create(new UserModel("testUsername4", "testPassword0", "testEmail0"));
        new SQLUserDAO().create(new UserModel("testUsername5", "testPassword1", "testEmail1"));

        new SQLUserDAO().deleteAll();

        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM user";
            ResultSet rs = new SQLUserDAO().executeQuery(conn, statement);

            assertFalse(rs.next());
        }
    }
}
