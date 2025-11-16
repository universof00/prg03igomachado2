/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.curso.dao;

import br.com.ifba.curso.entity.Curso;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

/**
 *
 * @author igo
 */
public class CursoDao implements CursoIDao {
    private final static EntityManagerFactory EMF = 
            Persistence.createEntityManagerFactory("poobanco2"); 
    
    private EntityManager getEntityManager() {
        return EMF.createEntityManager();
    }
    
    
    @Override
    public Curso save(Curso curso) { 
        EntityManager em = getEntityManager();
        Curso courseSaved = null;
        
        try {
            em.getTransaction().begin();
            if (curso.getId() == null) {
                //Salvando
                em.persist(curso);
                courseSaved = curso;
            } else {
                //atualizando
                courseSaved = em.merge(curso);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error saving/updating course.", e);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
        return courseSaved;
    }
    
    @Override
    public Curso findById(Long id) { 
        EntityManager em = getEntityManager();
        try {
            return em.find(Curso.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error finding course by ID.", e);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }
    
    @Override
    public List<Curso> findAll() { // Renomeado para findAll
        EntityManager em = getEntityManager();
        try {
            
            return em.createQuery("SELECT c FROM Curso c", Curso.class).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error finding all courses.", e);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }
    
    @Override
    public void delete(Long id) { 
        EntityManager em = getEntityManager();
        
        try {
            em.getTransaction().begin();
            Curso curso = em.find(Curso.class, id); 
            
            if (curso != null) {
                em.remove(curso);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error deleting course.", e);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }
}
