/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba;

import br.com.ifba.curso.view.CursoListar;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;


/**
 *
 * @author igo
 */
@SpringBootApplication
public class Prg03IgoAplicatrion {
   public static void main(String[] args){
      ConfigurableApplicationContext context = new SpringApplicationBuilder(Prg03IgoAplicatrion.class)
              .headless(false).run(args); 
      CursoListar novaTela = context.getBean(CursoListar.class);
      novaTela.setVisible(true);
   }
}