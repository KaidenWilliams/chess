package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage {


    // Made ChessGame serializer, might as well use it
    ChessGame game;

    public LoadGameMessage(ServerMessage.ServerMessageType type, ChessGame game) {
        super(type);
        this.game = game;
    }

    public ChessGame getGame() {
        return this.game;
    }

}
