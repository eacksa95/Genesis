package com.genesis.vistas;

import com.genesis.model.conexion;
import com.genesis.model.pojoProductoDetalle;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class wProducto extends javax.swing.JInternalFrame implements MouseListener, KeyListener, ActiveFrame {

    private Map<String, String> myData;
    private HashMap<String, String> myDet;

    private final tableController tc;
    private final tableController tcdet;

    ArrayList<pojoProductoDetalle> lista;
    ArrayList<pojoProductoDetalle> listaDetalles;//lista que simula la información de la BD

    JComboBox jcbColor;
    JComboBox jcbTamano;
    JComboBox jcbDiseno;

    ModeloTabla modelo; //modelo de la Jtable
    String CRUD = "";
    String menuName = "";

    private ArrayList<Map<String, String>> columnData, colDat;

    //private final tableModel tmProducto;
    Map<String, String> mapProductos;

    private final tableModel tmProductoDet;
    Map<String, String> mapProductoDet;

    public wProducto(String menuName) {
        initComponents();
        this.menuName = menuName;
        listaDetalles = new ArrayList<>();
        lista = new ArrayList<>();
        myData = new HashMap<>();
        columnData = new ArrayList<>();
        colDat = new ArrayList<>();
        jcbColor = new JComboBox();
        jcbTamano = new JComboBox();
        jcbDiseno = new JComboBox();

        // COMBO BOX DE LA CABECERA//
        ComboBox.pv_cargar(jcbMarca, "marcas", " id, nombre ", "id", "");
        ComboBox.pv_cargar(jcbProveedor, "proveedores", "id, nombre", "id", "");
        ComboBox.pv_cargar(jcbCategoria, "categorias", "id, nombre", "id", "");

        // COMBO BOX DE LA TABLA//
        ComboBox.pv_cargar(jcbColor, "colores", "id, color", "id", "");
        ComboBox.pv_cargar(jcbTamano, "tamanos", "id, tamano", "id", "");
        ComboBox.pv_cargar(jcbDiseno, "disenos", "id, diseno", "id", "");

        tc = new tableController();
        tc.init("productos");
        tcdet = new tableController();
        tcdet.init("producto_detalle");

        //PARA EL DETALLE
        mapProductoDet = new HashMap<>();
        tmProductoDet = new tableModel();
        tmProductoDet.init("producto_detalle");
        construirTabla();

        jtDetalle.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(jcbColor));
        jtDetalle.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(jcbTamano));
        jtDetalle.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(jcbDiseno));
        jtDetalle.addMouseListener(this);
        jtDetalle.addKeyListener(this);
        jtDetalle.setOpaque(false);
        jtDetalle.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "selectNextColumnCell");
    }

    /**
     * Metodo que permite construir la tabla para el detalle se crean primero
     * las columnas y luego se asigna la información
     */
    private void construirTabla() {
        listaDetalles = consultarListaDetalles();
        //Titulos de la tabla
        ArrayList<String> titulosList = new ArrayList<>();
        titulosList.add("Cod Barra");
        titulosList.add("Color");
        titulosList.add("Tamaño");
        titulosList.add("Diseño");
        titulosList.add("UxB");
        titulosList.add("Stock Min");

        String titulos[] = new String[titulosList.size()];

        for (int i = 0; i < titulos.length; i++) {
            titulos[i] = titulosList.get(i);
        }
        /*obtenemos los datos de la lista y los guardamos en la matriz
         * que luego se manda a construir la tabla
         */
        Object[][] data = obtenerMatrizDatos(titulosList);
        construirTabla(titulos, data);
    }

    /**
     * Este método simula una consulta a la base de datos y devuelve una lista
     * de pojoProductoDetalle.
     *
     * @return lista
     */
    private ArrayList<pojoProductoDetalle> consultarListaDetalles() {
        this.lista.add(new pojoProductoDetalle(0, "0", 0, 0, 0, 0, 0));
        //productoid, "cod_barra", colorid, disenoid, tamanoid, uxb, stock
        return lista;
    }

    /**
     * Este método convierte la lista de pojoProductoDetalle en una matriz de
     * objetos para ser usada por la tabla.
     *
     * @param titulosList
     * @return
     */
    private Object[][] obtenerMatrizDatos(ArrayList<String> titulosList) {

        String informacion[][] = new String[listaDetalles.size()][titulosList.size()];

        for (int x = 0; x < informacion.length; x++) {
            //Poner los nombres de los campos de la tabla de la bd
            informacion[x][0] = listaDetalles.get(x).getString("cod_barra");
            informacion[x][1] = listaDetalles.get(x).getInteger("colorid") + "";
            informacion[x][2] = listaDetalles.get(x).getInteger("tamanoid") + "";
            informacion[x][3] = listaDetalles.get(x).getInteger("disenoid") + "";
            informacion[x][4] = listaDetalles.get(x).getInteger("uxb") + "";
            informacion[x][5] = listaDetalles.get(x).getInteger("stock") + "";
        }
        return informacion;
    }

    /**
     * Con los titulos y la información a mostrar se crea el modelo para poder
     * personalizar la tabla, asignando tamaño de celdas tanto en ancho como en
     * alto así como los tipos de datos que va a poder soportar.
     *
     * @param titulos
     * @param data
     */
    private void construirTabla(String[] titulos, Object[][] data) {
        ArrayList<Integer> noEditable = new ArrayList<>();
        modelo = new ModeloTabla(data, titulos, noEditable);
        //se asigna el modelo a la tabla
        jtDetalle.setModel(modelo);

        //se asigna el tipo de dato que tendrán las celdas de cada columna definida respectivamente para validar su personalización
        jtDetalle.getColumnModel().getColumn(0).setCellRenderer(new GestionCeldas("texto"));
        jtDetalle.getColumnModel().getColumn(1).setCellRenderer(new GestionCeldas("jComboBox"));
        jtDetalle.getColumnModel().getColumn(2).setCellRenderer(new GestionCeldas("jComboBox"));
        jtDetalle.getColumnModel().getColumn(3).setCellRenderer(new GestionCeldas("jComboBox"));
        jtDetalle.getColumnModel().getColumn(4).setCellRenderer(new GestionCeldas("numerico"));
        jtDetalle.getColumnModel().getColumn(5).setCellRenderer(new GestionCeldas("numerico"));

        jtDetalle.getTableHeader().setReorderingAllowed(false);
        jtDetalle.setRowHeight(24);//Altura de las celdas
        jtDetalle.setGridColor(new java.awt.Color(0, 0, 0));
        //Se define el tamaño de largo para cada columna y su contenido
        jtDetalle.getColumnModel().getColumn(0).setPreferredWidth(100);//cod_barra
        jtDetalle.getColumnModel().getColumn(1).setPreferredWidth(50);//color
        jtDetalle.getColumnModel().getColumn(2).setPreferredWidth(50);//tamanho
        jtDetalle.getColumnModel().getColumn(3).setPreferredWidth(50);//disenho
        jtDetalle.getColumnModel().getColumn(4).setPreferredWidth(100);//UxB
        jtDetalle.getColumnModel().getColumn(5).setPreferredWidth(100);//stock min

        //personaliza el encabezado
        JTableHeader jtableHeader = jtDetalle.getTableHeader();
        jtableHeader.setDefaultRenderer(new GestionEncabezadoTabla());
        jtDetalle.setTableHeader(jtableHeader);

        //se asigna la tabla al scrollPane
        scrollPaneTabla.setViewportView(jtDetalle);
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
        jtfId = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        tfNombre = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tfDescripcion = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        tfImpuesto = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jcbMarca = new javax.swing.JComboBox<>();
        jcbProveedor = new javax.swing.JComboBox<>();
        jcbCategoria = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jCServicio = new javax.swing.JCheckBox();
        jCEstado = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        scrollPaneTabla = new javax.swing.JScrollPane();
        jtDetalle = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Producto");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Cabecera"));

        jLabel1.setText("ID ");

        jtfId.setText("0");
        jtfId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfIdKeyPressed(evt);
            }
        });

        jLabel2.setText("Nombre");

        jLabel3.setText("Descripción");

        jLabel4.setText("Marca");

        jLabel5.setText("Proveedor");

        jLabel6.setText("Categoría");

        jLabel9.setText("Impuesto");

        tfImpuesto.setPreferredSize(new java.awt.Dimension(13, 20));

        jLabel10.setText("Servicio");

        jLabel11.setText("Activo");

        jcbMarca.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Seleccione Marca" }));
        jcbMarca.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jcbMarcaFocusGained(evt);
            }
        });

        jcbProveedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Seleccione Proveedor" }));
        jcbProveedor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jcbProveedorFocusGained(evt);
            }
        });

        jcbCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Seleccione Categoria" }));
        jcbCategoria.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jcbCategoriaFocusGained(evt);
            }
        });

        jLabel7.setText("%");

        jCServicio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jCServicioKeyPressed(evt);
            }
        });

        jCEstado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jCEstadoKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jtfId, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tfNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(tfDescripcion)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(jcbProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(15, 15, 15)
                                .addComponent(jcbMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(15, 15, 15)
                                .addComponent(jcbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(114, 114, 114)
                                    .addComponent(jLabel9)
                                    .addGap(18, 18, 18)
                                    .addComponent(tfImpuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel7))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel10)
                                    .addGap(18, 18, 18)
                                    .addComponent(jCServicio)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(117, 117, 117)
                                .addComponent(jLabel11)
                                .addGap(18, 18, 18)
                                .addComponent(jCEstado)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtfId, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(tfNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(tfDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jcbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel7)
                    .addComponent(tfImpuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jcbMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10))
                    .addComponent(jCServicio))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(jcbProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel11))
                    .addComponent(jCEstado))
                .addGap(34, 34, 34))
        );

        tfImpuesto.getAccessibleContext().setAccessibleName("");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalle"));

        jtDetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"0", "0", "0", null, null, null}
            },
            new String [] {
                "Cod Barra", "Color", "Tamaño", "Diseño", "UxB", "Stock Min"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class
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
        scrollPaneTabla.setViewportView(jtDetalle);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneTabla, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 714, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPaneTabla, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtfIdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfIdKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.imBuscar();
        }
    }//GEN-LAST:event_jtfIdKeyPressed

    private void jcbCategoriaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jcbCategoriaFocusGained
        System.out.println(evt);
        jcbCategoria.showPopup();
    }//GEN-LAST:event_jcbCategoriaFocusGained

    private void jcbMarcaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jcbMarcaFocusGained
        System.out.println(evt);
        jcbMarca.showPopup();
    }//GEN-LAST:event_jcbMarcaFocusGained

    private void jcbProveedorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jcbProveedorFocusGained
        System.out.println(evt);
        jcbProveedor.showPopup();
    }//GEN-LAST:event_jcbProveedorFocusGained

    private void jCServicioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCServicioKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            boolean currentValue = jCServicio.isSelected();
            jCServicio.setSelected(!currentValue); // Toggle the value
        }
    }//GEN-LAST:event_jCServicioKeyPressed

    private void jCEstadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCEstadoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            boolean currentValue = jCEstado.isSelected();
            jCEstado.setSelected(!currentValue); // Toggle the value
        }
    }//GEN-LAST:event_jCEstadoKeyPressed

    private void jtDetalleFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtDetalleFocusGained
        System.out.println(evt);
        validarCamposCabecera();
    }//GEN-LAST:event_jtDetalleFocusGained

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCEstado;
    private javax.swing.JCheckBox jCServicio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JComboBox<String> jcbCategoria;
    private javax.swing.JComboBox<String> jcbMarca;
    private javax.swing.JComboBox<String> jcbProveedor;
    private javax.swing.JTable jtDetalle;
    private javax.swing.JTextField jtfId;
    private javax.swing.JScrollPane scrollPaneTabla;
    private javax.swing.JTextField tfDescripcion;
    private javax.swing.JTextField tfImpuesto;
    private javax.swing.JTextField tfNombre;
    // End of variables declaration//GEN-END:variables

    @Override
    public void mouseClicked(MouseEvent e) {
        //capturo fila o columna dependiendo de mi necesidad
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Object source = e.getSource(); //Origen del evento
        //System.out.println("keyPressed910: " + "key code: " + source);
        //------KeyListener TABLA-----------
        //si el evento keyPressed se ha ejecutado desde la Tabla
        if (source == jtDetalle) {
            int rows = jtDetalle.getRowCount();
            int row = jtDetalle.getSelectedRow();
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
//            if (!numeros && !decimalPoint && !erraser && !enter && !tabulacion) {
//                e.consume();
//            }

            if (numeros) {
                if (jtDetalle.getModel().isCellEditable(row, col)) {
                    jtDetalle.setValueAt("", row, col); //reemplazar valor de la celda
                }
            }

            if (enter || tabulacion || numeros) {
                if (jtDetalle.isEditing()) {
                    jtDetalle.getCellEditor().stopCellEditing();
                }
            }
            //Enter para Ultima Columna. si ultima fila de la tabla Inserta una nueva fila
            if (col == 5 && enter && lastRow) {
                //Verifica que se haya ingresado cod_barra Correcto en la fila
                String cod = this.jtDetalle.getValueAt(row, 0).toString();
                if (cod.equals("0") || cod.equals("")) {
                    JOptionPane.showMessageDialog(this, "keyPressed960: Favor ingrese un producto Valido!", "¡A T E N C I O N!", JOptionPane.INFORMATION_MESSAGE);

                } else {
                    this.imInsDet();
                }
            } //Fin lastColumn, lastRow
        } //Fin if jtDetalle
    }//Fin keyPressed

    @Override
    public void mousePressed(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void imGrabar(String crud) {
        this.CRUD = crud;
        String msg;
        //Validacion de permisos
        if (Tools.validarPermiso(conexion.getGrupoId(), menuName, crud) == 0) {
            msg = "NO TIENE PERMISO PARA REALIZAR ESTA OPERACIÓN ";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
        }
        //validacion de campos del Formulario Cabecera
        if (!validarCamposCabecera()) {
            return;
        }
        //validacion de campos de Tabla Detalle
        if (!validarCamposDetalle()) {
            return;
        }

        this.setData();
        int idProducto = Integer.parseInt(this.jtfId.getText());
        if (idProducto > 0) { //Actualizar Registro Producto
            this.imActualizar("U");
            return;
        }

        if (idProducto == 0) { // Crear Registro Producto
            int rows = this.tc.createReg(this.myData);
            if (rows <= 0) {
                msg = "Error al intentar crear el registro";
                JOptionPane.showMessageDialog(this, msg, "ATENCION...!", JOptionPane.DEFAULT_OPTION);
                return;
            } else {
                msg = "Se ha creado exitosamente el registro: " + idProducto;
                JOptionPane.showMessageDialog(this, msg, "ATENCION...!", JOptionPane.DEFAULT_OPTION);
                idProducto = this.tc.getMaxId();
            }
        }
        //DETALLES ------------------------------
        //si pasó quiere decir que tenemos cabecera y recorremos Detalles
        for (Map<String, String> myRow : columnData) {
            myRow.put("id", "0");
            myRow.put("productoid", idProducto + "");
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
            msg = "NO TIENE PERMISO PARA ACTUALIZAR ESTOS REGISTROS ";
            JOptionPane.showMessageDialog(this, msg, "ATENCION...:!", JOptionPane.OK_OPTION);
            return;
        }
        this.setData();
        ArrayList<Map<String, String>> alCabecera;
        alCabecera = new ArrayList<>();
        alCabecera.add(this.myData);
        int rowsAffected = this.tc.updateReg(alCabecera);
        if (rowsAffected < 1) {
            msg = "Error al intentar actualizar el registro: " + this.jtfId.getText();
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.ERROR_MESSAGE);
            return; // si tfId > 0 y no grabo cambios, entonces return
        } else {
            msg = "Se ha actualizado exitosamente el registro: " + this.jtfId.getText();
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.INFORMATION_MESSAGE);
        }
        //-----------DETALLES-------------------
        //si pasó quiere decir que tenemos cabecera y recorremos el detalle
        int idProducto = Integer.parseInt(jtfId.getText()); //id de la cabecera
        Map<String, String> where = new HashMap<>();      //Por qué campo buscar los registros
        Map<String, String> fields = new HashMap<>();     //Los campos que vamos a recuperar
        ArrayList<Map<String, String>> alDetalle;         //Declara array de Map, cada Map es para un registro
        fields.put("*", "*");
        
        for (Map<String, String> myRow : columnData) {
            where.put("productoid", idProducto + "");
            where.put("cod_barra", myRow.get("cod_barra"));
            //Buscar si ya existe un detalle de este cod_barra para esta producto.
            this.colDat = this.tcdet.searchListById(fields, where);
            // si no existe un detalle con este cod_barra para esta compra
            if (this.colDat.isEmpty()) { 
                myRow.put("id", "0");
                myRow.put("productoid", idProducto + "");
                rowsAffected = this.tcdet.createReg(myRow);
                if (rowsAffected <= 0) {
                    msg = "No se ha podido grabar el Detalle Codigo:" + myRow.get("cod_barra");
                    JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
                } else {
                    msg = "Se ha creado el Detalle:" + myRow.get("cod_barra");
                    JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.INFORMATION_MESSAGE);
                }
            } else { //si ya existe un detalle con este cod_barra
                myRow.put("id", colDat.get(0).get("id")); // id del detalle
                myRow.put("productoid", idProducto + "");
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
        } //Fin Recorrer Detalles
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
        int idProducto = Integer.parseInt(jtfId.getText());
        if(idProducto <= 0){
            msg = "NO SE HA ENCONTRADO EL REGISTRO";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
        }
        if(idProducto > 0){
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
        sql = "SELECT id AS codigo, "
                + "CONCAT(nombre, ' ',descripcion) AS descripcion "
                + "FROM productos "
                + "WHERE LOWER(CONCAT(id, ' ', nombre, ' ', descripcion)) LIKE '%";

        wBuscar frame = new wBuscar(sql, this.jtfId);
        frame.setVisible(true);
        wPrincipal.desktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.DEFAULT_OPTION);
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
        System.out.println("PRODUCTOS imBuscar " + this.myData.toString());
        this.limpiarTabla();
        if (this.myData.isEmpty()) {
            System.out.println("No hay registros que mostrar");
            this.resetData();
            this.fillView(myData, columnData);
            return;
        }
        Map<String, String> where = new HashMap<>();      //Por qué campo buscar los registros
        where.put("productoid", this.myData.get("id"));
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
        this.myData = this.tc.navegationReg(jtfId.getText(), "FIRST");
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
            System.out.println("no hay fila seleccionada imInsDet 1062");
            modelo.addRow(new Object[]{"0", "0", "0", "0", "0", "0"});
            return;
        }

        String cod = this.jtDetalle.getValueAt(currentRow, 0).toString();
        if (cod.equals("0") || cod.equals("")) {
            String msg = "POR FAVOR INGRESE UN PRODUCTO ";
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
        } else {
            System.out.println("entro en imInsDet910");
            modelo.addRow(new Object[]{"0", "0", "0", "0", "0", "0"});
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
            String msg = "NO hay fila seleccionada ";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
        }
        ((DefaultTableModel) this.jtDetalle.getModel()).removeRow(jtDetalle.getSelectedRow());

        //Si al eliminar queda vacía, habrá que insertar una nueva
        int rows = jtDetalle.getRowCount();
        if (rows == 0) {
            System.out.println("Se eliminaron todas las filas");
            this.imInsDet();
        }
    }

    @Override
    public void imCerrar() {
        this.setVisible(false);
        this.dispose();
    }

    private void setData() {
        //CABECERA
        myData.put("id", jtfId.getText());
        myData.put("nombre", tfNombre.getText());
        myData.put("descripcion", tfDescripcion.getText());
        myData.put("categoriaid", ComboBox.ExtraeCodigo(jcbCategoria.getSelectedItem().toString()));
        myData.put("proveedorid", ComboBox.ExtraeCodigo(jcbProveedor.getSelectedItem().toString()));
        myData.put("marca", ComboBox.ExtraeCodigo(jcbMarca.getSelectedItem().toString()));
        myData.put("impuesto", tfImpuesto.getText());
        //servicio
        int servicio = 0;
        if (jCServicio.isSelected()) {
            servicio = 1;
        }
        myData.put("servicio", servicio + "");
        //estado
        int estado = 0;
        if (jCEstado.isSelected()) {
            estado = 1;
        }
        myData.put("estado", estado + "");
        System.out.println("myData " + myData);

        //DETALlE
        int rows = this.jtDetalle.getRowCount();
        for (int row = 0; row < rows; row++) {

            //celda codigo de barra
            String codigo_barra = (String) jtDetalle.getValueAt(row, 0);
            if (codigo_barra.equals("0")) {
                continue;
            }
            //celda color
            String b = (String) jtDetalle.getValueAt(row, 1);
            Integer color = Integer.parseInt(ComboBox.ExtraeCodigo(b));
            //celda tamano
            String c = (String) jtDetalle.getValueAt(row, 2);
            Integer tamano = Integer.parseInt(ComboBox.ExtraeCodigo(c));
            //celda diseno
            String d = (String) jtDetalle.getValueAt(row, 3);
            Integer diseno = Integer.parseInt(ComboBox.ExtraeCodigo(d));
            //celda uxb
            String e = (String) jtDetalle.getValueAt(row, 4);
            Integer uxb = Integer.parseInt(e);
            // celda stock
            String f = (String) jtDetalle.getValueAt(row, 5);
            Integer stock = Integer.parseInt(f);

            myDet = new HashMap<>();
            String productoid = jtfId.getText();
            myDet.put("productoid", productoid);
            myDet.put("cod_barra", codigo_barra);
            myDet.put("colorid", color + "");
            myDet.put("tamanoid", tamano + "");
            myDet.put("disenoid", diseno + "");
            myDet.put("uxb", uxb + "");
            myDet.put("stock", stock + "");

            this.columnData.add(this.myDet);

            System.out.println("myDet " + myDet);
        }
    }//fin setData

    private void resetData() {
        //Cabecera
        this.myData = new HashMap<>();
        this.myData.put("id", "0");
        this.myData.put("nombre", "");
        this.myData.put("descripcion", "");
        this.myData.put("marca", "0");
        this.myData.put("proveedorid", "0");
        this.myData.put("categoriaid", "0");
        this.myData.put("impuesto", "0");
        this.myData.put("servicio", "0");
        this.myData.put("estado", "0");
        jcbMarca.setSelectedIndex(0);
        jcbProveedor.setSelectedIndex(0);
        jcbCategoria.setSelectedIndex(0);

        //Detalle
//        DefaultTableModel dm = (DefaultTableModel) this.jtDetalle.getModel();
//        dm.getDataVector().removeAllElements();
        this.myDet = new HashMap<>();
        this.columnData.clear();

        this.myDet.put("productoid", "0");
        this.myDet.put("cod_barra", "0");
        this.myDet.put("colorid", "0");
        this.myDet.put("tamanoid", "0");
        this.myDet.put("disenoid", "0");
        this.myDet.put("uxb", "0");
        this.myDet.put("stok", "0");

        this.columnData.add(this.myDet);

        fillView(myData, columnData);
        //jC.setSelected(false);
    }//fin reset data

    private void fillView(Map<String, String> data, List<Map<String, String>> columnData) {
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
                case "id":
                    jtfId.setText(value);
                    break;
                case "nombre":
                    tfNombre.setText(value);
                    break;
                case "descripcion":
                    tfDescripcion.setText(value);
                    break;
                case "colorid":
                    ComboBox.E_estado(jcbColor, "colores", "id, color", "id=" + value);
                    break;
                case "marca":
                    ComboBox.E_estado(jcbMarca, "marcas", "id, nombre", "id=" + value);
                    //jcbMarca.setSelectedItem(Integer.parseInt(value));
                    break;
                case "proveedorid":
                    ComboBox.E_estado(jcbProveedor, "proveedores", "id, nombre", "id=" + value);
                    // jcbProveedor.setSelectedItem(Integer.parseInt(value));
                    break;
                case "categoriaid":
                    ComboBox.E_estado(jcbCategoria, "categorias", "id, nombre", "id=" + value);
                    //jcbCategoria.setSelectedItem(Integer.parseInt(value));
                    break;
                case "impuesto":
                    tfImpuesto.setText(value);
                    break;
                case "servicio":
                    if (Integer.parseInt(value) == 0) {
                        jCServicio.setSelected(false);
                    } else {
                        jCServicio.setSelected(true);
                    }
                    break;
                case "estado":
                    if (Integer.parseInt(value) == 0) {
                        jCEstado.setSelected(false);
                    } else {
                        jCEstado.setSelected(true);
                    }
                    break;
            }//end switch
        }//end CABECERA   

        //DETALLE TABLA 
        int row;
        row = 0;
        for (Map<String, String> myRow : columnData) { //Detalles
            JComboBox cbColor = (JComboBox) jtDetalle.getCellEditor(row, 1).getTableCellEditorComponent(jtDetalle, "0-Seleccionar", true, row, 1);
            ComboBox.E_estado(cbColor, "colores", "id, color", "id=" + myRow.get("colorid"));

            JComboBox cbTamano = (JComboBox) jtDetalle.getCellEditor(row, 2).getTableCellEditorComponent(jtDetalle, "0-Seleccionar", true, row, 2);
            ComboBox.E_estado(cbTamano, "tamanos", "id, tamano", "id=" + myRow.get("tamanoid"));

            JComboBox cbDiseno = (JComboBox) jtDetalle.getCellEditor(row, 3).getTableCellEditorComponent(jtDetalle, "0-Seleccionar", true, row, 3);
            ComboBox.E_estado(cbDiseno, "disenos", "id, diseno", "id=" + myRow.get("disenoid"));

            this.modelo.addRow(new Object[]{
                myRow.get("cod_barra"),
                cbColor.getSelectedItem(),
                cbTamano.getSelectedItem(),
                cbDiseno.getSelectedItem(),
                myRow.get("uxb"),
                myRow.get("stock")
            });

            this.jtDetalle.getSelectionModel().setSelectionInterval(row, 0);
            //int exist = this.getProducto(row, 0);
            row++;
        }//end for 2

    }//end fill

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

    public boolean validarCamposCabecera() {
        String msg = "";
        boolean valor = true;
        //validar id
        if (jtfId.getText().isEmpty() || "".equals(jtfId.getText())) {
            msg = "El campo Id no debe ser vacio";
            valor = false;
            jtfId.setText("0");
            jtfId.requestFocus();
        }
        //validar Nombre
        if (tfNombre.getText().isEmpty() || "".equals(tfNombre.getText())) {
            msg = "Proporcione nombre para Producto";
            valor = false;
            tfNombre.requestFocus();
        } // validar Impuesto
        if (tfImpuesto.getText().isEmpty() || "".equals(tfImpuesto.getText())) {
            msg = "El campo impuesto no puede ser vacio";
            valor = false;
            tfImpuesto.requestFocus();
        }
        String validacionCategoria = ComboBox.ExtraeCodigo(jcbCategoria.getSelectedItem().toString());
        if (Integer.parseInt(validacionCategoria) < 1) {
            msg = "El producto debe tener una categoria";
            valor = false;
            jcbCategoria.requestFocus();
        }
        if (!valor) {
            JOptionPane.showMessageDialog(this, msg, "Validacion de Campos Cabecera!", JOptionPane.DEFAULT_OPTION);
        }
        return valor;
    }

    public boolean validarCamposDetalle() {
        //Validar Campos de los Detalles
        boolean valor = true;
        String msg = "Defecto Validar Detalles";
        String sql = "";
        int rows = this.jtDetalle.getRowCount();
        for (int i = 0; i < rows; i++) {
            //celda codigo de barra
            String codbar = (String) jtDetalle.getValueAt(i, 0);
            if (codbar.equals("0") || "".equals(codbar)) {
                msg = "Proporcione codigo de barra para el registro";
                JOptionPane.showMessageDialog(this, msg, "1206Validacion de Campos Detalle!", JOptionPane.DEFAULT_OPTION);
                jtDetalle.changeSelection(i, 0, false, false);
                valor = false;
                return valor;
            }

            //verificar que ya no se haya utilizado el codigo de barra para otro producto
            String productoId = jtfId.getText();
            sql = "SELECT CONCAT(p.nombre, ' ', "
                    + "m.nombre, ' , ', "
                    + "c.color, ' ', t.tamano, ' ', s.diseno) AS descripcion "
                    + "FROM productos p, producto_detalle d, marcas m, colores c, tamanos t, disenos s "
                    + "WHERE (p.id = d.productoid AND p.id != '" + productoId + "') " //Otro Producto ID
                    + "AND p.marca = m.id "
                    + "AND d.colorid = c.id "
                    + "AND d.tamanoid = t.id "
                    + "AND d.disenoid = s.id "
                    + "AND d.cod_barra = '" + codbar + "'";

            ResultSet rs;
            try {
                rs = conexion.ejecuteSQL(sql); //Esto devuelve un ResultSet
                ResultSetMetaData metaData = rs.getMetaData();
                int colCount = metaData.getColumnCount();
                if (rs.next()) {
                    valor = false;
                    
                    for (int r = 1; r <= colCount; r++) {
                        String descripcion = rs.getString("descripcion");
                        msg = "El codigo:" + codbar + "ya ha sido utilizado. \n"
                                + "Producto INFO: '" + descripcion +"'";
                        JOptionPane.showMessageDialog(this, msg, "validarCampoDetalle1237", JOptionPane.OK_OPTION);
                    }
                    return valor;
                }
            } catch (SQLException ex) {
                Logger.getLogger(tableModel.class.getName()).log(Level.SEVERE, null, ex);
            } //End Catch
        }//End For
        return valor;
    }//End ValidarCamposDetalles

} //End wProducto
