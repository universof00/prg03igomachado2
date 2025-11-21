/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.curso.controller;

/**
 *
 * @author igo
 */
public class CursoController implements CursoIController {
    private final CursoIService service = new CursoService();

    @Override
    public List<Curso> findAll() {
        return service.findAll();
    }

    @Override
    public Curso save(Curso curso) {
        return service.save(curso);
    }

    @Override
    public Curso update(Curso curso) {
        return service.update(curso);
    }

    @Override
    public void delete(Long id) {
        service.delete(id);
    }

    @Override
    public Curso findById(Long id) {
        return service.findById(id);
    }
}
