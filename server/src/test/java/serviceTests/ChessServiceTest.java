package serviceTests;
import dataAccess.DataAccessException;
import dataAccess.Memory.*;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ChessService;
import server.JsonRequestObjects.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ChessServiceTest {

    static final ChessService service = new ChessService(new MemoryDataAccess());

    @BeforeEach
    void clearBefore() {
        service.deleteAll();
    }
    @AfterEach
    void clearAfter() {
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
    void ListGamesSuccess() throws DataAccessException {

        new MemoryAuthDAO().create(new AuthModel("testAuthToken", "testUserName"));
        LogoutRequest inputLoginObject = new LogoutRequest("testAuthToken");
        AuthModel criteriaRegisterObject0 = new MemoryAuthDAO().getRowByAuthtoken(inputLoginObject.authToken());
        assertNotNull(criteriaRegisterObject0);

        service.logoutUser(inputLoginObject);

        AuthModel criteriaRegisterObject1 = new MemoryAuthDAO().getRowByAuthtoken(inputLoginObject.authToken());
        assertNull(criteriaRegisterObject1);
    }
    @Test
    void ListGamesFailure() {
        new MemoryAuthDAO().create(new AuthModel("testAuthToken", "testUserName"));

        LogoutRequest inputLoginObject = new LogoutRequest("wrongAuthToken");

        assertThrows(DataAccessException.class, () -> service.logoutUser(inputLoginObject));
    }


    @Test
    void deleteAllSuccess() {

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
