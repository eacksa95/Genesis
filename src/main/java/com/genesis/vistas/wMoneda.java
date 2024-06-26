package com.genesis.vistas;

import com.genesis.model.conexion;
import com.genesis.controladores.tableController;
import util.Tools;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;


public class wMoneda extends javax.swing.JInternalFrame implements ActiveFrame {

    private tableController tc;
    private Map<String, String> myData;
    String currentField;
    String menuName = "";
    String CRUD = "";
     
    /**
     * Creates new form wMoneda
     * @param menuName menu de wPrincipal
     */
    public wMoneda(String menuName) {
        initComponents();
        this.menuName = menuName;
        tc = new tableController();
        tc.init("monedas");
        myData = new HashMap<>();
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        tfnombre = new javax.swing.JTextField();
        tfabreviatura = new javax.swing.JTextField();
        tfdecimales = new javax.swing.JTextField();
        JCEstado = new javax.swing.JCheckBox();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("ABM Moneda");

        jLabel1.setText("ID");

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

        jLabel2.setText("Moneda");

        jLabel3.setText("Abreviatura");

        jLabel4.setText("Decimales");

        JCEstado.setText("Activo");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addComponent(jLabel2))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tfid, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                            .addComponent(tfnombre)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addComponent(jLabel4))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel3)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(JCEstado)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(tfdecimales, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(tfabreviatura, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(73, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(tfid, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tfnombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(tfabreviatura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tfdecimales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(JCEstado)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tfidFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfidFocusGained
        tfid.selectAll();
        currentField = "monedas";
    }//GEN-LAST:event_tfidFocusGained

    private void tfidKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfidKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            imBuscar();
    }//GEN-LAST:event_tfidKeyPressed
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox JCEstado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField tfabreviatura;
    private javax.swing.JTextField tfdecimales;
    private javax.swing.JTextField tfid;
    private javax.swing.JTextField tfnombre;
    // End of variables declaration//GEN-END:variables

    @Override
    public void imGrabar(String crud) {
        this.CRUD = crud;
        String msg;
        if (Tools.validarPermiso(conexion.getGrupoId(), menuName, crud) == 0) {
            msg = "NO TIENE PERMISO PARA REALIZAR ESTA OPERACIÓN ";
            JOptionPane.showMessageDialog(this, msg, "wMoneda225...!", JOptionPane.OK_OPTION);
            return;
        }
        int id = Integer.parseInt(tfid.getText());
        if (id > 0) {
            this.imActualizar("U");
            return;
        }
        this.setData();
        int rowsAffected = this.tc.createReg(this.myData);
        if (rowsAffected > 0) {
            msg = "SE CREÓ EL NUEVO REGISTRO: " + tfid.getText();
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "wMoneda238...!", JOptionPane.DEFAULT_OPTION);
            imNuevo();
        } else {
            msg = "NO SE PUDO CREAR EL NUEVO REGISTRO";
            JOptionPane.showMessageDialog(this, msg, "wMoneda242...!", JOptionPane.DEFAULT_OPTION);
            tfid.requestFocus();
        }
    } //Fin Grabar
    
    @Override
    public void imActualizar(String crud) {
        this.CRUD = crud;
        String msg;
        if (Tools.validarPermiso(conexion.getGrupoId(), menuName, crud) == 0) {
            msg = "NO TIENE PERMISO PARA REALIZAR ESTA OPERACIÓN ";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
        }
        this.setData();
        ArrayList<Map<String, String>> alCabecera;         //Declara array de Map, cada Map es para un registro
        alCabecera = new ArrayList<>(); 
        alCabecera.add(myData);                           //agrega el Map al array, para la cabecera será el mejor de los casos, es decir 1 registro 
        int rowsAffected = this.tc.updateReg(alCabecera); //Está guardando igual si en el detalle hay error
        if (rowsAffected > 0) {
            msg = "SE ACTUALIZO EL REGISTRO: " + tfid.getText();
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "wMoneda283...!", JOptionPane.DEFAULT_OPTION);
            imNuevo();
        } else {
            msg = "NO SE PUDO ACTUALIZAR EL REGISTRO";
            JOptionPane.showMessageDialog(this, msg, "wMoneda287...!", JOptionPane.DEFAULT_OPTION);
            tfid.requestFocus();
        }
    } //Fin Actualizar

    @Override
    public void imBorrar(String crud) {
        this.CRUD = crud;
        String msg;
        if (Tools.validarPermiso(conexion.getGrupoId(), menuName, crud) == 0) {
            msg = "NO TIENE PERMISO PARA REALIZAR ESTA OPERACIÓN ";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
        }
        this.setData();
        ArrayList<Map<String, String>> alRegister;              //Declara un Array de Map
        alRegister = new ArrayList<>();      //Instancia el array
        alRegister.add(myData);                                //Agregamos el map en el array
        int rowsAffected = this.tc.deleteReg(alRegister);               //Invocamos el método deleteReg del Modelo que procesa un array
        if (rowsAffected <= 0) {
            msg = "NO SE HA PODIDO ELIMINAR EL REGISTRO: " + tfid.getText();
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
        }
        if (rowsAffected > 0) {
            msg = "EL REGISTRO: " + tfid.getText() + " SE HA ELIMINADO CORRECTAMENTE";
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
            imNuevo();
        }
    } //Fin Borrar
    
    @Override
    public void imFiltrar() {
        String sql;
        sql = "SELECT id AS codigo, "
                + "CONCAT(moneda, ' ', abreviatura) AS descripcion "
                + "FROM monedas "
                + "WHERE LOWER(CONCAT(id, moneda)) LIKE '%";

        wBuscar frame = new wBuscar(sql, this.tfid);
        frame.setVisible(true);
        wPrincipal.desktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.DEFAULT_OPTION);
        }
    } //Fin imFiltrar

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
    
    private void setData() {
        myData.put("id", tfid.getText());
        myData.put("moneda", tfnombre.getText());
        myData.put("abreviatura", tfabreviatura.getText());
        myData.put("decimales", tfdecimales.getText());

        int estado = 0;
        if (JCEstado.isSelected()) {
            estado = 1;
        }
        myData.put("estado", estado + "");
    }//fin setData

    private void resetData() {
        myData.put("id", "0");
        myData.put("moneda", "");
        myData.put("abreviatura", "");
        myData.put("decimales", "0");
        myData.put("estado", "0");
        fillView(myData);
    }

    private void fillView(Map<String, String> data) {
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
                case "id":
                    tfid.setText(value);
                    break;
                case "moneda":
                    tfnombre.setText(value);
                    break;
                case "abreviatura":
                    tfabreviatura.setText(value);
                    break;
                case "decimales":
                    tfdecimales.setText(value);
                    break;

                case "estado":
                    if (Integer.parseInt(value) == 0) {
                        JCEstado.setSelected(false);
                    } else {
                        JCEstado.setSelected(true);
                    }
                    break;

            }//end switch
        }//end for
    }
    
    @Override
    public void imNuevo() {
        this.resetData();
        this.fillView(myData);    }

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

} //Fin Clase