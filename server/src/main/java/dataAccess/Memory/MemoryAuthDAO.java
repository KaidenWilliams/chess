package dataAccess.Memory;

import dataAccess.IAuthDAO;
import model.models.AuthModel;


public class MemoryAuthDAO extends GeneralMemoryDAO<AuthModel> implements IAuthDAO {

    public MemoryAuthDAO() {
        super();
        this.data = MemoryDB.getInstance().getAuthData();
    }


    //2. Delete row where authtoken equals
    public AuthModel deleteRowByAuthtoken(String providedAuthToken) {
        return deleteBy(model -> model.authToken().equals(providedAuthToken));
    }

    //3. Get row (username) from authtoken
    public AuthModel getRowByAuthtoken(String providedAuthToken) {
        return findOne(model -> model.authToken().equals(providedAuthToken));
    }

}