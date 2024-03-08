package com.genesis.model;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author Ezequiel Cristaldo
 */
public class conexion {
    public static Connection con = null;
    public static Statement statement;
    public static ResultSet resultset;
    private static PreparedStatement pstm;
    public static int psi_afectados;
    private static String driver = "";
    public static String db="";
    public static String url="";
    private static String usuario = "";
    private static String contrasena = "";
    private static String dbname = "";
    private static String dbtype = "";
    
    //Propiedades de la clase para almacenar usuario en sesion
    
    private static int il_usu = 0;
    private static String is_usu = "";
    private static int il_grupo = 0;
    private static String grupo_name;
    //Para leer archivo externo
    static FileInputStream inputStream = null;
    private static int ii_con = 0; //Cero si no se conecto o 1 si està conectado
    
    public static boolean Conectar() throws SQLException{
       boolean result = true;
       try{
            //Para acceder a los datos del .properties usaremos esta instruccion:
            //preguntar
            //inputStream = (FileInputStream) conexion.class.getClassLoader().getResourceAsStream("paronline.properties");

            inputStream =  new FileInputStream("C:\\Users\\eacks\\OneDrive\\Documentos\\NetBeansProjects\\genesis\\genesis.properties"); 
            //Ahora inicializamos el properties:
            Properties properties = new Properties ();
            try{		
                properties.load(inputStream);
                inputStream.close();
                //Y ahora si queremos los valores del properties:
                driver = properties.getProperty("driver");
                url = properties.getProperty("url");
                usuario = properties.getProperty("usuario");
                contrasena = properties.getProperty("contrasena");
                dbname = properties.getProperty("dbname");
                dbtype =  properties.getProperty("dbtype").replace("'", "");
                ii_con = 1;
            } catch(IOException ex) {
                ii_con = 0;
                System.out.println("Error imputstream 1");
                ex.printStackTrace();
            } 
        } catch (FileNotFoundException ex) {
            System.out.println("Error imputstream 2");
            ex.printStackTrace();
        } catch (NullPointerException nulo){
            System.out.println("Error imputstream 3");
            nulo.printStackTrace();
        } catch (Exception excep){
            System.out.println("Error imputstream 4");
            excep.printStackTrace();
        }
       
        try{
            Class.forName(driver);
            con = DriverManager.getConnection(url, usuario, contrasena);
            ii_con = 1;
        }catch(ClassNotFoundException Driver){
            System.out.println("Error driver 5" + Driver);
            JOptionPane.showMessageDialog(null, "Driver no localizado: "+Driver);
            result = false;
        }catch(SQLException Fonte){
            System.out.println("Error Conexion" + Fonte);
            JOptionPane.showMessageDialog(null, "Error en la conexion con la BD: "+Fonte);
            result = false;
            ii_con = 0;
        }
        
        return result;
    }
    
    public static void desconecta(){
        boolean result = true;
        try{
            con.close();
            System.out.println("BD cerrada");
            ii_con = 0;
        }catch(SQLException errorSQL){
            System.out.println("No fue posible cerrar la BD: "+errorSQL.getMessage());
            result = false;
        }
    }
    
    /* ResultSet executeQuery(String sql)
    * Devuelve un ResusltSet para su manipulación.
    * Utilizar para Select
    */
    public static ResultSet ejecuteSQL(String sql){
	try{
            System.out.println("conexion 121 ejecuteSQL SQL: "+sql);
            statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultset = statement.executeQuery(sql);
        }catch(SQLException sqlex){
            System.out.println("(conexion.ejecuteSQL)No fue posible ejecutar la instrucción QUERY: \n\r"+
                    sqlex.getMessage()+", \n\rEl sql pasado fue: "+sql);
            return null;
        }
        return resultset;
    }  
    
    /* int executeUpdate(String sql)
     * Devuelve la cantidad de registros afectados.  number of rows affected by the SQL statement (an INSERT typically affects one row, but an UPDATE or DELETE statement can affect more)
     */
    public static int ejecuteUPD(String sql){
        try{
            statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            psi_afectados = statement.executeUpdate(sql);
        }
        catch(SQLException sqlex){
            psi_afectados = 0;
            System.out.println("(conexion.ejecuteUPD 142)No fue posible ejecutar la instrucción: \n\r"+
                    sqlex.getMessage()+", \n\rEl sql pasado fue: "+sql);
        }
        return psi_afectados;
    }
    
    public static PreparedStatement prepStatement(String sql){
        try{
           pstm = con.prepareStatement(sql);
        } catch(SQLException sqlex){
            System.out.println("(conexionprpStatement)No fue posible ejecutar la instrucción UPDATE: \n\r"+
            sqlex.getMessage()+", \n\rEl sql pasado fue: "+sql);
        }
        return pstm;
    }
    
    //Una vez realizada al conexión se puede recuperar estos valores
    public static int getUserId(){
        return il_usu;
    }
    public static void setUserId(int aruser){
        il_usu = aruser;
    }

    public static String getUserName(){
        return is_usu;
    }
    public static void setUserName(String arname){
        is_usu = arname;
    }
    public static int getGrupoId(){
        return il_grupo;
    }
    public static void setGrupoId(int aruser){
        il_grupo = aruser;
    }
    public static String getGrupoName(){
        return grupo_name;
    }
    public static void setGrupoName(String arname){
        grupo_name = arname;
    }
    public static int isConected(){
        return ii_con;
    }
    
    //retorna las llaves primarias de tableName, siempre que columnkey sea igual a PRI
    //retorna Primary key de tableName. ColumnKey por default es PRI 
    public static ArrayList getKeyColumns(String tableName, String columnKey){
        ArrayList<String> fieldsKey = new ArrayList<String>();
        String sql = "";
        //System.out.println("tabla: "+tableName+ " Col: "+columnKey + " dbtype: "+dbtype);
        if(dbtype.equals("pgsql")){
            if(columnKey.equals("PRI")){
                columnKey = "PRIMARY KEY";
            }
            sql = "SELECT kcu.COLUMN_NAME " +
                "FROM information_schema.table_constraints tc " +
                "LEFT JOIN information_schema.key_column_usage kcu " +
                "ON tc.constraint_catalog = kcu.constraint_catalog " +
                "AND tc.constraint_schema = kcu.constraint_schema " +
                "AND tc.constraint_name = kcu.constraint_name " +
                "WHERE UPPER(tc.constraint_type) in ('"+columnKey+"') " +
                "AND tc.table_name = '"+tableName+"' " +
                "AND tc.table_catalog = '"+dbname+"'";          
        }
        if(dbtype.equals("mariadb")){
            sql = "SELECT c.COLUMN_NAME "
                + "FROM information_schema.COLUMNS c  "
                + "WHERE c.table_schema = '"+dbname
                +"' AND c.TABLE_NAME='"+tableName
                +"' AND c.COLUMN_KEY = '"+columnKey+"'";
        }
        //System.out.println("Conexion Sql: "+sql);
        resultset = ejecuteSQL(sql);
        try{
            while (resultset.next()) { 
                //System.out.println("key col: "+resultset.getString(1));
                fieldsKey.add(resultset.getString(1));
            }         
        }catch(SQLException sqlex){
            JOptionPane.showMessageDialog(null, "No fue posible ejecutar la instrucción QUERY: \n\r"+
                    sqlex.getMessage()+", \n\rEl sql pasado fue: "+sql);
        }
        return fieldsKey;
    }//fin getKeyColumns
   
    //retorna los nombres de los columnas de tableName
    public static ArrayList getColumnNames(String tableName){
        String sql="";
        ArrayList<String> columnNames = new ArrayList<String>();
        if(dbtype.equals("pgsql")){
            sql = "SELECT COLUMN_NAME, DATA_TYPE "
                + "FROM information_schema.COLUMNS "
                + "WHERE table_catalog = '"+dbname+"' AND TABLE_NAME='"+tableName+"'";
        }
        
        if(dbtype.equals("mariadb")){
            sql = "SELECT COLUMN_NAME, DATA_TYPE "
                + "FROM information_schema.COLUMNS "
                + "WHERE table_schema = '"+dbname+"' AND TABLE_NAME='"+tableName+"'";
        }
        try{
            
            resultset = ejecuteSQL(sql);
            while (resultset.next()) { 
                columnNames.add(resultset.getString(1));
            }
        }catch(SQLException sqlex){
            JOptionPane.showMessageDialog(null, "No fue posible ejecutar la instrucción QUERY: \n\r"+
                    sqlex.getMessage()+", \n\rEl sql pasado fue: "+sql);
            return null;
        }
        return columnNames;
    }//fin getColumnNames
    
    //retorna dataType de columnas de tablename
    public static ArrayList getColumnTypes(String tableName){
        String sql="";
        ArrayList<String> columnTypes = new ArrayList<String>();
        if(dbtype.equals("pgsql")){
            sql = "SELECT COLUMN_NAME, DATA_TYPE " +
                "FROM information_schema.columns " +
                "WHERE table_schema = 'public' " +
                "AND table_catalog = '"+dbname+"' " +
                "AND table_name = '"+tableName+"'";
        }
        if(dbtype.equalsIgnoreCase("mariadb")){
            sql = "SELECT COLUMN_NAME, DATA_TYPE "
                + "FROM information_schema.COLUMNS "
                + "WHERE table_schema = '"+dbname+"' AND TABLE_NAME='"+tableName+"'";
        }
        try{
            resultset = ejecuteSQL(sql);
            while (resultset.next()) { 
                columnTypes.add(resultset.getString(2));
            }
        }catch(SQLException sqlex){
            JOptionPane.showMessageDialog(null, "No fue posible ejecutar la instrucción QUERY: \n\r"+
                    sqlex.getMessage()+", \n\rEl sql pasado fue: "+sql);
            return null;
        }
        return columnTypes;
    }//fin getColumnType
}//Fin de la clase

