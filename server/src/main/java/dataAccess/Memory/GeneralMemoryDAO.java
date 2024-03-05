package dataAccess.Memory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// TODO error throwing

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

    //Update TODO needs work, will figure it our
    public T update(T entityNew, Predicate<T> predicate) {
        T entityOld = findOne(predicate);
        if (entityOld == null) {
            return null;
        }
        updateFields(entityNew, entityOld);
        return entityOld;
    }

    // Uses reflection to loop through every field,
    // - I didn't want to use a copy constructor or mapper
    public void updateFields(T entityNew, T entityOld) {

        Class<?> newClass = entityNew.getClass();

        for (Field field : newClass.getDeclaredFields()) {
            field.setAccessible(true);

            try {
                Object newValue = field.get(entityNew);
                Object oldValue = field.get(entityOld);

                if (!Objects.equals(oldValue, newValue)) {
                    field.set(entityOld, newValue);
                }

            // Should hopefully never throw if my code is set up correctly
            } catch (IllegalAccessException e) {
//                throw new DataAccessException("Could not Update");
            }
        }
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
