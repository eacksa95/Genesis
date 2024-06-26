package com.genesis.model;


public class pojoAjusteDetalle {
    
    private int ajusteid;
    private String cod_barra;
    private String descripcion;
    private double cantidad_actual;
    private double cantidad_ajuste;
    
    public pojoAjusteDetalle(){

    }

    public pojoAjusteDetalle(int ajusteid, String cod_barra, String descripcion, double cantidad_actual, double cantidad_ajuste) {
        this.ajusteid = ajusteid;
        this.cod_barra = cod_barra;
        this.descripcion = descripcion;
        this.cantidad_actual = cantidad_actual;
        this.cantidad_ajuste = cantidad_ajuste;
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
            case "ajusteid" :
                return this.ajusteid;
                //break;
            default :
                return 0;
        }
    }

    public double getDouble(String arg){
        switch(arg){
            case "cantidad_actual" :
                return this.cantidad_actual;
                //break;
            case "cantidad_ajuste" :
                return this.cantidad_ajuste;
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
            case "ajusteid" :
                this.ajusteid = value;
                //break;
        }
    }

    public void setDouble(String attribute, double value){
        switch(attribute){
            case "cantidad_actual" :
                this.cantidad_actual = value;
                //break;
            case "cantidad_ajuste" :
                this.cantidad_ajuste = value;
                //break;
        }
    }
    
} //Fin Clase
