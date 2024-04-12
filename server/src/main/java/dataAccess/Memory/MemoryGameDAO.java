package dataAccess.Memory;

import dataAccess.IGameDAO;
import model.DataAccessException;
import model.models.GameModel;

// TODO error throwing

public class MemoryGameDAO extends GeneralMemoryDAO<GameModel> implements IGameDAO {

    private int currId;

    public MemoryGameDAO() {
        super();
        this.data = MemoryDB.getInstance().getGameData();
        this.currId = 1;
    }


    //1. Get all games - already implemented

    //2. Insert row
    public GameModel create(GameModel createGame) {
        return super.create(new GameModel(getGameId(), null, null, createGame.gameName(), null));
    }

    //3. Get game from gameID
    public GameModel getRowByGameID(int gameID) {
        return findOne(model -> Integer.valueOf(model.gameID()).equals(gameID));
    }


    // I really don't want to actually implement this
    // If I end up needing too, will need to rework chessGame logic, take string not game
    public boolean updateGameDao(int gameId, String chessGame) throws DataAccessException {
        GameModel oldGame = findOne(model -> Integer.valueOf(model.gameID()).equals(gameId));
        GameModel newGame = new GameModel(oldGame.gameID(), oldGame.whiteUsername(), oldGame.blackUsername(), oldGame.gameName(), oldGame.chessGame());
        data.set(data.indexOf(oldGame), newGame);

        return false;
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


    private int getGameId() {
        int oldId = currId;
        currId++;
        return oldId;
    }


    //5. Delete all - already implemented

    //6. Add Spectator - don't have to yet



}