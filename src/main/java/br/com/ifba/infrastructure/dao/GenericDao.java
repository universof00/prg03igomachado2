/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.infrastructure.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

/**
 *
 * @author igo
 */
public abstract class GenericDao<T> implements GenericIDao<T> {
      @PersistenceContext
    protected EntityManager em;

    protected abstract Class<T> getEntityClass();

    @Override
    @Transactional
    public T save(T entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    @Transactional
    public T update(T entity) {
        return em.merge(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        T entity = em.find(getEntityClass(), id);
        if (entity != null) {
            em.remove(entity);
        }
    }

    @Override
    public T findById(Long id) {
        return em.find(getEntityClass(), id);
    }

    @Override
    public List<T> findAll() {
        return em.createQuery(
                "FROM " + getEntityClass().getSimpleName(),
                getEntityClass()
            )
            .getResultList();
    }
}
