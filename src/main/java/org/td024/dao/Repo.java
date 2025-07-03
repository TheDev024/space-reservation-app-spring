package org.td024.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.td024.entity.IEntity;

import java.util.List;
import java.util.Optional;

public abstract class Repo<T extends IEntity> {
    private final Class<T> clazz;

    @PersistenceContext
    protected EntityManager entityManager;

    public Repo(Class<T> clazz) {
        this.clazz = clazz;
    }

    protected int getLastId() {
        String query = "SELECT MAX(id) FROM " + clazz.getSimpleName();
        return entityManager.createQuery(query).getFirstResult();
    }

    public Optional<T> getById(int id) {
        T entity = entityManager.find(clazz, id);
        return Optional.ofNullable(entity);
    }

    public List<T> getAll() {
        String query = "SELECT e FROM " + clazz.getSimpleName() + " e ORDER BY e.id";
        List<T> entities = entityManager.createQuery(query, clazz).getResultList();
        return entities == null ? List.of() : entities;
    }

    /**
     * If id is 0, create a new entity, otherwise update an existing entity
     *
     * @return id of the created/updated entity, if not successful, -1
     */
    @Transactional
    public int save(T entity) {
        if (entity.getId() == 0) entityManager.persist(entity);
        else {
            entityManager.merge(entity);
            return entity.getId();
        }
        return getLastId();
    }

    /**
     * @return if delete is successful, true, otherwise, false
     */
    @Transactional
    public boolean delete(int id) {
        boolean result = false;
        T entity = entityManager.find(clazz, id);

        if (entity != null) {
            entityManager.remove(entity);
            result = true;
        }

        return result;
    }
}
