package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand {

    Integer gameId;
    ChessGame.TeamColor playerColor;

    public JoinPlayerCommand(String authToken, Integer gameId, ChessGame.TeamColor playerColor) {
        super(authToken);
        commandType = CommandType.JOIN_PLAYER;
        this.gameId = gameId;
        this.playerColor = playerColor;
    }

    public Integer getGameId() {
        return this.gameId;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return this.playerColor;
    }


}
