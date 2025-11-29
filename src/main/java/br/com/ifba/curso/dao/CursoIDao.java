/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.ifba.curso.dao;

import br.com.ifba.curso.entity.Curso;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 *
 * @author igo
 */
@Component

public interface CursoIDao {
  Curso save(Curso curso);

    Curso update(Curso curso);

    void delete(Long id);

    Curso findById(Long id);

    List<Curso> findAll();

    Curso findByNome(String nome);
}
