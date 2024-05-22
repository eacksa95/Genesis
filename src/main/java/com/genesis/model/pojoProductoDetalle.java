package com.genesis.model;

public class pojoProductoDetalle {
	
    private int productoid;
    private String cod_barra;
    private int colorid;
    private int tamanoid;
    private int disenoid;
    private int uxb;
    private int stock;

    public pojoProductoDetalle(){
    }
    

    public pojoProductoDetalle(int productoid, String cod_barra, int colorid, int tamanoid, int disenoid, 
        int uxb, int stock) {
        super();
        this.productoid = productoid;
        this.cod_barra = cod_barra;
        this.colorid = colorid;
        this.tamanoid = tamanoid;
        this.disenoid = disenoid;
        this.uxb = uxb;
        this.stock = stock;
    }

    //===================== GETTERS =======================================
    public String getString(String arg){
        switch(arg){
            case "cod_barra" :
                return this.cod_barra;
                //break;
            default :
                return "";
        }
    }

    public int getInteger(String arg){
        switch(arg){
            case "productoid" :
                return this.productoid;
                //break;
            case "colorid" :
                return this.colorid;
                //break;
            case "tamanoid" :
                return this.tamanoid;
                //break;
            case "disenoid" :
                return this.disenoid;
                //break;
            case "uxb" :
                return this.uxb;
                //break;
            case "stock" :
                return this.stock;
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
            
        }
    }

    public void setInteger(String attribute, int value){
        switch(attribute){
            case "productoid" :
                this.productoid = value;
                //break;
            case "colorid" :
                this.colorid = value;
                //break;
                
            case "tamanoid" :
                this.tamanoid = value;
                //break;
                
            case "disenoid" :
                this.disenoid = value;
                //break;
                
            case "uxb" :
                this.uxb = value;
                //break;
                
              case "stock" :
                this.stock = value;
                //break;
                  
                
        }
    }

    
    //=============================================

}
