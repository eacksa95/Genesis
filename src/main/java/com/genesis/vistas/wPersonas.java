package com.genesis.vistas;

import com.genesis.model.conexion;
import com.genesis.controladores.tableController;
import util.ComboBox;
import util.Tools;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

public class wPersonas extends javax.swing.JInternalFrame implements ActiveFrame {

    private tableController tc;
    private Map<String, String> myData;
    String currentField;

    private DateFormat dateFormat, dateTimeFormat, dateIns; //= new SimpleDateFormat("dd/MM/yyyy HH:mm"); 
    String menuName = "";
    String CRUD = "";
    /**
     * Creates new form wPersonas
     */
    public wPersonas(String menuName) {
        initComponents();
        this.menuName = menuName;
        tc = new tableController();
        tc.init("personas");
        this.currentField = "";
        myData = new HashMap<>();

        // COMBO BOX DESPLEGABLES DE LAS TABLAS//
        ComboBox.pv_cargar(jCbxPais, "paises", " id, pais ", "id", "");
        ComboBox.pv_cargar(jCbxDepartamento, "departamentos", " id, nombre ", "id", "");
        ComboBox.pv_cargar(jCbxCiudad, "ciudades", "id, nombre", "id", "");
        ComboBox.pv_cargar(jCbxTipoPersona, "tipo_persona", "id, tipo", "id", "");

        // Inicializamos las fechas
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        dateIns = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        jDFechaNacimiento.setDate(new Date());

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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        tfid = new javax.swing.JTextField();
        tfNombre = new javax.swing.JTextField();
        tfiApellido = new javax.swing.JTextField();
        tfdocumento = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        tfdireccion = new javax.swing.JTextField();
        jDFechaNacimiento = new com.toedter.calendar.JDateChooser();
        tfCelular = new javax.swing.JTextField();
        tfTelefono = new javax.swing.JTextField();
        tfCorreo = new javax.swing.JTextField();
        jCbxPais = new javax.swing.JComboBox<>();
        jChEstado = new javax.swing.JCheckBox();
        jCbxDepartamento = new javax.swing.JComboBox<>();
        jCbxCiudad = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jCbxTipoPersona = new javax.swing.JComboBox<>();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("ABM Personas");

        jLabel1.setText("Id");

        jLabel2.setText("Nombre");

        jLabel3.setText("Apellidos");

        jLabel4.setText("Nro Documento");

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

        jLabel5.setText("Dirección");

        jLabel6.setText("Fecha de Nacimiento");

        jLabel7.setText("Celular");

        jLabel8.setText("Teléfono");

        jLabel9.setText("Correo");

        jLabel10.setText("País");

        jLabel11.setText("Departamento");

        jLabel12.setText("Ciudad");

        jLabel14.setText("Activo");

        jDFechaNacimiento.setDateFormatString("dd/MM/yyyy HH:mm");

        jCbxPais.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Seleccione País" }));

        jCbxDepartamento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Seleccione Departamento" }));

        jCbxCiudad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Seleccione Ciudad" }));
        jCbxCiudad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCbxCiudadActionPerformed(evt);
            }
        });

        jLabel13.setText("Tipo Persona");

        jCbxTipoPersona.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Seleccione Tipo Persona" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(43, 43, 43)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel3)
                                .addComponent(jLabel4)
                                .addComponent(jLabel5))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(tfiApellido, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tfdireccion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tfdocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(81, 81, 81)
                                            .addComponent(jLabel7))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                            .addContainerGap()
                                            .addComponent(jLabel8)
                                            .addGap(6, 6, 6)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jLabel9)
                                        .addGap(6, 6, 6)))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jLabel10)
                                    .addGap(6, 6, 6)))
                            .addGap(26, 26, 26)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(tfCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jCbxPais, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(tfCelular, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(82, 82, 82)
                            .addComponent(jLabel12)
                            .addGap(24, 24, 24)
                            .addComponent(jCbxCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(54, 54, 54)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel13)
                                .addComponent(jLabel11))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jCbxTipoPersona, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jCbxDepartamento, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jLabel14)
                                    .addGap(18, 18, 18)
                                    .addComponent(jChEstado)
                                    .addGap(10, 10, 10))))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(69, 69, 69)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(tfNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel1)
                                        .addComponent(jLabel2))
                                    .addGap(33, 33, 33)
                                    .addComponent(tfid, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(tfTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(26, 26, 26)
                                .addComponent(jDFechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(0, 37, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tfid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tfNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfiApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfdocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(tfdireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDFechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(tfCelular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(tfTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(tfCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jCbxPais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jCbxDepartamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jCbxCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jCbxTipoPersona, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jChEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(8, 8, 8)))
                .addContainerGap())
        );

        jDFechaNacimiento.getAccessibleContext().setAccessibleName("Fecha/Hora de carga en el sistema");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tfidFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfidFocusGained
        this.currentField = "persona";         // TODO add your handling code here:
    }//GEN-LAST:event_tfidFocusGained

    private void tfidKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfidKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.imBuscar();
        }
    }//GEN-LAST:event_tfidKeyPressed

    private void jCbxCiudadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCbxCiudadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCbxCiudadActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> jCbxCiudad;
    private javax.swing.JComboBox<String> jCbxDepartamento;
    private javax.swing.JComboBox<String> jCbxPais;
    private javax.swing.JComboBox<String> jCbxTipoPersona;
    private javax.swing.JCheckBox jChEstado;
    private com.toedter.calendar.JDateChooser jDFechaNacimiento;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField tfCelular;
    private javax.swing.JTextField tfCorreo;
    private javax.swing.JTextField tfNombre;
    private javax.swing.JTextField tfTelefono;
    private javax.swing.JTextField tfdireccion;
    private javax.swing.JTextField tfdocumento;
    private javax.swing.JTextField tfiApellido;
    private javax.swing.JTextField tfid;
    // End of variables declaration//GEN-END:variables

    @Override
    public void imGrabar(String crud) {
        String msg;
        if (Tools.validarPermiso(conexion.getGrupoId(), menuName, crud) == 0) {
            msg = "NO TIENE PERMISO PARA REALIZAR ESTA OPERACIÓN ";
            JOptionPane.showMessageDialog(this, msg, "wPersonas352...!", JOptionPane.OK_OPTION);
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
            JOptionPane.showMessageDialog(this, msg, "wPersonas365...!", JOptionPane.DEFAULT_OPTION);
            imNuevo();
        }
        if (rowsAffected <= 0){
            msg = "NO SE HA PODIDO CREAR EL REGISTRO";
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "wPersonas371...!", JOptionPane.DEFAULT_OPTION);
        }
    } //Fin imGrabar
    
    @Override
    public void imActualizar(String crud) {
        String msg;
        if (Tools.validarPermiso(conexion.getGrupoId(), menuName, crud) == 0) {
            msg = "NO TIENE PERMISO PARA REALIZAR ESTA OPERACIÓN ";
            JOptionPane.showMessageDialog(this, msg, "wPersonas380...!", JOptionPane.OK_OPTION);
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
            JOptionPane.showMessageDialog(this, msg, "wPersonas391...!", JOptionPane.DEFAULT_OPTION);
        } else {
            msg = "No se pudo actualizar el registro";
            JOptionPane.showMessageDialog(this, msg, "wPersonas394...!", JOptionPane.DEFAULT_OPTION);
        }
    } //Fin imActualizar

    @Override
    public void imBorrar(String crud) {
        String msg;
        if (Tools.validarPermiso(conexion.getGrupoId(), menuName, crud) == 0) {
            msg = "NO TIENE PERMISO PARA REALIZAR ESTA OPERACIÓN ";
            JOptionPane.showMessageDialog(this, msg, "wPersonas403...!", JOptionPane.OK_OPTION);
            return;
        }
        this.setData();
        ArrayList<Map<String, String>> alRegister;              //Declara un Array de Map
        alRegister = new ArrayList<>();                         //Instancia el array
        alRegister.add(myData);                                //Agregamos el map en el array
        int rowsAffected = this.tc.deleteReg(alRegister);               //Invocamos el método deleteReg del Modelo que procesa un array 
        //int b =   this.tc.deleteReg(tf_id.getText());
        if (rowsAffected <= 0) {
            msg = "NO SE HA PODIDO ELIMINAR EL REGISTRO: " + tfid.getText();
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "wPersonas415...!", JOptionPane.OK_OPTION);
            return;
        }
        if (rowsAffected > 0) {
            msg = "EL REGISTRO: " + tfid.getText() + " SE HA ELIMINADO CORRECTAMENTE";
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "wPersonas421...!", JOptionPane.DEFAULT_OPTION);
        }
        imNuevo();
    }    
    
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
    
    @Override
    public void imFiltrar() {
        String sql = "";
        
        if (currentField.equals("")) {
            return;
        }
        switch (currentField) {
            case "persona":
                sql = "SELECT id AS codigo ,"
                        + "CONCAT(nombres, ' - ',apellidos, ' - ',nro_doc) AS descripcion "
                        + "FROM personas "
                        + "WHERE LOWER(CONCAT(nro_doc, nombres, apellidos)) LIKE '%";
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
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.OK_OPTION);
        }
    }

    private void setData() {
        myData.put("id", tfid.getText());
        myData.put("nombres", tfNombre.getText());
        myData.put("apellidos", tfiApellido.getText());        
        myData.put("nro_doc", tfdocumento.getText());
        myData.put("direccion", tfdireccion.getText());
        //Fecha de Nacimiento
        String fecha = (jDFechaNacimiento.getDate().getTime() / 1000L) + "";
        myData.put("fecha_nac", fecha);
        myData.put("celular", tfCelular.getText());
        myData.put("telefono", tfTelefono.getText());
        myData.put("correo", tfCorreo.getText());
        myData.put("pais", ComboBox.ExtraeCodigo(jCbxPais.getSelectedItem().toString()));
        myData.put("departamento", ComboBox.ExtraeCodigo(jCbxDepartamento.getSelectedItem().toString()));
        myData.put("ciudad", ComboBox.ExtraeCodigo(jCbxCiudad.getSelectedItem().toString()));
        //Estado
        int estado = 0;
        if (jChEstado.isSelected()) { estado = 1; }
        myData.put("estado", estado + "");
        myData.put("tipo_persona", ComboBox.ExtraeCodigo(jCbxTipoPersona.getSelectedItem().toString()));
    }//fin setData

    private void resetData() {
        this.myData = new HashMap<>();
        java.util.Date df = new java.util.Date();
        this.myData.put("id", "0");
        this.myData.put("nombres", "");
        this.myData.put("apellidos", "");
        this.myData.put("nro_doc", "");
        this.myData.put("direccion", "");
        //Fecha Nacimiento
        jDFechaNacimiento.setDate(new Date());
        String fecha = (jDFechaNacimiento.getDate().getTime() / 1000L) + "";
        myData.put("fecha_nac", fecha);
        this.myData.put("celular", "");
        this.myData.put("telefono", "");
        this.myData.put("correo", "");
        this.myData.put("pais", "0");
        this.myData.put("departamento", "0");
        this.myData.put("ciudad", "0");
        this.myData.put("estado", "0");
        this.myData.put("tipo_persona", "0");
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
                case "nombres":
                    tfNombre.setText(value);
                    break;
                case "apellidos":
                    tfiApellido.setText(value);
                    break;
                case "nro_doc":
                    tfdocumento.setText(value);
                    break;
                case "direccion":
                    tfdireccion.setText(value);
                    break;
                case "fecha_nac":
                    dateLong = Long.parseLong(value) * 1000L;
                    df = new Date(dateLong);
                    jDFechaNacimiento.setDate(df);
                    break;
                case "celular":
                    tfCelular.setText(value);
                    break;
                case "telefono":
                    tfTelefono.setText(value);
                    break;
                case "correo":
                    tfCorreo.setText(value);
                    break;
                case "pais":
                    ComboBox.E_estado(jCbxPais, "paises", "id, pais", "id=" + value);
                    break;
                case "departamento":
                    ComboBox.E_estado(jCbxDepartamento, "departamentos", "id, nombre", "id=" + value);
                    break;
                case "ciudad":
                    ComboBox.E_estado(jCbxCiudad, "ciudades", "id, nombre", "id=" + value);
                    break;
                case "tipo_persona":
                    ComboBox.E_estado(jCbxTipoPersona, "tipo_persona", "id, tipo", "id=" + value);
                    break;
                case "estado":
                    if (Integer.parseInt(value) == 0) {
                        jChEstado.setSelected(false);
                    } else {
                        jChEstado.setSelected(true);
                    }
                    break;
            }//end switch
        }//end for
    }//end fill

}
