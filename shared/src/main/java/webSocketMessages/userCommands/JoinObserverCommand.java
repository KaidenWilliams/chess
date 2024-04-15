package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinObserverCommand extends UserGameCommand {

    Integer gameId;

    public JoinObserverCommand(String authToken, Integer gameId) {
        super(authToken);
        commandType = CommandType.JOIN_OBSERVER;
        this.gameId = gameId;
    }

    public Integer getGameId() {
        return this.gameId;
    }
}
