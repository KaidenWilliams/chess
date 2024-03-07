package dataAccess.SQL;

import dataAccess.IAuthDAO;
import dataAccess.Memory.MemoryDB;
import model.AuthModel;

public class SQLAuthDAO implements IAuthDAO {


    public SQLAuthDAO() {
    }

    //1. Insert row


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
