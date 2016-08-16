package kg.infocom.dao;

import java.util.List;

/**
 * Created by eryspekov on 16.08.16.
 */
public interface AbstractDao<T> {

    T getById(Integer id);
    List<T> findAll();
    T create(T entity);
    T update(T entity);
    void delete(T entity);
}
