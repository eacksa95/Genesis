/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


/**
 * Esta clase permite gestionar la tabla y los eventos realizados sobre ella
 * cada celda seria un objeto personalizable
 * @author CHENAO
 *
 */
public class GestionCeldas extends DefaultTableCellRenderer{
	
    private String tipo="text";
    //Ver la forma de que la cantidad de decimales sea dinámica
    DecimalFormat formatea = new DecimalFormat("#,###.##");
    NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
    DecimalFormat df = new DecimalFormat();
    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    
    
    //se definen por defecto los tipos de datos a usar
    private Font normal = new Font( "Verdana",Font.PLAIN ,12 );
    private Font bold = new Font( "Verdana",Font.BOLD ,12 );
    
    //etiqueta que almacenará el icono a mostrar
    private JLabel label = new JLabel();
    //iconos disponibles para ser mostrados en la etiqueta dependiendo de la columna que lo contenga
//    private ImageIcon iconoGuardar = new ImageIcon(getClass().getResource("/images/ico_guardar.png"));
//    private ImageIcon iconoBuscar = new ImageIcon(getClass().getResource("/images/ico_buscar.png"));

    public GestionCeldas(){

    }

    /**
     * Constructor explicito con el tipo de dato que tendrá la celda
     * @param tipo
     */
    public GestionCeldas(String tipo){
        this.tipo = tipo;
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(symbols);
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
        Color colorFondoPorDefecto = new Color( 192, 192, 192);
        Color colorFondoSeleccion = new Color( 140, 140 , 140);
    	
        // Si la celda del evento es la seleccionada se asigna el fondo por defecto para la selección
        if (selected){                
            this.setBackground(colorFondoPorDefecto );   
        }else{
            //Para las que no están seleccionadas se pinta el fondo de las celdas de blanco
            this.setBackground(Color.white);
        }
                
        /*
         * Se definen los tipos de datos que contendrán las celdas basado en la instancia que
         * se hace en la ventana de la tabla al momento de construirla
         */
        if( tipo.equals("texto")){
            //si es tipo texto define el color de fondo del texto y de la celda así como la alineación
            if (focused) {
                colorFondo = colorFondoSeleccion;
            }else{
                colorFondo = colorFondoPorDefecto;
            }
            this.setHorizontalAlignment( JLabel.LEFT );
            
            //this.setText( (String) value );
            this.setValue((String) value );
            //this.setForeground( (selected)? new Color(255,255,255) :new Color(0,0,0) );   
            //this.setForeground( (selected)? new Color(255,255,255) :new Color(32,117,32) );
            this.setBackground( (selected)? colorFondo :Color.WHITE);	
            this.setFont(normal);   
            //this.setFont(bold);
            return this;
        }
         if (tipo.equals("numerico")) {
            double d = 0.0;
            Locale locale  = new Locale("es-PY", "PYT");
            String pattern = "###,###.###";
            DecimalFormat decimalFormat = (DecimalFormat)NumberFormat.getNumberInstance(locale);
            decimalFormat.applyPattern(pattern);
            Number number;
            
            String str = "";
            if (value != null && !value.toString().isEmpty()) {
                str = value.toString();
            }
            else {
                // Si el valor es nulo o una cadena vacía, asigna un valor predeterminado
                // o realiza alguna acción apropiada
                this.setValue("0.0");
                return this;
            }
            //System.out.println("El valor to string str "+str);
            try {
                df.parse(str);
                //System.out.println("El str parseado "+str);
                number = decimalFormat.parse(str);
                //System.out.println("El number parseado "+number);
                d = number.doubleValue();
                //System.out.println("El number en double "+d);
                //System.out.println("El number en double formateado "+formatea.format(d));
            } catch (ParseException ex) {
                Logger.getLogger(GestionCeldas.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (focused) {
                colorFondo = colorFondoSeleccion;
            } else {
                colorFondo = colorFondoPorDefecto;
            }
            this.setHorizontalAlignment(JLabel.RIGHT);

            //if(table.getModel().isCellEditable(row, column)){
            if (isNumeric((String) value + "")) {
                    this.setValue(formatea.format(d)+"");
            } else {
                this.setText("0.0");
            }
            this.setForeground((selected) ? new Color(255, 255, 255) : new Color(32, 117, 32));
            this.setBackground((selected) ? colorFondo : Color.WHITE);
            // this.setBackground( (selected)? colorFondo :Color.MAGENTA);
            this.setFont(bold);
            return this;
        }
        //si el tipo es icono entonces valida cual icono asignar a la etiqueta.
        if( tipo.equals("icono")){
            if( String.valueOf(value).equals("PERFIL") ){
            	//label.setIcon(iconoBuscar);
            }else if( String.valueOf(value).equals("EVENTO") ){
            	//label.setIcon(iconoGuardar);
            }
            label.setHorizontalAlignment( JLabel.LEFT );
            label.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
//            return boton;
            return label;
        }
        
        //definie si el tipo de dato el numerico para personalizarlo
        
        if( tipo.equals("numerico")){           
            if (focused) {
                colorFondo = colorFondoSeleccion;
            }else{
                colorFondo = colorFondoPorDefecto;
            }
            this.setHorizontalAlignment( JLabel.RIGHT );
            
            //if(table.getModel().isCellEditable(row, column)){
                if(isNumeric((String)value+"")){
                    String str = value.toString();
                    
                    Number number;
                    try {
                        number = format.parse(str);
                        df.parse(str);
                        double d = number.doubleValue();
                        //System.out.println("valor formateado "+formatea.format(d));
                        
                        //this.setText(formatea.format(d)); 
                        this.setValue(formatea.format(d)); 
                    } catch (ParseException ex) {
                        Logger.getLogger(GestionCeldas.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   
                    //this.setText(vd.toString());    
               // }else{
              //      this.setText("0.0");  //          
               // }
            }else{
                this.setText("0.0");
            }
            this.setForeground( (selected)? new Color(255,255,255) :new Color(32,117,32) );    
            this.setBackground( (selected)? colorFondo :Color.WHITE);
           // this.setBackground( (selected)? colorFondo :Color.MAGENTA);
            this.setFont(bold);            
            return this;   
        }
        
        if( tipo.equals("jComboBox")){           
            if (focused) {
                //colorFondo = colorFondoSeleccion;
            }else{
                colorFondo = colorFondoPorDefecto;
            }
            this.setHorizontalAlignment( JLabel.RIGHT );
            
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
            this.setForeground( (selected)? new Color(255,255,255) :new Color(32,117,32) );    
            this.setBackground( (selected)? colorFondo :Color.WHITE);
           // this.setBackground( (selected)? colorFondo :Color.MAGENTA);
            //this.setFont(bold);            
            //return this;   
            return comboBox;
        }
        
        
         if( tipo.equals("jCheckBox")){           
            if (focused) {
                //colorFondo = colorFondoSeleccion;
            }else{
                colorFondo = colorFondoPorDefecto;
            }
            this.setHorizontalAlignment( JLabel.RIGHT );
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
            this.setForeground( (selected)? new Color(255,255,255) :new Color(32,117,32) );    
            this.setBackground( (selected)? colorFondo :Color.WHITE);
  
            return checkBox;
        }
        	
        return this;	
    }	
    
    
    /**
     * Es una forma simple de verificar un valor es de tipo numérico
     * @param strNum String que es el valor a identificar si es o no un número
     * @return boolean true si que es un número o false si es cualquier otro valor distinto a un número
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
    
    
}//fin de la clase

