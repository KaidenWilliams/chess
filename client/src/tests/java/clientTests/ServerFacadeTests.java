package clientTests;

import clientlogic.ServerFacade;
import exceptionclient.ClientException;
import model.DataAccessException;
import org.junit.jupiter.api.*;
import server.Server;
import model.JsonRequestObjects.*;
import model.JsonResponseObjects.*;
import service.Service;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private static Service service;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        service = server.service;
        System.out.println("Started test HTTP server on " + port);
        String url = String.format("http://localhost:%d", port);
        serverFacade = new ServerFacade(url);
    }

    @BeforeEach
    public void delete() throws DataAccessException {
        service.deleteAll();
    }
    @AfterAll
    static void stopServer()  {
        server.stop();
    }



    @Test
    void registerUserSuccess() throws ClientException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@example.com");
        RegisterResponse response = serverFacade.registerUser(registerRequest);

        Assertions.assertNotNull(response.authToken());
        Assertions.assertEquals("username", response.username());
    }
    @Test
    void registerUserFailure() throws ClientException {
        RegisterRequest registerRequest = new RegisterRequest("", "password", "email@example.com");
        serverFacade.registerUser(registerRequest);

        Assertions.assertThrows(ClientException.class, () -> serverFacade.registerUser(registerRequest));
    }


    @Test
    void loginUserSuccess() throws ClientException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@example.com");
        serverFacade.registerUser(registerRequest);

        LoginRequest loginRequest = new LoginRequest("username", "password");
        LoginResponse response = serverFacade.loginUser(loginRequest);

        Assertions.assertNotNull(response.authToken());
        Assertions.assertEquals("username", response.username());
    }
    @Test
    void loginUserFailure() {
        LoginRequest loginRequest = new LoginRequest("invaliduser", "wrongpassword");

        Assertions.assertThrows(ClientException.class, () -> serverFacade.loginUser(loginRequest));
    }


    @Test
    void logoutUserSuccess() throws ClientException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@example.com");
        RegisterResponse registerResponse = serverFacade.registerUser(registerRequest);
        String validAuthToken = registerResponse.authToken();

        Assertions.assertDoesNotThrow(() -> serverFacade.logoutUser(validAuthToken));
    }
    @Test
    void logoutUserFailure() throws ClientException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@example.com");
        serverFacade.registerUser(registerRequest);

        String invalidAuthToken = "invalid_auth_token";
        Assertions.assertThrows(ClientException.class, () -> serverFacade.logoutUser(invalidAuthToken));
    }


    @Test
    void listGamesSuccess() throws ClientException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@example.com");
        RegisterResponse registerResponse = serverFacade.registerUser(registerRequest);
        String validAuthToken = registerResponse.authToken();

        CreateGameRequest.RequestBody requestBody = new CreateGameRequest.RequestBody("Test Game");
        serverFacade.createGame(requestBody, validAuthToken);

        ListGamesResponse response = serverFacade.listGames(validAuthToken);
        Assertions.assertEquals(response.games().size(), 1);

    }
    @Test
    void listGamesFailure() throws ClientException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@example.com");
        serverFacade.registerUser(registerRequest);

        String invalidAuthToken = "invalid_auth_token";
        Assertions.assertThrows(ClientException.class, () -> serverFacade.listGames(invalidAuthToken));
    }


    @Test
    void createGameSuccess() throws ClientException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@example.com");
        RegisterResponse registerResponse = serverFacade.registerUser(registerRequest);
        String validAuthToken = registerResponse.authToken();

        CreateGameRequest.RequestBody requestBody = new CreateGameRequest.RequestBody("Test Game");
        Assertions.assertDoesNotThrow(() -> serverFacade.createGame(requestBody, validAuthToken));

    }
    @Test
    void createGameFailure() throws ClientException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@example.com");
        RegisterResponse registerResponse = serverFacade.registerUser(registerRequest);
        String validAuthToken = registerResponse.authToken();

        CreateGameRequest.RequestBody requestBody = new CreateGameRequest.RequestBody(null);
        Assertions.assertThrows(ClientException.class, () -> serverFacade.createGame(requestBody, validAuthToken));

    }


    @Test
    void joinGameSuccess() throws ClientException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@example.com");
        RegisterResponse registerResponse = serverFacade.registerUser(registerRequest);
        String validAuthToken = registerResponse.authToken();

        CreateGameRequest.RequestBody createGameRequestBody = new CreateGameRequest.RequestBody("Test Game");
        serverFacade.createGame(createGameRequestBody, validAuthToken);
        serverFacade.listGames(validAuthToken);
        JoinGameRequest.RequestBody joinGameRequestBody = new JoinGameRequest.RequestBody("white", 1);

        // Act
        Assertions.assertDoesNotThrow(() -> serverFacade.joinGame(joinGameRequestBody, validAuthToken));

    }
    @Test
    void joinGameFailure() throws ClientException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@example.com");
        RegisterResponse registerResponse = serverFacade.registerUser(registerRequest);
        String validAuthToken = registerResponse.authToken();

        CreateGameRequest.RequestBody createGameRequestBody = new CreateGameRequest.RequestBody("Test Game");
        serverFacade.createGame(createGameRequestBody, validAuthToken);
        serverFacade.listGames(validAuthToken);
        JoinGameRequest.RequestBody joinGameRequestBody = new JoinGameRequest.RequestBody("red", 1);

        Assertions.assertThrows( ClientException.class, () -> serverFacade.joinGame(joinGameRequestBody, validAuthToken));

    }
    
    
    
}
