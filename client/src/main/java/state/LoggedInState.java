package state;

import clientlogic.ServerFacade;
import exceptionclient.ClientException;
import model.JsonRequestObjects.*;
import model.JsonResponseObjects.*;
import ui.LoggedInBuilder;
import ui.SharedBuilder;

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

        _commandMethods.put("logout", this::Logout);
        _commandMethods.put("list", this::List);
        _commandMethods.put("create", this::Create);
        _commandMethods.put("join ", this::Join);
        _commandMethods.put("spectate", this::Spectate);
        _commandMethods.put("help", this::Help);
    }


    private String Logout(String[] params)  {

        try {
            _serverFacade.logoutUser(_authToken);
            String tempUsername = _username;

            _username = null;
            _authToken = null;
            _observer.ChangeStateLoggedOut();
            return LoggedInBuilder.getLogoutString(tempUsername);
        }
        catch (ClientException e) {
            return SharedBuilder.getErrorStringRequest(e.toString(), "logout");
        }
    }


    private String List(String[] params)  {

        try {
            ListGamesResponse res = _serverFacade.listGames(_authToken);

            StringBuilder sb = new StringBuilder();
            int i = 1;
            LoggedInBuilder.gameNumberMap.clear();

            for (ListGamesResponse.Game game : res.games()) {
                sb.append(LoggedInBuilder.getListGamesString(i, game.gameName(), game.whiteUsername(), game.blackUsername()));
                LoggedInBuilder.gameNumberMap.put(i, game.gameID());
                i++;
            }
            return sb.toString();
        }
        catch (ClientException e) {
            return SharedBuilder.getErrorStringRequest(e.toString(), "list");
        }
    }

    private String Create(String[] params)  {

        if (params == null || params.length != 1) {
            return SharedBuilder.getErrorStringSyntax("create");
        }
        try {
            var req = new CreateGameRequest.RequestBody(params[0]);
            CreateGameResponse res = _serverFacade.createGame(req, _authToken);

            return LoggedInBuilder.getCreateGameString(params[0]);
        }
        catch (ClientException e) {
            return SharedBuilder.getErrorStringRequest(e.toString(), "list");
        }
    }

    private String Join(String[] params)  {

        if (params == null || params.length != 2) {
            return SharedBuilder.getErrorStringSyntax("join");
        }
        try {
            String color = params[1].toLowerCase();
            Integer gameNumber = LoggedInBuilder.gameNumberMap.get(Integer.parseInt(params[0]));

            if (gameNumber == null) {
                return LoggedInBuilder.getJoinGameErrorString(params[0]);
            }

            var req = new JoinGameRequest.RequestBody(color, gameNumber);
            _serverFacade.joinGame(req, _authToken);
            _observer.ChangeStateChessGame();

            return LoggedInBuilder.getJoinGameString(gameNumber, color);
        }
        catch (NumberFormatException e) {
            return LoggedInBuilder.getJoinGameErrorString(params[0]);
        }
        catch (ClientException e) {
            return SharedBuilder.getErrorStringRequest(e.toString(), "join");
        }
    }

    private String Spectate(String[] params) {
        if (params == null || params.length != 1) {
            return SharedBuilder.getErrorStringSyntax("spectate");
        }
        try {
            Integer gameNumber = LoggedInBuilder.gameNumberMap.get(Integer.parseInt(params[0]));

            if (gameNumber == null) {
                return LoggedInBuilder.getJoinGameErrorString(params[0]);
            }

            var req = new JoinGameRequest.RequestBody(null, gameNumber);
            _serverFacade.joinGame(req, _authToken);
            _observer.ChangeStateChessGame();

            return LoggedInBuilder.getSpecateGameString(gameNumber);
        }
        catch (NumberFormatException e) {
            return LoggedInBuilder.getJoinGameErrorString(params[0]);
        }
        catch (ClientException e) {
            return SharedBuilder.getErrorStringRequest(e.toString(), "spectate");
        }
    }



    private String Help(String[] params) {
        return LoggedInBuilder.helpString;
    }


    @Override
    Map<String, Function<String[], String>> getCommandMethods() {
        return _commandMethods;
    }

    @Override
    String DefaultCommand(String[] params) {
        return LoggedInBuilder.defaultString;
    }






}
