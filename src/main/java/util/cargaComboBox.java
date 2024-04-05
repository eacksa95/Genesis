package util;

import java.sql.*;
import javax.swing.JOptionPane;
import com.genesis.model.conexion;

/**
 *
 * @author Ezequiel Cristaldo
 * Clase Util que sirve para manejar ComboBox
 */
public class cargaComboBox {
    public static int x = 400, y = 400;
    public static String ls_sql = "";
    public static ResultSet rs_tabla;
    public static int li_cant, li_ind = 0;
    public static int grupo[]; //Lista
    public static javax.swing.JComboBox cb_carga;
    public static String codigo = "";
    public static char caracter;
    public ResultSet rs_suc;

    public static void pv_cargar(javax.swing.JComboBox cb, String aTabla, String fields, String aCampoId, String aWhere) {
        
        //cb.removeAllItems();
        try {
            cb_carga = cb;
           
            li_ind = 0;
            ls_sql = "SELECT "+fields+" FROM " + aTabla;
            if (aWhere.length() > 0) {
                ls_sql = ls_sql + " WHERE " + aWhere;
            }
            if (aCampoId.length() > 0){
            ls_sql = ls_sql + " ORDER BY " + aCampoId;
            }
            rs_tabla = conexion.ejecuteSQL(ls_sql);
            rs_tabla.last();
            li_cant = rs_tabla.getRow();
            grupo = new int[li_cant];
            if (li_cant < 1) {
                return;
            }
           
            //System.out.println(nTabla);
            rs_tabla.first();
            grupo[li_ind] = rs_tabla.getInt(1);
            
            cb.addItem(rs_tabla.getString(1) + "-" + rs_tabla.getString(2));
            
            while (rs_tabla.next()) {
                grupo[li_ind] = rs_tabla.getInt(1);
               
                cb.addItem(rs_tabla.getString(1) + "-" + rs_tabla.getString(2));
                li_ind = li_ind + 1;
            }
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "No se pudo recuperar el registro. \n\r ERROR: " + erro);
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "No se pudo recuperar el registro de " + aTabla + " \n\r ERROR: " + e);
        }
        //System.out.println(aTabla);
    }

    public static String extraeCodigoComboBox(String args) {
        codigo = "";
        for (int i = 0; i < args.length(); i++) {
            caracter = args.charAt(i);
            if (caracter != '-') {
                codigo = codigo + caracter;
            } else {
                break;
            }
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
