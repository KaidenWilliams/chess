package state;

import exceptionclient.ClientException;
import model.JsonRequestObjects.*;
import model.JsonResponseObjects.*;
import static ui.LoggedOutBuilder.*;
import static ui.SharedBuilder.*;
import ui.EscapeSequences;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


public class LoggedOutState extends AState {

    protected static final Map<String, Function<String[], String>> _commandMethods = new HashMap<>();

    private static final String _color = EscapeSequences.SET_TEXT_COLOR_ORANGE;


    public LoggedOutState(ClientContext context) {
        super(context);
        _commandMethods.put("register", this::register);
        _commandMethods.put("login", this::login);
        _commandMethods.put("quit", this::quit);
        _commandMethods.put("help", this::help);
    }

    private String register(String[] params) {

        if (params == null || params.length != 3) {
            return getErrorStringSyntax("register");
        }

        try {
            var req = new RegisterRequest(params[0], params[1], params[2]);
            RegisterResponse res = context.serverFacade.registerUser(req);
            context.authToken = res.authToken();
            context.username = params[0];
            context.observer.changeStateLoggedIn();
            return setStringColor(_color, getRegisterString(context.username));

        } catch (ClientException e) {
            return getErrorStringRequest(e.toString(), "register");
        }
    }

    private String login(String[] params)  {

        if (params == null || params.length != 2) {
            return getErrorStringSyntax("login");
        }
        try {
            var req = new LoginRequest(params[0], params[1]);
            LoginResponse res = context.serverFacade.loginUser(req);

            context.authToken = res.authToken();
            context.username = params[0];
            context.observer.changeStateLoggedIn();
            return setStringColor(_color, getLoginString(context.username));
        }
        catch (ClientException e) {
            return getErrorStringRequest(e.toString(), "login");
        }
    }

    private String quit(String[] params) {
        return setStringColor(_color, quitString);
    }

    private String help(String[] params) {
        return setStringColor(_color, helpString);
    }

    @Override
    public Map<String, Function<String[], String>> getCommandMethods() {
        return _commandMethods;
    }

    @Override
    String defaultCommand(String[] params) {
        return setStringColor(_color, defaultString);
    }

}
