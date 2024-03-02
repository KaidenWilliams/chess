package dataAccess.Memory;

import java.util.List;
import java.util.function.Predicate;

public interface IGeneralMemoryDAO<T> {

    // Create
    T create(T entity);

    // Read
    List<T> findAll(Predicate<T> predicate);
    T findOne(Predicate<T> predicate);

    List<T> listAll();

    //Update
    T update(T entityNew, Predicate<T> predicate);
    void updateFields(T entityNew, T entityOld);

    // Destroy
    T delete(Predicate<T> predicate);
    void deleteAll();
}
