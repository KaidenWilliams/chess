package dataAccess;

import model.AuthModel;

public interface IAuthDAO {

    //1. Insert row - already implemented
    public AuthModel create(AuthModel providedAuthModel);

    //2. Delete row where authtoken equals
    public AuthModel deleteRowByAuthtoken(String providedAuthToken);

    //3. Get row (username) from authtoken
    public AuthModel getRowByAuthtoken(String providedAuthToken);

    //4. Delete all - already implemented
    public void deleteAll();

}
