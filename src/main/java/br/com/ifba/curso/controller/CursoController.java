/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.curso.controller;

import br.com.ifba.curso.entity.Curso;
import br.com.ifba.curso.service.CursoIService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author igo
 */

@RestController
public class CursoController implements CursoIController {
    
    @Autowired
    private CursoIService cursoIservice;

    @Override
    public List<Curso> findAll() {
        return cursoIservice.findAll();
    }

    @Override
    public Curso save(Curso curso) {
        return cursoIservice.save(curso);
    }

    @Override
    public Curso update(Curso curso) {
        return cursoIservice.update(curso);
    }

    @Override
    public void delete(Long id) {
        cursoIservice.delete(id);
    }

    @Override
    public Curso findById(Long id) {
        return cursoIservice.findById(id);
    }
    
   @Override
    public Curso findByNome(String nome) {
        return cursoIservice.findByNome(nome); // <-- DELEGA PARA O SERVICE
    }
}
