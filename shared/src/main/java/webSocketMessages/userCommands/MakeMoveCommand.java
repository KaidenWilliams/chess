package webSocketMessages.userCommands;

import chess.ChessGame;
import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {

    Integer gameID;
    ChessMove move;

    public MakeMoveCommand(String authToken, Integer gameId, ChessMove move) {
        super(authToken);
        commandType = CommandType.MAKE_MOVE;
        this.gameID = gameId;
        this.move = move;
    }

    public Integer getGameId() {
        return this.gameID;
    }

    public ChessMove getMove() {
        return this.move;
    }

}
