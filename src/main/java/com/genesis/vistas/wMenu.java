/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genesis.vistas;

import com.genesis.model.conexion;
import com.genesis.controladores.tableController;
import util.Tools;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author RC
 */
public class wMenu extends javax.swing.JInternalFrame implements ActiveFrame {

    private tableController tc;
    private Map<String, String> myData;
    String currentField;
    String opcion = "";
    /**
     * Creates new form wMenu
     */
    public wMenu(String opcion) {
        initComponents();
        this.opcion = opcion;
        tc = new tableController();
        tc.init("menus");
        this.currentField = "";
        myData = new HashMap<String, String>();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        tfid = new javax.swing.JTextField();
        tfmenu = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jCtipo = new javax.swing.JCheckBox();
        jCOrden = new javax.swing.JCheckBox();
        jCSistema = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        tfnivel = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);

        jLabel1.setText("Id");

        tfid.setText("0");
        tfid.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfidFocusGained(evt);
            }
        });
        tfid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfidKeyPressed(evt);
            }
        });

        jLabel2.setText("Menú");

        jCtipo.setText("Tipo");

        jCOrden.setText("Orden");

        jCSistema.setText("Sistema");

        jLabel3.setText("Nivel");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tfnivel))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCSistema)
                            .addComponent(jCOrden)
                            .addComponent(jCtipo)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(tfmenu)
                                .addComponent(tfid, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tfid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfmenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addComponent(jCtipo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCOrden)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCSistema)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(tfnivel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(69, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tfidFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfidFocusGained
        tfid.selectAll();          // TODO add your handling code here:
    }//GEN-LAST:event_tfidFocusGained

    private void tfidKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfidKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.imBuscar();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_tfidKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCOrden;
    private javax.swing.JCheckBox jCSistema;
    private javax.swing.JCheckBox jCtipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField tfid;
    private javax.swing.JTextField tfmenu;
    private javax.swing.JTextField tfnivel;
    // End of variables declaration//GEN-END:variables

    @Override
    public void imGrabar(String crud) {
        int id, rows = 0;
        id = Integer.parseInt(tfid.getText());
        if (id > 0) {
            this.imActualizar("C");
            String msg = "SE HA ACTUALIZADO EXITOSAMENTE EL REGISTRO: " + tfid.getText();
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
            return;
        }
        this.setData();
        rows = this.tc.createReg(this.myData);
        this.fillView(myData);
        String msg = "SE CREÓ EL NUEVO REGISTRO: " + tfid.getText();
        System.out.println(msg);
        JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
    }

    @Override
    public void imFiltrar() {
        String sql;
        sql = "";
        sql = "SELECT id AS codigo, "
                + "CONCAT(menu, '-', nivel, '-', orden) AS descripcion "
                + "FROM menus "
                + "WHERE LOWER(CONCAT(id, menu)) LIKE '%";

        wBuscar frame = new wBuscar(sql, this.tfid);
        frame.setVisible(true);
        wPrincipal.desktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.OK_OPTION);
        }
    }

    @Override
    public void imActualizar(String crud) {
        System.out.println("V imActualizar");
        this.setData();
        ArrayList<Map<String, String>> alCabecera;         //Declara array de Map, cada Map es para un registro
        alCabecera = new ArrayList<Map<String, String>>(); //Instancia array
        alCabecera.add(myData);                           //agrega el Map al array, para la cabecera será el mejor de los casos, es decir 1 registro 
        int rowsAffected = this.tc.updateReg(alCabecera); //Está guardando igual si en el detalle hay error
    }

    @Override
    public void imBorrar(String crud) {
        this.setData();
        ArrayList<Map<String, String>> alRegister;              //Declara un Array de Map
        alRegister = new ArrayList<Map<String, String>>();      //Instancia el array
        alRegister.add(myData);                                //Agregamos el map en el array
        int b = this.tc.deleteReg(alRegister);               //Invocamos el método deleteReg del Modelo que procesa un array 
        //int b =   this.tc.deleteReg(tf_id.getText());
        if (b <= 0) {
            String msg = "NO SE HA PODIDO ELIMINAR EL REGISTRO: " + tfid.getText();
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
        }
        if (b > 0) {
            String msg = "EL REGISTRO: " + tfid.getText() + " SE HA ELIMINADO CORRECTAMENTE";
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
        }
        this.resetData();
        this.fillView(myData);
    }

    @Override
    public void imNuevo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void imBuscar() {
        this.setData();
        myData = tc.searchById(this.myData);
        if (this.myData.size() <= 0) {
            String msg = "NO SE HA PODIDO RECUPERAR EL REGISTRO: " + tfid.getText();
            this.resetData();
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
        }
        this.fillView(myData);
        System.out.println("V imBuscar myData: " + myData.toString());
    }

    @Override
    public void imPrimero() {
        this.setData();
        this.myData = this.tc.navegationReg(tfid.getText(), "FIRST");
        this.fillView(this.myData);
    }

    @Override
    public void imSiguiente() {
        this.setData();
        this.myData = this.tc.navegationReg(tfid.getText(), "NEXT");
        this.fillView(this.myData);
    }

    @Override
    public void imAnterior() {
        this.setData();
        this.myData = this.tc.navegationReg(tfid.getText(), "PRIOR");
        this.fillView(this.myData);
    }

    @Override
    public void imUltimo() {
        this.setData();
        this.myData = this.tc.navegationReg(tfid.getText(), "LAST");
        this.fillView(this.myData);
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

    private void setData() {
        myData.put("id", tfid.getText());
        myData.put("menu", tfmenu.getText());
        myData.put("nivel", tfnivel.getText());
        ///////////
        int estado = 0;
        if (jCtipo.isSelected()) {
            estado = 1;
        }
        myData.put("tipo", estado + "");
        ////////////
        int estado1 = 0;
        if (jCOrden.isSelected()) {
            estado1 = 1;
        }
        myData.put("orden", estado1 + "");
        ///////////
        int estado2 = 0;
        if (jCSistema.isSelected()) {
            estado2 = 1;
        }
        myData.put("sistema", estado2 + "");

        System.out.println("SET DATA:" + myData);
    }//fin setData

    private void resetData() {
        this.myData = new HashMap<String, String>();
        java.util.Date df = new java.util.Date();
        this.myData.put("id", "0");
        this.myData.put("menu", "");
        this.myData.put("orden", "0");
        this.myData.put("tipo", "0");
        this.myData.put("sistema", "0");
        this.myData.put("nivel", "");

        //fillView(myData);
    }

    private void fillView(Map<String, String> data) {
        Date df;
        long dateLong;
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
                case "id":
                    tfid.setText(value);
                    break;
                case "nivel":
                    tfnivel.setText(value);
                    break;
                case "menu":
                    tfmenu.setText(value);
                    break;

                case "tipo":
                    if (Integer.parseInt(value) == 0) {
                        jCtipo.setSelected(false);
                    } else {
                        jCtipo.setSelected(true);
                    }
                    break;
                ///////
                case "orden":
                    if (Integer.parseInt(value) == 0) {
                        jCOrden.setSelected(false);
                    } else {
                        jCOrden.setSelected(true);
                    }
                    break;
                /////////////
                case "sistema":
                    if (Integer.parseInt(value) == 0) {
                        jCSistema.setSelected(false);
                    } else {
                        jCSistema.setSelected(true);
                    }
                    break;
                /////////////

            }//end switch
        }//end for
    }//end fill
}