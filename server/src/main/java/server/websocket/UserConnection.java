package server.websocket;

import chess.ChessGame;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class UserConnection {

    public String authToken;
    public String userName;
    public Session session;

    public ChessGame.TeamColor color;


    public UserConnection(String authToken, String userName, Session session, ChessGame.TeamColor color) {
        this.authToken = authToken;
        this.userName = userName;
        this.session = session;
        this.color = color;
    }

    // Used to send Json string
    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }

}
