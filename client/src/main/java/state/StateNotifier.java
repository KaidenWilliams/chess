package state;

import clientlogic.ClientController;

public class StateNotifier {

    ClientController controller;
    public StateNotifier(ClientController controller) {
        this.controller = controller;
    }
    public void ChangeStateLoggedIn() {
        controller.SetStateLoggedIn();
    }

    public void ChangeStateLoggedOut() {
        controller.SetStateLoggedOut();
    }

    public void ChangeStateChessGame() {
        controller.SetStateChessGame();
    }
}
