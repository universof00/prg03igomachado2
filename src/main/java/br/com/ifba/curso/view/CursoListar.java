/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package br.com.ifba.curso.view;

import br.com.ifba.curso.controller.CursoIController;
import br.com.ifba.curso.entity.Curso;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;      // Necessário para o DocumentListener
import javax.swing.event.DocumentListener;   // Necessário para o DocumentListener
import javax.swing.RowFilter;                // Necessário para RowFilter.regexFilter
import javax.swing.table.TableRowSorter;     // Necessário para o sorter
import javax.swing.table.DefaultTableModel;  // Necessário para tipar o modelo
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 *
 * @author igo
 */
@Component
public class CursoListar extends javax.swing.JFrame {
    
    /** Controlador para ordenação e filtragem da JTable. */
    TableRowSorter<DefaultTableModel> sorter;

    /**
     * Creates new form CursoListar
     */
    @Autowired
    private  CursoIController cursoIController;
  
    @Autowired
    private NovoCurso novoCursoFrame;
    
    private Curso cursoSelecionado;
    
    @Autowired
    private ApplicationContext applicationContext;
    
    public CursoListar(CursoIController cursoIController, ApplicationContext applicationContext) {
        this.cursoIController = cursoIController;
        this.applicationContext = applicationContext;
        initComponents();
        carregarCursos();
        
        DefaultTableModel modelo = (DefaultTableModel) jtListaDeCursos.getModel();
       
        sorter = new TableRowSorter<>(modelo);
        jtListaDeCursos.setRowSorter(sorter); // Ativa ordenação automática na tabela
        anexarListenerBusca();
        
         btnEditar.setVisible(false);
         btnApagar.setVisible(false);
         
         /**
         * Listener para detectar seleção de linhas na tabela.
         * Faz com que os botões "Editar" e "Apagar" só apareçam se existir seleção.
         */
        jtListaDeCursos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) { 
                
                // Lógica de verificação embutida
                int linhaSelecionadaView = jtListaDeCursos.getSelectedRow();
                boolean linhaEhSelecionada = (linhaSelecionadaView >= 0);
                
                // Define a visibilidade
                btnEditar.setVisible(linhaEhSelecionada);
                btnApagar.setVisible(linhaEhSelecionada);
            }
        }
    });
     
    }

     /**
     * Carrega a lista de cursos do banco de dados e preenche a JTable.
     * Utiliza JPA para acessar o banco.
     */
    void carregarCursos() {

        List<Curso> cursos = cursoIController.findAll();
        DefaultTableModel model = (DefaultTableModel) jtListaDeCursos.getModel();
        model.setRowCount(0);
        for (Curso curso : cursos) {
            model.addRow(new Object[]{
                curso.getId(),
                curso.getCodigo(),
                curso.getNome()
            });
        }
    }
    
       /**
     * Adiciona um novo curso como uma linha na tabela.
     * @param novoCurso objeto Curso contendo os dados do curso criado.
     */
    
    public void adicionarLinhaNaTabela(Curso novoCurso) {
       
        DefaultTableModel modelo = (DefaultTableModel) jtListaDeCursos.getModel(); 
        // Adiciona cada curso como linha da tabela
        modelo.addRow(new Object[]{
            novoCurso.getId(),
            novoCurso.getCodigo(), 
            novoCurso.getNome()
        });
    }
    
     /**
     * Adiciona um DocumentListener ao campo de busca para filtrar a tabela
     * com atraso de 300ms (debounce), evitando múltiplas filtragens consecutivas.
     */
    private void anexarListenerBusca() {
          
            Timer filtroTimer = new Timer(300, new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    
                    filtrarTabela();
                }
            });
            
        filtroTimer.setRepeats(false);
        
        txtbuscar.getDocument().addDocumentListener(new DocumentListener() {

            private void reiniciarTimer() {
               
                if (filtroTimer.isRunning()) {
                    filtroTimer.restart();
                } else {
                    filtroTimer.start();
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                reiniciarTimer();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                reiniciarTimer();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                reiniciarTimer();
            }
        });
    }
       /**
     * Filtra os cursos da tabela com base no texto digitado pelo usuário.
     * Usa expressão regular (case-insensitive).
     */
    private void filtrarTabela() {
        String texto = txtbuscar.getText().trim();
        // ... lógica de RowFilter usando o sorter
        if (texto.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            try {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
            } catch (java.util.regex.PatternSyntaxException e) {
                sorter.setRowFilter(null);
            }
        }
    }
    
     /**
     * Obtém o curso selecionado na JTable.
     * @return Curso selecionado ou null se nenhuma linha estiver selecionada.
     */
    
    public Curso getCursoSelecionado() {
        int linhaSelecionada = jtListaDeCursos.getSelectedRow();
        if (linhaSelecionada >= 0) {
            Long id = (Long) jtListaDeCursos.getValueAt(linhaSelecionada, 0);
            String codigo = (String) jtListaDeCursos.getValueAt(linhaSelecionada, 1);
            String nome = (String) jtListaDeCursos.getValueAt(linhaSelecionada, 2);
            Curso cursoSelecionado = new Curso();
            cursoSelecionado.setId(id);
            cursoSelecionado.setCodigo(codigo);
            cursoSelecionado.setNome(nome);
            return cursoSelecionado;
        } else {
            return null;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtbuscar = new javax.swing.JTextField();
        btnAdicionar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtListaDeCursos = new javax.swing.JTable();
        btnEditar = new javax.swing.JButton();
        btnApagar = new javax.swing.JButton();
        lblTextoPesquisar = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtbuscar.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N

        btnAdicionar.setFont(new java.awt.Font("Liberation Sans", 0, 36)); // NOI18N
        btnAdicionar.setText("+");
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });

        jtListaDeCursos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Codigo Curso", "Nome Curso"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jtListaDeCursos);

        btnEditar.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnEditar.setText("Editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnApagar.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnApagar.setText("Apagar");
        btnApagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApagarActionPerformed(evt);
            }
        });

        lblTextoPesquisar.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblTextoPesquisar.setText("Pesquisar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTextoPesquisar)
                    .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(70, 70, 70)
                .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(btnApagar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 780, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblTextoPesquisar)
                        .addGap(11, 11, 11)
                        .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnApagar, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(28, 28, 28)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
     
        this.novoCursoFrame.setTelaPrincipal(this); 
        this.novoCursoFrame.setVisible(true);
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        // TODO add your handling code here:
       Curso cursoParaAtualizar = getCursoSelecionado();
    
        if (cursoParaAtualizar == null) {
            JOptionPane.showMessageDialog(this, "Nenhum curso foi selecionado para editar.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Chamada ao Controller para verificar a existência (baseado no seu código original)
        Curso validarAtualizar = cursoIController.findById(cursoParaAtualizar.getId());

        if (validarAtualizar != null) {

            // 1. OBTENÇÃO CORRETA DA TELA PELO SPRING
            // Isso garante que o CursoIController (cursoISalvar) seja injetado.
            CursoAtualizar telaAtualizar = applicationContext.getBean(CursoAtualizar.class);

            // 2. CHAMADA DOS SETTERS: Aqui você passa os dados e a referência da tela principal
            telaAtualizar.setCursoSelecionado(validarAtualizar); 
            telaAtualizar.setTelaPrincipal(this);              

            // 3. Torna a tela visível
            telaAtualizar.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(this, "O curso selecionado não foi encontrado no banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnApagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApagarActionPerformed
       Curso cursoParaDeletar = getCursoSelecionado();
    
        if (cursoParaDeletar == null) {
            JOptionPane.showMessageDialog(this, "Nenhum curso foi selecionado para apagar.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                this, 
                "AVISO! O CURSO SERÁ APAGADO!\nTem certeza que deseja continuar?",
                "Apagar Curso",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                // 1. CHAMA O CONTROLLER INJETADO para realizar a operação.
                // A verificação de existência será feita implicitamente no Service/DAO.
                cursoIController.delete(cursoParaDeletar.getId()); 

                JOptionPane.showMessageDialog(this,
                    "Curso '" + cursoParaDeletar.getNome() + "' apagado com sucesso!");

                // 2. Atualiza a lista
                carregarCursos();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erro ao apagar curso: " + e.getMessage() + "\nO ID pode não existir ou há restrições de chave estrangeira.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);

                e.printStackTrace(); // para debug no console
            }
        }
    }//GEN-LAST:event_btnApagarActionPerformed

    /**
     * @param args the command line arguments
     */
  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnApagar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtListaDeCursos;
    private javax.swing.JLabel lblTextoPesquisar;
    private javax.swing.JTextField txtbuscar;
    // End of variables declaration//GEN-END:variables
}
