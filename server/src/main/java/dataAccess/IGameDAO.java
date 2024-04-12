package dataAccess;


import model.DataAccessException;
import model.models.GameModel;

import java.util.Collection;

public interface IGameDAO {

    //1. Get all games - already implemented
    Collection<GameModel> listAll() throws DataAccessException;

    //2. Insert row - already implemented
    GameModel create(GameModel providedGameModel) throws DataAccessException;

    //3. Get game from gameID
    GameModel getRowByGameID(int gameID) throws DataAccessException;

    //4. Update username for correct color with id TODO needs work
    GameModel updateUsername(GameModel oldGame, String usernameNew, String color) throws DataAccessException;

    boolean updateChessGame(int gameId, String chessGame) throws DataAccessException;

    //5. Delete all - already implemented
    void deleteAll() throws DataAccessException;

    //6. Add Spectator - don't have to yet

    //7. Get game Id when creating new row
}
