/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genesis.vistas;

import com.genesis.model.conexion;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableColumn;
/**
 *
 * @author User
 */
public class w_buscar extends javax.swing.JInternalFrame{
    
    private boolean DEBUG = false;
    private boolean ALLOW_COLUMN_SELECTION = false;
    private boolean ALLOW_ROW_SELECTION = true;
    public String sql = "";
    public String ps_tabla = "";
    public String fields = "";
    public String conditions="";
    public String ps_condicion = "";
    public String ps_retorno = "";
    private JTextField jtfbuscar;
    private Object[][] datos; //Para el cuerpo de la tabla
    private final String[] cabecera = {"CODIGO", "DESCRIPCION"}; //Para la cabercera de las columnas
    private DefaultTableModel dtm; //Modelo de datos para la tabla
    private JTable tabla; //La tabla a ser mostrada
    private JScrollPane scrollPane; //para poder usar scroll
    private JLabel lblbuscar;
    public ResultSet rs;
    public String is_sql = "";
    public int ii_row = 0;
   private javax.swing.JButton jbtnAcept;
    private javax.swing.JButton jbtnCancel;
    public javax.swing.JTextField codigo;
    
    
    public w_buscar(String sql, javax.swing.JTextField tf_codigo) {
        initComponents();
        this.is_sql = sql;
        codigo = tf_codigo;
        setTitle("Buscar ...");
        getContentPane().setLayout(null);
        setIconifiable(true);
        setClosable(true);
        setBounds(120, 120, 410, 210);
        initmyComponents();
      
        jtfbuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfbuscarKeyPressed(evt);
            }
        });
        getContentPane().add(lblbuscar);
        getContentPane().add(jtfbuscar);
        tabla.setPreferredScrollableViewportSize(new Dimension(360, 90));
        tabla.setFillsViewportHeight(true);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        if (ALLOW_ROW_SELECTION) { // true by default
            ListSelectionModel rowSM = tabla.getSelectionModel();
            rowSM.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    //Ignore extra messages.
                    if (e.getValueIsAdjusting()) {
                        return;
                    }

                    ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                    if (lsm.isSelectionEmpty()) {
                        System.out.println("No hay fila seleccionada.");
                    } else {
                        int selectedRow = lsm.getMinSelectionIndex();
                        ps_retorno = tabla.getModel().getValueAt(selectedRow, 0).toString();
                        //System.out.println("Fila " + ps_retorno + " esta ahora seleccionada.");
                    }
                }
            });
        } else {
            tabla.setRowSelectionAllowed(false);
        }

        if (ALLOW_COLUMN_SELECTION) { // false by default
            if (ALLOW_ROW_SELECTION) {
                //We allow both row and column selection, which implies that we really want to allow individual
                //cell selection.
                tabla.setCellSelectionEnabled(true);
            }
            tabla.setColumnSelectionAllowed(true);
            ListSelectionModel colSM
                    = tabla.getColumnModel().getSelectionModel();
            colSM.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    //Ignore extra messages.
                    if (e.getValueIsAdjusting()) {
                        return;
                    }

                    ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                    if (lsm.isSelectionEmpty()) {
                        System.out.println("No hay columnas seleccionadas.");
                    } else {
                        int selectedCol = lsm.getMinSelectionIndex();
                        //System.out.println("Columna " + selectedCol + " esta ahora seleccionada.");
                    }
                }
            });
        }

        if (DEBUG) {
            tabla.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    printDebugData(tabla);
                    //tablaclic(e);
                }
            });
        }
        tabla.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tablaKeyPressed(evt);
            }
        });
        
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void  mousePressed(java.awt.event.MouseEvent evt) {
                if(evt.getClickCount()==2){
                    tablaclic(evt);
                }
            }
        });
        //Create the scroll pane and add the table to it.
        scrollPane = new JScrollPane(tabla);
        scrollPane.setBounds(10, 45, 380, 130);
        //Add the scroll pane to this panel.
        getContentPane().add(scrollPane);
        setAnchoColumnas();
    }//fin constructor

    /**
     * Creates new form w_buscar
     */
    public w_buscar() {
        initComponents();   
    }
    private void printDebugData(JTable table) {
        int numRows = table.getRowCount();
        int numCols = table.getColumnCount();
        javax.swing.table.TableModel model = table.getModel();
       // System.out.println("Valores de data: ");
        for (int i = 0; i < numRows; i++) {
            System.out.print("    Fila " + i + ":");
            for (int j = 0; j < numCols; j++) {
                System.out.print("  " + model.getValueAt(i, j));
            }
            //System.out.println();
        }
      //  System.out.println("--------------------------");
    }

    private void jtfbuscarFocusGained(java.awt.event.FocusEvent evt) {                                
        jtfbuscar.selectAll();
    }                               

    private void initmyComponents() {
        lblbuscar = new JLabel("Buscar:");
        lblbuscar.setBounds(10, 10, 46, 20);
        jtfbuscar = new JTextField();
        jtfbuscar.setBounds(64, 10, 300, 25);
//        setJTexFieldChanged(jtfbuscar);
        jtfbuscar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtfbuscarFocusGained(evt);
            }
        });
        datos = new Object[10][2];
        dtm = new DefaultTableModel();
        tabla = new JTable(dtm);

        jbtnAcept = new javax.swing.JButton("Aceptar");
        //jbtnAcept.setDefault();
        jbtnCancel = new javax.swing.JButton("Cancelar");
        dtm.addColumn("CODIGO");
        dtm.addColumn("DESCRIPCION");
       //cargarDatos();
    }


    private void cargarDatos() {
       String sql;
       sql = "";
       dtm.setRowCount(0);
       sql = is_sql + this.ps_condicion + "%' ORDER BY 1 ASC";
       System.out.println(sql);
        try {
            rs = conexion.ejecuteSQL(sql);
            while (rs.next()) {
                Object[] newRow = {rs.getString("codigo"), rs.getString("descripcion")};
                dtm.addRow(newRow);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al recuperar datos de tabla .\n\r ERROR: " + e);
        }
    }
    

    private void tablaKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == evt.VK_ENTER) {
            //System.out.println("Esta en la fila "+ps_retorno);
            codigo.setText(ps_retorno);
          
            codigo.requestFocus();
            setVisible(false);
            codigo.dispatchEvent(evt);
            dispose();
        }
    }
    private void tablaclic(java.awt.event.MouseEvent e){
        // System.out.println("Ha entrado en el metodo tabla clic");
        KeyEvent key = new KeyEvent(codigo, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, '\n');
        codigo.setText(ps_retorno);
        codigo.dispatchEvent(key);
        codigo.requestFocus();
        // System.out.println("codigo "+codigo);
        setVisible(false);
        dispose();
    }
//esto es para el buscar por la condición dada, para que cuando al dar enter busque
    private void jtfbuscarKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == evt.VK_ENTER) {
            ps_condicion = this.jtfbuscar.getText().replaceAll("\'","''").toLowerCase();
            cargarDatos();
            tabla.requestFocus();
        }
    }

    private void printIt(DocumentEvent documentEvent) {
        DocumentEvent.EventType type = documentEvent.getType();

        if (type.equals(DocumentEvent.EventType.CHANGE)) {

        } else if (type.equals(DocumentEvent.EventType.INSERT)) {
            ps_condicion = this.jtfbuscar.getText().toLowerCase();
            cargarDatos();
        } else if (type.equals(DocumentEvent.EventType.REMOVE)) {
            ps_condicion = this.jtfbuscar.getText().toLowerCase();
            cargarDatos();
        }
    }

    public void setAnchoColumnas() {
        int ancho = scrollPane.getWidth();
        int anchoColumna = 0;
        TableColumnModel modeloColumna = tabla.getColumnModel();
        TableColumn columnaTabla;
        int filas = tabla.getColumnCount();
        for (int i = 0; i < filas; i++) {
            columnaTabla = modeloColumna.getColumn(i);
            switch (i) {
                case 0:
                    anchoColumna = (20 * ancho) / 100;
                    break;
                case 1:
                    anchoColumna = (80 * ancho) / 100;
                    break;
                case 2:
                    anchoColumna = (50 * ancho) / 100;
                    break;
                case 3:
                    anchoColumna = (20 * ancho) / 100;
                    break;
            }
            columnaTabla.setPreferredWidth(anchoColumna);
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

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Hola");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 394, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 278, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
                        

 
    // Variables declaration - do not modify                     
    // End of variables declaration                   

}
