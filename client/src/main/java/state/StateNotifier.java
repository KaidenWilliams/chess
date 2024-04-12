package state;

import clientlogic.ClientController;

public class StateNotifier {

    ClientController controller;
    private ChessGameState chessGameState;

    public StateNotifier(ClientController controller) {
        this.controller = controller;
    }
    public void ChangeStateLoggedOut() {
        var state = new LoggedOutState(controller.getServerFacade(), this);
        controller.SetState(state);
    }
    public void ChangeStateLoggedIn() {
        var state = new LoggedInState(controller.getServerFacade(), this);
        chessGameState = null;
        controller.SetState(state);
    }

    public void ChangeStateChessGame() {
        chessGameState = new ChessGameState(controller.getServerFacade(), this);
        controller.SetState(chessGameState);
    }

    public void ChangeStateDefault() {
        ChangeStateLoggedOut();
    }

    public ChessGameState getChessGameState() {
        return chessGameState;
    }

}
