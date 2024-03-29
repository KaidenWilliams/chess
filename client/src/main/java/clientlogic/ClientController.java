package clientlogic;

import exceptionclient.ClientException;
import state.*;

import java.util.Arrays;


public class ClientController {

    // evaluates strings passed in by InputHandler
    // Simple case switch statement to decide what to do based on input Param
    // Can have enum state, or FSA, or different classes, or something like that to represent state

    // Handles all logic with inputs and outputs
    // Once handled, passes string to appropriate server facade method

    //calls server facade

    private AState state;

    private StateNotifier observer;
    private String authToken;
    private final String serverUrl;
    private ServerFacade serverFacade;

    public ClientController(String url) {
        serverUrl = url;
        serverFacade = new ServerFacade(serverUrl);
        observer = new StateNotifier(this);
        state = new LoggedOutState(serverFacade, observer);
    }

    public String routeInput(String input)  {

        String[] tokens = input.toLowerCase().split(" ");
        String commandName = (tokens.length > 0) ? tokens[0] : "help";
        // Make commands not case-sensitive just because
        String commandNameLower = commandName.toLowerCase();
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

        return state.eval(commandNameLower, params);
    }

    public void SetStateLoggedIn() {
        this.state = new LoggedInState(serverFacade, observer);
    }

    public void SetStateLoggedOut() {
        this.state = new LoggedOutState(serverFacade, observer);
    }

    public void SetStateChessGame() {
        this.state = new ChessGameState(serverFacade, observer);
    }

}
