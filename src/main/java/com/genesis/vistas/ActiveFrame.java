package com.genesis.vistas;

import javax.swing.JInternalFrame;


/**
 *
 * @author Ezequiel Cristaldo
 * 
 * Contrato que sera implementado por todas nuestras JInternalFrame
 * El objetivo de esta Interface es que todas las Ventanas internas del sistema
 * tengan una funcion getActive que nos ayudara a saber cual ventana se encuentra
 * activa en el DesktopPanel del PrincipalView
 * 
 */
public interface ActiveFrame {
    
    void imGrabar(String crud);
    void imFiltrar();
    void imActualizar(String crud);
    void imBorrar(String crud);
    void imNuevo();
    void imBuscar();
    void imPrimero();
    void imSiguiente();
    void imAnterior();
    void imUltimo();
    void imImprimir();
    void imInsDet();
    void imDelDet();
    void imCerrar();
    
    
    
}
