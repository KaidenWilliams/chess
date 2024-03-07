package dataAccess.SQL;

import dataAccess.DataAccessException;
import dataAccess.IAuthDAO;
import dataAccess.Memory.MemoryDB;
import model.AuthModel;

public class SQLAuthDAO implements IAuthDAO {


    //1. Insert row
    public AuthModel create(AuthModel providedAuthModel) throws DataAccessException {

    }


    //2. Delete row where authtoken equals
    public AuthModel deleteRowByAuthtoken(String providedAuthToken) throws DataAccessException {
        return deleteBy(model -> model.authToken().equals(providedAuthToken));
    }

    //3. Get row (username) from authtoken
    public AuthModel getRowByAuthtoken(String providedAuthToken) {
        return findOne(model -> model.authToken().equals(providedAuthToken));
    }

    //4. Delete all - already implemented
    public void deleteAll() throws DataAccessException {

    }

}
