/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.curso.service;
import br.com.ifba.curso.entity.Curso;
import br.com.ifba.curso.repository.CursoRepository;
import br.com.ifba.infrastructure.util.StringUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author igo
 */
@Service
@RequiredArgsConstructor
public class CursoService implements CursoIService {

    @Autowired
    CursoRepository cursoRepository;
    
    private static final Logger log = LogManager.getLogger(CursoService.class);
   
    @Override
    public Curso save(Curso curso) {
        log.info("Iniciando salvamento do curso: {}", curso.getNome());
        validarParaSalvar(curso);

        // --- Verificar se já existe curso com mesmo nome ---
        /*Curso existenteNome = cursoRepository.findBynome(curso.getNome());
        if (existenteNome != null) {
            throw new IllegalArgumentException("Já existe um curso com este nome.");
        }*/

        log.info("Curso salvo com sucesso: {}", curso.getNome());
        return cursoRepository.save(curso);
    }

    // ---------------------------------------------------------------
    // ATUALIZAR (Usa a transação read-write da classe)
    // ---------------------------------------------------------------
    @Override
    public Curso update(Curso curso) {
        log.info("validanddo ocurso: ID={}, Nome={} para atualziar", curso.getId(), curso.getNome());
        validarParaAtualizar(curso);

        // --- Verificar se o curso realmente existe ---
        Curso cursoBD = cursoRepository.findByid(curso.getId());
        if (cursoBD == null) {
            log.warn("curso não está presente no banco de daods: ID={}", curso.getId());
            throw new IllegalArgumentException("Curso não encontrado para atualizar.");
            
        }

        // --- Verificar duplicidade de nome ---
        /*Curso existenteNome = cursoRepository.findBynome(curso.getNome());
        if (existenteNome != null && !existenteNome.getId().equals(curso.getId())) {
            throw new IllegalArgumentException("Nome já está em uso por outro curso.");
        }*/
         log.info("o curso: ID={}, Nome={} foi atualziado com sucesso", curso.getId(), curso.getNome());
        return cursoRepository.save(curso);
    }

    // ---------------------------------------------------------------
    // DELETE (Usa a transação read-write da classe)
    // ---------------------------------------------------------------
    @Override
    public void delete(Long id) {
        log.info("verificando se o ID={} é null", id);
        if (id == null){
            log.warn("tentaiva de excluir um id null");
            throw new IllegalArgumentException("ID não pode ser nulo.");
        }
        log.info("curso excluido com suceddo IF{}", id);
        cursoRepository.deleteById(id);
    }

    @Override
    public Curso findById(Long id) {
        log.info("chegando se id é null");
        if (id == null){
            log.warn("id null, não pode excluir um id que não existe");
            throw new IllegalArgumentException("ID não pode ser nulo.");
        }
        return cursoRepository.findByid(id);
    }

    @Override
    public Curso findByNome(String nome) {
        if (StringUtil.isEmpty(nome)) {
            log.warn("nome null");
            throw new IllegalArgumentException("Nome para busca não pode ser vazio.");
        }
        log.info("nome encontrado");
        return cursoRepository.findBynome(nome);
    }

    @Override
    public List<Curso> findAll() {
        log.info("burcando todos os cursos");
        return cursoRepository.findAll();
    }

    // ---------------------------------------------------------------
    // VALIDAÇÕES
    // ---------------------------------------------------------------
    private void validarParaSalvar(Curso curso) {
        log.info("comencando a verificação");
        if (curso == null){
            log.warn("curso retornou null");
            throw new IllegalArgumentException("Curso é nulo.");
        }

        if (StringUtil.isEmpty(curso.getNome())){
            log.warn("curso com nome vazio");
            throw new IllegalArgumentException("Nome do curso é obrigatório.");
        }

        if (StringUtil.isEmpty(curso.getCodigo())){
            log.warn("curso com codigo vazio");
            throw new IllegalArgumentException("Código do curso é obrigatório.");
        }

        if (!StringUtil.hasLength(curso.getCodigo(), 2, 20)){
            log.warn("curso nao antendeu aos paramentros");
            throw new IllegalArgumentException("Código deve ter entre 2 e 20 caracteres.");
        }
        
        log.info("curso {} passou nos parâmetros", curso.getNome());
    }

    private void validarParaAtualizar(Curso curso) {
        log.info("inicianndo para tentar atualizar");
        if (curso == null){
            log.info("curso null");
            throw new IllegalArgumentException("Curso é nulo.");
        }
        if (curso.getId() == null) {
            log.info("curso com id null");
            throw new IllegalArgumentException("ID é obrigatório para atualizar.");
        }
        log.info("testando a validacao para salvar");
        validarParaSalvar(curso); // reaproveitando as regras
    }
}
