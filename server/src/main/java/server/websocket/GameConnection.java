package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.userCommands.JoinObserverCommand;
import webSocketMessages.userCommands.JoinPlayerCommand;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

// any difference in how observers and players are treated?

public class GameConnection {

    public int gameId;
    public UserConnection whiteUser;
    public UserConnection blackUser;
    public final ConcurrentHashMap<String, UserConnection> observers = new ConcurrentHashMap<>();

    public GameConnection(int gameId) {
        this.gameId = gameId;
    }

    public void addPerson(String authToken, String userName, ChessGame.TeamColor color, Session session) {
        UserConnection user = new UserConnection(authToken, userName, session, color);

        switch (color) {

            case null:
                observers.put(authToken, user);
            case ChessGame.TeamColor.WHITE:
                if (whiteUser == null) whiteUser = user;
            case ChessGame.TeamColor.BLACK:
                if (blackUser == null) blackUser = user;;
        }
    }

    public void removePerson(String authToken) {


        if (Objects.equals(whiteUser.authToken, authToken)) {
            whiteUser = null;
        }
        else if (Objects.equals(blackUser.authToken, authToken)) {
            blackUser = null;
        }
        else {
            observers.remove(authToken);
        }

    }

    public UserConnection getPerson(String authToken) {

        if (Objects.equals(whiteUser.authToken, authToken)) {
            return whiteUser;
        }
        else if (Objects.equals(blackUser.authToken, authToken)) {
            return blackUser;
        }
        else {
            return observers.get(authToken);
        }
        
    }


}
