/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genesis.vistas;

import com.genesis.model.conexion;
import com.genesis.controladores.tableController;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import util.Tools;
import util.cargaComboBox;


/**
 *
 * @author RC
 */
public class wProducto extends javax.swing.JInternalFrame implements ActiveFrame {
private tableController tc;
    private Map<String, String> myData;
    String currentField;
    String opcion = "";
    /**
     * Creates new form wProduct
     */
    public wProducto(String menuName) {
        initComponents();
        tc = new tableController();
        tc.init("productos");
        opcion = menuName;
        myData = new HashMap<String, String>();
        cargaComboBox.pv_cargar(jcbCategoria, "categorias", " id, nombre ", "id", "");
        this.currentField = "";
      
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCalendar1 = new com.toedter.calendar.JCalendar();
        tf_id = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tf_nombre_producto = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tf_descripcion_producto = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        tf_impuesto = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        tf_servicio = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        tf_estado = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        tf_marca = new javax.swing.JTextField();
        tf_proveedor = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jcbCategoria = new javax.swing.JComboBox<>();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("ABM Producto");

        tf_id.setText("0");
        tf_id.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tf_idFocusGained(evt);
            }
        });
        tf_id.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_idActionPerformed(evt);
            }
        });
        tf_id.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tf_idKeyPressed(evt);
            }
        });

        jLabel1.setText("ID PRODUCTO");

        jLabel2.setText("NOMBRE");

        jLabel3.setText("DESCRIPCIÓN");

        jLabel4.setText("IMPUESTO");

        jLabel5.setText("SERVICIO");

        tf_servicio.setText("0");

        jLabel6.setText("ESTADO");

        jLabel7.setText("MARCA");

        jLabel8.setText("PROVEEDOR");

        jLabel9.setText("CATEGORIA");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel9)
                        .addComponent(jLabel8))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(1, 1, 1)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tf_proveedor)
                    .addComponent(tf_id, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                    .addComponent(tf_nombre_producto)
                    .addComponent(tf_descripcion_producto)
                    .addComponent(tf_impuesto)
                    .addComponent(tf_servicio)
                    .addComponent(tf_estado)
                    .addComponent(tf_marca)
                    .addComponent(jcbCategoria, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tf_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(tf_nombre_producto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(tf_descripcion_producto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tf_impuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(tf_servicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tf_estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tf_marca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tf_proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jcbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleName("ABM Producto");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tf_idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_idActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tf_idActionPerformed

    private void tf_idKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf_idKeyPressed
 if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            imBuscar();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_tf_idKeyPressed

    private void tf_idFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tf_idFocusGained
 this.currentField = "id";        // TODO add your handling code here:
    }//GEN-LAST:event_tf_idFocusGained

    @Override
    public void imGrabar(String crud) {
      int id, rows = 0;
        id = Integer.parseInt(tf_id.getText());
        if (id>0){
        this.imActualizar("C");
        String msg = "SE HA ACTUALIZADO EXITOSAMENTE EL REGISTRO: "+tf_id.getText();
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
        return;
        }
        this.setData();
        rows = this.tc.createReg(this.myData);
        this.fillView(myData);
        String msg = "SE CREÓ EL NUEVO REGISTRO: "+tf_id.getText();
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
    }

    @Override
    public void imActualizar(String crud) {
        System.out.println("V imActualizar");
        this.setData();
        ArrayList<Map<String,String>> alCabecera;         //Declara array de Map, cada Map es para un registro
        alCabecera = new ArrayList<Map<String,String>>(); //Instancia array
        alCabecera.add(myData);                           //agrega el Map al array, para la cabecera será el mejor de los casos, es decir 1 registro 
        int rowsAffected = this.tc.updateReg(alCabecera); //Está guardando igual si en el detalle hay error
    }

    @Override
    public void imBorrar(String crud) {
       this.setData();
        ArrayList<Map<String,String>> alRegister;              //Declara un Array de Map
        alRegister = new ArrayList<Map<String,String>>();      //Instancia el array
        alRegister.add(myData);                                //Agregamos el map en el array
        int b =   this.tc.deleteReg(alRegister);               //Invocamos el método deleteReg del Modelo que procesa un array
        //int b =   this.tc.deleteReg(tf_id.getText());
       if(b<=0) {
            String msg = "NO SE HA PODIDO ELIMINAR EL REGISTRO: "+tf_id.getText();
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return; 
       }
       if (b>0){
            String msg = "EL REGISTRO: "+tf_id.getText()+" SE HA ELIMINADO CORRECTAMENTE";
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
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
        if(this.myData.size() <=0){
            String msg = "NO SE HA PODIDO RECUPERAR EL REGISTRO: "+tf_id.getText();
            this.resetData();
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
        }
        this.fillView(myData);
        System.out.println("V imBuscar myData: "+myData.toString());
    }

    @Override
    public void imPrimero() {
        this.setData();
        this.myData = this.tc.navegationReg(tf_id.getText(), "FIRST");
        this.fillView(this.myData);
    }

    @Override
    public void imSiguiente() {
        this.setData();
        this.myData = this.tc.navegationReg(tf_id.getText(), "NEXT");
        this.fillView(this.myData);
    }

    @Override
    public void imAnterior() {
        this.setData();
        this.myData = this.tc.navegationReg(tf_id.getText(), "PRIOR");
        this.fillView(this.myData);
    }

    @Override
    public void imUltimo() {
        this.setData();
        this.myData = this.tc.navegationReg(tf_id.getText(), "LAST");
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JCalendar jCalendar1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JComboBox<String> jcbCategoria;
    private javax.swing.JTextField tf_descripcion_producto;
    private javax.swing.JTextField tf_estado;
    private javax.swing.JTextField tf_id;
    private javax.swing.JTextField tf_impuesto;
    private javax.swing.JTextField tf_marca;
    private javax.swing.JTextField tf_nombre_producto;
    private javax.swing.JTextField tf_proveedor;
    private javax.swing.JTextField tf_servicio;
    // End of variables declaration//GEN-END:variables

    private void setData(){
        myData.put("id", tf_id.getText());
        myData.put("nombre", tf_nombre_producto.getText());
        myData.put("descripcion", tf_descripcion_producto.getText());
        myData.put("impuesto", tf_impuesto.getText());
        myData.put("servicio", tf_servicio.getText());
        myData.put("estado", tf_estado.getText());
        myData.put("marca", tf_marca.getText());
        myData.put("proveedor", tf_proveedor.getText());
        myData.put("categoriaid", Tools.ExtraeCodigo(jcbCategoria.getSelectedItem().toString()));
    }//fin setData
    
    private void resetData(){
        myData.put("id", "0");
        myData.put("nombre", "");
        myData.put("descripcion", "");
        myData.put("impuesto", "");
        myData.put("servicio", "0");
        myData.put("estado", "");
        myData.put("marca", "");
        myData.put("proveedor", "");
        myData.put("categoriaid", "0");
        fillView(myData);
    }
    private void fillView(Map<String, String> data){
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch(key) {
                case "id":
                  tf_id.setText(value);
                  break;
                case "nombre":
                  tf_nombre_producto.setText(value);
                  break;
                case "descripcion":
                  tf_descripcion_producto.setText(value);
                  break;
                case "impuesto":
                  tf_impuesto.setText(value);
                  break;
                case "servicio":
                  tf_servicio.setText(value);
                  break;
                case "estado":
                  tf_estado.setText(value);
                  break;
                case "marca":
                  tf_marca.setText(value);
                  break;
                case "proveedor":
                  tf_proveedor.setText(value);
                  break;
                case "categoriaid":
                  Tools.E_estado(jcbCategoria, "categorias", "id=" + value);
                  break;
            }//end switch
        }//end for
    }//end fill

    @Override
    public void imFiltrar() {
         String sql;
       sql = "";
       
       if(currentField.equals("")){
            return; 
       }
       switch (currentField) {
           case "id":
               sql = "SELECT id AS codigo ," + 
                     "CONCAT(nombre, ' - ',descripcion) AS descripcion "+ 
                     "FROM productos " + 
                     "WHERE LOWER(CONCAT(nombre, descripcion)) LIKE '%";
               break;
           case "idOtro":
               
               break;
       }
        
        wBuscar frame = new wBuscar(sql, this.tf_id);
        frame.setVisible(true);
        wPrincipal.desktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.OK_OPTION);
        }
         
    }

}