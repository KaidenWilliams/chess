package dataAccess;


import model.GameModel;

import java.util.List;

public interface IGameDAO {

    //1. Get all games - already implemented
    List<GameModel> listAll();

    //2. Insert row - already implemented
    GameModel create(GameModel providedGameModel);

    //3. Get game from gameID
    GameModel getRowByGameID(int gameID);

    //4. Update username for correct color with id TODO needs work
    GameModel updateUsername(GameModel oldGame, String usernameNew, String color);

    //5. Delete all - already implemented
    void deleteAll();

    //6. Add Spectator - don't have to yet

    //7. Get game Id when creating new row
    int getGameId();


}
