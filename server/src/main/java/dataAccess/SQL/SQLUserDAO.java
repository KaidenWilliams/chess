package dataAccess.SQL;

import dataAccess.DataAccessException;
import dataAccess.IUserDAO;
import dataAccess.Memory.MemoryDB;
import model.AuthModel;
import model.UserModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDAO extends GeneralSQLDAO implements IUserDAO {

    //3 - delete all doesn't count really

    //1. Get row based on username
    public UserModel getRowByUsername(String usernameInput) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM user WHERE username = ?";
            ResultSet rs = executeQuery(conn, statement, usernameInput);
            if (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                return new UserModel(username, password, email);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error while retrieving row by username in user", 500);
        }
    }

    //2. Insert row
    public UserModel create(UserModel providedUserModel) throws DataAccessException {

        try (var conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
            int rows = executeUpdateWithNumberRows(conn, statement, providedUserModel.username(), providedUserModel.password(), providedUserModel.email());
            if (rows == 1) {
                return providedUserModel;
            } else {
                throw new DataAccessException("Insert into user failed", 500);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to insert row into user", 500);
        }
    }

    //3. Delete all
    public void deleteAll() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "TRUNCATE user";
            executeUpdateWithNumberRows(conn, statement);

        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete all from user", 500);
        }
    }

}
