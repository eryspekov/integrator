package kg.infocom.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by eryspekov on 16.08.16.
 */
@Repository
@Transactional
public class AbstractDaoImpl<T> implements AbstractDao<T> {

    private Class<T> type;

    @Autowired
    protected HibernateTemplate ht;

    public AbstractDaoImpl(Class<T> type) { this.type = type; }

//    @Transactional
    public T getById(Integer id) { return ht.get(type, id); }

    @Transactional
    public List<?> getByNamedParam(String query, String param, T value) {
        return ht.findByNamedParam(query, param, value);
    }

    @Transactional
    public List<?> getByNamedParam(String query, String param[], T value[]) {
//        System.out.println("param:"+param[0]+";"+param[1]);
//        System.out.println("value:"+value[0]+";"+value[1]);
        return ht.findByNamedParam(query, param, value);
    }

//    @Transactional
    public List<T> findAll() {
        return ht.loadAll(type);
    }

    @Transactional
    public T create(T entity) {
        ht.save(entity);
        return entity;
    }

    @Transactional
    public T update(T entity) {
        ht.getSessionFactory().getCurrentSession().clear();
        ht.saveOrUpdate(entity);
        return entity;
    }

    @Transactional
    public void delete(T entity) {
        ht.delete(entity);
    }
}
