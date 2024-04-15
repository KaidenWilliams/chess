package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage {


    // Made ChessGame serializer, might as well use it
    ChessGame game;

    public LoadGameMessage( ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public ChessGame getGame() {
        return this.game;
    }

}
