package dataAccess.Memory;

import model.AuthModel;
import model.GameModel;
import model.UserModel;

import java.util.ArrayList;
import java.util.HashSet;

// My in-memory DB
public class MemoryDB {
    private static MemoryDB instance;
    public final ArrayList<UserModel> userData = new ArrayList<>();
    public final ArrayList<GameModel> gameData = new ArrayList<>();
    public final ArrayList<AuthModel> authData = new ArrayList<>();

    private MemoryDB() {
    }

    public static MemoryDB getInstance() {
        if (instance == null) {
            instance = new MemoryDB();
        }
        return instance;
    }

    //TODO make sure these actually work like they should
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