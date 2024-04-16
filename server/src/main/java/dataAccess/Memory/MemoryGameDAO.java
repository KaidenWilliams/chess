package dataAccess.Memory;

import chess.ChessGame;
import dataAccess.IGameDAO;
import model.DataAccessException;
import model.customSerializers.JsonRegistrar;
import model.models.GameModel;


public class MemoryGameDAO extends GeneralMemoryDAO<GameModel> implements IGameDAO {

    private int currId;

    public MemoryGameDAO() {
        super();
        this.data = MemoryDB.getInstance().getGameData();
        this.currId = 1;
    }


    //2. Insert row
    public GameModel create(GameModel createGame) {
        return super.create(new GameModel(getGameId(), null, null, createGame.gameName(), null));
    }

    //3. Get game from gameID
    public GameModel getRowByGameID(int gameID) {
        return findOne(model -> Integer.valueOf(model.gameID()).equals(gameID));
    }


    public void updateChessGame(int gameId, String chessGame) throws DataAccessException {
        GameModel oldGame = findOne(model -> Integer.valueOf(model.gameID()).equals(gameId));
        if (oldGame == null) {
            throw new DataAccessException("Error: Game not found", 500);
        }

        ChessGame newG = JsonRegistrar.getChessGameGson().fromJson(chessGame, ChessGame.class);
        GameModel newGame = new GameModel(oldGame.gameID(), oldGame.whiteUsername(), oldGame.blackUsername(), oldGame.gameName(), newG);
        data.set(data.indexOf(oldGame), newGame);

    }

    @Override
    public void deleteUsernameFromGame(int gameId, ChessGame.TeamColor color) throws DataAccessException {
        GameModel oldGame = findOne(model -> Integer.valueOf(model.gameID()).equals(gameId));
        if (oldGame == null) {
            throw new DataAccessException("Error: Game not found", 500);
        }

        GameModel newGame;
        if (color == ChessGame.TeamColor.WHITE){
            newGame = new GameModel(oldGame.gameID(), null, oldGame.blackUsername(), oldGame.gameName(), oldGame.chessGame());
        }
        else {
            newGame = new GameModel(oldGame.gameID(), oldGame.whiteUsername(), null, oldGame.gameName(), oldGame.chessGame());
        }

        data.set(data.indexOf(oldGame), newGame);
    }


    //4. Update username for correct color with id
    public GameModel updateUsername(GameModel oldGame, String usernameNew, String color) {

        if (color == null) {
            //Add Spectator here
            return oldGame;
        }

        if (color.equalsIgnoreCase("WHITE") ) {
            if (oldGame.whiteUsername() == null) {
                GameModel newGame = new GameModel(oldGame.gameID(), usernameNew, oldGame.blackUsername(), oldGame.gameName(), oldGame.chessGame());
                data.set(data.indexOf(oldGame), newGame);
                return newGame;
            }
            else {
                return null;
            }
        }

        if (color.equalsIgnoreCase("BLACK") ) {
            if (oldGame.blackUsername() == null) {
                GameModel newGame = new GameModel(oldGame.gameID(), oldGame.whiteUsername(), usernameNew, oldGame.gameName(), oldGame.chessGame());
                data.set(data.indexOf(oldGame), newGame);
                return newGame;
            }
            else {
                return null;
            }
        }

        return null;
    }

    private int getGameId() {
        int oldId = currId;
        currId++;
        return oldId;
    }


}