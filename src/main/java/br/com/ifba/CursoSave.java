/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba;

import br.com.ifba.curso.entity.Curso;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;



/**
 *
 * @author igo
 */
public class CursoSave {
   private final static EntityManagerFactory emf = 
            Persistence.createEntityManagerFactory("poobanco2"); 

    public void save(Curso curso) { 
        
        EntityManager em = emf.createEntityManager();
        
        try {
            em.getTransaction().begin(); 
            // Usa em.persist(curso) para save o objeto no banco de dados
            em.persist(curso);
            em.getTransaction().commit();
            System.out.println("Curso salvo com sucesso! ID: " + curso.getId());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao salvar curso.", e);
        } finally {
            if (em.isOpen()) {
                em.close(); 
            }
        }
    }
   
}
