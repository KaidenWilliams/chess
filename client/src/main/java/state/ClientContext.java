package state;

import clientlogic.ServerFacade;
import clientlogic.websocket.WebSocketFacade;

public class ClientContext {
    public String authToken;
    public String username;
    public String gameColor;
    public final ServerFacade serverFacade;
    public WebSocketFacade webSocketFacade;
    public final String url;
    public final StateNotifier observer;

    public ClientContext(ServerFacade serverFacade, String url, StateNotifier observer) {
        this.serverFacade = serverFacade;
        this.url = url;
        this.observer = observer;
    }
}