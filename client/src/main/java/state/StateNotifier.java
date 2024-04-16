package state;

import clientlogic.ClientController;
import clientlogic.ServerFacade;

public class StateNotifier {

    private ClientContext context;
    private final ClientController controller;
    private ChessGameState chessGameState;

    public StateNotifier(ClientController controller) {
        this.controller = controller;
    }

    public void populateContext(ServerFacade serverFacade, String url) {
        this.context = new ClientContext(serverFacade, url, this);
    }

    public void changeStateLoggedOut() {
        var state = new LoggedOutState(context);
        controller.setState(state);
    }
    public void changeStateLoggedIn() {
        var state = new LoggedInState(context);
        chessGameState = null;
        controller.setState(state);
    }

    public void changeStateChessGame() {
        chessGameState = new ChessGameState(context);
        controller.setState(chessGameState);
    }

    public void changeStateDefault() {
        changeStateLoggedOut();
    }

    // Not optimal, but best I could think of
    public ChessGameState getChessGameState() {
        return chessGameState;
    }

}
