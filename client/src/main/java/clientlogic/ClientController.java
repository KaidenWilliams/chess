package clientlogic;

import state.*;

import java.util.Arrays;


public class ClientController {

    // evaluates strings passed in by InputHandler
    // Simple case switch statement to decide what to do based on input Param
    // Can have enum state, or FSA, or different classes, or something like that to represent state

    // Handles all logic with inputs and outputs
    // Once handled, passes string to appropriate server facade method


    private AState state;

    public ClientController(String url) {
        ServerFacade serverFacade = new ServerFacade(url);
        StateNotifier observer = new StateNotifier(this);
        observer.populateContext(serverFacade, url);
        observer.changeStateDefault();
    }

    public String routeInput(String input)  {

        String[] tokens = input.toLowerCase().split(" ");
        String commandName = (tokens.length > 0) ? tokens[0] : "help";
        // Make commands not case-sensitive just because
        String commandNameLower = commandName.toLowerCase();
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

        return state.eval(commandNameLower, params);
    }

    public void setState(AState state) {
        this.state = state;
    }

}
