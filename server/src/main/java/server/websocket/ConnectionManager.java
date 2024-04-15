package server.websocket;

import chess.ChessGame;
import model.DataAccessException;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;
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

    public void removeGame(int gameId) {
        connections.remove(gameId);
    }


    public void addUser(int gameId, String authToken, String userName, ChessGame.TeamColor color, Session session) {
        GameConnection gameConnection = connections.get(gameId);

        assert(gameConnection != null);
        gameConnection.addPerson(authToken, userName, color, session);
    }

    public void removeUser(int gameId, String authToken, ChessGame.TeamColor color) {
        GameConnection gameConnection = connections.get(gameId);

        gameConnection.removePerson(authToken, color);
    }




    public void sendMessage(int gameId, String authToken, ChessGame.TeamColor color, String message) throws IOException {
        GameConnection gameConnection = connections.get(gameId);
        UserConnection userConnection = gameConnection.getPerson(authToken, color);
        userConnection.send(message);
    }

    public void broadcastMessageAll(int gameId, String authToken, ChessGame.TeamColor color, String message) throws IOException {

        GameConnection gameConnection = connections.get(gameId);

        if (gameConnection.blackUser != null && !Objects.equals(gameConnection.blackUser.authToken, authToken)) {
            gameConnection.blackUser.send(message);
        }

        if (gameConnection.whiteUser != null && !Objects.equals(gameConnection.whiteUser.authToken, authToken)) {
            gameConnection.whiteUser.send(message);
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
