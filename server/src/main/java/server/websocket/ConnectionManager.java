package server.websocket;

import chess.ChessGame;
import model.DataAccessException;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;
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

    public void removeUser(int gameId, String authToken, String userName, ChessGame.TeamColor color, {
        GameConnection gameConnection = connections.get(gameId);

        assert(gameConnection != null);
        gameConnection.removePerson(authToken, color);
    }




//    public voidsendMessage() {
//
//    }

//    public void broadcastMessage()(String excludeVisitorName, Notification notification) throws IOException {
//        var removeList = new ArrayList<Connection>();
//        for (var c : connections.values()) {
//            if (c.session.isOpen()) {
//                if (!c.visitorName.equals(excludeVisitorName)) {
//                    c.send(notification.toString());
//                }
//            } else {
//                removeList.add(c);
//            }
//        }
//
//        // Clean up any connections that were left open.
//        for (var c : removeList) {
//            connections.remove(c.visitorName);
//        }
//    }


}
