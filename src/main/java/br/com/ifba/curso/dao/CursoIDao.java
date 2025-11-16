/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.ifba.curso.dao;

import br.com.ifba.curso.entity.Curso;
import java.util.List;

/**
 *
 * @author igo
 */
public interface CursoIDao {
    // Save or Update (Salvar ou Atualizar)
    Curso save(Curso curso); 
    
    // Find by ID (Buscar por ID)
    Curso findById(Long id); 
    
    // Find All (Buscar Todos)
    List<Curso> findAll();
    
    // Delete (Deletar)
    void delete(Long id);
}
