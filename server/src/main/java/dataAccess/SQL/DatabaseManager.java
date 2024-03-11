package dataAccess.SQL;

import dataAccess.DataAccessException;

import java.sql.*;
import java.util.Properties;
import java.sql.DriverManager;

// Just used to load database initially. None of the repository stuff, those will be other files
// I want to move this to the SQL Folder, don't think I can though. After everything is done I will see
public class DatabaseManager {
    private static final String databaseName;
    private static final String user;
    private static final String password;
    private static final String connectionUrl;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) throw new Exception("Unable to load db.properties");
                Properties props = new Properties();
                props.load(propStream);
                databaseName = props.getProperty("db.name");
                user = props.getProperty("db.user");
                password = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    static void createDatabase(Connection conn) throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), 500);
        }
    }

    static final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auth (
              `token` varchar(256) NOT NULL UNIQUE,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`token`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,

            """
            CREATE TABLE IF NOT EXISTS  game (
              `id` int NOT NULL AUTO_INCREMENT,
              `whiteusername` varchar(256),
              `blackusername` varchar(256),
              `gamename` varchar(256) NOT NULL,
              `game` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,

            """
            CREATE TABLE IF NOT EXISTS  user (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };


    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    public static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), 500);
        }
    }
}
