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
        data.add(entity);
        return entity;
    }

    // Read
    public List<T> findAll(Predicate<T> predicate) {
        return data.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public T findOne(Predicate<T> predicate) {
        return data.stream()
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }

    public List<T> listAll()  {
        return data;
    }

    //Update TODO needs work, will figure it our
    public T update(T entityNew, Predicate<T> predicate) {
        T entityOld = findOne(predicate);
        if (entityOld != null) {
            updateFields(entityNew, entityOld);
        }
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
        if (entityToDelete != null) {
            data.remove(entityToDelete);
        }
        return entityToDelete;
    }

    public void deleteAll() {
        data.clear();
    }


}
