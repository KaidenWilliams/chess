package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class UserConnection {

    public String authToken;

    public String userName;
    public Session session;


    public UserConnection(String authToken, String userName, Session session) {
        this.authToken = authToken;
        this.userName = userName;
        this.session = session;
    }

    // Used to send Json string
    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }

}
