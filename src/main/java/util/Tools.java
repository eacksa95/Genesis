package util;

import java.awt.Component;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author user
 */
public class Tools {

    /**
     *@param panel JPanel de la Vista 
     *@return paramsMap
     */
    public static Map<String, String> paramsToMap(JPanel panel){
    Map<String, String> paramsMap = new HashMap<String, String>();
    //recorrer los elementos del panel
    for(Component comp : panel.getComponents()){
        if(comp instanceof JTextField){
        JTextField componente = (JTextField) comp;
        String nombreComponente = componente.getName();
        //obtener la clave para el Map a partir del nombre del componente
        String key = getKeyFromComponentName(nombreComponente, "textField");
        paramsMap.put(key, componente.getText().toLowerCase());
        }
        else if (comp instanceof JComboBox){
        JComboBox componente = (JComboBox) comp;
        String nombreComponente = componente.getName();
        //obtener la clave para el Map a partir del nombre del componente
        String key = getKeyFromComponentName(nombreComponente, "comboBox");
        String valor = componente.getSelectedItem().toString().toLowerCase();
        paramsMap.put(key, valor);
        }
    }
    return paramsMap;
    }
    
    /**
     * se usa en la funcion paramsToMap para conseguir el key
     * del (key, value) del Map a partir del nombre del componente
     * ejemplo: textFieldUserName -> UserName
    */
    private static String getKeyFromComponentName(String nombreComponente, String tipoComponente){
    int index;
    //verifica si el nombre del componente contiene el tipo del componente
    if (nombreComponente.toLowerCase().contains(tipoComponente.toLowerCase())){
    //obtener la parte del nombre del componente despues del tipo de componente
    index = nombreComponente.toLowerCase().indexOf(tipoComponente.toLowerCase()) + tipoComponente.length();
    return nombreComponente.substring(index).toLowerCase(); //devuelve el valor para key en lowercase
    } else{
        return nombreComponente;
    }
    }
    
    
    
    
    
    /**
    *@param psw input password que viene de LoginForm
    * encripta input password
    */
    public static String encryptMD5(String psw){
        String rtn="";
        String password = psw;
        String encryptedpassword = null;
        try{
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(password.getBytes());
            byte[] bytes = m.digest();
            StringBuilder s = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            encryptedpassword = s.toString();
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        rtn = encryptedpassword;
        //System.out.println("Text password: "+password);
        //System.out.println("Encrypted password MD5: "+encryptedpassword);
        return rtn;
    }//fin encryptMD5
    
    
    public static String arrayStringtoString(Object[] a) {
        if (a == null)
            return "null";

        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        //b.append('[');
        for (int i = 0; ; i++) {
            b.append(String.valueOf(a[i]));
            if (i == iMax)
                return b.toString(); //return b.append(']').toString();
            b.append(", ");
        }
    }
      
    public static boolean hasColumn(ResultSet rs, String column){
        try{
            rs.findColumn(column);
            return true;
        } catch (SQLException sqlex){
            //JOptionPane.showMessageDialog(null,  "Fecha incorrecta ");
            return false;
        }
        //return false;
    } 
}//FIN DE LA CLASE TOOLS
