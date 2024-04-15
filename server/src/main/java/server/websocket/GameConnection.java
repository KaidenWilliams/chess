package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.userCommands.JoinObserverCommand;
import webSocketMessages.userCommands.JoinPlayerCommand;

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
        UserConnection user = new UserConnection(authToken, userName, session);

        switch (color) {

            case null:
                observers.put(authToken, user);
            case ChessGame.TeamColor.WHITE:
                whiteUser = user;
            case ChessGame.TeamColor.BLACK:
                blackUser = user;
        }
    }

    public void removePerson(String authToken, ChessGame.TeamColor color) {
        switch (color) {

            case null:
                observers.remove(authToken);
            case ChessGame.TeamColor.WHITE:
                whiteUser = null;
            case ChessGame.TeamColor.BLACK:
                blackUser = null;
        }
    }

    public UserConnection getPerson(String authToken, ChessGame.TeamColor color) {

        switch (color) {

            case null:
                return observers.get(authToken);
            case ChessGame.TeamColor.WHITE:
                return whiteUser;
            case ChessGame.TeamColor.BLACK:
                return blackUser;
        }
    }


}
