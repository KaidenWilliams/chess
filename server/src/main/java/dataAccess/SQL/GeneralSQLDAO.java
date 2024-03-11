package dataAccess.SQL;

import chess.ChessGame;
import dataAccess.DataAccessException;


import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class GeneralSQLDAO {

    public ResultSet executeQuery(Connection conn, String statement, Object... params) throws DataAccessException {
        try {
            PreparedStatement ps = createPreparedStatement(conn, statement, params);
            return ps.executeQuery();
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to execute query: %s, %s", statement, e.getMessage()), 500);
        }
    }

    public int executeUpdateWithNumberRows(Connection conn, String statement, Object... params) throws DataAccessException {
        try (var ps = createPreparedStatement(conn, statement, params)) {
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database, WithNumberRows: %s, %s", statement, e.getMessage()), 500);
        }
    }

    public int executeUpdateWithKeys(Connection conn, String statement, Object... params) throws DataAccessException {
        try (var ps = createPreparedStatement(conn, statement, Statement.RETURN_GENERATED_KEYS, params)) {
            ps.executeUpdate();
            var rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database, WithKeys: %s, %s", statement, e.getMessage()), 500);
        }
    }

    public PreparedStatement createPreparedStatement(Connection conn, String statement, Object... params) throws DataAccessException {
        try {
            var ps = conn.prepareStatement(statement);
            for (int i = 0; i < params.length; i++) {
                Object param = params[i];
                if (param == null) {
                    ps.setNull(i + 1, Types.NULL);
                } else {
                    ps.setObject(i + 1, param);
                }
            }
            return ps;
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to create prepared statement: %s, %s", statement, e.getMessage()), 500);
        }
    }

    public PreparedStatement createPreparedStatement(Connection conn, String statement, int flag, Object... params) throws DataAccessException {
        try {
            var ps = conn.prepareStatement(statement, flag);
            for (int i = 0; i < params.length; i++) {
                Object param = params[i];
                if (param == null) {
                    ps.setNull(i + 1, Types.NULL);
                } else {
                    ps.setObject(i + 1, param);
                }
            }
            return ps;
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to create prepared statement: %s, %s", statement, e.getMessage()), 500);
        }
    }

}