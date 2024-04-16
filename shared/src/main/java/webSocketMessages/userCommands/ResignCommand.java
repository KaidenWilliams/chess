package webSocketMessages.userCommands;

import chess.ChessGame;

public class ResignCommand extends UserGameCommand {

    Integer gameID;

    public ResignCommand(String authToken, Integer gameId) {
        super(authToken);
        commandType = CommandType.RESIGN;
        this.gameID = gameId;
    }

    public Integer getGameId() {
        return this.gameID;
    }

}
