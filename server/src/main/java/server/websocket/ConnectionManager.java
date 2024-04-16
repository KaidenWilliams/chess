package server.websocket;

import chess.ChessGame;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

// Manages Connection instances
// Need ConcurrentConnection/HashMap for every game, maybe make gameconnection class, so HashMap is list
// - of gameconnections which stores all connections?
// - gameConnection could store a game as well

public class ConnectionManager {

    public final ConcurrentHashMap<Integer, GameConnection> connections = new ConcurrentHashMap<>();

    public void addGame(int gameId) {
        if (connections.get(gameId) == null) {
            var connection = new GameConnection(gameId);
            connections.put(gameId, connection);
        }
    }

    public void addUser(int gameId, String authToken, String userName, ChessGame.TeamColor color, Session session) {
        GameConnection gameConnection = connections.get(gameId);

        assert(gameConnection != null);
        gameConnection.addPerson(authToken, userName, color, session);
    }

    public void removeUser(int gameId, String authToken) {
        GameConnection gameConnection = connections.get(gameId);

        gameConnection.removePerson(authToken);
    }

    public UserConnection getUser(int gameId, String authToken) {
        GameConnection gameConnection = connections.get(gameId);

        return gameConnection.getPerson(authToken);
    }


    public void sendMessageToConnection(Session session, String msg) throws IOException {
        session.getRemote().sendString(msg);
    }


    public void broadcastMessage(int gameId, String authToken, String message) throws IOException {

        GameConnection gameConnection = connections.get(gameId);

        if (gameConnection.blackUser != null && !Objects.equals(gameConnection.blackUser.authToken, authToken)) {

            if (gameConnection.blackUser.session.isOpen()) {
                gameConnection.blackUser.send(message);
            }
            else {
                gameConnection.blackUser = null;
            }
        }

        if (gameConnection.whiteUser != null && !Objects.equals(gameConnection.whiteUser.authToken, authToken)) {
            if (gameConnection.whiteUser.session.isOpen()) {
                gameConnection.whiteUser.send(message);
            }
            else {
                gameConnection.whiteUser = null;
            }
        }


        for (UserConnection observer : gameConnection.observers.values()) {
            if (observer.session.isOpen()) {
                if (!Objects.equals(observer.authToken, authToken)) {
                    observer.send(message);
                }
            } else {
                gameConnection.observers.remove(observer.authToken);
            }
        }
    }



}
