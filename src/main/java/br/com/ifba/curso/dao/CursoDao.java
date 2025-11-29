/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.curso.dao;

import br.com.ifba.curso.entity.Curso;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author igo
 */
@Repository
public class CursoDao implements CursoIDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Curso save(Curso curso) {
        em.persist(curso);
        return curso;
    }

    @Override
    public Curso update(Curso curso) {
        return em.merge(curso);
    }

    @Override
    public void delete(Long id) {
        Curso curso = em.find(Curso.class, id);
        if (curso != null) {
            em.remove(curso);
        }
    }

    @Override
    public Curso findById(Long id) {
        return em.find(Curso.class, id);
    }

    @Override
    public List<Curso> findAll() {
        return em.createQuery("SELECT c FROM Curso c", Curso.class)
                 .getResultList();
    }

    @Override
    public Curso findByNome(String nome) {
        List<Curso> lista = em.createQuery(
                "SELECT c FROM Curso c WHERE c.nome = :nome", Curso.class)
                .setParameter("nome", nome)
                .getResultList();

        return lista.isEmpty() ? null : lista.get(0);
    }
}
