package dataAccess.Memory;

import dataAccess.IAuthDAO;
import model.AuthModel;

// TODO error throwing

public class MemoryAuthDAO extends GeneralMemoryDAO<AuthModel> implements IAuthDAO {

    private final MemoryDB memoryDB;

    public MemoryAuthDAO() {
        super();
        this.memoryDB = MemoryDB.getInstance();
        this.data = memoryDB.getAuthData();
    }

    //1. Insert row - already implemented

    //2. Delete row where authtoken equals
    public AuthModel deleteRowFromAuthtoken(String providedAuthToken) {
        return delete(model -> model.authToken().equals(providedAuthToken));
    }

    //3. Get row (username) from authtoken
    public AuthModel getRowFromAuthtoken(String providedAuthToken) {
        return findOne(model -> model.authToken().equals(providedAuthToken));
    }

    //4. Delete all - already implemented



}