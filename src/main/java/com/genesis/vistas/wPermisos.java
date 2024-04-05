/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genesis.vistas;

import com.genesis.model.conexion;
import com.genesis.model.tableModel;
import com.genesis.tabla.ModeloTabla;
import com.genesis.controladores.tableController;
import util.Tools;
import util.cargaComboBox;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author RC
 */
public class wPermisos extends javax.swing.JInternalFrame implements ActiveFrame {

    private Map<String, String> myData;
    private HashMap<String, String> myDet;
    private tableController tc;
    String Opcion = "";
    String CRUD = "";
    /**
     * Creates new form wPermisosgre
     */
    public wPermisos(String Opcion) {
        initComponents();
        this.Opcion = Opcion;
        myData = new HashMap<String, String>();
        cargaComboBox.pv_cargar(jcbRol, "roles", " id, rol ", "id", "");
        tc = new tableController();
        tc.init("permisos");
    }// fin constructor

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrlTabla = new javax.swing.JScrollPane();
        jtDetalle = new javax.swing.JTable();
        jcbRol = new javax.swing.JComboBox<>();
        label1 = new java.awt.Label();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Permisos");

        jtDetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Menu Id", "Menu", "VER", "CREATE", "READ", "UPDATE", "DELETE"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        scrlTabla.setViewportView(jtDetalle);
        if (jtDetalle.getColumnModel().getColumnCount() > 0) {
            jtDetalle.getColumnModel().getColumn(2).setPreferredWidth(60);
            jtDetalle.getColumnModel().getColumn(2).setMaxWidth(60);
            jtDetalle.getColumnModel().getColumn(3).setPreferredWidth(60);
            jtDetalle.getColumnModel().getColumn(3).setMaxWidth(60);
            jtDetalle.getColumnModel().getColumn(4).setPreferredWidth(60);
            jtDetalle.getColumnModel().getColumn(4).setMaxWidth(60);
            jtDetalle.getColumnModel().getColumn(5).setPreferredWidth(60);
            jtDetalle.getColumnModel().getColumn(5).setMaxWidth(60);
            jtDetalle.getColumnModel().getColumn(6).setPreferredWidth(60);
            jtDetalle.getColumnModel().getColumn(6).setMaxWidth(60);
        }

        jcbRol.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Seleccione Rol" }));
        jcbRol.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbRolItemStateChanged(evt);
            }
        });

        label1.setText("Elija Rol:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcbRol, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(scrlTabla, javax.swing.GroupLayout.PREFERRED_SIZE, 661, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jcbRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(scrlTabla, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jcbRolItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbRolItemStateChanged
        mostrarMenus();
    }//GEN-LAST:event_jcbRolItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> jcbRol;
    private javax.swing.JTable jtDetalle;
    private java.awt.Label label1;
    private javax.swing.JScrollPane scrlTabla;
    // End of variables declaration//GEN-END:variables

    public void mostrarMenus() {
        limpiarTabla();
        DefaultTableModel dtm = (DefaultTableModel) jtDetalle.getModel();
        int row = 0;
        int rolid = 0;
        rolid = Integer.parseInt(Tools.ExtraeCodigo(jcbRol.getSelectedItem().toString()));
        if (rolid == 0) {
            JOptionPane.showMessageDialog(null, "Seleccione un Rol válido.");
            return;
        }
        String sql, menuid, menutext;
        Boolean showMenu, createReg, readReg, updateReg, deleteReg;

        sql = "SELECT p.ver AS VER, p.C AS c, p.R AS r, p.U AS u, p.D AS d, p.menuid, m.menu AS menutext "
                + " FROM permisos p INNER JOIN menus m ON m.id = p.menuid "
                + "WHERE p.rolid = " + rolid;

        Map<String, String> rtnnn = new HashMap<String, String>();
        ResultSet rsss;
        try {
            rsss = conexion.ejecuteSQL(sql); //Esto devuelve un ResultSet
            while (rsss.next()) {
                menuid = rsss.getString("menuid");
                menutext = rsss.getString("menutext");
                showMenu = (rsss.getInt("ver") == 1) ? true : false;
                createReg = (rsss.getInt("C") == 1) ? true : false;
                readReg = (rsss.getInt("R") == 1) ? true : false;
                updateReg = (rsss.getInt("U") == 1) ? true : false;
                deleteReg = (rsss.getInt("D") == 1) ? true : false;

                dtm.addRow(new Object[]{menuid, menutext, showMenu, createReg,
                    readReg, updateReg, deleteReg});
                row++;
            }//end while
        } //fin deleteRegister
        catch (SQLException ex) {
            Logger.getLogger(tableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//fin mostrarMenus();

    public void limpiarTabla() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) jtDetalle.getModel();
            dtm.setRowCount(0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al limpiar la tabla.");
        }
    }//fin limpiarTabla

    @Override
    public void imGrabar(String crud) {
        this.CRUD = crud;
        int metodo = 3;
        System.out.println("OPCION DE LA VENTANA PRINCIPAL: " + Opcion);
        metodo = Tools.validarPermiso(conexion.getGrupoId(), Opcion, crud);
        System.out.println("METODOOO; " + metodo);
        if (Tools.validarPermiso(conexion.getGrupoId(), Opcion, crud) == 0) {
            String msg = "NO TIENE PERMISO PARA REALIZAR ESTA OPERACIÓN ";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
        }
        String msg;
        int rowsAffected = 0;
        //Sirve para poner en el map el nombre de la columna coincide con la estructura de la tabla
        String[] fields = {"menuid", "menutext", "ver", "C", "R", "U", "D"};
        myData.put("rolid", Tools.ExtraeCodigo(jcbRol.getSelectedItem().toString())); //capturamos el id del rol

        DefaultTableModel dftm = (DefaultTableModel) jtDetalle.getModel();
        int numRows = jtDetalle.getRowCount();
        int numCols = jtDetalle.getColumnCount();
        //rolid, ver, C, R, U, D, menuid
        Object fieldValue = null;  //Como las columnas almancenan datos distintos se tomo com Objetos
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                fieldValue = jtDetalle.getValueAt(i, j);
                if (fieldValue instanceof Boolean) {
                    if ((Boolean) fieldValue) {
                        myData.put(fields[j], "1");
                    } else {
                        myData.put(fields[j], "0");
                    }
                }
                if (fieldValue instanceof String) {
                    myData.put(fields[j], (String) fieldValue);
                }
            }
            rowsAffected = this.tc.createReg(myData); //El controlador se encargará de ver si guarda o actualiza

        }
        if (rowsAffected < 1) {
            msg = "ERROR AL INTENTAR ACTUALIZAR EL PERMISO";
        } else {
            msg = "SE HA ACTUALIZADO CORRECTAMENTE LOS PERMISOS";
        }
        JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
    }//fin imGrabar

    @Override
    public void imFiltrar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void imActualizar(String crud) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void imBorrar(String crud) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void imNuevo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void imBuscar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void imPrimero() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void imSiguiente() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void imAnterior() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void imUltimo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void imImprimir() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void imInsDet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void imDelDet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void imCerrar() {
        setVisible(false);
        dispose();
    }

}//fin ventana