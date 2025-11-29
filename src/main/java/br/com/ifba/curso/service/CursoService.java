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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author igo
 */
@Service
@Transactional
public class CursoService implements CursoIService {
   
    @Autowired
    private CursoIDao dao;

   
    @Override
    public Curso save(Curso curso) {
        validarParaSalvar(curso);

        // --- Verificar se já existe curso com mesmo nome ---
        Curso existenteNome = dao.findByNome(curso.getNome());
        if (existenteNome != null) {
            throw new IllegalArgumentException("Já existe um curso com este nome.");
        }

        return dao.save(curso);
    }

    // ---------------------------------------------------------------
    // ATUALIZAR (Usa a transação read-write da classe)
    // ---------------------------------------------------------------
    @Override
    public Curso update(Curso curso) {
        validarParaAtualizar(curso);

        // --- Verificar se o curso realmente existe ---
        Curso cursoBD = dao.findById(curso.getId());
        if (cursoBD == null) {
            throw new IllegalArgumentException("Curso não encontrado para atualizar.");
        }

        // --- Verificar duplicidade de nome ---
        Curso existenteNome = dao.findByNome(curso.getNome());
        if (existenteNome != null && !existenteNome.getId().equals(curso.getId())) {
            throw new IllegalArgumentException("Nome já está em uso por outro curso.");
        }

        return dao.update(curso);
    }

    // ---------------------------------------------------------------
    // DELETE (Usa a transação read-write da classe)
    // ---------------------------------------------------------------
    @Override
    public void delete(Long id) {
        if (id == null) throw new IllegalArgumentException("ID não pode ser nulo.");
        dao.delete(id);
    }

    // ---------------------------------------------------------------
    // FIND BY ID (Anotado como readOnly = true para otimização)
    // ---------------------------------------------------------------
    @Transactional(readOnly = true)
    @Override
    public Curso findById(Long id) {
        if (id == null) throw new IllegalArgumentException("ID não pode ser nulo.");
        return dao.findById(id);
    }
    
    // FIND BY NOME (Anotado como readOnly = true para otimização)
    @Transactional(readOnly = true)
    @Override
    public Curso findByNome(String nome) {
        if (StringUtil.isEmpty(nome)) {
            throw new IllegalArgumentException("Nome para busca não pode ser vazio.");
        }
        return dao.findByNome(nome);
    }

    // ---------------------------------------------------------------
    // FIND ALL (Anotado como readOnly = true para otimização)
    // ---------------------------------------------------------------
    @Transactional(readOnly = true)
    @Override
    public List<Curso> findAll() {
        return dao.findAll();
    }

    // ---------------------------------------------------------------
    // VALIDAÇÕES
    // ---------------------------------------------------------------
    private void validarParaSalvar(Curso curso) {
        if (curso == null) throw new IllegalArgumentException("Curso é nulo.");

        if (StringUtil.isEmpty(curso.getNome()))
            throw new IllegalArgumentException("Nome do curso é obrigatório.");

        if (StringUtil.isEmpty(curso.getCodigo()))
            throw new IllegalArgumentException("Código do curso é obrigatório.");

        if (!StringUtil.hasLength(curso.getCodigo(), 2, 20))
            throw new IllegalArgumentException("Código deve ter entre 2 e 20 caracteres.");
    }

    private void validarParaAtualizar(Curso curso) {
        if (curso == null) throw new IllegalArgumentException("Curso é nulo.");
        if (curso.getId() == null) throw new IllegalArgumentException("ID é obrigatório para atualizar.");
        validarParaSalvar(curso); // reaproveitando as regras
    }
}
