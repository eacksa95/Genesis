package com.genesis.model;

public class pojoPrecioDetalle {
    private int precioid;
    private String cod_barra;
    private String descripcion;
    private double precio;
 
    public pojoPrecioDetalle(int precioid, String cod_bara, String descripcion, double precio) {
        this.precioid = precioid;
        this.cod_barra = cod_bara;
        this.precio = precio;
        this.descripcion = descripcion; 
    }

    public pojoPrecioDetalle() {
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
            case "precioid" :
                return this.precioid;
                //break;
         
        
            default :
                return 0;
        }
    }

    public double getDouble(String arg){
        switch(arg){
            case "precio" :
                return this.precio;
                //break;
            
           
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
            case "precioid" :
                this.precioid = value;
                //break;
          
            
        }
    }

    public void setDouble(String attribute, double value){
        switch(attribute){
            case "precio" :
                this.precio = value;
             
        }
    }
    //=============================================
}


