/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.curso.service;

import br.com.ifba.curso.dao.CursoDao;
import br.com.ifba.curso.dao.CursoIDao;
import br.com.ifba.curso.entity.Curso;
import br.com.ifba.infrastructure.util.StringUtil;
import java.util.List;

/**
 *
 * @author igo
 */
public class CursoService implements CursoIService {
    private final CursoIDao dao = new CursoDao();

    @Override
    public Curso save(Curso curso) {
        validarParaSalvar(curso);
        return dao.save(curso);
    }

    @Override
    public Curso update(Curso curso) {
        validarParaAtualizar(curso);
        //return dao.update(curso);
        return curso;
    }

    @Override
    public void delete(Long id) {
        dao.delete(id);
    }

    @Override
    public Curso findById(Long id) {
        return dao.findById(id);
    }

    @Override
    public List<Curso> findAll() {
        return dao.findAll();
    }

    private void validarParaSalvar(Curso curso) {
        if (curso == null) throw new IllegalArgumentException("Curso é nulo");
        if (StringUtil.isEmpty(curso.getNome())) throw new IllegalArgumentException("Nome é obrigatório");
        if (StringUtil.isEmpty(curso.getCodigo())) throw new IllegalArgumentException("Código é obrigatório");
        // Exemplo: checar tamanho
        if (!StringUtil.hasLength(curso.getCodigo(), 2, 20))
            throw new IllegalArgumentException("Código deve ter entre 2 e 20 caracteres");
    }

    private void validarParaAtualizar(Curso curso) {
        if (curso == null) throw new IllegalArgumentException("Curso é nulo");
        if (curso.getId() == null) throw new IllegalArgumentException("ID é obrigatório para atualizar");
    } 
}
