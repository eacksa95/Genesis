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
public class pojoAjusteDetalle {
    private int ajusteid;
    private String cod_barra;
    private String descripcion;
    private double cantidad_actual;
    private double cantidad_ajuste;
    private double impuesto;
    private double precio_bruto;
    private double precio_neto;
    
     public pojoAjusteDetalle(){

    }

    public pojoAjusteDetalle(int ajusteid, String cod_barra, String descripcion, double cantidad_actual, double cantidad_ajuste, double impuesto, double precio_bruto, double precio_neto) {
        this.ajusteid = ajusteid;
        this.cod_barra = cod_barra;
        this.descripcion = descripcion;
        this.cantidad_actual = cantidad_actual;
        this.cantidad_ajuste = cantidad_ajuste;
        this.impuesto = impuesto;
        this.precio_bruto = precio_bruto;
        this.precio_neto = precio_neto;
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
            case "impuesto" :
                return this.impuesto;
                //break;
            case "precio_bruto" :
                return this.precio_bruto;
                //break;
            case "precio_neto" :
                return this.precio_neto;
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
            case "impuesto" :
                this.impuesto = value;
                //break;
            case "precio_bruto" :
                this.precio_bruto = value;
                //break;
            case "precio_neto" :
                this.precio_neto = value;
        }
    }
    //=============================================
    
}
