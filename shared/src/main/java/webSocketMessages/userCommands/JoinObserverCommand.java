package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinObserverCommand extends UserGameCommand {

    Integer gameID;

    public JoinObserverCommand(String authToken, Integer gameId) {
        super(authToken);
        commandType = CommandType.JOIN_OBSERVER;
        this.gameID = gameId;
    }

    public Integer getGameId() {
        return this.gameID;
    }
}
