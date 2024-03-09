package dataAccess.SQL;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.IAuthDAO;
import dataAccess.Memory.MemoryDB;
import model.AuthModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLAuthDAO extends GeneralSQLDAO implements IAuthDAO {

//    """
//            CREATE TABLE IF NOT EXISTS  auth (
//              `token` varchar(256) NOT NULL,
//              `username` varchar(256) NOT NULL,
//              PRIMARY KEY (`token`)
//            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
//            """,


    //1. Insert row
    public AuthModel create(AuthModel providedAuthModel) throws DataAccessException {

        var conn = getConnectionInDAO();
        var statement = "INSERT INTO auth (token, username) VALUES (?, ?)";
        int rows = executeUpdateWithNumberRows(conn, statement, providedAuthModel.authToken(), providedAuthModel.username());
        if (rows == 1) {
            return providedAuthModel;
        }
        else {
            throw new DataAccessException("Insert into auth failed", 500);
        }

    }

    //2. Delete row where authtoken equals
    public Object deleteRowByAuthtoken(String providedAuthToken) throws DataAccessException {

        var conn = getConnectionInDAO();
        var statement = "DELETE FROM auth WHERE token = ?";
        int rows = executeUpdateWithNumberRows(conn, statement, providedAuthToken);
        if (rows >= 1) {
            return providedAuthToken;
        }
        else {
            throw new DataAccessException("Delete from auth failed", 500);
        }
    }

    //3. Get row (username) from authtoken
    public AuthModel getRowByAuthtoken(String providedAuthToken) throws DataAccessException {

        var conn = getConnectionInDAO();
        var statement = "SELECT FROM auth WHERE token = ?";
        try {
            ResultSet rs = executeQuery(conn, statement, providedAuthToken);
            if (rs != null) {
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
        var conn = getConnectionInDAO();
        var statement = "TRUNCATE auth";
        executeUpdateWithNumberRows(conn, statement);
        }

}
