package dataAccess.Memory;

import chess.ChessGame;
import dataAccess.IGameDAO;
import model.AuthModel;
import model.GameModel;

import java.util.ArrayList;

// TODO error throwing

public class MemoryGameDAO extends GeneralMemoryDAO<GameModel> implements IGameDAO {

    private final MemoryDB memoryDB;

    public MemoryGameDAO() {
        super();
        this.memoryDB = MemoryDB.getInstance();
        this.data = memoryDB.gameData;
    }


    //1. Get all games - already implemented

    //2. Insert row - already implemented

    //3. Get game from gameID
    public GameModel getRowFromGameID(int gameID) {
        return findOne(model -> Integer.valueOf(model.gameID()).equals(gameID));
    }

    //4. Update username for correct color with id TODO needs work
    public GameModel deleteRowFromAuthtoken(GameModel entityNew, ChessGame.TeamColor color) {
        if (color == ChessGame.TeamColor.WHITE) {
            return update(entityNew, model -> Integer.valueOf(model.gameID()).equals(entityNew.gameID()));
        } else {
            return update(entityNew, model -> Integer.valueOf(model.gameID()).equals(entityNew.gameID()));
        }
    }


    //5. Delete all - already implemented

    //6. Add Spectator - don't have to yet



}