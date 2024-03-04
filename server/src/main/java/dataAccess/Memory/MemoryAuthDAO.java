package dataAccess.Memory;

import dataAccess.IAuthDAO;
import model.AuthModel;

// TODO error throwing

public class MemoryAuthDAO extends GeneralMemoryDAO<AuthModel> implements IAuthDAO {

    public MemoryAuthDAO() {
        super();
        this.data = MemoryDB.getInstance().getAuthData();
    }

    //1. Insert row - already implemented

    //2. Delete row where authtoken equals
    public AuthModel deleteRowByAuthtoken(String providedAuthToken) {
        return deleteBy(model -> model.authToken().equals(providedAuthToken));
    }

    //3. Get row (username) from authtoken
    public AuthModel getRowByAuthtoken(String providedAuthToken) {
        return findOne(model -> model.authToken().equals(providedAuthToken));
    }

    //4. Delete all - already implemented

}