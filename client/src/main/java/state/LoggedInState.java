package state;

import chess.ChessGame;
import clientlogic.ServerFacade;
import clientlogic.websocket.WebSocketFacade;
import exceptionclient.ClientException;
import model.JsonRequestObjects.*;
import model.JsonResponseObjects.*;
import static ui.LoggedInBuilder.*;
import ui.EscapeSequences;
import static ui.SharedBuilder.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class LoggedInState extends AState {


    protected static Map<String, Function<String[], String>> _commandMethods = new HashMap<>();

    private final String _color = EscapeSequences.SET_TEXT_COLOR_MAGENTA;

    public LoggedInState(ServerFacade serverFacade, StateNotifier observer) {
        super(serverFacade, observer);

        _commandMethods.put("logout", this::Logout);
        _commandMethods.put("list", this::List);
        _commandMethods.put("create", this::Create);
        _commandMethods.put("join", this::Join);
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
            return setStringColor(_color, getLogoutString(tempUsername));
        }
        catch (ClientException e) {
            return setStringColor(_color, getErrorStringRequest(e.toString(), "logout"));
        }
    }


    private String List(String[] params)  {

        try {
            ListGamesResponse res = _serverFacade.listGames(_authToken);

            StringBuilder sb = new StringBuilder();
            int i = 1;
            gameNumberMap.clear();

            for (ListGamesResponse.Game game : res.games()) {
                sb.append(getListGamesString(i, game.gameName(), game.whiteUsername(), game.blackUsername()));
                gameNumberMap.put(i, game.gameID());
                i++;
            }
            return setStringColor(_color, sb.toString());
        }
        catch (ClientException e) {
            return setStringColor(_color, getErrorStringRequest(e.toString(), "list"));
        }
    }

    private String Create(String[] params)  {

        if (params == null || params.length != 1) {
            return setStringColor(_color, getErrorStringSyntax("create"));
        }
        try {
            var req = new CreateGameRequest.RequestBody(params[0]);
            CreateGameResponse res = _serverFacade.createGame(req, _authToken);

            return setStringColor(_color, getCreateGameString(params[0]));
        }
        catch (ClientException e) {
            return setStringColor(_color, getErrorStringRequest(e.toString(), "list"));
        }
    }


    private String Join(String[] params)  {

        if (params == null || params.length != 2) {
            return setStringColor(_color, getErrorStringSyntax("join"));
        }
        try {
            String color = params[1].toLowerCase();
            Integer gameNumber = gameNumberMap.get(Integer.parseInt(params[0]));

            if (gameNumber == null) {
                return setStringColor(_color, getJoinGameErrorString(params[0]));
            }

            var req = new JoinGameRequest.RequestBody(color, gameNumber);
            _serverFacade.joinGame(req, _authToken);
            _webSocketFacade = new WebSocketFacade(_URL, _authToken, gameNumber, _observer);
            _webSocketFacade.JoinPlayer(color);

            _observer.ChangeStateChessGame();
            _gameColor = color;
            return setStringColor(_color, getJoinGameString(gameNumber, color));
        }
        catch (NumberFormatException e) {
            return setStringColor(_color, getJoinGameErrorString(params[0]));
        }
        catch (ClientException e) {
            return setStringColor(_color, getErrorStringRequest(e.toString(), "join"));
        }
    }

    private String Spectate(String[] params) {
        if (params == null || params.length != 1) {
            return setStringColor(_color, getErrorStringSyntax("spectate"));
        }
        try {
            Integer gameNumber = gameNumberMap.get(Integer.parseInt(params[0]));

            if (gameNumber == null) {
                return setStringColor(_color, getJoinGameErrorString(params[0]));
            }

            var req = new JoinGameRequest.RequestBody(null, gameNumber);
            _serverFacade.joinGame(req, _authToken);
            _webSocketFacade = new WebSocketFacade(_URL, _authToken, gameNumber, _observer);
            _webSocketFacade.JoinObserver();

            _observer.ChangeStateChessGame();
            _gameColor = null;
            return setStringColor(_color, getSpecateGameString(gameNumber));
        }
        catch (NumberFormatException e) {
            return setStringColor(_color, getJoinGameErrorString(params[0]));
        }
        catch (ClientException e) {
            return setStringColor(_color, getErrorStringRequest(e.toString(), "spectate"));
        }
    }



    private String Help(String[] params) {
        return setStringColor(_color, helpString);
    }


    @Override
    Map<String, Function<String[], String>> getCommandMethods() {
        return _commandMethods;
    }

    @Override
    String DefaultCommand(String[] params) {
        return setStringColor(_color, defaultString);
    }


}
