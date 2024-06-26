package util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import com.genesis.model.conexion;
import com.genesis.model.tableModel;
import com.toedter.calendar.IDateEditor;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tools {
public static javax.swing.JMenuBar barra;
    private static ResultSet rs_suc;
    protected IDateEditor dateEditor;

    /**
     * Validacion del password ingresado en wLogin
     * @param passArray[] array de caracteres que se utiliza para analizar el password ingresado
     * caracter por caracter
     * @return rtn. true si hay error en el password
    */    
    public static boolean validatePsw(char passArray[]){
        boolean rtn = true;
        int caracterInvalido = 0;
        for (int i = 0; i < passArray.length; i++) {
            char c = passArray[i];
            if (!Character.isLetterOrDigit(c)) {
                caracterInvalido++;
            }
        }
        if (caracterInvalido > 0) {
            JOptionPane.showMessageDialog(null, "La contrase\u00F1a tiene carcteres inválidos!", "Atencion", JOptionPane.ERROR_MESSAGE);
            rtn = false;
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

/**
 * Actualmente la funcion recibe un valor String que viene de un JTable jtDetalle
 * @param value La cadena que representa un número.
 * value = jtDetalle.getModel().getValueAt(row, col).tostring()
 * 
 * @return El valor double correspondiente o 0.0 si la conversión falla.
 */
public static double sGetDecimalStringAnyLocaleAsDouble(String value) {
    // Definir la configuración de localización y el patrón de formato
    Locale locale = new Locale("es-PY", "PYT");
    DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(locale);
    String pattern = "###,###.###"; //formato por defecto
    decimalFormat.applyPattern(pattern);
    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    symbols.setDecimalSeparator(',');
    symbols.setGroupingSeparator('.');
    decimalFormat.setDecimalFormatSymbols(symbols);
    
    double d = 0.0; // Inicializar un valor numérico por defecto
    
    // Verificar si el valor no es nulo ni vacío
    if (value != null && !value.isEmpty()) {
        if (isNumeric(value)) {
            try { // Formatear el valor de String a Double con decimalFormat
                Number number = decimalFormat.parse(value);
                d = number.doubleValue();
                return d;
            } catch (ParseException ex) {
                Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else { //Si no es Numerico
            return 0.0;
        }
    } else {
        return 0.0; // Asignar un valor predeterminado si es nulo o vacío
    }
    return 0.0;
}
//    // Verificar si el valor es nulo y retornar 0.0 en ese caso
//    if (value == null) {
//        return 0.0;
//    }
//    // Obtener la configuración regional predeterminada del sistema
//    Locale theLocale = Locale.getDefault();
//    // Crear un formateador numérico según la configuración regional
//    NumberFormat numberFormat = DecimalFormat.getInstance(theLocale);
//    try {
//        // Intentar convertir la cadena a un número usando el formateador
//        Number theNumber = numberFormat.parse(value);
//        // Retornar el valor double del número
//        return theNumber.doubleValue();
//    } catch (ParseException e) {
//        // Si ocurre un error de parseo, intentar un enfoque alternativo
//        // Reemplazar todas las comas (,) en la cadena por puntos (.)
//        String valueWithDot = value.replaceAll(",", ".");
//        try {
//            // Intentar convertir la cadena modificada a un valor double
//            return Double.valueOf(valueWithDot);
//        } catch (NumberFormatException e2) {
//            // Si ocurre otro error, retornar 0.0
//            return 0.0;
//        }
//    }
//} // Fin sGetDecimalStringAnyLocaleAsDouble


    /**
     * Establece un formato para un numero decimal y devuelve en forma de String
     * @param numb numero que se desea formatear
     * @return rtn String del numero formateado
    */
    public static String decimalFormat(double numb) {
        DecimalFormat formatea = new DecimalFormat("#,###.##");
        return formatea.format(numb);
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

    /**
     * Se utiliza en practicamente todas las vistas para verificar que el usuario en sesion
     * tenga los permisos necesarios para realizar las operaciones que se necesiten
     * tanto para ver el menu en wPrincipal o para realizar operaciones de C,R,U,D
     * @param idrol id rol del usuario. Se obtiene de conexion
     * @param menuName menu seleccionado en wPrincipal para abrir la ventana actual
     * @param opcion operaciones: ver, C, R, U, D
     * @return 
     */
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
        } catch (SQLException error) {
            Logger.getLogger(tableModel.class.getName()).log(Level.SEVERE, null, error);
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
        String sql = "";
        double total = 0.0;
        double cotizacion = 0.0;
        int operador = 0;
        int decimales = 0;
        BigDecimal rtn;
        //VALIDACION DE MONEDAS
        if (origen == 0 || destino == 0) {
            //Mensaje de no se encuentra Moneda origen o destino
            return 0.0;
        }
        if (importe == 0 && defecto > 0) {
            importe = defecto;
        }
        if (origen == destino) { //Las monedas origen y destino son iguales
            return importe;
        }

        try {
            sql = "SELECT c.cotizacion, c.operacion, m.decimales "
                    + "FROM cotizaciones c, monedas m "
                    + "WHERE c.monedadestid = m.id "
                    + " AND monedaorigid = " + origen
                    + " AND monedadestid = " + destino
                    + " AND FROM_UNIXTIME(fecha, '%d/%m/%Y') = FROM_UNIXTIME(" + fecha + ", '%d/%m/%Y')";
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

    /**
     * Es una forma simple de verificar un valor es de tipo numérico
     *
     * @param strNum String que es el valor a identificar si es o no un número
     * @return boolean true si que es un número o false si es cualquier otro
     * valor distinto a un número
     */
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }//fin isNumeric
}//fin clase
