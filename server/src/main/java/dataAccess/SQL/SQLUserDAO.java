package dataAccess.SQL;

import dataAccess.DataAccessException;
import dataAccess.IUserDAO;
import dataAccess.Memory.MemoryDB;
import model.UserModel;

public class SQLUserDAO implements IUserDAO {


    //1. Get row based on username
    public UserModel getRowByUsername(String username) {
        return findOne(model -> model.username().equals(username));
    }

    //2. Insert row
    public UserModel create(UserModel providedUserModel) throws DataAccessException {

    }


    //3. Delete all
    public void deleteAll() throws DataAccessException {

    }



}
