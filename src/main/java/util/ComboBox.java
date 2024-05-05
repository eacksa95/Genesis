package util;

import java.sql.*;
import javax.swing.JOptionPane;
import com.genesis.model.conexion;

/**
 *
 * @author Ezequiel Cristaldo
 * Clase Util que sirve para manejar ComboBox
 */
public class ComboBox {
    public static String ls_sql = "";
    public static ResultSet rs;
    public static int li_cant, li_index;
    public static int grupo[]; //Lista
    public static javax.swing.JComboBox cb_carga;
    public ResultSet rs_suc;

    public static void pv_cargar(javax.swing.JComboBox cb, String aTabla, String fields, String aCampoId, String aWhere) {

        cb.removeAllItems();
        
        try { //construimos el SQL Query y resultSet se carga en cb
            cb_carga = cb;
            li_index = 0;
            ls_sql = "SELECT "+fields+" FROM " + aTabla;
            if (aWhere.length() > 0) {
                ls_sql = ls_sql + " WHERE " + aWhere;
            }
            if (aCampoId.length() > 0){
            ls_sql = ls_sql + " ORDER BY " + aCampoId;
            }
            rs = conexion.ejecuteSQL(ls_sql);
            rs.last();
            li_cant = rs.getRow(); //número total de filas
            grupo = new int[li_cant];
            if (li_cant < 1) { // no hay registros
                return;
            }
            rs.first();
            grupo[li_index] = rs.getInt(1);
            
            cb.addItem(rs.getString(1) + "-" + rs.getString(2));
            
            while (rs.next()) {
                grupo[li_index] = rs.getInt(1);
                cb.addItem(rs.getString(1) + "-" + rs.getString(2));
                li_index = li_index + 1;
            }
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "No se pudo recuperar el registro. \n\r ERROR: " + erro);
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "No se pudo recuperar el registro de " + aTabla + " \n\r ERROR: " + e);
        }
        //System.out.println(aTabla);
    }

    public static String ExtraeCodigo(String args) {
        String codigo = "";
        String caracter;
        for (int i = 0; i < args.length(); i++) {
            caracter = args.substring(i, i + 1);
            if (caracter.equals("-")) {
                break;
            } else {
                codigo = codigo + caracter;
            }
        }
        if (codigo.length() <= 0) {
            codigo = "0";
        }
        return codigo;
    }

    public void listSelected(javax.swing.JComboBox cb, String aTabla, String fields , String arg) {
         String nTabla= aTabla.substring(1,9);  
        try {
            javax.swing.JComboBox cb_carga;
            cb_carga = cb;
            rs_suc = conexion.ejecuteSQL("SELECT "+fields+" FROM " + aTabla + " WHERE " + arg + " ORDER BY 1");
            if (!rs_suc.first()) {
                return;
            }
            cb_carga.setSelectedItem(rs_suc.getInt(1) + "-" + rs_suc.getString(2));
            
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "No se pudo recuperar el registro. - ERROR: " + erro);
        }
    }
}
