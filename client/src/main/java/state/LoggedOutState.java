package state;

import exceptionclient.ClientException;
import model.JsonRequestObjects.*;
import model.JsonResponseObjects.*;
import ui.LoggedOutBuilder;
import clientlogic.ServerFacade;
import static ui.SharedBuilder.*;
import ui.EscapeSequences;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


public class LoggedOutState extends AState {

    protected static Map<String, Function<String[], String>> _commandMethods = new HashMap<>();

    private final String _color = EscapeSequences.SET_TEXT_COLOR_ORANGE;

    public LoggedOutState(ServerFacade serverFacade, StateNotifier observer) {
        super(serverFacade, observer);
        _commandMethods.put("register", this::Register);
        _commandMethods.put("login", this::Login);
        _commandMethods.put("quit", this::Quit);
        _commandMethods.put("help", this::Help);
    }

    private String Register(String[] params) {

        if (params == null || params.length != 3) {
            return setStringColor(_color, LoggedOutBuilder.getErrorStringSyntax("register"));
        }

        try {
            var req = new RegisterRequest(params[0], params[1], params[2]);
            RegisterResponse res = _serverFacade.registerUser(req);
            _authToken = res.authToken();
            _username = params[0];
            _observer.ChangeStateLoggedIn();
            return setStringColor(_color, LoggedOutBuilder.getRegisterString(_username));

        } catch (ClientException e) {
            return setStringColor(_color, LoggedOutBuilder.getErrorStringRequest(e.toString(), "register"));
        }
    }

    private String Login(String[] params)  {

        if (params == null || params.length != 2) {
            return setStringColor(_color, LoggedOutBuilder.getErrorStringSyntax("login"));
        }
        try {
            var req = new LoginRequest(params[0], params[1]);
            LoginResponse res = _serverFacade.loginUser(req);

            _authToken = res.authToken();
            _username = params[0];
            _observer.ChangeStateLoggedIn();
            return setStringColor(_color, LoggedOutBuilder.getLoginString(_username));
        }
        catch (ClientException e) {
            return setStringColor(_color, LoggedOutBuilder.getErrorStringRequest(e.toString(), "login"));
        }
    }

    private String Quit(String[] params) {
        return setStringColor(_color, LoggedOutBuilder.quitString);
    }

    private String Help(String[] params) {
        return setStringColor(_color, LoggedOutBuilder.helpString);
    }

    @Override
    public Map<String, Function<String[], String>> getCommandMethods() {
        return _commandMethods;
    }

    @Override
    String DefaultCommand(String[] params) {
        return setStringColor(_color, LoggedOutBuilder.defaultString);
    }

}
