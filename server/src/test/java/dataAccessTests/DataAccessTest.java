package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.Memory.*;
import model.AuthModel;
import model.GameModel;
import model.UserModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.JsonRequestObjects.*;
import service.Service;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataAccessTest {

    static final Service service = new Service(new MemoryDataAccess());

    @BeforeEach
    void clearBefore() throws DataAccessException {
        service.deleteAll();
    }
    @AfterEach
    void clearAfter() throws DataAccessException {
        service.deleteAll();
    }

    @Test
    void registerUserSuccess() throws DataAccessException {
        String inputUsername = "Hey I'm Bob";
        RegisterRequest inputRegisterObject = new RegisterRequest(inputUsername, "abc123", "a@b.com");

        AuthModel outputRegisterObject = service.registerUser(inputRegisterObject);
        AuthModel criteriaRegisterObject = new MemoryAuthDAO().getRowByAuthtoken(outputRegisterObject.authToken());

        String criteriaUsername = criteriaRegisterObject.username();
        assertEquals(inputUsername, criteriaUsername);

        List<UserModel> userModelList = new MemoryUserDAO().findAll(model -> model.username().equals(inputUsername));
        assertEquals(1, userModelList.size());
    }
    @Test
    void registerUserFailure() throws DataAccessException {
        String inputUsername = "Hey I'm Bob";
        RegisterRequest inputRegisterObject = new RegisterRequest(inputUsername, "abc123", "a@b.com");
        service.registerUser(inputRegisterObject);

        assertThrows(DataAccessException.class, () -> service.registerUser(inputRegisterObject));
    }


    @Test
    void loginUserSuccess() throws DataAccessException {

        new MemoryUserDAO().create(new UserModel("testUsername4", "testPassword0", "testEmail0"));

        LoginRequest inputLoginObject = new LoginRequest("testUsername4", "testPassword0");


        AuthModel outputLoginObject = service.loginUser(inputLoginObject);
        AuthModel criteriaRegisterObject = new MemoryAuthDAO().getRowByAuthtoken(outputLoginObject.authToken());

        String criteriaUsername = criteriaRegisterObject.username();
        assertEquals("testUsername4", criteriaUsername);
    }
    @Test
    void loginUserFailure() {
        new MemoryUserDAO().create(new UserModel("testUsername4", "testPassword0", "testEmail0"));

        LoginRequest inputLoginObject = new LoginRequest("testUsername4", "wrongPassword");

        assertThrows(DataAccessException.class, () -> service.loginUser(inputLoginObject));
    }


    @Test
    void logoutUserSuccess() throws DataAccessException {

        new MemoryAuthDAO().create(new AuthModel("testAuthToken", "testUserName"));
        LogoutRequest inputLoginObject = new LogoutRequest("testAuthToken");
        AuthModel criteriaRegisterObject0 = new MemoryAuthDAO().getRowByAuthtoken(inputLoginObject.authToken());
        assertNotNull(criteriaRegisterObject0);

        service.logoutUser(inputLoginObject);

        AuthModel criteriaRegisterObject1 = new MemoryAuthDAO().getRowByAuthtoken(inputLoginObject.authToken());
        assertNull(criteriaRegisterObject1);
    }
    @Test
    void logoutUserFailure() {
        new MemoryAuthDAO().create(new AuthModel("testAuthToken", "testUserName"));

        LogoutRequest inputLoginObject = new LogoutRequest("wrongAuthToken");

        assertThrows(DataAccessException.class, () -> service.logoutUser(inputLoginObject));
    }

    @Test
    void listGamesSuccess() throws DataAccessException {

        new MemoryAuthDAO().create(new AuthModel("testAuthToken", "testUserName"));
        new MemoryGameDAO().create(new GameModel(1, "test1", "test1", "testGame1", null));
        new MemoryGameDAO().create(new GameModel(2, "test1", "test1", "testGame2", null));
        new MemoryGameDAO().create(new GameModel(3, "yadaYa", "yadaYa", "testGame3", null));

        ListGamesRequest inputLoginObject = new ListGamesRequest("testAuthToken");
        Collection<GameModel> criteriaRegisterObject = service.listGames(inputLoginObject);
        assertEquals(3, criteriaRegisterObject.size());
    }
    @Test
    void listGamesFailure() {
        new MemoryAuthDAO().create(new AuthModel("testAuthToken", "testUserName"));
        new MemoryGameDAO().create(new GameModel(1, "test1", "test1", "testGame1", null));
        new MemoryGameDAO().create(new GameModel(2, "test1", "test1", "testGame2", null));
        new MemoryGameDAO().create(new GameModel(3, "yadaYa", "yadaYa", "testGame3", null));

        ListGamesRequest inputLoginObject = new ListGamesRequest("failAuthToken");
        assertThrows(DataAccessException.class, () -> service.listGames(inputLoginObject));
    }

    @Test
    void createGameSuccess() throws DataAccessException {

        new MemoryAuthDAO().create(new AuthModel("testAuthToken", "testUserName"));
        CreateGameRequest inputLoginObject = new CreateGameRequest("testAuthToken", new CreateGameRequest.RequestBody("testGameName"));
        service.createGame(inputLoginObject);

        List<GameModel> criteriaGame = new MemoryGameDAO().findAll(model -> (model.gameName()).equals("testGameName"));

        assertEquals(1, criteriaGame.size());
    }
    @Test
    void createGameFailure()  {
        new MemoryAuthDAO().create(new AuthModel("testAuthToken", "testUserName"));
        CreateGameRequest inputLoginObject = new CreateGameRequest("failAuthToken", new CreateGameRequest.RequestBody("testGameName"));

        assertThrows(DataAccessException.class, () -> service.createGame(inputLoginObject));
    }

    @Test
    void joinGameSuccess() throws DataAccessException {

        new MemoryAuthDAO().create(new AuthModel("testAuthToken", "testUserName"));
        new MemoryGameDAO().create(new GameModel(1, "boing", null, "testGame1", null));
        new MemoryGameDAO().create(new GameModel(2, null, null, "testGame2", null));
        new MemoryGameDAO().create(new GameModel(3, "yadaYa", "yadaYa", "testGame3", null));

        JoinGameRequest inputLoginObject = new JoinGameRequest("testAuthToken", new JoinGameRequest.RequestBody("White", 2));

        service.joinGame(inputLoginObject);

        List<GameModel> criteriaGame = new MemoryGameDAO().findAll(model -> (model.whiteUsername()).equals("testUserName"));

        assertEquals(1, criteriaGame.size());
    }
    @Test
    void joinGameFailure() {
        new MemoryAuthDAO().create(new AuthModel("testAuthToken", "testUserName"));
        new MemoryGameDAO().create(new GameModel(1, "boing", null, "testGame1", null));
        new MemoryGameDAO().create(new GameModel(2, null, null, "testGame2", null));
        new MemoryGameDAO().create(new GameModel(3, "yadaYa", "yadaYa", "testGame3", null));

        JoinGameRequest inputLoginObject = new JoinGameRequest("testAuthToken", new JoinGameRequest.RequestBody("Purple", 2));

        assertThrows(DataAccessException.class, () -> service.joinGame(inputLoginObject));
    }


    @Test
    void deleteAllSuccess() throws DataAccessException {

        new MemoryAuthDAO().create(new AuthModel("testToken0", "testUsername0"));
        new MemoryAuthDAO().create(new AuthModel("testToken1", "testUsername1"));

        new MemoryGameDAO().create(new GameModel(0, "testUsername2", "testUsername3", "testGameName0", null));
        new MemoryGameDAO().create(new GameModel(1, null, null, null, null));

        new MemoryUserDAO().create(new UserModel("testUsername4", "testPassword0", "testEmail0"));
        new MemoryUserDAO().create(new UserModel("testUsername5", "testPassword1", "testEmail1"));

        assertEquals(2, MemoryDB.getInstance().authData.size());
        assertEquals(2, MemoryDB.getInstance().gameData.size());
        assertEquals(2, MemoryDB.getInstance().userData.size());

        service.deleteAll();

        assertEquals(0, MemoryDB.getInstance().authData.size());
        assertEquals(0, MemoryDB.getInstance().gameData.size());
        assertEquals(0, MemoryDB.getInstance().userData.size());
    }
}
