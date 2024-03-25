package state;

import exceptionclient.ClientException;
import model.JsonRequestObjects.*;
import model.JsonResponseObjects.*;
import ui.LoggedOutBuilder;
import clientlogic.ServerFacade;
import ui.SharedBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


//1. Help	Displays text informing the user what actions they can take.
//2. Quit	Exits the program.
//3. Login	Prompts the user to input login information. Calls the server login API to login the user.
// - When successfully logged in, the client should transition to the Postlogin UI.
//4. Register	Prompts the user to input registration information. Calls the server
// - register API to register and login the user. If successfully registered, the client should be
// - logged in and transition to the Postlogin UI.


public class LoggedOutState extends AState {

    protected static Map<String, Function<String[], String>> _commandMethods = new HashMap<>();

    public LoggedOutState(ServerFacade serverFacade, StateNotifier observer) {
        super(serverFacade, observer);
        _commandMethods.put("register", this::Register);
        _commandMethods.put("login", this::Login);
        _commandMethods.put("quit", this::Quit);
        _commandMethods.put("help", this::Help);
    }

    private String Register(String[] params) {

        if (params == null || params.length != 3) {
            return SharedBuilder.getErrorStringSyntax("register");
        }

        try {
            var req = new RegisterRequest(params[0], params[1], params[2]);
            RegisterResponse res = _serverFacade.registerUser(req);
            _authToken = res.authToken();
            _username = params[0];
            _observer.ChangeStateLoggedIn();
            return LoggedOutBuilder.getRegisterString(_username);

        } catch (ClientException e) {
            return SharedBuilder.getErrorStringRequest(e.toString(), "register");
        }

    }

    private String Login(String[] params)  {

        if (params == null || params.length != 2) {
            return SharedBuilder.getErrorStringSyntax("login");
        }
        try {
            var req = new LoginRequest(params[0], params[1]);
            LoginResponse res = _serverFacade.loginUser(req);

            _authToken = res.authToken();
            _username = params[0];
            _observer.ChangeStateLoggedIn();
            return LoggedOutBuilder.getLoginString(_username);
        }
        catch (ClientException e) {
            return SharedBuilder.getErrorStringRequest(e.toString(), "login");
        }
    }

    private String Quit(String[] params) {
        return LoggedOutBuilder.quitString;
    }

    private String Help(String[] params) {
        return LoggedOutBuilder.helpString;
    }

    @Override
    public Map<String, Function<String[], String>> getCommandMethods() {
        return _commandMethods;
    }

    @Override
    String DefaultCommand(String[] params) {
        return LoggedOutBuilder.defaultString;
    }

}
