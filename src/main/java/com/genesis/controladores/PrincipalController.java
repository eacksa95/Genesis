/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.genesis.controladores;

import com.genesis.vistas.ActiveFrame;
import com.genesis.vistas.Login;
import com.genesis.vistas.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JInternalFrame;

/**
 *
 * @author Ezequiel Cristaldo
 */
public class PrincipalController implements AccionesListener{
    private static Principal principalView;
    private static Map<String, String> paramsMap = new HashMap<>();
    private static ArrayList<Map<String, String>> alMapData;
    
    public static void mostrarPrincipalView(){
        principalView = new Principal();
        principalView.setTitle("Genesys");
        principalView.setVisible(true);
    }
    
    public static void getActiveInternalFrame(){
        //Obtener el JInternalFrame activo desde el JDesktopPane
        ActiveFrame activeFrame = (ActiveFrame) principalView.panelPrincipal.getSelectedFrame();
        JInternalFrame activeInternalFrame = activeFrame.getActive();
        System.out.println("tituloFrame:" + activeInternalFrame.getTitle());
    }
    
    @Override
    public void guardar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void eliminar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void primero() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void ultimo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void siguiente() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void anterior() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
