/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.ifba.curso.repository;

import br.com.ifba.curso.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author igo
 */
public interface CursoRepository extends JpaRepository <Curso, Long>{
    public Curso findByid(Long ir);
    public Curso findBynome(String nome);
}
