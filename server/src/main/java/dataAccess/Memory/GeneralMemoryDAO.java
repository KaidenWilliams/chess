package dataAccess.Memory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


abstract class GeneralMemoryDAO<T> {

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


}
