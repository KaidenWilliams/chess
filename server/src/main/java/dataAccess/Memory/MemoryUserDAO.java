package dataAccess.Memory;

import dataAccess.IUserDAO;
import model.models.UserModel;


public class MemoryUserDAO extends GeneralMemoryDAO<UserModel> implements IUserDAO {


    public MemoryUserDAO() {
        super();
        this.data = MemoryDB.getInstance().getUserData();
    }

    public UserModel getRowByUsername(String username) {
        return findOne(model -> model.username().equals(username));
    }

}