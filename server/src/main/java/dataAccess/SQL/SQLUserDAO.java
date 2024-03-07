package dataAccess.SQL;

import dataAccess.IUserDAO;
import dataAccess.Memory.MemoryDB;
import model.UserModel;

public class SQLUserDAO implements IUserDAO {


    //Database name =

    public SQLUserDAO() {
    }

    //1. Get row based on username
    public UserModel getRowByUsername(String username) {
        return findOne(model -> model.username().equals(username));
    }

    //2. Insert row


    //3. Get row based on username and password
    public UserModel getRowByUsernameAndPassword(String username, String password) {
        return findOne(model -> model.username().equals(username) && model.password().equals(password));
    }

    //4. Delete all



}
