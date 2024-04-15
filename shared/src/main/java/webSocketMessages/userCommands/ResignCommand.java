package webSocketMessages.userCommands;

import chess.ChessGame;

public class ResignCommand extends UserGameCommand {

    Integer gameId;

    public ResignCommand(String authToken, Integer gameId) {
        super(authToken);
        commandType = CommandType.RESIGN;
        this.gameId = gameId;
    }

    public Integer getGameId() {
        return this.gameId;
    }

}
