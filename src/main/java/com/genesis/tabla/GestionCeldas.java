package com.genesis.tabla;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import util.Tools;

/**
 * Esta clase permite gestionar la tabla y los eventos realizados sobre ella
 * cada celda seria un objeto personalizable
 */
public class GestionCeldas extends DefaultTableCellRenderer {

    private String tipo = "text";
    //se definen los tipos de fuentes a usar
    private Font normal = new Font("Verdana", Font.PLAIN, 12);
    private Font bold = new Font("Verdana", Font.BOLD, 12);

    /**
     * Constructor explicito con el tipo de dato que tendrá la celda
     *
     * @param tipo
     */
    public GestionCeldas(String tipo) {
        this.tipo = tipo;
    }

    /*
    * Este metodo controla toda la tabla, podemos obtener el valor que contiene
    * definir que celda está seleccionada, la fila y columna al tener el foco en ella.
    * 
    * cada evento sobre la tabla invocará a este metodo
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
        //definimos colores por defecto
        Color colorFondo = null;
        Color colorFondoPorDefecto = new Color(192, 192, 192);
        Color colorFondoSeleccion = new Color(140, 140, 140);

        // Si la celda del evento es la seleccionada se asigna el fondo por defecto para la selección
        if (selected) {
            this.setBackground(colorFondoPorDefecto);
        } else {
            //Para las que no están seleccionadas se pinta el fondo de las celdas de blanco
            this.setBackground(Color.white);
        }

        /*
         * Se definen los tipos de datos que contendrán las celdas basado en la instancia que
         * se hace en la ventana de la tabla al momento de construirla
         */
        //TIPO TEXTO -------------------------------------
        if (tipo.equals("texto")) {
            //si es tipo texto define el color de fondo del texto y de la celda así como la alineación
            if (focused) {
                colorFondo = colorFondoSeleccion;
            } else {
                colorFondo = colorFondoPorDefecto;
            }
            this.setHorizontalAlignment(JLabel.LEFT);
            this.setValue((String) value);
            this.setBackground((selected) ? colorFondo : Color.WHITE);
            this.setFont(normal);
            return this;
            //this.setFont(bold);
            //this.setText( (String) value );
            //this.setForeground( (selected)? new Color(255,255,255) :new Color(0,0,0) );   
            //this.setForeground( (selected)? new Color(255,255,255) :new Color(32,117,32) );
        } //Fin Tipo Texto

        //TIPO NUMERICO -------------------------------------
        if (tipo.equals("numerico")) {
            // Definir la configuración de localización y el patrón de formato
            Locale locale = new Locale("es-PY", "PYT");
            DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(locale);
            String pattern = "###,###.###";
            decimalFormat.applyPattern(pattern);
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator('.');
            decimalFormat.setDecimalFormatSymbols(symbols);
     
            double d = 0.0; // Inicializar un valor numérico por defecto
            
            // Verificar si el valor no es nulo ni vacío
            if (value != null && !value.toString().isEmpty()) {
                if (Tools.isNumeric(value.toString().replace(".", "").replace(",", "."))) {
                    try { // Formatear el valor de String a Double con decimalFormat
                        Number number = decimalFormat.parse(value.toString());
                        d = number.doubleValue();
                        this.setValue(decimalFormat.format(d) + "");
                    } catch (ParseException ex) {
                        Logger.getLogger(GestionCeldas.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else { //Si no es Numerico
                    this.setText("0.0");
                    JOptionPane.showMessageDialog(null, "GestionCeldas: Una Celda Recibio valor no Numerico", "ATENCIÓN...!", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                this.setValue("0.0"); // Asignar un valor predeterminado si es nulo o vacío
                JOptionPane.showMessageDialog(null, "GestionCeldas: Una Celda Recibio valor nulo o vacio", "ATENCIÓN...!", JOptionPane.ERROR_MESSAGE);
                return this;
            }

            // Determinar el color de fondo en función de si la celda está enfocada
            if (focused) {
                colorFondo = colorFondoSeleccion;
            } else {
                colorFondo = colorFondoPorDefecto;
            }
            this.setHorizontalAlignment(JLabel.RIGHT);

            // Ajustar el estilo de la celda
            this.setForeground((selected) ? new Color(255, 255, 255) : new Color(32, 117, 32));
            this.setBackground((selected) ? colorFondo : Color.WHITE);
            this.setFont(bold);
            return this;
        }

        if (tipo.equals("jComboBox")) {
            if (focused) {
                //colorFondo = colorFondoSeleccion;
            } else {
                colorFondo = colorFondoPorDefecto;
            }
            this.setHorizontalAlignment(JLabel.RIGHT);

            //if(table.getModel().isCellEditable(row, column)){            
            //Component cellComponent = super.getTableCellRendererComponent(table, value, selected, focused, row, column);
            //retorna un combo con el valor seleccionado
            JComboBox comboBox = new JComboBox();
            comboBox.addItem("0-Seleccione");
            //table.getColumnModel().getColumn(column).setCellEditor(new DefaultCellEditor(comboBox));
            //Set up tool tips for the sport cells.

            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
            renderer.setToolTipText("Click for combo box");
            table.getColumnModel().getColumn(column).setCellRenderer(renderer);
            //https://stackoverflow.com/questions/14355712/adding-jcombobox-to-a-jtable-cell
            this.setForeground((selected) ? new Color(255, 255, 255) : new Color(32, 117, 32));
            this.setBackground((selected) ? colorFondo : Color.WHITE);
            // this.setBackground( (selected)? colorFondo :Color.MAGENTA);
            //this.setFont(bold);            
            //return this;   
            return comboBox;
        }

        if (tipo.equals("jCheckBox")) {
            if (focused) {
                //colorFondo = colorFondoSeleccion;
            } else {
                colorFondo = colorFondoPorDefecto;
            }
            this.setHorizontalAlignment(JLabel.RIGHT);
            //if(table.getModel().isCellEditable(row, column)){            
            //Component cellComponent = super.getTableCellRendererComponent(table, value, selected, focused, row, column);

            //retorna un combo con el valor seleccionado
            JCheckBox checkBox = new JCheckBox();
            checkBox.setSelected(false);
            //table.getColumnModel().getColumn(column).setCellEditor(new DefaultCellEditor(comboBox));
            //Set up tool tips for the sport cells.

            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
            renderer.setToolTipText("Click for combo box");
            table.getColumnModel().getColumn(column).setCellRenderer(renderer);
            this.setForeground((selected) ? new Color(255, 255, 255) : new Color(32, 117, 32));
            this.setBackground((selected) ? colorFondo : Color.WHITE);

            return checkBox;
        }

        //si el tipo es icono entonces valida cual icono asignar a la etiqueta.
        if (tipo.equals("icono")) {
            // etiqueta que almacenará el icono a mostrar
            JLabel label = new JLabel();
            // iconos disponibles para ser mostrados en la etiqueta dependiendo de la columna que lo contenga
            // private ImageIcon iconoGuardar = new ImageIcon(getClass().getResource("/images/ico_guardar.png"));
            // private ImageIcon iconoBuscar = new ImageIcon(getClass().getResource("/images/ico_buscar.png"));
            if (String.valueOf(value).equals("PERFIL")) {
                //label.setIcon(iconoBuscar);
            } else if (String.valueOf(value).equals("EVENTO")) {
                //label.setIcon(iconoGuardar);
            }
            label.setHorizontalAlignment(JLabel.LEFT);
            label.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            // return boton;
            return label;
        }

        return this;
    }

}//fin de la clase
