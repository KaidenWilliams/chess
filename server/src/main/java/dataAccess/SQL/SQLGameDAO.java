package dataAccess.SQL;

import dataAccess.DataAccessException;
import dataAccess.IGameDAO;
import dataAccess.Memory.MemoryDB;
import model.GameModel;

import java.util.ArrayList;
import java.util.Collection;

public class SQLGameDAO implements IGameDAO {

    public SQLGameDAO() {
    }


    //1. Get all games
    public Collection<GameModel> listAll() throws DataAccessException {
        var result = new ArrayList<GameModel>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, json FROM game";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readPet(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()), 500);
        }
        return result;
    }


    //2. Insert row
    GameModel create(GameModel providedGameModel) throws DataAccessException;


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

    //5. Delete all

    void deleteAll() throws DataAccessException;




    //6. Add Spectator - don't have to yet





}
