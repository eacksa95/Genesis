/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genesis.model;

/**
 *
 * @author RC
 */
public class pojoTransferenciaDetalle {
   private int transferenciaid;
    private String cod_barra;
    private String descripcion;
    private int cantidad_pedido;
    private int cantidad_envio;

    public pojoTransferenciaDetalle(int transferenciaid, String cod_barra, String descripcion, int cantidad_pedido, int cantidad_envio) {
        this.transferenciaid = transferenciaid;
        this.cod_barra = cod_barra;
        this.descripcion = descripcion;
        this.cantidad_pedido = cantidad_pedido;
        this.cantidad_envio = cantidad_envio;
    }
    public pojoTransferenciaDetalle() {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    //===================== GETTERS =======================================
    public String getString(String arg){
        switch(arg){
            case "cod_barra" :
                return this.cod_barra;
                //break;
             case "descripcion" :
                return this.descripcion;
                //break;
            default :
                return "";
        }
    }
    
    public int getInteger(String arg){
        switch(arg){
            case "transferenciaid" :
                return this.transferenciaid;
                //break
                  case "cantidad_pedido" :
                return this.cantidad_pedido;
                //break
                  case "cantidad_envio" :
                return this.cantidad_envio;
                //break
            default :
                return 0;
        }
    }

   

    //===================== SETTERS =======================================
    
    public void setString(String attribute, String value){
        switch(attribute){
            case "cod_barra" :
                this.cod_barra = value;
                //break;
             case "descripcion" :
                this.descripcion = value;
                //break;
            
        }
    }
    public void setInteger(String attribute, int value){
        switch(attribute){
          case "transferenciaid" :
                this.transferenciaid = value;
                //break;
          case "cantidad_pedido" :
                this.cantidad_pedido = value;
                //break;
          case "cantidad_envio" :
                this.cantidad_envio = value;
                //break;
            
        }
    }
    //=============================================
}
