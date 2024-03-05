package dataAccess;

import chess.ChessGame;
import model.AuthModel;
import model.GameModel;

import java.util.List;

public interface IGameDAO {

    //1. Get all games - already implemented
    public List<GameModel> listAll();

    //2. Insert row - already implemented
    public GameModel create(GameModel providedGameModel);

    //3. Get game from gameID
    public GameModel getRowByGameID(int gameID);

    //4. Update username for correct color with id TODO needs work
    public GameModel deleteRowByAuthtoken(GameModel entityNew, ChessGame.TeamColor color);


    //5. Delete all - already implemented
    public void deleteAll();

    //6. Add Spectator - don't have to yet

    //7. Get game Id when creating new row
    public int getGameId();


}
