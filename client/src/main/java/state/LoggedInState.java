package state;

import clientlogic.ServerFacade;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class LoggedInState extends AState {

//1. Help	Displays text informing the user what actions they can take.
//2. Logout	Logs out the user. Calls the server logout API to logout the user.
// - After logging out with the server, the client should transition to the Prelogin UI.
//3. Create Game	Allows the user to input a name for the new game. Calls the server
// - create API to create the game. This does not join the player to the created game; it
// - only creates the new game in the server.
//4. List Games	Lists all the games that currently exist on the server. Calls the
// - server list API to get all the game data, and displays the games in a numbered list,
// - including the game name and players (not observers) in the game. The numbering for the
// - list should be independent of the game IDs.
//5. Join Game	Allows the user to specify which game they want to join and what color
// - they want to play. They should be able to enter the number of the desired game. Your
// - client will need to keep track of which number corresponds to which game from the last
// - time it listed the games. Calls the server join API to join the user to the game.
//6. Join Observer	Allows the user to specify which game they want to observe. They should
// - be able to enter the number of the desired game. Your client will need to keep track of which
// - number corresponds to which game from the last time it listed the games. Calls the server join
// - API to verify that the specified game exists.


    protected static Map<String, Function<String[], String>> _commandMethods = new HashMap<>();

    public LoggedInState(ServerFacade serverFacade, StateNotifier observer) {
        super(serverFacade, observer);
//        _commandMethods.put("logout", this::Help);
//        _commandMethods.put("help", this::Logout);
//        _commandMethods.put("logout", this::CreateGame);
//        _commandMethods.put("help", this::ListGames);
//        _commandMethods.put("logout", this::JoinGame);
//        _commandMethods.put("help", this::JoinObserver);
    }

    @Override
    Map<String, Function<String[], String>> getCommandMethods() {
        return null;
    }

    @Override
    String DefaultCommand(String[] params) {
        return null;
    }


//   private String Help() {
//
//    }





}
