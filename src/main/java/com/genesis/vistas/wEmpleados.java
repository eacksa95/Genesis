package com.genesis.vistas;

import com.genesis.controladores.tableController;
import com.genesis.model.conexion;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import util.Tools;

/**
 * @author Ezequiel Cristaldo
 */
public class wEmpleados extends javax.swing.JInternalFrame implements ActiveFrame {
    
    private final tableController tc;
    private Map<String, String> myData;
    String currentField;
    javax.swing.JTextField idObj;
    String Opcion = "";
    String CRUD = "";
    String menuName = "";
    
    /**
     * Creates new form RegistrosEmpleados
     * @param menuName
     */
    public wEmpleados(String menuName) {
        initComponents();
        this.menuName = menuName;
        currentField = "";
        tc = new tableController();
        tc.init("empleados");
        myData = new HashMap<>();
        jtfId.setText("0");
        dateFieldFechaNacimiento.setDateFormatString("yyyy-MM-dd");
        dateFieldFechaIngreso.setDateFormatString("yyyy-MM-dd");
        dateFieldFechaIngreso.setDate(new Date());
        dateFieldFechaBaja.setDateFormatString("yyyy-MM-dd");
    }
    
    private void setData(){
        String fechaNacimiento, fechaIngreso, fechaBaja;
        myData.put("id", jtfId.getText());
        myData.put("nombre", jftNombre.getText());
        myData.put("apellido", jtfApellido.getText());
        myData.put("cedula", textFieldCedula.getText());
        
        if(dateFieldFechaNacimiento.getDate() != null){
        fechaNacimiento = String.valueOf(dateFieldFechaNacimiento.getDate().getTime() / 1000L);
        myData.put("fecha_nacimiento", fechaNacimiento);
        } else myData.put("fecha_nacimiento", "");
        
        if(dateFieldFechaIngreso.getDate() != null){
        fechaIngreso = String.valueOf(dateFieldFechaIngreso.getDate().getTime() / 1000L);
        myData.put("fecha_ingreso", fechaIngreso);
        } else myData.put("fecha_ingreso", "");
        
        if(dateFieldFechaBaja.getDate() != null){
        fechaBaja = String.valueOf(dateFieldFechaBaja.getDate().getTime() / 1000L);
        myData.put("fecha_baja", fechaBaja);
        } else myData.put("fecha_baja", "");

        myData.put("cargo", textFieldCargo.getText());
        myData.put("sueldo", textFieldSueldo.getText());
        myData.put("observacion", textFieldObservacion.getText());
    }

    private void resetData(){
        myData.put("id", "0");
        myData.put("nombre", "");
        myData.put("apellido", "");
        myData.put("cedula", "");
        myData.put("fecha_nacimiento", null);
        myData.put("fecha_ingreso", null);
        myData.put("fecha_baja", null);
        myData.put("cargo", "");
        myData.put("sueldo", "");
        myData.put("observacion", "");
        fillView(myData);
        jftNombre.requestFocus();
    }
    
    private void fillView(Map<String, String> data){
        for(Map.Entry<String, String> entry : data.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            switch(key){
                case"id":
                    jtfId.setText(value);
                    break;
                case "nombre":
                    jftNombre.setText(value);
                    break;
                case"apellido":
                    jtfApellido.setText(value);
                    break;
                case"cedula":
                    textFieldCedula.setText(value);
                    break;
                case"fecha_nacimiento":
                    if (value != null) {
                    long dateLong = Long.parseLong(value) * 1000L;
                    Date fechaNacimiento = new Date(dateLong);
                    // Establecer la fecha en el JDateChooser
                    dateFieldFechaNacimiento.setDate(fechaNacimiento);
                    } else {
                       dateFieldFechaNacimiento.setDate(null);
                    }
                    break;
                case"fecha_ingreso":
                    if (value != null) {
                    long dateLong = Long.parseLong(value) * 1000L;
                    Date fechaIngreso = new Date(dateLong);
                    // Establecer la fecha en el JDateChooser
                    dateFieldFechaIngreso.setDate(fechaIngreso);
                    } else {
                       dateFieldFechaIngreso.setDate(null);
                    }
                    break;
                case"fecha_baja":
                    if (value != null) {
                    long dateLong = Long.parseLong(value) * 1000L;
                    Date fechaBaja = new Date(dateLong);
                    // Establecer la fecha en el JDateChooser
                    dateFieldFechaBaja.setDate(fechaBaja);
                    } else {
                       dateFieldFechaBaja.setDate(null);
                    }
                    break;
                case"cargo":
                    textFieldCargo.setText(value);
                    break;
                case"sueldo":
                    textFieldSueldo.setText(value);
                    break;
                case"observacion":
                    textFieldObservacion.setText(value);
                    break;
            }//end swich
        }//end for
    }//end fillView
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        textFieldCedula = new javax.swing.JTextField();
        lblCedula = new javax.swing.JLabel();
        jtfApellido = new javax.swing.JTextField();
        lblApellido = new javax.swing.JLabel();
        jftNombre = new javax.swing.JTextField();
        lblNombre = new javax.swing.JLabel();
        lblId = new javax.swing.JLabel();
        jtfId = new javax.swing.JTextField();
        lblFechaNacimiento = new javax.swing.JLabel();
        lblFechaIngreso = new javax.swing.JLabel();
        lblFechaBaja = new javax.swing.JLabel();
        lblCargo = new javax.swing.JLabel();
        textFieldCargo = new javax.swing.JTextField();
        lblSueldo = new javax.swing.JLabel();
        lblObservacion = new javax.swing.JLabel();
        textFieldSueldo = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        textFieldObservacion = new javax.swing.JTextArea();
        dateFieldFechaBaja = new com.toedter.calendar.JDateChooser();
        dateFieldFechaIngreso = new com.toedter.calendar.JDateChooser();
        dateFieldFechaNacimiento = new com.toedter.calendar.JDateChooser();

        setClosable(true);
        setIconifiable(true);

        textFieldCedula.setName("textFieldCedula"); // NOI18N
        textFieldCedula.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textFieldCedulaFocusGained(evt);
            }
        });

        lblCedula.setText("N° Doc.");

        jtfApellido.setName("jtfApellido"); // NOI18N

        lblApellido.setText("Apellido");

        jftNombre.setName("jftNombre"); // NOI18N

        lblNombre.setText("Nombre");

        lblId.setText("ID");

        jtfId.setName("jtfId"); // NOI18N
        jtfId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtfIdFocusGained(evt);
            }
        });
        jtfId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfIdKeyPressed(evt);
            }
        });

        lblFechaNacimiento.setText("Fecha Nacimiento");

        lblFechaIngreso.setText("Fecha Ingreso");

        lblFechaBaja.setText("Fecha Baja");

        lblCargo.setText("Cargo");

        lblSueldo.setText("Sueldo");

        lblObservacion.setText("Observacion");

        textFieldObservacion.setColumns(20);
        textFieldObservacion.setRows(5);
        jScrollPane1.setViewportView(textFieldObservacion);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblFechaNacimiento)
                            .addComponent(lblCedula)
                            .addComponent(lblFechaIngreso)
                            .addComponent(lblFechaBaja)
                            .addComponent(lblCargo)
                            .addComponent(lblSueldo)
                            .addComponent(lblObservacion))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(textFieldCargo)
                            .addComponent(textFieldSueldo)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                            .addComponent(textFieldCedula, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dateFieldFechaBaja, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dateFieldFechaNacimiento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dateFieldFechaIngreso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblId)
                                    .addComponent(lblNombre))
                                .addGap(18, 18, 18))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblApellido)
                                .addGap(16, 16, 16)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jftNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                                .addComponent(jtfApellido))
                            .addComponent(jtfId, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblId))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombre)
                    .addComponent(jftNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(textFieldCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCedula))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblFechaNacimiento)
                    .addComponent(dateFieldFechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblFechaIngreso)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblFechaBaja)
                            .addComponent(dateFieldFechaBaja, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCargo)
                            .addComponent(textFieldCargo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblSueldo)
                            .addComponent(textFieldSueldo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblObservacion)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(38, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(dateFieldFechaIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtfIdFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfIdFocusGained
        this.currentField = "id";
        this.jtfId.selectAll();
    }//GEN-LAST:event_jtfIdFocusGained

    private void textFieldCedulaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textFieldCedulaFocusGained
        this.currentField = "cedula";
    }//GEN-LAST:event_textFieldCedulaFocusGained

    private void jtfIdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfIdKeyPressed
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                imBuscar();
         }
    }//GEN-LAST:event_jtfIdKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser dateFieldFechaBaja;
    private com.toedter.calendar.JDateChooser dateFieldFechaIngreso;
    private com.toedter.calendar.JDateChooser dateFieldFechaNacimiento;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTextField jftNombre;
    public javax.swing.JTextField jtfApellido;
    public javax.swing.JTextField jtfId;
    private javax.swing.JLabel lblApellido;
    private javax.swing.JLabel lblCargo;
    private javax.swing.JLabel lblCedula;
    private javax.swing.JLabel lblFechaBaja;
    private javax.swing.JLabel lblFechaIngreso;
    private javax.swing.JLabel lblFechaNacimiento;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblObservacion;
    private javax.swing.JLabel lblSueldo;
    private javax.swing.JTextField textFieldCargo;
    public javax.swing.JTextField textFieldCedula;
    private javax.swing.JTextArea textFieldObservacion;
    private javax.swing.JTextField textFieldSueldo;
    // End of variables declaration//GEN-END:variables
    

    @Override
    public void imGrabar(String CRUD){ // operacion C
        int validacion = Tools.validarPermiso(conexion.getGrupoId(), menuName, CRUD);
        if (validacion == 0){ 
            String msg = "NO TIENE PERMISO PARA REALIZAR ESTA OPERACIÓN ";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
        }
        
        int id = Integer.parseInt(jtfId.getText());
        if(validacion == 1){
            if (id > 0) {
                this.setData();
                this.imActualizar("U");
                String msg = "SE HA ACTUALIZADO EXITOSAMENTE EL REGISTRO: " + jtfId.getText();
                System.out.println(msg);
                JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
                return;
            }
            this.setData();
            int rowsAffected = this.tc.createReg(this.myData);
            if (rowsAffected > 0) {
                String msg = "SE CREÓ EL NUEVO REGISTRO: " + jtfId.getText();
                System.out.println(msg);
                JOptionPane.showMessageDialog(this, msg, "wEmpleados388...!", JOptionPane.DEFAULT_OPTION);
                imNuevo();
            } else {
                String msg = "NO PUDO CREARSE EL NUEVO REGISTRO: " + jtfId.getText();
                System.out.println(msg);
                JOptionPane.showMessageDialog(this, msg, "wEmpleados393...!", JOptionPane.DEFAULT_OPTION);
                imNuevo();
            }

        }
    }
    
    @Override
    public void imActualizar(String CRUD){
        this.CRUD = CRUD;
        String msg;
        int validacion = Tools.validarPermiso(conexion.getGrupoId(), menuName, CRUD);
        if(validacion == 0){ 
                msg = "NO TIENE PERMISO PARA REALIZAR ESTA OPERACIÓN ";
                JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
                return;
        }
        this.setData();
        ArrayList<Map<String, String>> alCabecera;         //Declara array de Map, cada Map es para un registro
        alCabecera = new ArrayList<>(); //Instancia array
        alCabecera.add(myData);                           //agrega el Map al array, para la cabecera será el mejor de los casos, es decir 1 registro 
        int rowsAffected = this.tc.updateReg(alCabecera); //Está guardando igual si en el detalle hay error
        if (rowsAffected < 1) {
            msg = "Error al intentar actualizar el registro: " + this.jtfId.getText();
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.ERROR_MESSAGE);
            return; // si tfId > 0 y no grabo cambios, entonces return
        } else {
            msg = "Se ha actualizado exitosamente el registro: " + this.jtfId.getText();
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    @Override
    public void imBorrar(String CRUD){
        this.CRUD = CRUD;
        int validacion = Tools.validarPermiso(conexion.getGrupoId(), menuName, CRUD);
        if(validacion == 0){
                String msg = "NO TIENE PERMISO PARA REALIZAR ESTA OPERACIÓN ";
                JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
                return;
        }
        if(validacion == 1){
        this.setData();
        ArrayList<Map<String, String>> alRegister;              //Declara un Array de Map
        alRegister = new ArrayList<Map<String, String>>();      //Instancia el array
        alRegister.add(myData);                                //Agregamos el map en el array
        int b = this.tc.deleteReg(alRegister);               //Invocamos el método deleteReg del Modelo que procesa un array
        //int b =   this.tc.deleteReg(tf_id_marca.getText());
        if (b <= 0) {
            String msg = "NO SE HA PODIDO ELIMINAR EL REGISTRO: " + jtfId.getText();
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
            return;
        }
        if (b > 0) {
            String msg = "EL REGISTRO: " + jtfId.getText() + " SE HA ELIMINADO CORRECTAMENTE";
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
        }
        imNuevo();
    }
    }
    
    @Override
    public void imNuevo(){
        this.resetData();
        this.fillView(myData);
    }
    
    @Override
    public void imFiltrar(){
    String sql;
        sql = "";
        if (currentField.equals("")) {
            return;
        }
        switch (currentField) {
            case "id":
                sql = "SELECT id AS codigo, "
                        + "CONCAT(nombre, ', ', apellido) AS descripcion "
                        + "FROM empleados "
                        + "WHERE LOWER(CONCAT(id, ' ',nombre)) LIKE '%";
                break;
            case "cedula":
//                sql = "SELECT cedula AS codigo, "
//                        + "CONCAT(nombre, ', ', apellido) AS descripcion "
//                        + "FROM empleados "
//                        + "WHERE LOWER(CONCAT(cedula, ' ',nombre)) LIKE '%";
//                codigo = textFieldCedula;
                break;
        }
        wBuscar frame = new wBuscar(sql, jtfId);
        frame.setVisible(true);
        wPrincipal.desktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.DEFAULT_OPTION);
        }
    }
    
   @Override
    public void imBuscar() {
        this.setData();
        myData = tc.searchById(this.myData);
        if (this.myData.size() <= 0) {
            String msg = "NO SE HA PODIDO RECUPERAR EL REGISTRO: " + jtfId.getText();
            this.resetData();
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
        }
        this.fillView(myData);
        System.out.println("RegEmpleados 357 V imBuscar myData: " + myData.toString());
    }

    @Override
    public void imPrimero() {
        this.setData();
        this.myData = this.tc.navegationReg(jtfId.getText(), "FIRST");
        this.fillView(this.myData);
    }

    @Override
    public void imSiguiente() {
        this.setData();
        this.myData = this.tc.navegationReg(jtfId.getText(), "NEXT");
        this.fillView(this.myData);
    }

    @Override
    public void imAnterior() {
        this.setData();
        this.myData = this.tc.navegationReg(jtfId.getText(), "PRIOR");
        this.fillView(this.myData);
    }

    @Override
    public void imUltimo() {
        this.setData();
        this.myData = this.tc.navegationReg(jtfId.getText(), "LAST");
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
   
  
}
