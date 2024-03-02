package dataAccess.Memory;

import dataAccess.IAuthDAO;
import dataAccess.IUserDAO;
import model.AuthModel;
import model.UserModel;

import java.util.ArrayList;

// TODO error throwing

public class MemoryUserDAO extends GeneralMemoryDAO<UserModel> implements IUserDAO {

    private final MemoryDB memoryDB;

    public MemoryUserDAO() {
        super();
        this.memoryDB = MemoryDB.getInstance();
        this.data = memoryDB.userData;
    }

    //1. Get row based on username
    public UserModel getRowFromUsername(String username) {
        return findOne(model -> model.username().equals(username));
    }

    //2. Insert row: already implemented


    //4. Get row based on username and password
    public UserModel getRowFromUsernameAndPassword(String username, String password) {
        return findOne(model -> model.username().equals(username) && model.password().equals(password));
    }

    //3. Delete all: already implemented

}