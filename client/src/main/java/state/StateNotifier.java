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

    public void ChangeStateLoggedOut() {
        var state = new LoggedOutState(context);
        controller.SetState(state);
    }
    public void ChangeStateLoggedIn() {
        var state = new LoggedInState(context);
        chessGameState = null;
        controller.SetState(state);
    }

    public void ChangeStateChessGame() {
        chessGameState = new ChessGameState(context);
        controller.SetState(chessGameState);
    }

    public void ChangeStateDefault() {
        ChangeStateLoggedOut();
    }

    // Not optimal, but best I could think of
    public ChessGameState getChessGameState() {
        return chessGameState;
    }

}
