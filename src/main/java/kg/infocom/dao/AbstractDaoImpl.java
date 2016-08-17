package kg.infocom.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by eryspekov on 16.08.16.
 */
@Transactional
public class AbstractDaoImpl<T> implements AbstractDao<T> {

    private Class<T> type;

    @Autowired
    protected HibernateTemplate ht;

    public AbstractDaoImpl(Class<T> type) { this.type = type; }

    public T getById(Integer id) { return ht.get(type, id); }

    public List<T> findAll() {
        return null;
    }

    public T create(T entity) {
        return null;
    }

    public T update(T entity) {
        return null;
    }

    public void delete(T entity) {

    }
}
