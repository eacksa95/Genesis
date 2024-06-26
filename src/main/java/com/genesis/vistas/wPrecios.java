package com.genesis.vistas;

import com.genesis.model.conexion;
import com.genesis.model.pojoPrecioDetalle;
import com.genesis.model.tableModel;
import com.genesis.tabla.GestionCeldas;
import com.genesis.tabla.GestionEncabezadoTabla;
import com.genesis.tabla.ModeloTabla;
import com.genesis.controladores.tableController;
import util.ComboBox;
import util.Tools;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyVetoException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class wPrecios extends javax.swing.JInternalFrame implements MouseListener, KeyListener, ActiveFrame {

    private Map<String, String> myData;
    private HashMap<String, String> myDet;
    private tableController tc;
    private tableController tcdet;

    ArrayList<pojoPrecioDetalle> lista;
    ArrayList<pojoPrecioDetalle> listaDetalles;

    String currentSelection;

    JCheckBox jcbAprobado;

    ModeloTabla modelo;//modelo definido en la clase ModeloTabla

    private ArrayList<Map<String, String>> columnData, colDat;

    private final tableModel tmPrecio;
    Map<String, String> mapPrecio;// = new HashMap<String, String>();

    private final tableModel tmPrecioDet;
    Map<String, String> mapPrecioDet;

    private final tableModel tmProducto;
    Map<String, String> mapProducto;// = new HashMap<String, String>();

    private final tableModel tmProductoDet;
    Map<String, String> mapProductoDet;

    private DateFormat dateFormat, dateTimeFormat, dateIns; //= new SimpleDateFormat("dd/MM/yyyy HH:mm"); 
    String menuName = "";
    String CRUD = "";

    public wPrecios(String menuName) {
        initComponents();
        this.menuName = menuName;
        listaDetalles = new ArrayList<>();
        lista = new ArrayList<>();
        myData = new HashMap<>();
        columnData = new ArrayList<>();

        // COMBO BOX DESPLEGABLES DE LAS TABLAS//
        ComboBox.pv_cargar(jCMoneda, "monedas", " id, moneda ", "id", "");
        currentSelection = "";
        tc = new tableController();
        tc.init("precios");
        tcdet = new tableController();
        tcdet.init("precio_detalle");

        //PARA EL DETALLE
        mapProducto = new HashMap<>();
        tmProducto = new tableModel();
        tmProducto.init("productos");

        mapProductoDet = new HashMap<>();
        tmProductoDet = new tableModel();
        tmProductoDet.init("producto_detalle");

        tmPrecio = new tableModel();
        tmPrecio.init("precio");
        mapPrecioDet = new HashMap<>();
        tmPrecioDet = new tableModel();
        tmPrecioDet.init("precio_detalle");
        //setLocationRelativeTo(null);
        construirTabla();

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        dateIns = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        jDateFecha.setDate(new Date());

        jtDetalle.addMouseListener(this);
        jtDetalle.addKeyListener(this);
        this.tfcodbarra.addKeyListener(this);
        this.tfcodbarra.addMouseListener(this);
        jtDetalle.setOpaque(false);
        jtDetalle.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "selectNextColumnCell");
    }

    private void construirTabla() {
        listaDetalles = consultarListaDetalles();
        ArrayList<String> titulosList = new ArrayList<>();

        titulosList.add("Cod Barra");
        titulosList.add("Descripcion");
        titulosList.add("Precio");

        String titulos[] = new String[titulosList.size()];

        for (int i = 0; i < titulos.length; i++) {
            titulos[i] = titulosList.get(i);
        }
        Object[][] data = obtenerMatrizDatos(titulosList);
        construirTabla(titulos, data);
    }

    private ArrayList<pojoPrecioDetalle> consultarListaDetalles() {
        //ArrayList<pojoCompraDetalle> lista = new ArrayList<>();
        this.lista.add(new pojoPrecioDetalle(0, "0", "Descripcion", 0)); //cambiar pojo
        //listaid, "cod_barra", descripcion, precio
        return lista;
    }

    private Object[][] obtenerMatrizDatos(ArrayList<String> titulosList) {
        String informacion[][] = new String[listaDetalles.size()][titulosList.size()];

        for (int x = 0; x < informacion.length; x++) {
            //Poner los nombres de los campos de la tabla de la bd
            informacion[x][0] = listaDetalles.get(x).getString("cod_barra");
            informacion[x][1] = listaDetalles.get(x).getString("descripcion");
            informacion[x][2] = listaDetalles.get(x).getDouble("precio") + "";
        }
        return informacion;
    }

    private void construirTabla(String[] titulos, Object[][] data) {
        ArrayList<Integer> noEditable = new ArrayList<>();
        noEditable.add(1);
        modelo = new ModeloTabla(data, titulos, noEditable);
        //se asigna el modelo a la tabla
        jtDetalle.setModel(modelo);

        //se asigna el tipo de dato que tendrán las celdas de cada columna definida respectivamente para validar su personalización
        jtDetalle.getColumnModel().getColumn(0).setCellRenderer(new GestionCeldas("texto"));
        jtDetalle.getColumnModel().getColumn(1).setCellRenderer(new GestionCeldas("texto"));
        jtDetalle.getColumnModel().getColumn(2).setCellRenderer(new GestionCeldas("numerico"));

        jtDetalle.getTableHeader().setReorderingAllowed(false);
        jtDetalle.setRowHeight(25);//tamaño de las celdas
        jtDetalle.setGridColor(new java.awt.Color(0, 0, 0));
        //Se define el tamaño de largo para cada columna y su contenido
        jtDetalle.getColumnModel().getColumn(0).setPreferredWidth(50);//cod_barra
        jtDetalle.getColumnModel().getColumn(1).setPreferredWidth(250);//descripcion
        jtDetalle.getColumnModel().getColumn(2).setPreferredWidth(50);//precio

        //personaliza el encabezado
        JTableHeader jtableHeader = jtDetalle.getTableHeader();
        jtableHeader.setDefaultRenderer(new GestionEncabezadoTabla());
        jtDetalle.setTableHeader(jtableHeader);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jChEstado = new javax.swing.JCheckBox();
        jtfId = new javax.swing.JTextField();
        jCMoneda = new javax.swing.JComboBox<>();
        jDateFecha = new com.toedter.calendar.JDateChooser();
        tflista = new javax.swing.JTextField();
        jCAprobado = new javax.swing.JCheckBox();
        tfcodbarra = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtDetalle = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("ABM Precios");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Cabecera"));

        jLabel1.setText("ID");

        jLabel2.setText("Moneda");

        jLabel3.setText("Fecha");

        jLabel4.setText("Lista");

        jChEstado.setText("Activo");

        jtfId.setText("0");
        jtfId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtfIdFocusGained(evt);
            }
        });
        jtfId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfIdKeyPressed(evt);
            }
        });

        jCMoneda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Seleccione Moneda" }));

        jCAprobado.setText("Aprobado");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2)
                        .addComponent(jLabel4)
                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfcodbarra, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jtfId)
                        .addComponent(jCMoneda, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jDateFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jChEstado)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                                    .addComponent(jCAprobado))
                                .addComponent(tflista))
                            .addGap(1, 1, 1))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtfId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jCMoneda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jDateFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tflista, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jChEstado)
                    .addComponent(jCAprobado))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(tfcodbarra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalle"));

        jtDetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, "Descripción", null}
            },
            new String [] {
                "Cod Barra", "Descripción", "Precio"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jtDetalle.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtDetalleFocusGained(evt);
            }
        });
        jScrollPane1.setViewportView(jtDetalle);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtfIdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfIdKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.imBuscar();
        }
    }//GEN-LAST:event_jtfIdKeyPressed

    private void jtfIdFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfIdFocusGained
        this.currentSelection = "id";
    }//GEN-LAST:event_jtfIdFocusGained

    private void jtDetalleFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtDetalleFocusGained
      currentSelection = "tabla";   
      validarCombo(); //validar cabecera
    }//GEN-LAST:event_jtDetalleFocusGained


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCAprobado;
    private javax.swing.JComboBox<String> jCMoneda;
    private javax.swing.JCheckBox jChEstado;
    private com.toedter.calendar.JDateChooser jDateFecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtDetalle;
    private javax.swing.JTextField jtfId;
    private javax.swing.JTextField tfcodbarra;
    private javax.swing.JTextField tflista;
    // End of variables declaration//GEN-END:variables

    /**
     * @param e evento del mouse
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("mouseClicked 838 source: " + e.getSource());
        //Verificar si se ejecuto desde Tabla o jtfCodigo
        Object source = e.getSource();
        if (source == jtDetalle) {
            //Capturamos fila y columna del evento click e.getPoint()
            int fila = jtDetalle.rowAtPoint(e.getPoint());
            int columna = jtDetalle.columnAtPoint(e.getPoint());
            System.out.println("mouseClicked tabla: fila: " + fila + " columna: " + columna);
            //si columna Seleccionada corresponde a Cod_barra
            if (columna == 0) { //0 corresponde a cod barra
                validarSeleccionMouse(fila);
            }
            //si columna Seleccionada corresponde a Precio
            if (columna == 2) { //2 corresponde a Precio
                //JOptionPane.showMessageDialog(null, "Evento del otro icono");
            }
        }
        if (source == tfcodbarra) {
            System.out.println("el click se realizo en jTextFieldCodBarra");
        }
    }

    private void validarSeleccionMouse(int fila) {
        //int filaSeleccionada = fila;
        //teniendo la fila entonces se obtiene el objeto correspondiente para enviarse como parammetro o imprimir la información
        pojoPrecioDetalle rowDetalle = new pojoPrecioDetalle();
        rowDetalle.setString("cod_barra", jtDetalle.getValueAt(fila, 0).toString());
        rowDetalle.setString("descripcion", jtDetalle.getValueAt(fila, 1).toString());

        String info="ValidarSeleccionMouse: \n";
       info+="Código: "+rowDetalle.getString("cod_barra")+"\n";
       info+="Descripción: "+rowDetalle.getString("descripcion")+"\n";
       System.out.println("wPrecios: " + info);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int row = jtDetalle.getSelectedRow();
        int rows = jtDetalle.getRowCount();
        int col = jtDetalle.getSelectedColumn();
        boolean lastRow = (row == (rows - 1));

        //validacion de tecla pulsada
        int key = e.getKeyChar(); //Tecla Pulsada
        //Ascci 48-57 numeros arriba del teclado. Ascci 96-105 teclado numerico del costado
        boolean numeros = (key >= 48 && key <= 57) || (key >= 96 && key <= 105);   // 0 al 9
        boolean decimalPoint = key == 46;           // '.'
        boolean erraser = key == 8;                //Backspace
        boolean enter = key == 10;                  //Enter
        boolean tabulacion = key == 9;              //Tab
        //Si no es numero, no es decimal, no es tecla borrar, no es Enter y no es tabulacion
        //entonces no hacer nada e ignorar el evento
//        if (!numeros && !decimalPoint && !erraser && !enter && !tabulacion) {
//            e.consume();
//        }
            
        if (numeros) {
            if (jtDetalle.getModel().isCellEditable(row, col)) {
                jtDetalle.setValueAt("", row, col); //reemplazar valor de la celda
            }
        }

        if ((enter || tabulacion) && col == 0) {
            jtDetalle.getCellEditor().stopCellEditing();
            this.getProducto(row, col);
            return;
        }
        
        //Enter para Ultima Columna. si ultima fila de la tabla Inserta una nueva fila
        if (col == 2 && enter && lastRow) {
            //Verifica que se haya ingresado cod_barra Correcto en la fila
            String cod = this.jtDetalle.getValueAt(row, 0).toString();
            if (cod.equals("0") || cod.equals("")) {
                JOptionPane.showMessageDialog(this, "keyPressed960: Favor ingrese un producto Valido!", "¡A T E N C I O N!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                this.imInsDet();
            }
        }
    } //Fin KeyPressed
    
    @Override
    public void keyReleased(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void imGrabar(String crud) {
        String msg;
        this.CRUD = crud;
        //validar permisos para el usuario. si tiene permiso de Crear registros en esta vista
        if (Tools.validarPermiso(conexion.getGrupoId(), menuName, crud) == 0) {
            msg = "NO TIENE PERMISO PARA REALIZAR ESTA OPERACIÓN ";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
        }
        //validacion de campos del Formulario Cabecera
        if (!validarCabecera()) {
            return;
        }
        //validacion de campos de Tabla Detalle
        if (!validarDetalles()) {
            return;
        }

        this.setData();
        int idPrecio = Integer.parseInt(this.jtfId.getText());
        if (idPrecio > 0) {
            this.imActualizar("U");
            return;
        } 
        if (idPrecio == 0) {
            int rowsAffected = this.tc.createReg(this.myData);
            if (rowsAffected <= 0) {
                msg = "Error al intentar crear el registro";
                JOptionPane.showMessageDialog(this, msg, "ATENCION...!", JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                msg = "Se ha creado exitosamente el registro";
                JOptionPane.showMessageDialog(this, msg, "ATENCION...!", JOptionPane.INFORMATION_MESSAGE);
                this.jtfId.requestFocus();
                idPrecio = this.tc.getMaxId();
            }
        } // Fin Cabecera
        
        //DETALLES---------------------------------------------------------
       //si pasó quiere decir que tenemos cabecera y recorremos Detalles
        for (Map<String, String> myRow : columnData) {
            myRow.put("id", "0");
            myRow.put("preciodi", idPrecio + "");
            //Los demas datos del detalle ya se han cargado en setData
            int rowsAffected = this.tcdet.createReg(myRow);
            if (rowsAffected < 1) {
                msg = "No se ha podido grabar el Detalle Codigo:" + myRow.get("cod_barra");
                JOptionPane.showMessageDialog(this, msg, "ATENCION...!", JOptionPane.DEFAULT_OPTION);
            } else {
                msg = "Se ha creado el Detalle:" + myRow.get("cod_barra");
                JOptionPane.showMessageDialog(this, msg, "ATENCION...!", JOptionPane.DEFAULT_OPTION);
            }
        }
        this.imNuevo();
    }//fin imGrabar
    
    @Override
    public void imActualizar(String crud) {
        //Ya se ha validado cabecera y detalle desde imGrabar
        this.CRUD = crud;
        String msg;
        //validar permisos para el usuario. si tiene permiso de actualizar registros de esta vista
        if (Tools.validarPermiso(conexion.getGrupoId(), menuName, crud) == 0) {
            msg = "NO TIENE PERMISO PARA REALIZAR ESTA OPERACIÓN ";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
        }

        this.setData();
        ArrayList<Map<String, String>> alCabecera;
        alCabecera = new ArrayList<>();
        alCabecera.add(this.myData); //Mapa con Datos de cabecera en array.
        int rowsAffected = this.tc.updateReg(alCabecera);
        if (rowsAffected <= 0) {
            msg = "Error al intentar actualizar el registro: " + this.jtfId.getText();
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.ERROR_MESSAGE);
            return; // si tfId > 0 y no grabo cambios, entonces return
        } else {
            msg = "Se ha actualizado exitosamente el registro: " + this.jtfId.getText();
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.INFORMATION_MESSAGE);
        } 
        // DETALLES -----------------------------
        //si pasó quiere decir que tenemos cabecera y recorremos el detalle
        int idPrecio = Integer.parseInt(jtfId.getText()); //id de la cabecera
        Map<String, String> where = new HashMap<>();      //Por qué campo buscar los registros
        Map<String, String> fields = new HashMap<>();     //Los campos que vamos a recuperar
        ArrayList<Map<String, String>> alDetalle;         //Declara array de Map, cada Map es para un registro
        fields.put("*", "*");
        
        for (Map<String, String> myRow : columnData) {
            where.put("precioid", idPrecio + "");
            where.put("cod_barra", myRow.get("cod_barra"));
            //Buscar si ya existe un detalle de este cod_barra para esta lista de precios.
            this.colDat = this.tcdet.searchListById(fields, where);
            // si no existe un detalle con este cod_barra para esta lista de precios
            if (this.colDat.isEmpty()) { 
                myRow.put("id", "0");
                myRow.put("precioid", idPrecio + "");
                rowsAffected = this.tcdet.createReg(myRow);
                if (rowsAffected <= 0) {
                    msg = "No se ha podido grabar el Detalle Codigo:" + myRow.get("cod_barra");
                    JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
                } else {
                    msg = "Se ha creado el Detalle:" + myRow.get("cod_barra");
                    JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.INFORMATION_MESSAGE);
                }
            } else { //si ya existe un detalle con este cod_barra para esta lista
                myRow.put("id", colDat.get(0).get("id")); // id del detalle
                myRow.put("precioid", idPrecio + "");
                alDetalle = new ArrayList<>(); //necesitamos el alDetalle por la estructura de la funcion tcdet.updateReg
                alDetalle.add(myRow);
                rowsAffected = this.tcdet.updateReg(alDetalle);   //Recordar que el modelo sólo procesa de a uno los registros
                if (rowsAffected <= 0) {
                    msg = "No se ha podido actualizar el detalle: " + myRow.get("cod_barra");
                    JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
                    return;
                } else {
                    msg = "Se ha actualizado el Detalle con este codigo:" + myRow.get("cod_barra");
                    JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
                }
            }
        }
    } //Fin imActualizar

    /**
     * Esta funcion se apoya en un Trigger en la base de datos para elimiar
     * tanto el registro de cabecera como sus detalles
     * @param crud D
     */
    @Override
    public void imBorrar(String crud) {
        String msg;
        this.CRUD = crud;
        //validar permisos para el usuario. si tiene permiso de borrar registros de esta vista
        if (Tools.validarPermiso(conexion.getGrupoId(), menuName, crud) == 0) {
            msg = "NO TIENE PERMISO PARA REALIZAR ESTA OPERACIÓN ";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
        }
        this.setData();
        int idPrecios = Integer.parseInt(jtfId.getText());
        if(idPrecios <= 0){
            msg = "NO SE HA ENCONTRADO EL REGISTRO";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
        }
        if(idPrecios > 0){
            ArrayList<Map<String, String>> alCabecera;
            alCabecera = new ArrayList<>();
            alCabecera.add(myData);                           
            int rowsAffected = this.tc.deleteReg(alCabecera);
            if (rowsAffected <= 0) {
                msg = "NO SE HA PODIDO ELIMINAR EL REGISTRO: " + jtfId.getText();
                JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
                return;
            }
            if (rowsAffected > 0) {
                msg = "EL REGISTRO: " + jtfId.getText() + " SE HA ELIMINADO CORRECTAMENTE";
                JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
            }
            imNuevo();
        }    
    }//fin ImBorrar

    @Override
    public void imFiltrar() {
        String sql;
        wBuscar frame = new wBuscar();
        if (currentSelection.equals("id")){
        sql = "SELECT p.id AS codigo, "
                + "CONCAT(p.lista, ' en ',m.moneda) AS descripcion "
                + "FROM precios p, monedas m "
                + "WHERE p.moneda = m.id and LOWER(CONCAT(p.id, p.lista, p.moneda, m.moneda)) LIKE '%";

        frame = new wBuscar(sql, this.jtfId);
        }
        if (currentSelection.equals("tabla")) { //Buscar un producto por filtro
            sql = "SELECT d.cod_barra as codigo, "
                    + "CONCAT(p.nombre, ' - ', "
                    + "p.descripcion, ' - ', m.nombre, ' - ', "
                    + "c.color, ' - ', t.tamano, ' - ', s.diseno) AS descripcion "
                    + "FROM productos p, producto_detalle d, marcas m, colores c, tamanos t, disenos s "
                    + "WHERE p.marca = m.id "
                    + "AND d.colorid = c.id "
                    + "AND d.tamanoid = t.id "
                    + "AND d.disenoid = s.id "
                    + "AND p.id = d.productoid "
                    + "AND LOWER(CONCAT(p.descripcion, ' ', m.nombre, ' ',c.color, ' ', t.tamano, ' ', s.diseno, ' ',p.nombre)) LIKE '%";
            //System.out.println("ENVIAMOS " + sql);
            frame = new wBuscar(sql, this.tfcodbarra);
        }
        frame.setVisible(true);
        wPrincipal.desktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.OK_OPTION);
        }
    } //Fin imFiltrar


    @Override
    public void imNuevo() {
        this.resetData();
        this.limpiarTabla();
        this.fillView(myData, columnData);
        this.imInsDet();
    }

    @Override
    public void imBuscar() {
        this.setData(); //Hace tomar los datos de la vista
        this.myData = this.tc.searchById(myData);  //Usa el mismo myData para devolver los valores de la cabecera
        System.out.println("PRECIOS imBuscar myDatax " + this.myData.toString());
        this.limpiarTabla();
        if (this.myData.isEmpty()) {
            System.out.println("No hay registros que mostrar");
            this.resetData();
            //this.fillView(myData, columnData);
            return;
        }
        Map<String, String> where = new HashMap<>();      //Por qué campo buscar los registros
        where.put("precioid", this.myData.get("id"));
        //Los campos que vamos a recuperar
        Map<String, String> fields = new HashMap<>();
        fields.put("*", "*");
        //verificar tablaModel 407 cuando no existe el reg
        this.columnData = this.tcdet.searchListById(fields, where);
        if (this.columnData.isEmpty()) {
            this.resetData();
            //return
        }
        this.fillView(myData, columnData);
    } //Fin imBuscar
    
    @Override
    public void imPrimero() {
        this.myData = this.tc.navegationReg(jtfId.getText(), "LAST");
        this.fillView(this.myData, columnData);
        this.setData();
        imBuscar();
    }

    @Override
    public void imSiguiente() {
        this.myData = this.tc.navegationReg(jtfId.getText(), "NEXT");
        this.fillView(this.myData, columnData);
        this.setData();
        imBuscar();
    }

    @Override
    public void imAnterior() {
        this.myData = this.tc.navegationReg(jtfId.getText(), "PRIOR");
        this.fillView(this.myData, columnData);
        this.setData();
        imBuscar();
    }

    @Override
    public void imUltimo() {
        this.myData = this.tc.navegationReg(jtfId.getText(), "LAST");
        this.fillView(this.myData, columnData);
        this.setData();
        imBuscar();
    }

    @Override
    public void imImprimir() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void imInsDet() {
        int currentRow = jtDetalle.getSelectedRow();
        if (currentRow == -1) {
            System.out.println("no hay fila seleccionada imInsDet 831");
            modelo.addRow(new Object[]{"0", "Descripcion", 0.0});
            return;
        }
        String cod = this.jtDetalle.getValueAt(currentRow, 0).toString();
        if (cod.equals("0") || cod.equals("")) {
            String msg = "POR FAVOR INGRESE UN PRODUCTO ";
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
        } else {
            System.out.println("entro en imInsDet841");
            modelo.addRow(new Object[]{"", "Descripcion", 0.0});
            /**
             * LUEGO DE CARGAR LA INFORMACIÓN DE LA SOLICITUD EN LA VISTA NOS
             * POSICIONAMOS EN LA PRIMERA CELDA DE LA SIGUIENTE FILA DE LA TABLA
             * DE LA VENTANA PRINCIPAL
             */

            /**
             * DEBEMOS DEVOLVERLE EL FOCO A LA TABLA
             */
            this.jtDetalle.requestFocus();

            /**
             * tabla.getRowCount () - 1 -> PARA INDICAR QUE ES LA ULTIMA FILA 0
             * -> EN MI CASO PARA INDICAR QUE DEBE SER EN LA PRIMERA COLUMNA
             * false, false -> LOS DEJO ASÍ PUES NO NECESITO LA FUNCIONALIDAD DE
             * ESOS PARÁMETROS
             */
            /* toggle: false, extend: false. Clear the previous selection and ensure the new cell is selected.
            * toggle: false, extend: true. Extend the previous selection from the anchor to the specified cell, clearing all other selections.
            * toggle: true, extend: false. If the specified cell is selected, deselect it. If it is not selected, select it.
            * toggle: true, extend: true. Apply the selection state of the anchor to all cells between it and the specified cell.
             */
            int toRow = this.jtDetalle.getRowCount() - 1;
            //System.out.println("a la fila "+toRow);
            this.jtDetalle.changeSelection(toRow, 0, false, false);
        }

    }

    @Override
    public void imDelDet() {
       int currentRow = jtDetalle.getSelectedRow();
        if (currentRow == -1) {
            System.out.println("no hay fila seleccionada");
            return;
        }

        modelo.removeRow(currentRow);
        //Si al eliminar queda vacía, habrá que insertar una nueva
        int rows = jtDetalle.getRowCount();

        if (rows == 0) {
            System.out.println("Se eliminaron todas las filas");
            this.imInsDet();
        }
    }

    @Override
    public void imCerrar() {
        setVisible(false);
        dispose();
    }
    
        /**
     * Controla que todos los datos obligatorios de la cabecera tengan los
     * valores requeridos En caso contrario se devuelve el foco a la cabecera.
     * Este método se llama en el evento focus de la tabla
     *
     * @return boolean que indica si las condiciones se cumplen o no, true/false
     */
    public boolean validarCabecera() { //Acordate que esto tenes que completar
        boolean rtn;
        rtn = true;
        String msg = "Defecto Cabecera";
        //validar id
        if (jtfId.getText().isEmpty() || "".equals(jtfId.getText())) {
            msg = "El campo Id no debe ser vacio";
            rtn = false;
            jtfId.setText("0");
            jtfId.requestFocus();
        }
        //Fecha Proceso
        Date fecha = jDateFecha.getDate();
        if (fecha == null) {
            msg = "Favor ingrese una fecha de operación válida!";
            jDateFecha.requestFocus();
            jDateFecha.setDate(new Date());
            rtn = false;
        }
        //continuar con los demás controles
        if (!rtn) {
            JOptionPane.showMessageDialog(this, msg, "¡Favor Verificar926!", JOptionPane.INFORMATION_MESSAGE);
        }

        return rtn;
    }

    public boolean validarDetalles() {
        //Validar Campos de los Detalles
        boolean rtn = true;
        String msg = "Defecto Detalle";
        int rows = this.jtDetalle.getRowCount();
        for (int i = 0; i < rows; i++) {
            //celda codigo de barra
            String codigo_barra = (String) jtDetalle.getValueAt(i, 0);
            if (codigo_barra.equals("0") || "".equals(codigo_barra)) {
                msg = "Proporcione codigo de barra para la fila: " + i;
                jtDetalle.changeSelection(i, 0, false, false);
                rtn = false;
                continue;
            }

            String precio = (String) jtDetalle.getValueAt(i, 2);
            if (Double.parseDouble(precio) < 1) {
                msg = "Precio no puede ser menor a 1";
                jtDetalle.changeSelection(i, 2, false, false);
                rtn = false;
            }
        }
        if (!rtn) {
            JOptionPane.showMessageDialog(this, msg, "validarDetalles955!", JOptionPane.DEFAULT_OPTION);
        }
        return rtn;
    } //Fin validarDetalles
    
    public int getProducto(int row, int col) { 
        String codbar, sql;
        int Exito = 0;
        //recuperar cod_barra de la row
        codbar = this.jtDetalle.getModel().getValueAt(row, 0).toString(); //col == 0
        if (codbar.equals("")) { //para evitar error en el sql
            codbar = "0";
            JOptionPane.showMessageDialog(this, "Proporcione codigo para la fila: " + row, "getProducto967", JOptionPane.OK_OPTION);
            Exito = 0;
        }
        sql = "SELECT CONCAT(p.nombre, ' ', "
                + "m.nombre, ' , ', "
                + "c.color, ' ', t.tamano, ' ', s.diseno) AS descripcion "
                + "FROM productos p, producto_detalle d, marcas m, colores c, tamanos t, disenos s "
                + "WHERE p.id = d.productoid "
                + "AND p.marca = m.id "
                + "AND d.colorid = c.id "
                + "AND d.tamanoid = t.id "
                + "AND d.disenoid = s.id "
                + "AND d.cod_barra = '" + codbar + "'";
        ResultSet rs;

        try {
            rs = conexion.ejecuteSQL(sql);
            ResultSetMetaData metaData = rs.getMetaData();
            int colCount = metaData.getColumnCount();
            if (rs.next()) {
                Exito = 1;
                System.out.println("Producto INFO: \n");
                for (int r = 1; r <= colCount; r++) {
                    System.out.println("column "+metaData.getColumnName(r)+" valor "+rs.getString(metaData.getColumnName(r)));
                    //producto.put(metaData.getColumnName(r), rs.getString("descripcion"));
                    String descripcion = rs.getString("descripcion");
                    this.jtDetalle.getModel().setValueAt(descripcion, row, 1); //Descripcion en jtDetalle
                    //this.jtDetalle.getModel().setValueAt("10", row, 2);
                }
            } else {
                this.jtDetalle.getModel().setValueAt("0", row, 0);
                this.jtDetalle.getModel().setValueAt("", row, 1);
                String msg = "No se ha encontrado Producto con el Codigo: " + codbar;
                JOptionPane.showMessageDialog(this, msg, "getProducto939", JOptionPane.OK_OPTION);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Exito;
    } //Fin getProducto

    private void setData() {
        //CABECERA
        myData.put("id", jtfId.getText());
        myData.put("lista", tflista.getText());
        myData.put("moneda", ComboBox.ExtraeCodigo(jCMoneda.getSelectedItem().toString()));
        String fecha = (jDateFecha.getDate().getTime() / 1000L) + "";
        myData.put("fecha", fecha);
        int estado = 0; //estado
        if (jChEstado.isSelected()) { estado = 1; }
        myData.put("estado", estado + "");
        int aprobado = 0; //aprobado
        if (jCAprobado.isSelected()) { aprobado = 1; }
        myData.put("aprobado", aprobado + "");

        //DETALlES
        columnData.clear(); //Limpiar Array Detalles
        int rows = this.jtDetalle.getRowCount();
        for (int row = 0; row < rows; row++) {  //Recorremos cada Row de la Tabla
            String codbar = this.jtDetalle.getModel().getValueAt(row, 0).toString();
            if (codbar.equals("0") || codbar.equals("")) { //Si no se especifica código, no tiene sentido continuar
                continue;
            }
            double precio = Tools.sGetDecimalStringAnyLocaleAsDouble(this.jtDetalle.getModel().getValueAt(row, 2).toString());
            myDet = new HashMap<>();
            String id = jtfId.getText();
            myDet.put("precioid", id);
            myDet.put("cod_barra", this.jtDetalle.getModel().getValueAt(row, 0).toString());
            myDet.put("precio", precio + "");
            myDet.put("descripcion", this.jtDetalle.getModel().getValueAt(row, 1).toString());

            this.columnData.add(this.myDet);
        }
    }//fin setData

    private void resetData() {
        this.myData = new HashMap<>();
        java.util.Date df = new java.util.Date();
        this.myData.put("id", "0");
        this.myData.put("moneda", "0");
        this.myData.put("lista", "0");
        this.myData.put("estado", "0");
        this.myData.put("aprobado", "0");
        // this.myData.put("fecha", df.getTime() + "");
         jCMoneda.setSelectedIndex(0);

        //Detalle
        this.myDet = new HashMap<>();
        this.myDet.put("precioid", "0");
        this.myDet.put("cod_barra", "");
        this.myDet.put("descripcion", "");
        this.myDet.put("precio", "0");

        this.columnData.add(this.myDet);
    }//fin reset data

    private void fillView(Map<String, String> data, List<Map<String, String>> columnData) {
        Date df;
        long dateLong;
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey(); //end for
            String value = entry.getValue();
//            System.out.println("fecha "+dateTimeFormat.format(value.toString()));
            switch (key) {
                case "id":
                    jtfId.setText(value);
                    break;
                case "moneda":
                    ComboBox.E_estado(jCMoneda, "monedas", "id, moneda", "id=" + value);
                    break;
                case "lista":
                    tflista.setText(value);
                    break;
                case "fecha":
                    dateLong = Long.parseLong(value) * 1000L;
                    df = new Date(dateLong);
                    jDateFecha.setDate(df);
                    break;
                case "estado":
                    if (Integer.parseInt(value) == 0) {
                        jChEstado.setSelected(false);
                    } else {
                        jChEstado.setSelected(true);
                    }
                    break;

                case "aprobado":
                    if (Integer.parseInt(value) == 0) {
                        jCAprobado.setSelected(false);
                    } else {
                        jCAprobado.setSelected(true);
                    }
                    break;
            }//end switch
        }//end CABECERA   

        //DETALLE TABLA
        this.modelo.setRowCount(0); // Limpiar la tabla antes de llenar
        
        for (Map<String, String> myRow : columnData) { //Detalles
            // Añadir una nueva fila vacía al modelo
            this.modelo.addRow(new Object[]{"0", "", 0.0, 0.0, 0.0, 0.0});
            int row = modelo.getRowCount() - 1; // Índice de la última fila añadida
            //cargar codigo de barra en la nueva fila
            this.jtDetalle.setValueAt(myRow.get("cod_barra"), row, 0);
            //verificar que el codigo de barra corresponda a un producto existente en la DB antes de continuar
            int exist = this.getProducto(row, 0);
            if(exist == 0){
                String msg = "Producto: " + myRow.get("cod_barra") + " no encontrado";
                JOptionPane.showMessageDialog(this, msg, "1767Validacion de Campos Detalle!", JOptionPane.DEFAULT_OPTION);
                modelo.removeRow(row);
                continue;
            }
            this.jtDetalle.setValueAt(Tools.decimalFormat(Double.parseDouble(myRow.get("precio"))), row, 2);
            //this.jtDetalle.setValueAt(myRow.get("precio"), row, 2);
            row++;
        }//end for 2
    }//end fill
    
     public void validarCombo(){
           int codigo = 0;
            codigo = Integer.parseInt(ComboBox.ExtraeCodigo(jCMoneda.getSelectedItem().toString()));            
            if (codigo == 0){
            this.jCMoneda.requestFocus();
            JOptionPane.showMessageDialog(this, "Favor Seleccione Moneda!", "¡A T E N C I O N!", JOptionPane.WARNING_MESSAGE);
            }       
            
     }
     
    public void limpiarTabla() {
        this.columnData.clear();
        try {
            DefaultTableModel modelo = (DefaultTableModel) jtDetalle.getModel();
            int filas = jtDetalle.getRowCount();
            for (int i = 0; filas > i; i++) {
                modelo.removeRow(0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al limpiar la tabla.");
        }
    }//fin limpiarTabla
}
