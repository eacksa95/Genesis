package com.genesis.vistas;

import com.genesis.model.conexion;
import com.genesis.controladores.tableController;
import com.genesis.model.tableModel;
import util.Tools;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class wRol extends javax.swing.JInternalFrame implements ActiveFrame {

    private final tableController tc;
    private final tableController tcMenu;
    private final tableController tcPermisos;
    private Map<String, String> myData;
    String currentField;
    
    //variables para control de permisos
    String menuName = "";
    String CRUD = "";
    int idRol = conexion.getGrupoId();
    
    /**
     * /**
     * Creates new form wRol
     * @param menuName nombre del menu accionado desde wPrincipal
     */
    public wRol(String menuName) {
        initComponents();
        this.menuName = menuName;
        tc = new tableController();
        tcMenu = new tableController();
        tcPermisos = new tableController();
        tc.init("roles");
        tcMenu.init("menus");
        tcPermisos.init("permisos");
        this.currentField = "";
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

        tfid = new javax.swing.JTextField();
        tfnombre = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jCheckActivo = new javax.swing.JCheckBox();
        jCheckAdm = new javax.swing.JCheckBox();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Abm Roles");

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

        jLabel1.setText("Id");

        jLabel2.setText("Rol");

        jCheckActivo.setText("Activo");

        jCheckAdm.setText("Adm");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckActivo)
                    .addComponent(tfnombre, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfid, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckAdm))
                .addGap(38, 38, 38))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tfid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tfnombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jCheckAdm)
                .addGap(18, 18, 18)
                .addComponent(jCheckActivo)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tfidKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfidKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            imBuscar();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_tfidKeyPressed

    private void tfidFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfidFocusGained
        this.currentField = "roles";          // TODO add your handling code here:
    }//GEN-LAST:event_tfidFocusGained


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckActivo;
    private javax.swing.JCheckBox jCheckAdm;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField tfid;
    private javax.swing.JTextField tfnombre;
    // End of variables declaration//GEN-END:variables

    
    /**
     * Grabar Rol. Leeme
     * si se crea un nuevo Rol entonces se crean los permisos basicos para el rol.
     * si es admin se otorgan permiso a los menuNames para admin
     * @param crud 
     */
    @Override
    public void imGrabar(String crud) {
         this.CRUD = crud;
        if (Tools.validarPermiso(idRol, menuName, crud) == 0) {
            String msg = "NO TIENE PERMISO PARA REALIZAR ESTA OPERACIÓN ";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
        }
        int id, rows;
        id = Integer.parseInt(tfid.getText());
        if (id > 0) { //Actualizar
            this.imActualizar("U");
            return;
        }
        if (id == 0) { //Crear Nuevo
            this.setData();
            rows = this.tc.createReg(this.myData);
            if (rows > 0) {
                //Establecer permisos para el nuevo Rol
                id = this.tc.getMaxId();
                this.setPermisos(myData.get("admin"), id); //si admin entonces permiso total, sino permisos Limitados
                String msg = "SE CREÓ EL NUEVO REGISTRO: " + id;
                System.out.println(msg);
                JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
                imNuevo();
            }
            
        }
        
    }

    /**
    * Prepara SQL para buscador y ejecuta ventana de busqueda
    */
    @Override
    public void imFiltrar() {
        String sql;
        sql = "";

        if (currentField.equals("")) {
            return;
        }
        switch (currentField) {
            case "roles":
                sql = "SELECT id AS codigo, "
                        + "rol AS descripcion "
                        + "FROM roles "
                        + "WHERE LOWER(CONCAT(rol, admin, activo)) LIKE '%";
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

    @Override
    public void imActualizar(String crud) {
        this.CRUD = crud;
        if (Tools.validarPermiso(conexion.getGrupoId(), menuName, crud) == 0) {
            String msg = "NO TIENE PERMISO PARA REALIZAR ESTA OPERACIÓN ";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
        }
        System.out.println("V imActualizar");
        this.setData();
        ArrayList<Map<String, String>> alCabecera;         //Declara array de Map, cada Map es para un registro
        alCabecera = new ArrayList<Map<String, String>>(); //Instancia array
        alCabecera.add(myData);                           //agrega el Map al array, para la cabecera será el mejor de los casos, es decir 1 registro 
        int rowsAffected = this.tc.updateReg(alCabecera);
        if (rowsAffected > 0){
            String msg = "SE HA ACTUALIZADO EXITOSAMENTE EL REGISTRO: " + tfid.getText();
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
        }
    }

    @Override
    public void imBorrar(String crud) {
         this.CRUD = crud;
        if (Tools.validarPermiso(idRol, menuName, crud) == 0) {
            String msg = "NO TIENE PERMISO PARA REALIZAR ESTA OPERACIÓN ";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
        }
        this.setData();
        ArrayList<Map<String, String>> alRegister;              //Declara un Array de Map
        alRegister = new ArrayList<Map<String, String>>();      //Instancia el array
        alRegister.add(myData);                                //Agregamos el map en el array
        int b = this.tc.deleteReg(alRegister);               //Invocamos el método deleteReg del Modelo que procesa un array
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
       imNuevo();
    }

    @Override
    public void imNuevo() {
       this.fillView(myData);
        resetData();
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
        myData.put("rol", tfnombre.getText());
        //check Activo
        int activo = 0; 
        if (jCheckActivo.isSelected()) {
            activo = 1;
        }
        myData.put("activo", activo + "");
        //check Admin
        int admin = 0; 
        if (jCheckAdm.isSelected()) {
            admin = 1;
        }
        myData.put("admin", admin + "");
    }//fin setData

    private void resetData() {
        myData.put("id", "0");
        myData.put("rol", "");
        myData.put("admin", "0");
        myData.put("activo", "0");

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
                case "rol":
                    tfnombre.setText(value);
                    break;

                case "admin":
                    if (Integer.parseInt(value) == 1) {
                        jCheckAdm.setSelected(true);
                    } else {
                        jCheckAdm.setSelected(false);
                    }
                    break;

                case "activo":
                    if (Integer.parseInt(value) == 1) {
                        jCheckActivo.setSelected(true);
                    } else {
                        jCheckActivo.setSelected(false);
                    }
                    break;
            }//end switch
        }//end for
    }//end fill

    private void setPermisos(String isAdmin, int idRol){
        int maxMenuId = this.tcMenu.getMaxId();
        String menu;
        int idNuevoRol = idRol;
        String rolName = myData.get("rol");
        Map<String, String> myPermisos = new HashMap<>();
        String sql = "";
        ResultSet rs;
        // Como minimo el Rol de un usuario debe permitir ver los menus Basicos del sistema que son Archivo y Editar
        //el ultimo miten de Editar actualmente es menuId 16
        for (int menuIndex = 0; menuIndex <= maxMenuId; menuIndex++){
            sql = "SELECT m.menu FROM menus m "
                + "WHERE m.id = " + menuIndex;
            try {
                rs = conexion.ejecuteSQL(sql);
                while (rs.next()) {
                    myPermisos.put("menu", rs.getString("menu")); //Nombre del menu actual
                }//end while
            } //end try
            catch (SQLException ex) {
                    Logger.getLogger(tableModel.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            myPermisos.put("id", "0");
            myPermisos.put("rolid", idNuevoRol + "");
            myPermisos.put("rol", rolName);
            myPermisos.put("menuid", menuIndex + "");
            //Si Admin
            if("1".equals(isAdmin)){
                myPermisos.put("ver", "1");
                myPermisos.put("c", "1");
                myPermisos.put("r", "1");
                myPermisos.put("u", "1");
                myPermisos.put("d", "1");
            }
            //No es Admin
            if("0".equals(isAdmin)){
                if(menuIndex < 18){ //Archivo y Editar llegan hasta 17
                    myPermisos.put("ver", "1");
                    myPermisos.put("c", "1");
                    myPermisos.put("r", "1");
                    myPermisos.put("u", "1");
                    myPermisos.put("d", "1");
                } else {
                    myPermisos.put("ver", "0");
                    myPermisos.put("c", "0");
                    myPermisos.put("r", "0");
                    myPermisos.put("u", "0");
                    myPermisos.put("d", "0");
                }
            }//end IF no es Admin
            System.out.println("myPermisos: " + myPermisos);
            myPermisos.put("id", "0 ");
            int rows = this.tcPermisos.createReg(myPermisos);
            if (rows < 1) {
                String msg = "ERROR AL ASIGNAR PERMISOS AL ROL: " + rolName;
                System.out.println(msg);
                JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
                return;
            }
        } //endFor

    }
}