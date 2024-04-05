package com.genesis.controladores;

import com.genesis.model.conexion;
import com.genesis.model.tableModel;
import com.genesis.vistas.ActiveFrame;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
//import javax.swing.table.DefaultTableModel;
import util.Tools;

/**
 * @author Ezequiel Cristaldo
 */
public class RegistrosController {
    
    private JPanel formPanel;
    private DefaultTableModel tablaVistaModel;
    private JTable tabView;
    
    private tableModel tbModel;
    private ArrayList<String> tableKeys = new ArrayList<>();
    private ArrayList<String> columnNames = new ArrayList<>(); //Nombre de todas las columnas de la tabla
    private ArrayList<String> columnTypes = new ArrayList<>(); //Tipo de cada columna de la tabla
    
    private Map<String, String> paramsMap = new HashMap<>();
    private Map<String, String> paramsMapModel = new HashMap<>(); //params filtrados para tbmodel columns
    private String opc = "";
    
    public void init(String modelo, JPanel view){
        tbModel = new tableModel();
        tbModel.init(modelo);
        tableKeys = conexion.getKeyColumns(modelo, "PRI");
        columnNames = conexion.getColumnNames(modelo);
        columnTypes = conexion.getColumnTypes(modelo);        
        formPanel = view;
    } //Fin init

    public void ProcesarSolicitud(String opcion) {
        int rows = 0;
        String msg = "";
        //paramsMap = Tools.paramsToMap(formPanel);
        opc = opcion;
        //System.out.println("operacion tipo:" + opc);
        switch(opc){
            case "C": //Create
                //Aquí hay que decidir si se enviá a upd o sigue para insertar
                if(paramsMap.containsKey("id")){                        //Asegura que exista un campo id
                    if(!paramsMap.get("id").equals("")){                //Asegura que si hay id tenga un valor distinto a vacío
                        if(Integer.parseInt(paramsMap.get("id")) > 0){  //Si el id > 0 es porque se debe actualizar
                            ArrayList<Map<String, String>> alUPD ;
                            alUPD = new ArrayList<>(); 
                            alUPD.add(paramsMap);
                            rows = updateReg(alUPD);
                            if(rows > 0){
                                msg = "Registro Actualizado Correctamente";
                                JOptionPane.showMessageDialog(formPanel, msg, "Información", JOptionPane.ERROR_MESSAGE);
                            }else{
                                msg = "No se pudo actualizar Registro";
                                JOptionPane.showMessageDialog(formPanel, msg, "Información", JOptionPane.ERROR_MESSAGE);
                            }
                        }else{   //Si el id = 0 entonces se inserta el registro 
                            rows = createReg(paramsMap);
                            if(rows > 0){
                                msg = "Registro grabado correctamente";
                                JOptionPane.showMessageDialog(formPanel, msg, "Información", JOptionPane.INFORMATION_MESSAGE);
                            }else{
                                msg = "No se pudo grabar el Registro";
                                JOptionPane.showMessageDialog(formPanel, msg, "Información", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }else{
                        msg = "¡El campo ID no tiene valor asignado!";
                        System.out.println("RegistrosController 117: El campo ID no tiene valor asignado");
                        JOptionPane.showMessageDialog(formPanel, msg, "Información", JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    msg = "¡El campo ID no se ha enviado!"; 
                    System.out.println("RegistrosController 126: El campo ID no se ha enviado");
                    JOptionPane.showMessageDialog(formPanel, msg, "Información", JOptionPane.ERROR_MESSAGE);
                }                
                break;
            case "R": //Read
                if(paramsMap.containsKey("id")){                            
                    if(!paramsMap.get("id").equals("")){                    
                        if(Integer.parseInt(paramsMap.get("id")) > 0){      
                            Map<String, String> alUPD ;
                            //alUPD = new HashMap<>(); 
                            paramsMapModel = tbModel.justTableFields(paramsMap, false);
                            alUPD = searchById(paramsMapModel);
                            System.out.println("tamaño "+alUPD.size());
                            if(!alUPD.isEmpty()){
                                msg = "¡Registro recuperado correctamente!";
                                JOptionPane.showMessageDialog(formPanel, msg, "Información", JOptionPane.INFORMATION_MESSAGE);
                            }else{
                                msg = "¡No se pudo recuperar el Registro!";
                                JOptionPane.showMessageDialog(formPanel, msg, "Información", JOptionPane.ERROR_MESSAGE);
                            }
                            
                        }else{                             
                            msg = "El ID no es válido";
                            JOptionPane.showMessageDialog(formPanel, msg, "Información", JOptionPane.ERROR_MESSAGE);
                        }
                    }else{
                        msg = "¡El campo ID no tiene valor asignado!";
                        JOptionPane.showMessageDialog(formPanel, msg, "Información", JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                   msg = "¡El campo ID no fue enviado!";
                   JOptionPane.showMessageDialog(formPanel, msg, "Información", JOptionPane.ERROR_MESSAGE);
                }
              break;
            case "U":
              // code Update
                ArrayList<Map<String,String>> alReg;
                alReg = new ArrayList<>();
                paramsMapModel = tbModel.justTableFields(paramsMap, false);
                alReg.add(paramsMapModel);
                rows = updateReg(alReg);
                //refrescarVista();
                if(rows > 0){
                    msg = "¡Registro actualizado correctamente!";
                    JOptionPane.showMessageDialog(formPanel, msg, "Información", JOptionPane.INFORMATION_MESSAGE);
                }else{
                    msg = "¡No se pudo actualizar el Registro!";
                    JOptionPane.showMessageDialog(formPanel, msg, "Información", JOptionPane.ERROR_MESSAGE);
                }               
              break;
            case "D":
                alReg = new ArrayList<>();
                paramsMapModel = tbModel.justTableFields(paramsMap, false);
                alReg.add(paramsMapModel);
                int respuesta = JOptionPane.showConfirmDialog(null, "Desea Eliminar Registro?");
                if(respuesta == 0){
                    rows = deleteReg(alReg);
                }
                if(rows > 0){
                    msg  = "Registro Eliminado";
                    JOptionPane.showMessageDialog(formPanel, msg, "Información", JOptionPane.ERROR_MESSAGE);
                }else{
                    msg = "¡No se pudo eliminar el Registro!";
                    System.out.println(msg);
                }
              break;
            case "F":
                Map<String, String> rtnMap ;
                rtnMap = new HashMap<>();
//                rtn.add(navegationReg("1", "F"));
//                if(rtnMap.size() > 0){
//                }
                break;
            case "N":
              // code Next
              break;
            case "P":
              // code Prior
              break;
            case "L":
              // code Last
              break;
            default:
              // code block
        }
    }//Fin ProcesarSolicitud

    public int createReg(Map<String, String> paramsMap){
        //Recibe todos los campos de la vista
        //Limpiar el Map dejando sólo los campos de la tabla
        paramsMapModel = tbModel.justTableFields(paramsMap, false);
        //enviar el nuevo Map para ser procesado
        int rtn, id;
        rtn = 0;   
        int size;
        size = tableKeys.size();
        if(size == 0 || size >= 2){//si es un detalle por lo general ya se le pasará
            System.out.println("La tabla no tiene clave primaria asignada o es de clave compusta");
            rtn = tbModel.saveRegister(paramsMapModel);
        }
        if(size == 1){
            System.out.println("La tabla es de clave primaria única");
            String idname = tableKeys.get(0);
            id = tbModel.getMaxId(); // Para los detalles hay ver cual es el que se recupera
            id = id + 1;
            //System.out.println("el ID = "+id);
            paramsMap.put(idname, id+"");
            paramsMapModel.put(idname, id+"");
            rtn = tbModel.saveRegister(paramsMapModel);
        }       
        return rtn;
    }//Fin createReg      
     
    /**
     * Método que prepara la actualiación de un registro.
     * utiliza el método updateRegister de la clase tableModel
     * @param viewRegister Map con los datos de la vista.
     * @return rows cantidad de filas afectadas.
     */
    public int updateReg(ArrayList<Map<String,String>> viewRegister){
        int rows = 0;
        for(Map<String,String> myRow : viewRegister){        //En el mejor de los casos será un solo registro
            rows += tbModel.updateRegister(myRow);        //Hay que pasar un Map, devuelve cantidad de filas afectadas
        }
        return rows;
    }//Fin updateReg
    
    /**
     * Método que permite la búsqueda de un registro por el id. Recurre al método interno createIdVal
     * y a readRegisterById de la clase tableModel.
     * @param viewRegister Map par de campos y valores que se pasa desde la vista
     * @return rtn Map con el registro recuperado
     */
    public Map<String, String> searchById(Map<String, String> viewRegister){
        Map<String, String> rtn;
        Map<String, String> where;
        rtn = new HashMap<String, String>();
        where = createIdVal(viewRegister);
        rtn = tbModel.readRegisterById(viewRegister, where);
        System.out.println(rtn.toString());
        return rtn;
    }//Fin searchById
    
    /**
     * Metodo que permite la busqueda de Lista de Registros filtrados.
     * @param fieldsToSelect
     * @param where
     * @return rtn
     */
    public ArrayList<Map<String, String>> searchListById(Map<String, String> fieldsToSelect, Map<String, String> where){
        ArrayList<Map<String, String>> rtn;
        rtn = new ArrayList<Map<String, String>>(); 
        System.out.println("rc 88 viewreg "+fieldsToSelect);
        System.out.println("rc 89 where "+where);
        rtn = tbModel.readRegisterList(fieldsToSelect, where);
        return rtn;
    }//Fin searchListById
     
    /**
     * Método que prepara la navegación entre registros.
     * @param id String en el que se pasa el número de registro actual
     * @param goTo String en que se dice a qué posición se desea mover (FIRST, NEXT, PRIOR, LAST)
     * @return rtn Map que contiene el registro recuperado
     */
    public Map<String, String> navegationReg(String id, String goTo){
        Map<String, String> rtn;
        System.out.println("tc 100 id "+id+" goto "+goTo);
        rtn = new HashMap<String, String>();
        rtn = tbModel.readNavetionReg (id, goTo); 
        System.out.println("tc 103 ");
        return rtn;
    }//Fin searchById
    
    /**
     * Método que prepara para la eliminación de registro. Usa el método deleteRegister de la clase tableModel
     * @param registers Sring el código actual en la vista
     * @return rtn int devuelve las filas afectadas
     */
    public int deleteReg(ArrayList<Map<String,String>> registers){
        int rows = 0;
        for(Map<String,String> myRow : registers){        //En el mejor de los casos será un solo registro
            rows += tbModel.deleteRegister(myRow);        //Hay que pasar un Map, devuelve cantidad de filas afectadas
        }
        return rows;                                      //Retorna la suma de todas las filas eliminadas
    }//Fin deleteReg
                
    /**
     * Método que construye un Map con sólo los campos de clave primar con sus respectivos valores.
     * @param paramsMapModel Map con los pares campo-valor de claves primarias
     * @return rtn Map de las claves de tabla con sus valores si los tiene
     */
    public Map<String, String> createIdVal(Map<String, String> paramsMapModel){
        Map<String, String> rtn;
        rtn = new HashMap<>();
        Iterator<String> arrayIterator = tableKeys.iterator();
        while(arrayIterator.hasNext()){
            String elemento = arrayIterator.next();
            if(paramsMapModel.containsKey(elemento)){
                rtn.put(elemento, paramsMapModel.get(elemento));
            }
        }//fin while    
        return rtn;
    }//Fin createIdVal
    
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
    }//Fin createSetFieldsValues
   
}
