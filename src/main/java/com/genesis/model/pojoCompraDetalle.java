package com.genesis.model;

public class pojoCompraDetalle {
	
    private int compraid;
    private String cod_barra;
    private String descripcion;
    private double precio;
    private double cantidad;
    private double descuento;
    private double bonificado;
    private double total;


    public pojoCompraDetalle(){

    }

    public pojoCompraDetalle(int compraid, String cod_barra, String descripcion, 
            double precio, double cantidad, double descuento, double bonificado, double total) {
            super();
            this.compraid = compraid;
            this.cod_barra = cod_barra;
            this.descripcion = descripcion;
            this.precio = precio;
            this.cantidad = cantidad;
            this.descuento = descuento;
            this.bonificado = bonificado;
            this.total = total;
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
            case "compraid" :
                return this.compraid;
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
            case "cantidad" :
                return this.cantidad;
                //break;
            case "descuento" :
                return this.descuento;
                //break;
            case "bonificado" :
                return this.bonificado;
                //break;
            case "total" :
                return this.total;
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
            case "compraid" :
                this.compraid = value;
                //break;
        }
    }

    public void setDouble(String attribute, double value){
        switch(attribute){
            case "precio" :
                this.precio = value;
                //break;
            case "cantidad" :
                this.cantidad = value;
                //break;
            case "descuento" :
                this.descuento = value;
                //break;
            case "bonificado" :
                this.bonificado = value;
                //break;
            case "total" :
                this.total = value;
        }
    }
    //=============================================

}
