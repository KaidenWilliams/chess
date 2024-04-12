package webSocketMessages.userCommands;

import chess.ChessGame;

public class LeaveCommand extends UserGameCommand {

    Integer gameId;

    public LeaveCommand(String authToken, Integer gameId) {
        super(authToken);
        commandType = CommandType.JOIN_PLAYER;
        this.gameId = gameId;
    }

    public Integer getGameId() {
        return this.gameId;
    }

}