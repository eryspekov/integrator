package kg.infocom.dao;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by eryspekov on 16.08.16.
 */
public interface AbstractDao<T> {

    T getById(Integer id);
    List<?> getByNamedParam(String query, String param, T value);
    List<T> findAll();
    T create(T entity);
    T update(T entity);
    void delete(T entity);
}
