package dataAccess.SQL;

import chess.ChessGame;
import dataAccess.DataAccessException;


import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class GeneralSQLDAO {


    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    // Uses 0 based indexing
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param instanceof ChessGame p) ps.setString(i + 1, p.toString());
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()), 500);
        }
    }
}




//    protected ArrayList<T> data = new ArrayList<>();
//
//    // Create
//    public T create(T entity, String) {
//        return data.add(entity) ? entity : null;
//    }
//
//    // Read
//    public List<T> findAll(Predicate<T> predicate) {
//        List<T> result = data.stream()
//                .filter(predicate)
//                .toList();
//        return result.isEmpty() ? null : result;
//    }
//
//    public T findOne(Predicate<T> predicate) {
//        return data.stream()
//                .filter(predicate)
//                .findFirst()
//                .orElse(null);
//    }
//
//    // Just make sure not to change the data, technically exposing db to frontend
//    public List<T> listAll()  {
//        return data.isEmpty() ? null : data;
//    }
//
//
//    // Destroy
//    public T deleteBy(Predicate<T> predicate) {
//        T entityToDelete = findOne(predicate);
//        if (entityToDelete == null) {
//            return null;
//        }
//        return data.remove(entityToDelete) ? entityToDelete : null;
//    }
//
//    public void deleteAll() {
//        data.clear();
//    }

//}
