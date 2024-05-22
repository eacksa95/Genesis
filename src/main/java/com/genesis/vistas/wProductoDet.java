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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class wProductoDet extends javax.swing.JInternalFrame implements MouseListener, KeyListener, ActiveFrame {
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
    String Opcion = "";
    private int filasTabla;
    private int columnasTabla;
    public static int filaSeleccionada;

    private ArrayList<Map<String, String>> columnData, colDat;

    //private final tableModel tmProducto;
    Map<String, String> mapProductos;

    private final tableModel tmProductoDet;
    Map<String, String> mapProductoDet;

    public wProductoDet(String Opcion) {
        initComponents();
        this.Opcion = Opcion;
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
        //Map<String, String> select = new HashMap<>();
        //Map<String, String> where = new HashMap<>();
    }

    /**
     * Metodo que permite construir la tabla para el detalle se crean primero
     * las columnas y luego se asigna la información
     */
    private void construirTabla() {
        /**
         * Aquí se inicializan los valores. El metodo consultar tiene la forma
         * como debería ser en el metodo de recuperar registro.
         */
        listaDetalles = consultarListaDetalles();        
        //Este array cambiará de valores según la tabla que querramos representar
        //en este caso nuestro detalle tiene esa estructura de columnas
        ArrayList<String> titulosList = new ArrayList<>();
        titulosList.add("Cod Barra");
        titulosList.add("Color");
        titulosList.add("Tamaño");
        titulosList.add("Diseño");
        titulosList.add("UxB");
        titulosList.add("Stock Min");

        //se asignan las columnas al arreglo para enviarse al momento de construir la tabla
        //Esto es porque la tabla recibe arreglo [] y no un ArrayList
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
     * Este método simula una consulta a la base de datos y devuelve una lista de pojoProductoDetalle.
     *@return lista
     */
    private ArrayList<pojoProductoDetalle> consultarListaDetalles() {
        //ArrayList<pojoCompraDetalle> lista = new ArrayList<>();
        this.lista.add(new pojoProductoDetalle(0, "0", 0, 0, 0, 0, 0));
        //productoid, "cod_barra", colorid, disenoid, tamanoid, uxb, stock
        return lista;
    }

    /**
     * Este método convierte la lista de pojoProductoDetalle
     * en una matriz de objetos para ser usada por la tabla.
     * @param titulosList
     * @return
     */
    private Object[][] obtenerMatrizDatos(ArrayList<String> titulosList) {

        /*se crea la matriz donde las filas son dinamicas pues corresponde
         * a todos los usuarios, mientras que las columnas son estaticas
         * correspondiendo a las columnas definidas por defecto
         */
//        System.out.println("lista det size "+listaDetalles.size());
//        System.out.println("title info "+titulosList.size());
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

        filasTabla = jtDetalle.getRowCount();
        columnasTabla = jtDetalle.getColumnCount();
        
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
        //scrollPaneTabla.setViewportView(jtDetalle);
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
        tfId = new javax.swing.JTextField();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jtDetalle = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Producto");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Cabecera"));

        jLabel1.setText("ID ");

        tfId.setText("0");
        tfId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfIdKeyPressed(evt);
            }
        });

        jLabel2.setText("Nombre");

        jLabel3.setText("Descripción");

        jLabel4.setText("Marca");

        jLabel5.setText("Proveedor");

        jLabel6.setText("Categoría");

        jLabel9.setText("Impuesto");

        jLabel10.setText("Servicio");

        jLabel11.setText("Estado");

        jcbMarca.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Seleccione Marca" }));

        jcbProveedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Seleccione Proveedor" }));

        jcbCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Seleccione Categoria" }));

        jLabel7.setText("%");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jcbProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(31, 31, 31)
                                .addComponent(jcbMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(jcbCategoria, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tfId)
                                    .addComponent(tfNombre)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tfDescripcion)))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel11))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCServicio)
                                    .addComponent(jCEstado)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tfImpuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel7)))))
                .addContainerGap(268, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(tfDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(tfImpuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jCServicio))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jCEstado))))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jcbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jcbProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jcbMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

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
        jScrollPane1.setViewportView(jtDetalle);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 706, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tfIdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfIdKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.imBuscar();
        }
    }//GEN-LAST:event_tfIdKeyPressed


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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> jcbCategoria;
    private javax.swing.JComboBox<String> jcbMarca;
    private javax.swing.JComboBox<String> jcbProveedor;
    private javax.swing.JTable jtDetalle;
    private javax.swing.JTextField tfDescripcion;
    private javax.swing.JTextField tfId;
    private javax.swing.JTextField tfImpuesto;
    private javax.swing.JTextField tfNombre;
    // End of variables declaration//GEN-END:variables

    @Override
    public void mouseClicked(MouseEvent e) {
//        //capturo fila o columna dependiendo de mi necesidad
//        
//        //OBS: Aquí debemos llamar a un método que controle que los campos de la cabecera estén completos
        int fila = jtDetalle.rowAtPoint(e.getPoint());
//        int columna = jtDetalle.columnAtPoint(e.getPoint());
//
//        /*uso la columna para valiar si corresponde a la columna de perfil garantizando
//         * que solo se produzca algo si selecciono una fila de esa columna
//         */
//        if (columna == 0) { //0 corresponde a cod barra
//            //sabiendo que corresponde a la columna de perfil, envio la posicion de la fila seleccionada
//            validarSeleccionMouse(fila);
//        }else if (columna == 2){//se valida que sea la columna del otro evento 2 que corresponde a precio
//            //JOptionPane.showMessageDialog(null, "Evento del otro icono");
//        }
    }

    /**
     * Este metodo simularia el proceso o la acción que se quiere realizar si se
     * presiona alguno de los botones o iconos de la tabla
     *
     * @param fila
     */
    private void validarSeleccionMouse(int fila) {
        this.filaSeleccionada = fila;
        //teniendo la fila entonces se obtiene el objeto correspondiente para enviarse como parammetro o imprimir la información
        pojoProductoDetalle rowDetalle = new pojoProductoDetalle();
        rowDetalle.setString("cod_barra", jtDetalle.getValueAt(fila, 0).toString());
       //rowDetalle.setString("descripcion", jtDetalle.getValueAt(fila, 1).toString());

      //  String info="INFO PERSONA\n";
       // info+="Código: "+rowDetalle.getString("cod_barra")+"\n";
       // info+="Descripción: "+rowDetalle.getString("descripcion")+"\n";
   }
//    esta funcion estuvo comentada por completo

    @Override
    public void imGrabar(String crud) {
        this.CRUD = crud;        
        String msg;
        if (Tools.validarPermiso(conexion.getGrupoId(), Opcion, crud) == 0) {
            msg = "NO TIENE PERMISO PARA REALIZAR ESTA OPERACIÓN ";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
        }
        //validacion de campos del Formulario Cabecera
           //validar id
        if (tfId.getText().isEmpty() || "".equals(tfId.getText())) {
             JOptionPane.showMessageDialog(this, "El campo Id no debe ser vacio");
             tfId.requestFocus();
            return;
        } //validar Nombre
        if (tfNombre.getText().isEmpty() || "".equals(tfNombre.getText())) {
             JOptionPane.showMessageDialog(this, "Proporcione nombre para Producto");
             tfNombre.requestFocus();
            return;
        } // validar Impuesto
        if (tfImpuesto.getText().isEmpty() || "".equals(tfImpuesto.getText())) {
             JOptionPane.showMessageDialog(this, "El campo Id no debe ser vacio");
             tfImpuesto.requestFocus();
            return;
        }
        
        this.setData();
        ArrayList<Map<String, String>> alCabecera;         //Cada Map es para un registro cabecera
        alCabecera = new ArrayList<>(); //Instancia array
        int idProducto, rows;
        idProducto = Integer.parseInt(this.tfId.getText());
        
        if (idProducto > 0) { 
            //Actualizar Registro Producto Cabecera
            alCabecera.add(this.myData); 
            int rowsAffected = this.tc.updateReg(alCabecera);
            //si rowsAffected < 1 return
            if (rowsAffected < 1) {
                msg = "Error al intentar actualizar el registro: " + this.tfId.getText();
                JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
                return; // si tfId < 0 y no grabo cambios, entonces return
            } else {
                msg = "Se ha actualizado exitosamente el registro: " + this.tfId.getText();
                JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
            }
            
        } else { 
            // Crear Registro Producto Cabecera
            rows = this.tc.createReg(this.myData);
            idProducto = this.tc.getMaxId();
            if (rows < 1) {
                msg = "Error al intentar crear el registro";
                JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
                return; //si tfId = 0 pero no grabo reg, entonces return
            } else {
                msg = "Se ha creado exitosamente el registro: " + idProducto;
                JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
                this.tfId.requestFocus();
            }
        }
        //Detalles
        //si pasó quiere decir que tenemos cabecera y recorremos Detalles
        Map<String, String> where = new HashMap<>();      //Por qué campo buscar los registros
        Map<String, String> fields = new HashMap<>();     //Los campos que vamos a recuperar
        ArrayList<Map<String, String>> alDetalle;        //Declara array de Map, cada Map es para un registro
        fields.put("*", "*");

        for (Map<String, String> myRow : columnData) {
            where.put("productoid", idProducto + "");
            where.put("cod_barra", myRow.get("cod_barra"));
            this.colDat = this.tcdet.searchListById(fields, where);
            
            myRow.put("productoid", idProducto + ""); 

            if (this.colDat.isEmpty()) { // si no existe un detalle con este cod_barra para este producto
                myRow.put("id", "0");
                rows = this.tcdet.createReg(myRow);
                //crear registro en la tabla Stock para el nuevo ProductoDetalle
                if(rows < 1){
                    msg = "No se ha podido grabar el Detalle Codigo:" + myRow.get("cod_barra");
                    JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
                } else {
                    msg = "Se ha creado el Detalle:" + myRow.get("cod_barra") + "para este producto";
                    JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
                }

            } else { //si ya existe un detalle con este cod_barra para este producto
                myRow.put("id", colDat.get(0).get("id"));
                alDetalle = new ArrayList<>(); //necesitamos el alDetalle por la estructura de la funcion tcdet.updateReg
                alDetalle.add(myRow);
                int rowsAffected = this.tcdet.updateReg(alDetalle);   //Recordar que el modelo sólo procesa de a uno los registros
                if(rowsAffected < 1){
                    msg = "No se ha podido actualizar el detalle: " + myRow.get("cod_barra") + " del producto. Por favor verifique";
                    JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
                    return;
                } else {
                    msg = "Ya existe un Detalle con este codigo de barra:" + myRow.get("cod_barra") + "para este producto";
                    JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
                }
            }
        }
        this.imInsDet();
        this.imNuevo();
        // this.fillView(myData, columnData);
    }//fin imGrabar

    public void limpiarCelda(JTable tabla) {
        tabla.setValueAt("", tabla.getSelectedRow(), tabla.getSelectedColumn());
    }

    @Override
    public void imFiltrar() {
        String sql;
        sql = "SELECT id AS codigo, "
                + "CONCAT(nombre, ' ',descripcion) AS descripcion "
                + "FROM productos "
                + "WHERE LOWER(CONCAT(id, ' ',  nombre, ' ',descripcion)) LIKE '%";

        wBuscar frame = new wBuscar(sql, this.tfId);
        frame.setVisible(true);
        wPrincipal.desktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.DEFAULT_OPTION);
        }
    }

    @Override
    public void imActualizar(String crud) {
        this.CRUD = crud;
        if (Tools.validarPermiso(conexion.getGrupoId(), Opcion, crud) == 0) {
            String msg = "NO TIENE PERMISO PARA REALIZAR ESTA OPERACIÓN ";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
        }
        this.setData();
        ArrayList<Map<String, String>> alCabecera;         //Declara array de Map, cada Map es para un registro
        alCabecera = new ArrayList<>(); //Instancia array
        alCabecera.add(this.myData);                      //agrega el Map al array, para la cabecera será el mejor de los casos, es decir 1 registro 
        int rowsAffected = this.tc.updateReg(alCabecera); //Está guardando igual si en el detalle hay error
        //Para el DETALLE
        ArrayList<Map<String, String>> alDetalle;         //Declara array de Map, cada Map es para un registro
        alDetalle = new ArrayList<>(); //Instancia array

        for (Map<String, String> myRow : columnData) {       //hay que recorrer el detalle y envira de a uno.
            System.out.println("ENVIAMOS " + myData.get("id"));
            myRow.put("productoid", myData.get("id"));      //asignamos el id de la cabecera como el fk del detalle
            alDetalle.add(myRow);
        }
        int affected = this.tcdet.updateReg(alDetalle);   //Recordar que el modelo sólo procesa de a uno los registros
        if (affected <= 0) {                               //Si no guardó la cabecera, no se procesa detalle
            System.out.println("No se ha podido actualizar el detalle");
            return;
        }
        this.resetData();
        this.fillView(myData, columnData);
    }

    @Override
    public void imBorrar(String crud) {
        this.CRUD = crud;
        if (Tools.validarPermiso(conexion.getGrupoId(), Opcion, crud) == 0) {
            String msg = "NO TIENE PERMISO PARA REALIZAR ESTA OPERACIÓN ";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
        }
        this.setData();
        //Para el DETALLE
        ArrayList<Map<String, String>> alDetalle;         //Declara array de Map, cada Map es para un registro
        alDetalle = new ArrayList<>(); //Instancia array

        for (Map<String, String> myRow : columnData) {       //hay que recorrer el detalle y envira de a uno.
            myRow.put("precioid", myData.get("id"));      //asignamos el id de la cabecera como el fk del detalle
            alDetalle.add(myRow);
        }
        int affected = this.tcdet.deleteReg(alDetalle);   //Recordar que el modelo sólo procesa de a uno los registros
        ArrayList<Map<String, String>> alCabecera;         //Declara array de Map, cada Map es para un registro
        alCabecera = new ArrayList<>(); //Instancia array
        alCabecera.add(myData);                           //agrega el Map al array, para la cabecera será el mejor de los casos, es decir 1 registro 
        int rowsAffected = this.tc.deleteReg(alCabecera); //Está guardando igual si en el detalle hay error
        //Invocamos el método deleteReg del Modelo que procesa un array
        if (rowsAffected <= 0) {
            String msg = "NO SE HA PODIDO ELIMINAR EL REGISTRO: " + tfId.getText();
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
            return;
        }
        if (rowsAffected > 0) {
            String msg = "EL REGISTRO: " + tfId.getText() + " SE HA ELIMINADO CORRECTAMENTE";
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
        }
        imNuevo();
    }//fin ImBorrar

    @Override
    public void imNuevo() {
        this.resetData();
        this.limpiarTabla();
        this.imInsDet();
        this.fillView(myData, columnData);
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
        this.myData = this.tc.navegationReg(tfId.getText(), "PRIOR");
        this.fillView(this.myData, columnData);
        this.setData();
        imBuscar();
    }

    @Override
    public void imSiguiente() {
        this.myData = this.tc.navegationReg(tfId.getText(), "NEXT");
        this.fillView(this.myData, columnData);
        this.setData();
        imBuscar();
    }

    @Override
    public void imAnterior() {
        this.myData = this.tc.navegationReg(tfId.getText(), "PRIOR");
        this.fillView(this.myData, columnData);
        this.setData();
        imBuscar();
    }

    @Override
    public void imUltimo() {
        this.myData = this.tc.navegationReg(tfId.getText(), "LAST");
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
            System.out.println("entro en imInsDet");
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
        ((DefaultTableModel) this.jtDetalle.getModel()).removeRow(jtDetalle.getSelectedRow());
    }

    @Override
    public void imCerrar() {
        this.setVisible(false);
        this.dispose();
    }

    private void setData() {
        //CABECERA
        myData.put("id", tfId.getText());
        myData.put("nombre", tfNombre.getText());
        myData.put("descripcion", tfDescripcion.getText());
        myData.put("categoriaid", ComboBox.ExtraeCodigo(jcbCategoria.getSelectedItem().toString()));
        myData.put("proveedorid", ComboBox.ExtraeCodigo(jcbProveedor.getSelectedItem().toString()));
        myData.put("marca", ComboBox.ExtraeCodigo(jcbMarca.getSelectedItem().toString()));
        myData.put("impuesto", tfImpuesto.getText());
        //servicio
        int servicio = 0;
        if (jCServicio.isSelected()) { servicio = 1; }
        myData.put("servicio", servicio + "");
        //estado
        int estado = 0;
        if (jCEstado.isSelected()) { estado = 1; }
        myData.put("estado", estado + "");
        System.out.println("myData " + myData);

        //DETALlE
        int rows = this.jtDetalle.getRowCount();
        int i;
        for (i = 0; i < rows; i++) {

            //celda codigo de barra
            String codigo_barra = (String) jtDetalle.getValueAt(i, 0);
            if (codigo_barra.equals("0")) {
                continue;
            }
            //celda color
            String b = (String) jtDetalle.getValueAt(i, 2);
            Integer color = Integer.parseInt(ComboBox.ExtraeCodigo(b));
            //celda tamano
            String c = (String) jtDetalle.getValueAt(i, 2);
            Integer tamano = Integer.parseInt(ComboBox.ExtraeCodigo(c));
            //celda diseno
            String d = (String) jtDetalle.getValueAt(i, 3);
            Integer diseno = Integer.parseInt(ComboBox.ExtraeCodigo(d));
            //celda uxb
            String e = (String) jtDetalle.getValueAt(i, 4);
            Integer uxb = Integer.parseInt(e);
            // celda stock
            String f = (String) jtDetalle.getValueAt(i, 5);
            Integer stock = Integer.parseInt(f);

            myDet = new HashMap<>();
            String productoid = tfId.getText();
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
                    tfId.setText(value);
                    break;
                case "nombre":
                    tfNombre.setText(value);
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

        for (Map<String, String> myRow : columnData) {

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
    
    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.print("\n Estoy en el keyListener, keyPressed");
        int row = jtDetalle.getSelectedRow();
        int rows = jtDetalle.getRowCount();
        int col = jtDetalle.getSelectedColumn();

        int key = e.getKeyChar();
        //System.out.println("tecla pulsada "+key);

        boolean numeros = key >= 48 && key <= 57;
        boolean decimalPoint = key == 46;
        boolean erraser = key == 8;

        if (!numeros && !decimalPoint && !erraser && key != 10) {
            //e.consume();
        } else {
            if (numeros) {
                if (jtDetalle.getModel().isCellEditable(row, col)) {
                    this.limpiarCelda(jtDetalle);
                }
            }

        }

        //System.out.println("key code "+e.getKeyCode());
        //System.out.println("Fila : "+row+ "/"+rows+" Column :"+col);
        if (key == 10 || key == 9 || (key >= 37 && key <= 40)) {//10 es enter
            if (jtDetalle.isEditing()) {
                jtDetalle.getCellEditor().stopCellEditing();
            }

            if (col == 0) {

                return;
            }
            if (col == 2 || col == 3 || col == 4 || col == 5) {

            }
            //System.out.println("Col "+col+ " key "+key);
            if (col == 5 && key == 10 && (row == (rows - 1))) { //Si está en la última columna y presiona enter, inserta una nueva fila
                //Podría controlarse que se haya ingresado previamente el codigo
                String cod = this.jtDetalle.getValueAt(row, 0).toString();

                //System.out.println("Codigo "+cod);
                if (cod.equals("0") || cod.equals("") || cod == null) {
                    JOptionPane.showMessageDialog(this, "Favor ingrese un producto!", "¡A T E N C I O N!", JOptionPane.INFORMATION_MESSAGE);
                    return;
                } else {
                    this.imInsDet();
                }

            }
        }
    }

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
        //capturo fila o columna dependiendo de mi necesidad
        //OBS: Aquí debemos llamar a un método que controle que los campos de la cabecera estén completos
        int fila = jtDetalle.rowAtPoint(e.getPoint());
        int columna = jtDetalle.columnAtPoint(e.getPoint());

        /*uso la columna para valiar si corresponde a la columna de perfil garantizando
         * que solo se produzca algo si selecciono una fila de esa columna
         */
        if (columna == 0) { //0 corresponde a cod barra
            //sabiendo que corresponde a la columna de perfil, envio la posicion de la fila seleccionada
            // validarSeleccionMouse(fila);
        } else if (columna == 2) {//se valida que sea la columna del otro evento 2 que corresponde a precio
            //JOptionPane.showMessageDialog(null, "Evento del otro icono");
        }
    }

    /**
     * Este metodo simularia el proceso o la acción que se quiere realizar si se
     * presiona alguno de los botones o iconos de la tabla
     *
     * @param fila
     */
    /* private void validarSeleccionMouse(int fila) {
        this.filaSeleccionada = fila;
        //teniendo la fila entonces se obtiene el objeto correspondiente para enviarse como parammetro o imprimir la información
        pojoProductoDetalle rowDetalle = new pojoProductoDetalle();
        rowDetalle.setString("cod_barra", jtDetalle.getValueAt(fila, 0).toString());
       //rowDetalle.setString("descripcion", jtDetalle.getValueAt(fila, 1).toString());

      //  String info="INFO PERSONA\n";
       // info+="Código: "+rowDetalle.getString("cod_barra")+"\n";
       // info+="Descripción: "+rowDetalle.getString("descripcion")+"\n";
    } */
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

}
