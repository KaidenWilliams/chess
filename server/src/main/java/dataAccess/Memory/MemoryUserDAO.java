package dataAccess.Memory;

import dataAccess.IAuthDAO;
import dataAccess.IUserDAO;
import model.AuthModel;
import model.UserModel;

import java.util.ArrayList;

// TODO error throwing

public class MemoryUserDAO extends GeneralMemoryDAO<UserModel> implements IUserDAO {


    public MemoryUserDAO() {
        super();
        this.data = MemoryDB.getInstance().getUserData();
    }

    //1. Get row based on username
    public UserModel getRowByUsername(String username) {
        return findOne(model -> model.username().equals(username));
    }

    //2. Insert row: already implemented


    //3. Get row based on username and password
    public UserModel getRowByUsernameAndPassword(String username, String password) {
        return findOne(model -> model.username().equals(username) && model.password().equals(password));
    }

    //4. Delete all: already implemented

}