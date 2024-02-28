package com.genesis.controladores;

import com.genesis.model.conexion;
import com.genesis.model.tableModel;
import com.genesis.vistas.Principal;
import com.genesis.vistas.Registros;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import util.Tools;

/**
 *
 * @author eacks
 */
public class RegistrosController {

    private static final Registros ventana = new Registros(); // Solo puede haber un JFrame par Registros
    private static JPanel panelVista = new JPanel();           // Se alternan los paneles para cambiar las vistas
    
    private static final tableModel tbModel = new tableModel();
    private static ArrayList<String> tableKeys = new ArrayList<>();
    
    private static Map<String, String> paramsMap = new HashMap<>();
    private static Map<String, String> paramsMapModel = new HashMap<>(); //params filtrados para tbmodel columns
    private static Map<String, Object> rtn = new HashMap<>();

    private static String opc = "";
    
    
    

    public static Registros getRegistrosContainer() {
        return ventana;
    }

    public static void mostrarRegistroPanel(JPanel vista, String modelo) {
        try {
            ventana.panelContent.removeAll();
            tbModel.init(modelo); //Se inicializa nuevo tableModel para la vista
            tableKeys = conexion.getKeyColumns(modelo, "PRI");
            panelVista = vista;
            
            ventana.setName("Registros");
            ventana.setTitle(modelo);
            // Establece BorderLayout y agrega componentes a la ventana
            ventana.panelContent.setLayout(new BorderLayout());
            ventana.panelContent.add(vista, BorderLayout.CENTER);

            ventana.panelContent.revalidate();
            ventana.panelContent.repaint();

            ventana.pack();
            ventana.setVisible(true);
            ventana.show();
        } catch (NullPointerException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void ProcesarSolicitud() {
        int rows = 0;
        String msg = "";
        paramsMap = Tools.paramsToMap(panelVista);
        opc = paramsMap.get("operacion");
        switch(opc){
            case "C": //Create
                //Aquí hay que decidir si se enviá a upd o sigue para insertar
                if(paramsMap.containsKey("id")){
                    if(!paramsMap.get("id").equals("")){
                        if(Integer.parseInt(paramsMap.get("id")) > 0){
                            ArrayList<Map<String, String>> alUPD ;
                            alUPD = new ArrayList<Map<String, String>>(); 
                            alUPD.add(paramsMap);
                            rows = updateReg(alUPD);
                            if(rows > 0){
                                rtn.put("msg", "¡Registro Actualizado correctamente!");
                                rtn.put("typeAlert", "alert-success");
                                rtn.put("rows", 1);
                                rtn.put("data", paramsMap);
                            }else{
                                rtn.put("msg","¡No se pudo actualizar el Registro!");
                                rtn.put("typeAlert", "alert-warning");
                                rtn.put("rows", 0);
                                rtn.put("data", "");
                            }
                            rtn.put("opc", "U");
                        }else{                                                 //Si el id = 0 entonces se inserta el registro 
                            rows = createReg(paramsMap);
                            if(rows > 0){
                                msg = "Registro grabado correctamente";
                                rtn.put("typeAlert", "alert-success");
                                rtn.put("msg", msg);
                                rtn.put("rows", rows);
                                rtn.put("data", paramsMap);
                            }else{
                                msg = "No se pudo grabar el Registro";
                                rtn.put("typeAlert", "alert-warning");
                                rtn.put("msg", msg);
                                rtn.put("rows", 0);
                                rtn.put("data", "");
                            }
                            rtn.put("opc", "C");
                        }
                    }else{
                        msg = "¡El campo ID no tiene valor asignado!";
                        rtn.put("typeAlert", "alert-danger");
                        rtn.put("msg", msg);
                        rtn.put("rows", 0);
                        rtn.put("data", "");
                        rtn.put("opc", "C");
                    }
                }else{
                    msg = "¡El campo ID no se ha enviado!"; 
                    rtn.put("typeAlert", "alert-danger");
                    rtn.put("msg", msg);
                    rtn.put("rows", 0);
                    rtn.put("data", "");
                    rtn.put("opc", "C");
                }                
                break;
            case "R": //Read
                if(paramsMap.containsKey("id")){                            //Asegura que exista un campo id
                    if(!paramsMap.get("id").equals("")){                    //Asegura que si hay id tenga un valor distinto a vacío
                        if(Integer.parseInt(paramsMap.get("id")) > 0){      //Si el id > 0 es porque se debe actualizar
                            Map<String, String> alUPD ;
                            alUPD = new HashMap<String, String>(); 
                            paramsMapModel = tbModel.justTableFields(paramsMap, false);
                            //System.out.println(paramsMapModel.toString());
                            alUPD = searchById(paramsMapModel);
                            System.out.println("tamaño "+alUPD.size());
                            if(alUPD.size() > 0){
                                msg = "¡Registro recuperado correctamente!";
                                rtn.put("typeAlert", "alert-success");
                                rtn.put("rows", 1);
                            }else{
                                msg = "¡No se pudo recuperar el Registro!";
                                rtn.put("typeAlert", "alert-warning");
                                rtn.put("rows", 0);
                            }
                            rtn.put("opc", "R");  
                            rtn.put("data", alUPD);
                            
                        }else{                                                 //Si el id = 0 entonces se inserta el registro                             
                            msg = "El ID no es válido";
                            rtn.put("typeAlert", "alert-warning");                            
                            rtn.put("opc", "R");
                            rtn.put("data", "");
                            rtn.put("rows", 0);
                        }
                    }else{
                        msg = "¡El campo ID no tiene valor asignado!";
                        rtn.put("typeAlert", "alert-danger");
                        rtn.put("opc", "R");
                        rtn.put("data", "");
                        rtn.put("rows", 0);
                    }
                }else{
                   msg = "¡El campo ID no fue enviado!"; 
                    rtn.put("typeAlert", "alert-danger");
                    rtn.put("opc", "R");
                    rtn.put("data", "");
                    rtn.put("rows", 0);
                }
              break;
            case "U":
              // code Update
                ArrayList<Map<String,String>> alReg;         //Declara array de Map, cada Map es para un registro
                alReg = new ArrayList<Map<String,String>>(); //Instancia array
                paramsMapModel = tbModel.justTableFields(paramsMap, false);
                alReg.add(paramsMapModel);
                rows = updateReg(alReg);
                if(rows > 0){
                    msg = "¡Registro actualizado correctamente!";
                    rtn.put("typeAlert", "alert-success");
                    rtn.put("rows", 1);
                }else{
                    msg = "¡No se pudo actualizar el Registro!";
                    rtn.put("typeAlert", "alert-warning");
                    rtn.put("rows", 0);
                }
                rtn.put("opc", "D");  
                rtn.put("data", "");                
              break;
            case "D":
                alReg = new ArrayList<Map<String,String>>(); //Instancia array
                paramsMapModel = tbModel.justTableFields(paramsMap, false);
                alReg.add(paramsMapModel);
                rows = deleteReg(alReg);
                if(rows > 0){
                    msg = "¡Registro eliminado correctamente!";
                    rtn.put("typeAlert", "alert-success");
                    rtn.put("rows", 1);
                }else{
                    msg = "¡No se pudo eliminar el Registro!";
                    rtn.put("typeAlert", "alert-warning");
                    rtn.put("rows", 0);
                }
                rtn.put("opc", "D");  
                rtn.put("data", "");
              break;
            case "F":
                Map<String, String> rtnMap ;
                rtnMap = new HashMap<String, String>();
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
        rtn.put("msg", msg);
        //createResponse(rtn, response);
    }//Fin processRequest
                        
    public static int createReg(Map<String, String> paramsMap){
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
    }//en createReg      
    
    public static int updateReg(ArrayList<Map<String,String>> registers){
        int rows = 0;
        for(Map<String,String> myRow : registers){        //En el mejor de los casos será un solo registro
            rows += tbModel.updateRegister(myRow);        //Hay que pasar un Map, devuelve cantidad de filas afectadas
        }
        return rows;
    }//end updateReg
    
    public static Map<String, String> searchById(Map<String, String> paramsMapModel){
        Map<String, String> rtn;
        Map<String, String> where;
        rtn = new HashMap<String, String>();
        where = createIdVal(paramsMapModel);
        rtn = tbModel.readRegisterById(paramsMapModel, where);
        System.out.println(rtn.toString());
        return rtn;
    }//fin searchById
    
    public static ArrayList<Map<String, String>> searchListById(Map<String, String> paramsMap, Map<String, String> where){ //ok
        ArrayList<Map<String, String>> rtn;
        rtn = new ArrayList<Map<String, String>>(); 
        System.out.println("tc 88 viewreg "+paramsMap);
        System.out.println("tc 89 where "+where);
        rtn = tbModel.readRegisterList(paramsMap, where);
        return rtn;
    }//fin searchListById
     
    /**
     * Método que prepara la navegación entre registros.
     * @param id String en el que se pasa el número de registro actual
     * @param goTo String en que se dice a qué posición se desea mover (FIRST, NEXT, PRIOR, LAST)
     * @return rtn Map que contiene el registro recuperado
     */
    public static Map<String, String> navegationReg(String id, String goTo){
        Map<String, String> rtn;
        System.out.println("tc 100 id "+id+" goto "+goTo);
        rtn = new HashMap<String, String>();
        rtn = tbModel.readNavetionReg (id, goTo); 
        System.out.println("tc 103 ");
        return rtn;
    }//fin searchById
    
    /**
     * Método que prepara para la eliminación de registro. Usa el método deleteRegister de la clase tableModel
     * @param id Sring el código actual en la vista
     * @return rtn int devuelve las filas afectadas
     */
    public static int deleteReg(ArrayList<Map<String,String>> registers){//ok
        int rows = 0;
        for(Map<String,String> myRow : registers){        //En el mejor de los casos será un solo registro
            rows += tbModel.deleteRegister(myRow);        //Hay que pasar un Map, devuelve cantidad de filas afectadas
        }
        return rows;                                      //Retorna la suma de todas las filas eliminadas
    }//fin deleteReg
                
    /**
     * Método que prepara la actualiación de un registro. Invoca métodos propios de la clase createIdVal, createSetFieldsValues; así como
     * el método updateRegister de la clase tableModel
     * @param viewRegister Map con los datos de la vista.
     * @return rtn int cantidad de filas afectadas.
     */
    
    /**
     * Método que construye un Map con sólo los campos de clave primar con sus respectivos valores.
     * @param viewRegister Map con los pares campo-valor de claves primarias
     * @return rtn Map de las claves de tabla con sus valores si los tiene
     */
    public static Map<String, String> createIdVal(Map<String, String> paramsMapModel){
        Map<String, String> rtn;
        rtn = new HashMap<String, String>();
        Iterator<String> arrayIterator = tableKeys.iterator();
        while(arrayIterator.hasNext()){
            String elemento = arrayIterator.next();
            if(paramsMapModel.containsKey(elemento)){
                rtn.put(elemento, paramsMapModel.get(elemento));
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
    

    
    public static int GuardarRegistro() {
        paramsMap = Tools.paramsToMap(panelVista);
        String operacion = paramsMap.get("operacion");
        if ("nuevo".equals(operacion)) { //guardar nuevo registro
            int max_id = tbModel.getMaxId();
            String nuevo_id = String.valueOf(max_id + 1);
            paramsMap.put("id", nuevo_id);
            paramsMapModel = tbModel.justTableFields(paramsMap, false);
            int rows = tbModel.saveRegister(paramsMapModel);
            return rows; //cantidad de registros insertados
        }
        if ("modificar".equals(operacion)) { //modificar registros
            paramsMap = Tools.paramsToMap(panelVista);
            paramsMapModel = tbModel.justTableFields(paramsMap, false);
            System.out.println("RegistrosController 68 GuardarRegistro paramsMap:" + paramsMap);
            int rows = tbModel.updateRegister(paramsMapModel);
            return rows; //cantidad de filas afectadas
        }
        return (-1);
    }
    
    public static int eliminarRegistro() {
        int rtn = 0;
        paramsMap = Tools.paramsToMap(panelVista);
        paramsMapModel = tbModel.justTableFields(paramsMap, false);
        int rows = tbModel.deleteRegister(paramsMapModel);
        return rows; //cantidad de registros eliminados
    }

public static DefaultTableModel cargarTabla() {
    Map<String, String> where = new HashMap<String, String>();
    String margarita = "margarita"; 
    //where.put("nombre", margarita); 

    Map<String, String> fields = new HashMap<String, String>();
    fields.put("*", "*");

    ArrayList<Map<String, String>> registros = tbModel.readRegister(fields, where);

    DefaultTableModel modeloTabla = new DefaultTableModel(); // Crear un nuevo modelo de tabla

    // Agregar las columnas al modelo de tabla
    for (String columnName : registros.get(0).keySet()) {
        modeloTabla.addColumn(columnName);
    }

    // Agregar los registros al modelo de tabla
    for (Map<String, String> registro : registros) {
        Object[] fila = new Object[registro.size()];
        int i = 0;
        for (String value : registro.values()) {
            fila[i++] = value;
        }
        modeloTabla.addRow(fila);
    }

    return modeloTabla;
}

    /**
     * Método que permite la búsqueda de un registro por el id. Recurre al método interno createIdVal
     * y a readRegisterById de la clase tableModel.
     * @param viewRegister Map par de campos y valores que se pasa desde la vista
     * @return rtn Map con el registro recuperado
     */





}
