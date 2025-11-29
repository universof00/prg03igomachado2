/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.infrastructure.util;

/**
 *
 * @author igo
 */
public class StringUtil {
   /**
     * Verifica se uma String é nula, vazia, ou contém apenas espaços em branco.
     * Este é um método fundamental para validações de campos obrigatórios.
     * * @param value A String a ser verificada.
     * @return true se a String for null, ou se for uma String vazia ("")
     * após remover espaços em branco iniciais/finais (" ".trim() resulta em "").
     */
    public static boolean isEmpty(String value) {
        // Verifica se a referência ao objeto é nula.
        return value == null 
            // OU
            // Chama trim() para remover espaços iniciais e finais, 
            // e verifica se o resultado é uma String vazia.
            || value.trim().isEmpty();
    }

    /**
     * Verifica se uma String, após remover espaços em branco iniciais/finais,
     * tem um comprimento (tamanho) que está dentro de um intervalo específico (mínimo e máximo).
     * * @param value A String a ser verificada.
     * @param min O comprimento mínimo permitido (inclusivo).
     * @param max O comprimento máximo permitido (inclusivo).
     * @return true se o comprimento da String estiver entre min e max (ambos inclusivos),
     * e false se a String for null.
     */
    public static boolean hasLength(String value, int min, int max) {
        // Se a String for nula, ela não tem um comprimento válido.
        if (value == null) return false;
        
        // Calcula o comprimento da String após remover espaços em branco.
        int len = value.trim().length();
        
        // Verifica se o comprimento está dentro do intervalo [min, max].
        return len >= min && len <= max;
    }
}
