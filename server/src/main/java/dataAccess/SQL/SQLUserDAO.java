package dataAccess.SQL;

import dataAccess.DataAccessException;
import dataAccess.IUserDAO;
import dataAccess.Memory.MemoryDB;
import model.AuthModel;
import model.UserModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDAO extends GeneralSQLDAO implements IUserDAO {

    //1. Get row based on username
    public UserModel getRowByUsername(String usernameInput) throws DataAccessException {
        var conn = getConnectionInDAO();
        var statement = "SELECT * FROM user WHERE username = ?";
        try  {
            ResultSet rs = executeQuery(conn, statement, usernameInput);
            if (rs != null) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                return new UserModel(username, password, email);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error while retrieving row by username", 500);
        }
    }

    //2. Insert row
    public UserModel create(UserModel providedUserModel) throws DataAccessException {

        var conn = getConnectionInDAO();
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?)";
        int rows = executeUpdateWithNumberRows(conn, statement, providedUserModel.username(), providedUserModel.password(), providedUserModel.email());
        if (rows == 1) {
            return providedUserModel;
        }
        else {
            throw new DataAccessException("Insert into user failed", 500);
        }
    }

    //3. Delete all
    public void deleteAll() throws DataAccessException {
        var conn = getConnectionInDAO();
        var statement = "TRUNCATE user";
        executeUpdateWithNumberRows(conn, statement);
    }

}
