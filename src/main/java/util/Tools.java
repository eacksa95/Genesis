package util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import com.genesis.model.conexion;
import com.toedter.calendar.IDateEditor;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class Tools {
public static javax.swing.JMenuBar barra;
    private static ResultSet rs_suc;
    protected IDateEditor dateEditor;

//    public static void E_estado(javax.swing.JComboBox cb, String aTabla, String arg) {
//        try {
//            //javax.swing.JComboBox cb_carga;
//            //cb_carga = cb;
//            rs_suc = conexion.ejecuteSQL("SELECT * FROM " + aTabla + " WHERE " + arg + " ORDER BY 1");
//            if (!rs_suc.first()) {
//                return;
//            }
//
//            cb.setSelectedItem(rs_suc.getString(1) + "-" + rs_suc.getString(2));
//
//        } catch (SQLException erro) {
//            JOptionPane.showMessageDialog(null, "No se pudo recuperar el registro. - ERROR: " + erro);
//        }
//    }
    

    
    public static boolean validatePsw(char passArray[]){
        boolean rtn = false;
        int li_valido = 0;
        for (int i = 0; i < passArray.length; i++) {
            char c = passArray[i];
            if (!Character.isLetterOrDigit(c)) {
                li_valido++;
            }
        }
        if (li_valido == 0) {
            JOptionPane.showMessageDialog(null, "La contrase\u00F1a tiene carcteres inválidos!", "Atencion", JOptionPane.ERROR_MESSAGE);
            rtn = true;
        }
        return rtn;
    }

    public boolean fechaCorrecta(String fecha) {
        if ((fecha.substring(2, 3)).compareTo("/") == 0) {
            int año = Integer.parseInt(fecha.substring(6));
            int mes = Integer.parseInt(fecha.substring(3, 5));
            int dia = Integer.parseInt(fecha.substring(0, 2));
            if (año > 1900) {
                if (mes > 0 && mes < 13) {
                    int tope;
                    if (mes == 1 || mes == 3 || mes == 5 || mes == 7 || mes == 8 || mes == 10 || mes == 12) {
                        tope = 31;
                    } else if (mes == 2) {
                        if (año % 4 == 0) {
                            tope = 29; //es bisiesto 
                        } else {
                            tope = 28;
                        }
                    } else {
                        tope = 30;
                    }
                    if (dia > 0 && dia < tope + 1) {
                        return true;
                    } else {
                        JOptionPane.showMessageDialog(null, "Fecha incorrecta ");
                        return false;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Fecha incorrecta ");
                    return false;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Fecha incorrecta ");
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Fecha incorrecta ");
            return false;
        }
    }//fin fechacorrecta

    public static boolean hasColumn(ResultSet rs, String column) {
        try {
            rs.findColumn(column);
            return true;
        } catch (SQLException sqlex) {
            //JOptionPane.showMessageDialog(null,  "Fecha incorrecta ");
            return false;
        }
        //return false;
    }

    public static String encryptMD5(String psw) {
        String rtn = "";
        String password = psw;
        String encryptedpassword = null;
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(password.getBytes());
            byte[] bytes = m.digest();
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));

            }
            encryptedpassword = s.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        rtn = encryptedpassword;
        //System.out.println("Text password: "+password);
        //System.out.println("Encrypted password MD5: "+encryptedpassword);
        return rtn;
    }

    public static double sGetDecimalStringAnyLocaleAsDouble(String value) {
        if (value == null) {
            //System.out.println("CORE - Null value!");
            return 0.0;
        }

        Locale theLocale = Locale.getDefault();
        NumberFormat numberFormat = DecimalFormat.getInstance(theLocale);
        Number theNumber;
        try {
            theNumber = numberFormat.parse(value);
            return theNumber.doubleValue();
        } catch (ParseException e) {
            // The string value might be either 99.99 or 99,99, depending on Locale.
            // We can deal with this safely, by forcing to be a point for the decimal separator, and then using Double.valueOf ...
            //http://stackoverflow.com/questions/4323599/best-way-to-parsedouble-with-comma-as-decimal-separator
            String valueWithDot = value.replaceAll(",", ".");

            try {
                return Double.valueOf(valueWithDot);
            } catch (NumberFormatException e2) {
                // This happens if we're trying (say) to parse a string that isn't a number, as though it were a number!
                // If this happens, it should only be due to application logic problems.
                // In this case, the safest thing to do is return 0, having first fired-off a log warning.
//                System.out.println("CORE - Warning: Value is not a number" + value);
                return 0.0;
            }
        }
    }

    public static String decimalFormat(double numb) {
        String rtn;
        DecimalFormat formatea = new DecimalFormat("#,###.##");
        rtn = formatea.format(numb);
        return rtn;
    }

    /**
     * Sets the date format string. E.g "MMMMM d, yyyy" will result in "July 21,
     * 2004" if this is the selected date and locale is English.
     *
     * @param dfString The dateFormatString to set.
     */
    public void setDateFormatString(String dfString) {
        dateEditor.setDateFormatString(dfString);
    }

    /**
     * Gets the date format string.
     *
     * @return Returns the dateFormatString.
     */
    public String getDateFormatString() {
        return dateEditor.getDateFormatString();
    }

    /**
     * Returns the date. If the JDateChooser is started with a null date and no
     * date was set by the user, null is returned.
     *
     * @return the current date
     */
    public Date getDate() {
        return dateEditor.getDate();
    }

    public static int validarPermiso(int idrol, String menuName, String opcion) {
        ResultSet sql;
        int valor = 0;
        try {
            sql = conexion.ejecuteSQL("SELECT " + opcion
                    + " FROM permisos WHERE rolid = " + idrol
                    + " AND menu = '" + menuName + "'");
            if (!sql.next()) {
                valor = 0;
            } else {
                valor = sql.getInt(opcion);

            }
//            System.out.println("SQL FUNCION VALIDARPERMISO;" + sql);
        } catch (Exception error) {

        }
        return valor;
    }

    /**
     *
     * @param origen int Id Moneda Origen
     * @param destino int Id Moneda Destino
     * @param fecha bigint/long fecha de la contización
     * @param importe double en la moneda origen
     * @param defecto cotización por defecto
     * @return double convertido a moneda destino y redondeado
     */
    public static double cambiarCotizacion(int origen, int destino, long fecha, double importe, double defecto) {
        String sql;
        double total, cotizacion;
        BigDecimal rtn;
        int operador, decimales;

        operador = 0;
        decimales = 0;
        total = 0.0;
        sql = "";
        cotizacion = 0.0;

        if (origen == 0 || destino == 0) {
            //Mensaje de no se encuentra Moenda origen o destino
            return 0.0;
        }
        if (importe == 0 && defecto > 0) {
            importe = defecto;
        }
        if (origen == destino) {
            //Las monedas origen y destino son iguales
            return importe;
        }

        try {
            sql = "SELECT cotizacion, operacion, decimales "
                    + "FROM SYS_COTIZACIONES c, SYS_MONEDAS m "
                    + "WHERE c.monedadestid = m.id "
                    + " AND monedaorigid = " + origen
                    + " AND monedadestid = " + destino
                    + " AND FROM_UNIXTIME(fecha, '%d/%m/%Y') = FROM_UNIXTIME(" + fecha + ", '%d/%m/%Y')";

//            System.out.println("sql = " + sql);
            rs_suc = conexion.ejecuteSQL(sql);
            if (!rs_suc.first()) {
                return 0.0;
            }
            operador = rs_suc.getInt("operacion"); //0 = multiplicar y 1 = Dividir
            cotizacion = rs_suc.getDouble("cotizacion");
            if (defecto > 0) {
                cotizacion = defecto;
            }
            decimales = rs_suc.getInt("decimales");
            //Si rtn = 0 returnar algo...
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "No se pudo recuperar el registro. - ERROR: " + erro);
        }
        //
        if (operador == 0) {                //Multiplica
            total = cotizacion * importe;
            rtn = new BigDecimal(total).setScale(decimales, RoundingMode.HALF_UP);
        } else {                            //Divide
            total = importe / cotizacion;
            rtn = new BigDecimal(total).setScale(decimales, RoundingMode.HALF_UP);
        }
//        System.out.println("de: " + origen + " a: " + destino + " oper " + operador + " cotiz: " + cotizacion + " rtn: " + total + " dec: " + decimales);
        return rtn.doubleValue();
    }
    public static void procesarMenus(javax.swing.JMenuBar b) {
        barra = b;
        javax.swing.JMenu m_menu;
        javax.swing.JMenu m_submenu;
        javax.swing.JMenu m_item;
        javax.swing.JMenu m_subitem;
//        System.out.println("Entro en metodo procesarMenus");
        String s_nombre = "", s_text = "", s_menu = "";
        int cantm = 0, cod = 0, cants = 0, canti = 0, cantj = 0, li_grabados = 0;
        int li_grabar = 0, li_cm, li_csm, li_ci, li_csi; //utilizo para saber si 1-grabo o 0-modifico

        cantm = (int) barra.getMenuCount();
        cod = 0;
        //System.out.println(s_nombre+" con "+cantm+" menus");
        if (cantm > 0) {
            li_cm = 0;
            for (int m = 0; m < cantm; m++) {
                s_nombre = "";
                li_cm++;

                if (barra.getMenu(m) instanceof javax.swing.JMenu) {
                    cod++;
                    m_menu = barra.getMenu(m);
                    s_nombre = m_menu.getName();
                    if (Tools.validarPermiso(conexion.getGrupoId(), s_nombre, "ver") == 0) {
                        m_menu.setVisible(false);
                        cants = 0; //Tenemos que prevenir que haga el siguiente if(cants >0)
                    } else {
                        m_menu.setVisible(true);
                        cants = m_menu.getItemCount();
                    }
                }

                if (cants > 0) {
                    li_csm = 1;
                    m_menu = barra.getMenu(m);
                    for (int s = 0; s < cants; s++) {
                        s_nombre = "";
                        if (m_menu.getItem(s) instanceof javax.swing.JMenu) {
                            cod++;
                            m_submenu = (javax.swing.JMenu) m_menu.getItem(s);
                            s_nombre = m_submenu.getName();
                            if (Tools.validarPermiso(conexion.getGrupoId(), s_nombre, "ver") == 0) {
                                m_submenu.setVisible(false);
                                cants = 0; //Tenemos que prevenir que haga el siguiente if(cants >0)
                            } else {
                                m_submenu.setVisible(true);
                                canti = m_submenu.getItemCount();
                                li_csm++;
                            }                            
                        } else {
                            if (m_menu.getItem(s) instanceof javax.swing.JMenuItem) {
                                cod++;
                                s_nombre = m_menu.getItem(s).getName();
                                if (Tools.validarPermiso(conexion.getGrupoId(), s_nombre, "ver") == 0) {
                                    m_menu.getItem(s).setVisible(false);
                                    canti = 0; //Tenemos que prevenir que haga el siguiente if(cants >0)
                                } else {
                                    m_menu.getItem(s).setVisible(true);
                                    canti = 0;
                                    li_csm++;
                                }
                            } else {
                                canti = 0;
                            }
                        }
                        
                        if (canti > 0) {
                            li_ci = 1;
                            m_submenu = (javax.swing.JMenu) m_menu.getItem(s);
                            for (int i = 0; i < canti; i++) {
                                s_nombre = "";
                                if (m_submenu.getItem(i) instanceof javax.swing.JMenu) {
                                    cod++;
                                    m_item = (javax.swing.JMenu) m_submenu.getItem(i);
                                    s_nombre = m_item.getName();
                                    if (Tools.validarPermiso(conexion.getGrupoId(), s_nombre, "ver") == 0) {
                                        m_item.setVisible(false);
                                        cantj = 0; //Tenemos que prevenir que haga el siguiente if(cants >0)
                                    } else {
                                        m_item.setVisible(true);
                                        cantj = m_item.getItemCount();
                                        li_ci++;
                                    }
                                } else {
                                    if (m_submenu.getItem(i) instanceof javax.swing.JMenuItem) {
                                        cod++;
                                        s_nombre = m_submenu.getItem(i).getName();
                                        if (Tools.validarPermiso(conexion.getGrupoId(), s_nombre, "ver") == 0) {
                                            m_submenu.getItem(i).setVisible(false);
                                            cantj = 0; //Tenemos que prevenir que haga el siguiente if(cants >0)
                                        } else {
                                            m_submenu.getItem(i).setVisible(true);
                                            cantj = 0;
                                            li_ci++;
                                        }
                                    } else {
                                        cantj = 0;
                                    }
                                }
                                //System.out.println(s_nombre+" con "+cantj+" subitems");
                                if (cantj > 0) {
                                    li_csi = 0;
                                    for (int j = 0; j < cantj; j++) {
                                        cod++;
                                        s_nombre = "";
                                        s_text = "";
                                        m_subitem = (javax.swing.JMenu) m_menu.getItem(i);
                                        li_csi++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }//fin procesarMenus

}//fin clase
