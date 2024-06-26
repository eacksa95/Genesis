package com.genesis.vistas;

import com.genesis.model.conexion;
import com.genesis.controladores.tableController;
import util.ComboBox;
import util.Tools;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;


public class wCliente extends javax.swing.JInternalFrame implements ActiveFrame {

    private tableController tc;
    private Map<String, String> myData;
    String currentField;
    String menuName = "";
    String CRUD = "";

    /**
     * Creates new form wCliente
     * @param menuName nombre del Menu en Vista Principal que ejecuto la ventana
     */
    public wCliente(String menuName) {
        initComponents();
        this.menuName = menuName;
        tc = new tableController();
        tc.init("clientes");

        myData = new HashMap<>();

        // COMBO BOX DESPLEGABLES DE LAS TABLAS//
        ComboBox.pv_cargar(jcbPersona, "personas", " id, CONCAT(nombres, apellidos) AS Descripcion", "id", "");
        ComboBox.pv_cargar(jcbMoneda, "monedas", " id, moneda", "id ", "");
        ComboBox.pv_cargar(jcbPrecio, "precios", "id, lista", "id", "");

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tfid = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        tf_ruc = new javax.swing.JTextField();
        tfContacto = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        tfreferencia = new javax.swing.JTextField();
        tfcredito = new javax.swing.JTextField();
        tfexento = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jCactivo = new javax.swing.JCheckBox();
        jcbPersona = new javax.swing.JComboBox<>();
        jcbMoneda = new javax.swing.JComboBox<>();
        jcbPrecio = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        tfnroreferencia = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("ABM CLIENTES");

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

        jLabel1.setText("ID");

        jLabel2.setText("NRO CONTACTO");

        jLabel3.setText("REFERENCIA");

        jLabel4.setText("RUC");

        jLabel5.setText("MONEDA");

        jLabel6.setText("PERSONA");

        jLabel7.setText("CRÉDITO");

        jLabel8.setText("PRECIO");

        jLabel9.setText("EXENTO");

        jCactivo.setText("Activo");

        jcbPersona.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Seleccione Persona" }));

        jcbMoneda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Seleccione Moneda" }));

        jcbPrecio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Seleccione Precio" }));

        jLabel10.setText("NRO REFENCIA");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1)
                    .addComponent(jLabel5)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel7)
                        .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jCactivo)
                    .addComponent(tfexento)
                    .addComponent(tfcredito)
                    .addComponent(tfContacto)
                    .addComponent(tfid, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                    .addComponent(tf_ruc)
                    .addComponent(tfreferencia)
                    .addComponent(jcbPersona, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcbMoneda, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcbPrecio, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tfnroreferencia))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jcbPersona, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tf_ruc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfContacto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfreferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jcbMoneda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfcredito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jcbPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfexento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(tfnroreferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(jCactivo)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tfidKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfidKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            imBuscar();
        }
    }//GEN-LAST:event_tfidKeyPressed

    private void tfidFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfidFocusGained
        System.out.println("evento tfidFocusGained: " + evt.getSource());
        this.currentField = "cliente";
    }//GEN-LAST:event_tfidFocusGained


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCactivo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JComboBox<String> jcbMoneda;
    private javax.swing.JComboBox<String> jcbPersona;
    private javax.swing.JComboBox<String> jcbPrecio;
    private javax.swing.JTextField tfContacto;
    private javax.swing.JTextField tf_ruc;
    private javax.swing.JTextField tfcredito;
    private javax.swing.JTextField tfexento;
    private javax.swing.JTextField tfid;
    private javax.swing.JTextField tfnroreferencia;
    private javax.swing.JTextField tfreferencia;
    // End of variables declaration//GEN-END:variables

    @Override
    public void imGrabar(String crud) {
        String msg;
        if (Tools.validarPermiso(conexion.getGrupoId(), menuName, crud) == 0) {
            msg = "NO TIENE PERMISO PARA REALIZAR ESTA OPERACIÓN ";
            JOptionPane.showMessageDialog(this, msg, "wCliente330...!", JOptionPane.OK_OPTION);
            return;
        }
        int id = Integer.parseInt(tfid.getText());
        if (id > 0) {
            this.imActualizar("U");
            return;
        }
        this.setData();
        int rowsAffected  = this.tc.createReg(this.myData);
        if (rowsAffected > 0){
            msg = "SE CREÓ EL NUEVO REGISTRO: " + tfid.getText();
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "wCliente343...!", JOptionPane.DEFAULT_OPTION);
            imNuevo();
        }
        if (rowsAffected <= 0){
            msg = "NO SE HA PODIDO CREAR EL REGISTRO";
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "wCliente349...!", JOptionPane.DEFAULT_OPTION);
        }
    } //Fin imGrabar

    @Override
    public void imActualizar(String crud) {
        String msg;
        if (Tools.validarPermiso(conexion.getGrupoId(), menuName, crud) == 0) {
            msg = "NO TIENE PERMISO PARA REALIZAR ESTA OPERACIÓN ";
            JOptionPane.showMessageDialog(this, msg, "wCLiente265...!", JOptionPane.OK_OPTION);
            return;
        }
        this.setData();
        ArrayList<Map<String, String>> alCabecera;         //Declara array de Map, cada Map es para un registro
        alCabecera = new ArrayList<>();                    //Instancia array
        alCabecera.add(myData);                            //agrega el Map al array, para la cabecera será el mejor de los casos, es decir 1 registro 
        int rowsAffected = this.tc.updateReg(alCabecera);  //Está guardando igual si en el detalle hay error
        if (rowsAffected > 0) {
            msg = "SE ACTUALIZO EL REGISTRO: " + tfid.getText();
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "wCliente369...!", JOptionPane.DEFAULT_OPTION);
        } else {
            msg = "No se pudo actualizar el registro";
            JOptionPane.showMessageDialog(this, msg, "wCliente372...!", JOptionPane.DEFAULT_OPTION);
        }
    } //Fin imActualizar

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
        alRegister = new ArrayList<>();                         //Instancia el array
        alRegister.add(myData);                                //Agregamos el map en el array
        int rowsAffected = tc.deleteReg(alRegister);          //Invocamos el método deleteReg del Modelo que procesa un array
        if (rowsAffected > 0) {
            msg = "EL REGISTRO: " + tfid.getText() + " SE HA ELIMINADO CORRECTAMENTE";
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "wCliente393...!", JOptionPane.DEFAULT_OPTION);
            imNuevo();
        }
        if (rowsAffected <= 0) {
            msg = "NO SE HA PODIDO ELIMINAR EL REGISTRO: " + tfid.getText();
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "wCliente399...!", JOptionPane.OK_OPTION);
        }
    } //Fin imBorrar

    @Override
    public void imNuevo() {
        this.resetData();  
        this.fillView(myData);
    }

    @Override
    public void imBuscar() {
        this.setData();
        myData = tc.searchById(this.myData);
        if (this.myData.size() <= 0) {
            String msg = "NO SE HA PODIDO RECUPERAR EL REGISTRO: " + tfid.getText();
            this.resetData();
            JOptionPane.showMessageDialog(this, msg, "wCliente323...!", JOptionPane.OK_OPTION);
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

    @Override
    public void imFiltrar() {
        String sql = "";
        
        if (currentField.equals("")) {
            return;
        }
        
        switch (currentField) {
            case "cliente":
                sql = "SELECT c.id AS codigo ,"
                        + " CONCAT(p.nombres, ' ', p.apellidos, ' ', c.ruc) AS descripcion "
                        + " FROM clientes c, personas p "
                        + " WHERE p.id = c.personaid "
                        + " AND CONCAT(c.id, c.personaid, c.ruc, p.nombres, p.apellidos) LIKE '%";
                break;
            case "idOtro":

                break;
        }

        wBuscar frame = new wBuscar(sql, this.tfid);
        frame.setVisible(true);
        wPrincipal.desktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.DEFAULT_OPTION);
        }
    } //fin Filtrar
    
    private void setData() {
        myData.put("id", tfid.getText());
        myData.put("personaid", ComboBox.ExtraeCodigo(jcbPersona.getSelectedItem().toString()));
        myData.put("ruc", tf_ruc.getText());
        myData.put("nro_contacto", tfContacto.getText());
        myData.put("persona_referencia", tfreferencia.getText());
        myData.put("monedaid", ComboBox.ExtraeCodigo(jcbMoneda.getSelectedItem().toString()));
        myData.put("credito", tfcredito.getText());
        myData.put("precioid", ComboBox.ExtraeCodigo(jcbPrecio.getSelectedItem().toString()));
        myData.put("exento", tfexento.getText());
        myData.put("nro_referencia", tfnroreferencia.getText());

        int estado = 0;
        if (jCactivo.isSelected()) { estado = 1; }
        myData.put("estado", estado + "");

        System.out.println("myData " + myData);
    }

    private void resetData() {
        myData.put("id", "0");
        myData.put("personaid", "0");
        myData.put("ruc", "");
        myData.put("nro_contacto", "");
        myData.put("persona_referencia", "");
        myData.put("monedaid", "0");
        myData.put("credito", "");
        myData.put("precioid", "0");
        myData.put("exento", "");
        myData.put("estado", "1");
        myData.put("nro_referencia", "");
        jcbPrecio.setSelectedIndex(0);
        jcbMoneda.setSelectedIndex(0);
        jcbPersona.setSelectedIndex(0);
        
    }

    private void fillView(Map<String, String> data) {
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
                case "id":
                    tfid.setText(value);
                    break;

                case "personaid":
                    ComboBox.E_estado(jcbPersona, "personas", " id, CONCAT(nombres, apellidos) AS Descripcion", "id=" + value);
                    break;

                case "ruc":
                    tf_ruc.setText(value);
                    break;

                case "nro_contacto":
                    tfContacto.setText(value);
                    break;

                case "persona_referencia":
                    tfreferencia.setText(value);
                    break;

                case "monedaid":
                    ComboBox.E_estado(jcbMoneda, "monedas", " id, moneda", "id=" + value);
                    break;

                case "credito":
                    tfcredito.setText(value);
                    break;

                case "precioid":
                    ComboBox.E_estado(jcbPrecio, "precios", "id, lista", "id=" + value);
                    break;

                case "exento":
                    tfexento.setText(value);
                    break;

                case "nro_referencia":
                    tfnroreferencia.setText(value);
                    break;

                case "estado":
                    if (Integer.parseInt(value) == 1) {
                        jCactivo.setSelected(true);
                    } else {
                        jCactivo.setSelected(false);
                    }
                    break;
            }
        }
    } //Fin fillView
    
} //Fin Clase
