package dataAccess;

import model.UserModel;

public interface IUserDAO {

    //1. Get row based on username
    public UserModel getRowByUsername(String username) throws DataAccessException;

    //2. Insert row: already implemented
    public UserModel create(UserModel providedUserModel) throws DataAccessException;

    //3. Delete all: already implemented
    public void deleteAll() throws DataAccessException;


}
