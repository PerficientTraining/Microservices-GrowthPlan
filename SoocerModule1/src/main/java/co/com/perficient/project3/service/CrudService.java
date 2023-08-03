package co.com.perficient.project3.service;

import java.util.List;
import java.util.Optional;

public interface CrudService<T, I> {
    T create(T t);

    List<T> findAll();

    Optional<T> findById(I id);

    T update(T tOld, T tNew);

    void delete(I id);
}
