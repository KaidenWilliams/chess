package dataAccess.Memory;

import model.AuthModel;
import model.GameModel;
import model.UserModel;

import java.util.ArrayList;

public class MemoryDB {
    private static MemoryDB instance;
    public final ArrayList<UserModel> userData;
    public final ArrayList<GameModel> gameData;
    public final ArrayList<AuthModel> authData;

    private MemoryDB() {
        this.userData = new ArrayList<>();
        this.gameData = new ArrayList<>();
        this.authData = new ArrayList<>();
    }

    public static MemoryDB getInstance() {
        if (instance == null) {
            instance = new MemoryDB();
        }
        return instance;
    }

    public ArrayList<UserModel> getUserData() {
        return userData;
    }

    public ArrayList<GameModel> getGameData() {
        return gameData;
    }

    public ArrayList<AuthModel> getAuthData() {
        return authData;
    }
}