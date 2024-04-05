/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genesis.controladores;

import com.genesis.model.conexion;
import com.genesis.model.tableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author user
 */
public class tableController {
    //Claves primarias de la tabla
    private ArrayList<String> tableKeys;
    private Map<String, String> tableMap;
    //El modelo para nuestra tabla
    tableModel tm;
    
    public void init(String table){
        this.tableKeys = new ArrayList<String>();
        this.tableKeys = conexion.getKeyColumns(table, "PRI");     //Se recuperan todos los campos primarios  
        tm = new tableModel(); //Instanciamos el modelo de la tabla
        tm.init(table);        //Especificamos la tabla
    }//fin init
    
    public int getMaxId (){
        return this.tm.getMaxId();
    }
    
    public  Map<String, String> getTableMap(){
        return this.tableMap;
    }
    /**
     * Prepara y ejecuta sentencia para insertar un registro. Utiliza el método de saveRegister de tableModel
     * @param viewRegister Map<String, String> para clave valor de datos de la vista
     * @return rtn int valor según la ejecución haya sido exitoso el id o no 0.
     */
    public int createReg(Map<String, String> viewRegister){
        //Recibe todos los campos de la vista viewRegister
        //Limpiar el Map dejando sólo los campos de la tabla  
        int rtn, id;
        rtn = 0;   
        int size;
        tableMap = this.tm.justTableFields(viewRegister, false); 
        //Primero hay que decidir si se crea o se actualiza si es que ya existe
        if(this.tm.existAny(viewRegister) > 0){
//            System.out.println("Ya existe, se supone debe actualizar");
            rtn = this.tm.updateRegister(tableMap);
        }else{
            //enviar el nuevo Map para ser procesado
            size = this.tableKeys.size(); //Para saber cuántas claves tiene la tabla        
            if(size == 0){//si el detalle no tiene clave alguna
//                System.out.println("La tabla no tiene clave primaria asignada");
                rtn = this.tm.saveRegister(tableMap);
            }

            if(size >= 2){//Cuando es detalle por lo general tiene clave compuesta
//                System.out.println("La tabla tiene clave primaria compusta");
                rtn = this.tm.saveRegister(tableMap);
            }

            if(size == 1){
//                System.out.println("La tabla es de clave primaria única");
                String idname = this.tableKeys.get(0);
                id = this.tm.getMaxId(); // Para los detalles hay ver cual es el que se recupera
                id = id + 1;
                //System.out.println("el ID = "+id);
                viewRegister.put(idname, id+"");
                tableMap.put(idname, id+"");
                rtn = this.tm.saveRegister(tableMap);
                if(rtn > 0){
                    rtn = id;
                }
            }
        }
        return rtn;
    }//en createReg
    
    
    public int createRegString(Map<String, String> viewRegister){
        //Recibe todos los campos de la vista viewRegister
        //Limpiar el Map dejando sólo los campos de la tabla       
        tableMap = this.tm.justTableFields(viewRegister, false); 
        //enviar el nuevo Map para ser procesado
        int rtn;
        String id = new String();
        rtn = 0;   
        int size;
        size = this.tableKeys.size();
        if(size == 0 || size >= 2){//si es un detalle por lo general ya se le pasará
//            System.out.println("La tabla no tiene clave primaria asignada o es de clave compusta");
            rtn = this.tm.saveRegister(tableMap);
        }
        if(size == 1){
//            System.out.println("La tabla es de clave primaria única");
//            String idname = this.tableKeys.get(0);
//            id = this.tm.getMaxIdString(); // Para los detalles hay ver cual es el que se recupera
//            id = id + 1;
//            //System.out.println("el ID = "+id);
//            viewRegister.put(idname, id+"");
//            tableMap.put(idname, id+"");
            rtn = this.tm.saveRegister(tableMap);
        }
       
        return rtn;
    }//en createReg
    

    /**
     * Método que permite la búsqueda de un registro por el id. Recurre al método interno createIdVal
     * y a readRegisterById de la clase tableModel.
     * @param viewRegister Map par de campos y valores que se pasa desde la vista
     * @return rtn Map con el registro recuperado
     */
    public Map<String, String> searchById(Map<String, String> viewRegister){ //ok
        Map<String, String> rtn;
        Map<String, String> where;
        rtn = new HashMap<String, String>();
        where = this.createIdVal(viewRegister);
        rtn = this.tm.readRegisterById(viewRegister, where);
        return rtn;
    }//fin searchById
    
    public ArrayList<Map<String, String>> searchListById(Map<String, String> viewRegister, Map<String, String> where){ //ok
        ArrayList<Map<String, String>> rtn;
        rtn = new ArrayList<Map<String, String>>(); 
//        System.out.println("tc 132 viewreg "+viewRegister);
//        System.out.println("tc 133 where "+where);
        rtn = this.tm.readRegisterList(viewRegister, where);
        return rtn;
    }//fin searchById
    
    /**
     * Méttodo que prepara la navegación entre registros.
     * @param id String en el que se pasa el número de registro actual
     * @param goTo String en que se dice a qué posición se desea mover (FIRST, NEXT, PRIOR, LAST)
     * @return rtn Map que contiene el registro recuperado
     */
    public Map<String, String> navegationReg(String id, String goTo){
        Map<String, String> rtn;
//        System.out.println("tc 100 id "+id+" goto "+goTo);
        rtn = new HashMap<String, String>();
        rtn = this.tm.readNavetionReg (id, goTo); 
//        System.out.println("tc 103 ");
        return rtn;
    }//fin searchById
    
    /**
     * Método que prepara para la eliminación de registro. Usa el método deleteRegister de la clase tableModel
     * @param id Sring el código actual en la vista
     * @return rtn int devuelve las filas afectadas
     */
    public int deleteReg(ArrayList<Map<String,String>> registers){//ok
        int rows = 0;
        for(Map<String,String> myRow : registers){        //En el mejor de los casos será un solo registro
            rows += this.tm.deleteRegister(myRow);        //Hay que pasar un Map, devuelve cantidad de filas afectadas
        }
        return rows;                                      //Retorna la suma de todas las filas eliminadas
    }//fin deleteReg
    
    /**
     * Método que prepara la actualiación de un registro. Invoca métodos propios de la clase createIdVal, createSetFieldsValues; así como
     * el método updateRegister de la clase tableModel
     * @param viewRegister Map con los datos de la vista.
     * @return rtn int cantidad de filas afectadas.
     */
    public int updateReg(ArrayList<Map<String,String>> registers){
        int rows = 0;
        for(Map<String,String> myRow : registers){        //En el mejor de los casos será un solo registro
            rows += this.tm.updateRegister(myRow);        //Hay que pasar un Map, devuelve cantidad de filas afectadas
        }
        return rows;
    }//end updateReg
    
    /**
     * Método que construye un Map con sólo los campos de clave primar con sus respectivos valores.
     * @param viewRegister Map con los pares campo-valor de claves primarias
     * @return rtn Map de las claves de tabla con sus valores si los tiene
     */
    public Map<String, String> createIdVal(Map<String, String> viewRegister){
         Map<String, String> rtn;
        rtn = new HashMap<String, String>();
        Iterator<String> arrayIterator = this.tableKeys.iterator();
        while(arrayIterator.hasNext()){
            String elemento = arrayIterator.next();
            if(viewRegister.containsKey(elemento)){
                rtn.put(elemento, viewRegister.get(elemento));
            }
        }//fin while    
        return rtn;
    }//createIdVal
    
    /**
     * Método que construye un Map de pares campos-valores que no sean clave primaria en la tabla.
     * @param viewRegister Map de pareas campos-valores enviados desde la vista
     * @return rtn Map de pares campos-valores que no sean clave primaria en la tabla
     */
    public Map<String, String> createSetFieldsValues(Map<String, String> viewRegister){
        Map<String, String> rtn;
        rtn = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : viewRegister.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if(!this.tableKeys.contains(key)){
                rtn.put(key, value);
            }
        }  
        //System.out.println("C createSetFieldsValues RETURN "+rtn.toString());
        return rtn;
    }//createSetFieldsValues
   
}//fin de la clase
