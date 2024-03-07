package dataAccess.SQL;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class GeneralSQLDAO<T> {

    protected ArrayList<T> data = new ArrayList<>();

    // Create
    public T create(T entity) {
        return data.add(entity) ? entity : null;
    }

    // Read
    public List<T> findAll(Predicate<T> predicate) {
        List<T> result = data.stream()
                .filter(predicate)
                .toList();
        return result.isEmpty() ? null : result;
    }

    public T findOne(Predicate<T> predicate) {
        return data.stream()
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }

    // Just make sure not to change the data, technically exposing db to frontend
    public List<T> listAll()  {
        return data.isEmpty() ? null : data;
    }


    // Destroy
    public T deleteBy(Predicate<T> predicate) {
        T entityToDelete = findOne(predicate);
        if (entityToDelete == null) {
            return null;
        }
        return data.remove(entityToDelete) ? entityToDelete : null;
    }

    public void deleteAll() {
        data.clear();
    }



//    public Pet getPet(int id) throws ResponseException {
//        try (var conn = DatabaseManager.getConnection()) {
//            var statement = "SELECT id, json FROM pet WHERE id=?";
//            try (var ps = conn.prepareStatement(statement)) {
//                ps.setInt(1, id);
//                try (var rs = ps.executeQuery()) {
//                    if (rs.next()) {
//                        return readPet(rs);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
//        }
//        return null;
//    }


}
