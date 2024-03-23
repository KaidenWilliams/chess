package dataAccess;

import model.models.AuthModel;
import model.DataAccessException;

public interface IAuthDAO {

    //1. Insert row - already implemented
    public AuthModel create(AuthModel providedAuthModel) throws DataAccessException;

    //2. Delete row where authtoken equals
    public Object deleteRowByAuthtoken(String providedAuthToken) throws DataAccessException;

    //3. Get row (username) from authtoken
    public AuthModel getRowByAuthtoken(String providedAuthToken) throws DataAccessException;

    //4. Delete all - already implemented
    public void deleteAll() throws DataAccessException;

}
