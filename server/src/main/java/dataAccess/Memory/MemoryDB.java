package dataAccess.Memory;

import model.AuthModel;
import model.GameModel;
import model.UserModel;

import java.util.ArrayList;
import java.util.HashSet;

public class MemoryDB {
    private static MemoryDB instance;
    public final HashSet<Integer, UserModel> userData;
    public final ArrayList<GameModel> gameData;
    public final ArrayList<AuthModel> authData;

    private MemoryDB() {
        this.userData = new HashSet<>();
        this.gameData = new HashSet<>();
        this.authData = new HashSet<>();
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