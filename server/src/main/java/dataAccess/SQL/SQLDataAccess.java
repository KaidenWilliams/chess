package dataAccess.SQL;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.IDataAccess;

import java.sql.SQLException;

public class SQLDataAccess implements IDataAccess {

    private static SQLDataAccess instance;
    private SQLAuthDAO authDAO;

    private SQLGameDAO gameDAO;

    private SQLUserDAO userDAO;

    public SQLDataAccess() throws DataAccessException {
        configureDatabase();
        authDAO = new SQLAuthDAO();
        gameDAO = new SQLGameDAO();
        userDAO = new SQLUserDAO();
    }

    public static SQLDataAccess getInstance() throws  DataAccessException {
        if (instance == null) {
            instance = new SQLDataAccess();
        }
        return instance;
    }


    private void configureDatabase() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : DatabaseManager.createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()), 500);
        }
    }

    public SQLAuthDAO getAuthDAO() {
        return authDAO;
    }

    public SQLGameDAO getGameDAO() {
        return gameDAO;
    }

    public SQLUserDAO getUserDAO() {
        return userDAO;
    }


}
