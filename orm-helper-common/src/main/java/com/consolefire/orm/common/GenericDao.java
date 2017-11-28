package com.consolefire.orm.common;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * @author sabuj.das
 */
public interface GenericDao<E, I extends Serializable> {

    /**
     * The data type of the Entity
     *
     * @return
     */
    Class<E> getEntityType();

    /**
     * Gets the @Id or @IdClass field name
     *
     * @return
     */
    String getIdentityFieldName();

    /**
     * Find by ID
     *
     * @param id
     * @return
     */
    E find(I id);

    /**
     * Find by properties/parameters
     *
     * @param parameters
     * @return
     */
    Collection<E> find(Map<String, Object> parameters);

    /**
     * Find by properties/parameters
     *
     * @param properties
     * @return
     */
    Collection<E> find(Properties properties);

    /**
     * Find all items in the table. It can have a performance issue
     */
    Collection<E> findAll();

    /**
     * Find all items in the table for the mentioned IDs in the list.
     *
     * @param list
     * @return
     */
    Collection<E> findAll(List<I> list);

    Collection<E> findByNamedQuery(String namedQuery, Map<String, Object> parameters);
    
    E findUniqueByNamedQuery(String namedQuery, Map<String, Object> parameters);
    
    /**
     * Count the rows of current entity. it is similar to "select count(*) from T"
     *
     * @return
     */
    long count();

    /**
     * Count using properties. Like "select count(*) from T where ..."
     *
     * @param properties
     * @return
     */
    long count(Map<String, Object> properties);

    /**
     * Insert entity
     *
     * @param entity
     * @return
     */
    E save(E entity);

    /**
     * Update entity
     *
     * @param entity
     * @return
     */
    E update(E entity);

    /**
     * If present, then update, else insert
     *
     * @param entity
     * @return
     */
    E saveOrUpdate(E entity);

    /**
     * Inser or Update collection
     *
     * @param entities
     * @return
     */
    int saveOrUpdate(List<E> entities);

    /**
     * Delete the entity by ID
     *
     * @param id
     */
    void delete(I id);

    /**
     * Delete the Entity
     *
     * @param entity
     */
    void delete(E entity);

}
