package com.genesis.controladores;

import com.genesis.model.conexion;
import com.genesis.model.tableModel;
import com.genesis.vistas.Login;
import com.genesis.vistas.Principal;
import java.awt.Component;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import util.Tools;

public class AuthController {
    //private static Principal principalView;
    private static Login loginView;
    private static Map<String, String> paramsMap = new HashMap<>();
    private static ArrayList<Map<String, String>> alMapData;
    
    public static void mostrarLoginView(){
        loginView = new Login();
        loginView.setVisible(true);
    }

    public static void iniciarSesion() {
        paramsMap = Tools.paramsToMap(loginView.formPanel);
        if (validateForm(paramsMap) == false) {
            JOptionPane.showMessageDialog(null, "Campos del Formulario imcompletos");
            return;
        } else {
            try {
                conexion.Conectar();
                int li_valido = 0;
                String usr = paramsMap.get("usuario");
                String psw = paramsMap.get("password");
                char passArray[] = psw.toCharArray();
                for (int i = 0; i < passArray.length; i++) {
                    char c = passArray[i];
                    if (!Character.isLetterOrDigit(c)) {
                        li_valido++;
                    }
                }
                if (li_valido > 0) {
                    System.out.println("La contrase\u00F1a tiene carcteres inválidos!");
                    return;
                }
                String vPass = new String(passArray);
                if (vPass.length() == 0) {
                    System.out.println("Ingrese su password!");
                    return;
                }
                String encryptedPSW = "";
                encryptedPSW = Tools.encryptMD5(vPass);
                System.out.println("AuthController.java 71 iniciarSesion CLAVE ENCRIPTADA: " + encryptedPSW);
                
                tableModel mUser = new tableModel();
                mUser.init("usuarios");
                
                Map<String, String> where = new HashMap<String, String>();      //Por qué campo buscar los registros
                Map<String, String> fields = new HashMap<String, String>();     //Los campos que vamos a recuperar
                ArrayList<Map<String, String>> alDetalle;                      //Declara array de Map, cada Map es para un registro
                
                fields.put("*", "*");
                where.put("username", usr);
                where.put("userpassword", encryptedPSW);
                alMapData = mUser.readRegister(fields, where);
                System.out.println("AuthController 85 IniciarSesion: cant. usuarios: "+alMapData.size());
                if(!alMapData.isEmpty()){
                System.out.println("AuthController 87 IniciarSesion: user: "+alMapData.get(0).get("username"));
                //mostrar principal y esconder login luego return;
                //principalView = new Principal();
                //principalView.setVisible(true);
                PrincipalController.mostrarPrincipalView();
                loginView.setVisible(false);
                }else{
                    System.out.println("no se encontro usuario");
                }
            } catch (SQLException ex) {
                System.out.println("AuthController 95 Iniciar sesion error" + ex);
            }
        }
    }

    public static boolean validateForm(Map<String, String> paramsMap) {
        System.out.println("AuthController.java 102 validateForm cantidad de parametros: " + paramsMap.size());
        if (paramsMap.size() == 2 && paramsMap.containsKey("usuario") && paramsMap.containsKey("password")) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Debe Completar todos los campos del formulario");
            return false;
        }
    }//fin validateForm
    
    public void mostrarMensaje() {
    System.out.println("Hola amigo");
    Component[] componentes = loginView.formPanel.getComponents();
    for (Component comp : componentes) {
        System.out.println("Tipo de componente: " + comp.getClass().getName());
        System.out.println("Nombre: " + comp.getName());
        System.out.println("Tamaño: " + comp.getSize());
        System.out.println("Ubicación: " + comp.getLocation());
        // Puedes agregar más información sobre el componente según tus necesidades
        System.out.println("-------------------------------------");
    }
}  
}

