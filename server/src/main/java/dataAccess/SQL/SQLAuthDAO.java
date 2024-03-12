package dataAccess.SQL;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.IAuthDAO;
import model.AuthModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLAuthDAO extends GeneralSQLDAO implements IAuthDAO {

    //4 - delete all doesn't count really

    //1. Insert row
    public AuthModel create(AuthModel providedAuthModel) throws DataAccessException {

        try (var conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO auth (token, username) VALUES (?, ?)";
            int rows = executeUpdateWithNumberRows(conn, statement, providedAuthModel.authToken(), providedAuthModel.username());
            if (rows == 1) {
                return providedAuthModel;
            } else {
                throw new DataAccessException("Insert into auth failed", 500);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to create new Auth row", 500);
        }

    }

    //2. Delete row where authtoken equals
    public Object deleteRowByAuthtoken(String providedAuthToken) throws DataAccessException {

        try (var conn = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM auth WHERE token = ?";
            int rows = executeUpdateWithNumberRows(conn, statement, providedAuthToken);
            if (rows >= 1) {
                return providedAuthToken;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete row by AuthToken", 500);
        }
    }

    //3. Get row (username) from authtoken
    public AuthModel getRowByAuthtoken(String providedAuthToken) throws DataAccessException {

        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM auth WHERE token = ?";
            ResultSet rs = executeQuery(conn, statement, providedAuthToken);
            if (rs.next()) {
                String token = rs.getString("token");
                String username = rs.getString("username");
                return new AuthModel(token, username);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error while retrieving row by authToken", 500);
        }
    }

    //4. Delete all - already implemented
    public void deleteAll() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
        var statement = "TRUNCATE auth";
        executeUpdateWithNumberRows(conn, statement);
        } catch (SQLException e) {
            throw new DataAccessException("Error while deleting all from authTOken", 500);
        }
    }

}
