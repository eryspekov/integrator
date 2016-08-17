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
        return ht.loadAll(type);
    }

    public T create(T entity) {
        ht.save(entity);
        return entity;
    }

    public T update(T entity) {
        ht.getSessionFactory().getCurrentSession().clear();
        ht.saveOrUpdate(entity);
        return entity;
    }

    public void delete(T entity) {
        ht.delete(entity);
    }
}
