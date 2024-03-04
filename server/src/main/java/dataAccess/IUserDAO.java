package dataAccess;

import model.UserModel;

public interface IUserDAO {

    //1. Get row based on username
    public UserModel getRowByUsername(String username);

    //2. Insert row: already implemented
    public UserModel create(UserModel providedUserModel);

    //3. Get row based on username and password
    public UserModel getRowByUsernameAndPassword(String username, String password);

    //4. Delete all: already implemented
    public void deleteAll();


}
