package dataAccess.SQL;

import dataAccess.IGameDAO;
import dataAccess.Memory.MemoryDB;
import model.GameModel;

public class SQLGameDAO extends GeneralSQLDAO<GameModel> implements IGameDAO {

    private int currId;

    public SQLGameDAO() {
        super();
        this.data = MemoryDB.getInstance().getGameData();
        this.currId = 1;
    }


    //1. Get all games - already implemented

    //2. Insert row - already implemented

    //3. Get game from gameID
    public GameModel getRowByGameID(int gameID) {
        return findOne(model -> Integer.valueOf(model.gameID()).equals(gameID));
    }

    //4. Update username for correct color with id TODO needs work
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

    public int getGameId() {
        int oldId = currId;
        currId++;
        return oldId;
    }


    //5. Delete all - already implemented

    //6. Add Spectator - don't have to yet





}
