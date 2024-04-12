package webSocketMessages.userCommands;

import chess.ChessGame;
import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {

    Integer gameId;
    ChessMove move;

    public MakeMoveCommand(String authToken, Integer gameId, ChessMove move) {
        super(authToken);
        commandType = CommandType.JOIN_PLAYER;
        this.gameId = gameId;
        this.move = move;
    }

    public Integer getGameId() {
        return this.gameId;
    }

    public ChessMove getMove() {
        return this.move;
    }

}
