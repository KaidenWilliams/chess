package dataAccess.SQL;

import dataAccess.IUserDAO;
import dataAccess.Memory.MemoryDB;
import model.UserModel;

public class SQLUserDAO extends GeneralSQLDAO<UserModel> implements IUserDAO {


    public SQLUserDAO() {
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
