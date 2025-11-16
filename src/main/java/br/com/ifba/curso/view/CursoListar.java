/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package br.com.ifba.curso.view;

import br.com.ifba.curso.dao.CursoDao;
import br.com.ifba.curso.dao.CursoIDao;
import br.com.ifba.curso.entity.Curso;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;      // Necessário para o DocumentListener
import javax.swing.event.DocumentListener;   // Necessário para o DocumentListener
import javax.swing.RowFilter;                // Necessário para RowFilter.regexFilter
import javax.swing.table.TableRowSorter;     // Necessário para o sorter
import javax.swing.table.DefaultTableModel;  // Necessário para tipar o modelo
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author igo
 */
public class CursoListar extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CursoListar.class.getName());
    /** Controlador para ordenação e filtragem da JTable. */
    TableRowSorter<DefaultTableModel> sorter;

    /**
     * Creates new form CursoListar
     */
    private Curso cursoSelecionado;
    public CursoListar() {
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
   
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("poobanco2"); 
        EntityManager em = emf.createEntityManager();

        List<Curso> cursos = new ArrayList<Curso>();

        try {
            cursos = em.createQuery("SELECT c FROM Curso c", Curso.class).getResultList();

            DefaultTableModel model = (DefaultTableModel) jtListaDeCursos.getModel();

            model.setRowCount(0); 

            for (Curso curso : cursos) {
                model.addRow(new Object[]{
                    curso.getId(),
                    curso.getCodigo(), 
                    curso.getNome()
                });
            }

        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Erro ao carregar cursos", e);
            javax.swing.JOptionPane.showMessageDialog(this, "Erro ao carregar os dados dos cursos: " + e.getMessage(), "Erro de Banco de Dados", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
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
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtbuscar.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        getContentPane().add(txtbuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, 180, -1));

        btnAdicionar.setFont(new java.awt.Font("Liberation Sans", 0, 36)); // NOI18N
        btnAdicionar.setText("+");
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });
        getContentPane().add(btnAdicionar, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 40, 190, 80));

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

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 148, 780, 286));

        btnEditar.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnEditar.setText("Editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });
        getContentPane().add(btnEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 60, 110, 60));

        btnApagar.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnApagar.setText("Apagar");
        btnApagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApagarActionPerformed(evt);
            }
        });
        getContentPane().add(btnApagar, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 60, 110, 60));

        lblTextoPesquisar.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblTextoPesquisar.setText("Pesquisar");
        getContentPane().add(lblTextoPesquisar, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        // TODO add your handling code here:
         NovoCurso telaCadastro = new NovoCurso(this); 
         telaCadastro.setVisible(true);
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        // TODO add your handling code here:
        
        Curso cursoParaAtualizar = getCursoSelecionado();
        CursoIDao cursoAtualizar = new CursoDao();
        //para verificar se é null ou não
        Curso validarAtualizar = new Curso();
    
        if (cursoParaAtualizar == null) {
            JOptionPane.showMessageDialog(this, "Nenhum curso foi selecionado para apagar.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        validarAtualizar = cursoAtualizar.findById(cursoParaAtualizar.getId());
        /**
         * se for diferente de null chama a tela para editar e atualizar sabendo o objeto
         * tem no banco de dados
         */
        
        if(validarAtualizar != null){
            CursoAtualizar telaAtualizar = new CursoAtualizar(this); 
            telaAtualizar.setVisible(true);  
        }
        
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnApagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApagarActionPerformed
        // TODO add your handling code here:
        Curso cursoParaDeletar = getCursoSelecionado();
        CursoIDao cursoApagar = new CursoDao();
        //para verificar se é null ou não
        Curso validarApagar = new Curso();
    
        if (cursoParaDeletar == null) {
            JOptionPane.showMessageDialog(this, "Nenhum curso foi selecionado para apagar.", "Erro", JOptionPane.ERROR_MESSAGE);
            return; // Sai do método se for nulo
        }
    
        int confirmacao = JOptionPane.showConfirmDialog(
                    null,
                    "AVISO! O CURSO SERÁ APAGAGO!\nTem certeza que deseja continuar?",
                    "Apagar Curso",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

        if (confirmacao == JOptionPane.YES_OPTION) {
            
            try {
                //busca no banco para ver se realmente tem, caso tenha no if
                validarApagar = cursoApagar.findById(cursoParaDeletar.getId());

                if (validarApagar != null) {

                    cursoApagar.delete(cursoParaDeletar.getId());

                    JOptionPane.showMessageDialog(this,
                        "Curso '" + cursoParaDeletar.getNome() + "' apagado com sucesso!");

                    carregarCursos();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Curso não encontrado no banco de dados.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception e) {

                JOptionPane.showMessageDialog(this,
                    "Erro ao apagar curso: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);

                e.printStackTrace(); // para debug no console
            }
        
        }
    }//GEN-LAST:event_btnApagarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new CursoListar().setVisible(true));
    }

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
