package webSocketMessages.userCommands;

import chess.ChessGame;

public class LeaveCommand extends UserGameCommand {

    Integer gameID;

    public LeaveCommand(String authToken, Integer gameId) {
        super(authToken);
        commandType = CommandType.LEAVE;
        this.gameID = gameId;
    }

    public Integer getGameId() {
        return this.gameID;
    }

}
