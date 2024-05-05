package com.genesis.vistas;

import com.genesis.model.conexion;
import com.genesis.model.pojoCompraDetalle;
import com.genesis.model.tableModel;
import com.genesis.tabla.GestionCeldas;
import com.genesis.tabla.GestionEncabezadoTabla;
import com.genesis.tabla.ModeloTabla;
import com.genesis.controladores.tableController;
import util.ComboBox;
import util.Tools;
import static util.Tools.decimalFormat;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author user
 */
public class wCompra extends javax.swing.JInternalFrame implements MouseListener, KeyListener, ActiveFrame {

    //Controladores
    private tableController tc;
    private tableController tcdet;
    String Opcion = "";
    String CRUD = "";
    private tableModel tmMoneda;
    Map<String, String> mapMoneda;// = new HashMap<String, String>();
    private String currentField;
    private String currentTable;

    private tableModel tmProducto;
    Map<String, String> mapProducto;// = new HashMap<String, String>();

    private tableModel tmProductoDet;
    Map<String, String> mapProductoDet;// = new HashMap<String, String>();

    ArrayList<pojoCompraDetalle> lista;// = new ArrayList<>();
    public static int filaSeleccionada;
    ArrayList<pojoCompraDetalle> listaDetalles;//lista que simula la información de la BD

    //
    private Map<String, String> myData;
    private HashMap<String, String> myDet, dataDet; 
    private ArrayList<Map<String, String>> columnData, colData, colDat;

    ModeloTabla modelo;//modelo de la JTable
    private int filasTabla;
    private int columnasTabla;

    private Date fecha; //= jdcFechaProceso.getDate();
    private DateFormat dateFormat, dateTimeFormat, dateIns; //= new SimpleDateFormat("dd/MM/yyyy HH:mm"); 

    /**
     * Creates new form wCompra
     */
    public wCompra(String Opcion) {
        initComponents();
        this.Opcion = Opcion;
        System.out.println("formato: " + decimalFormat(650.75));
        listaDetalles = new ArrayList<pojoCompraDetalle>();
        lista = new ArrayList<>();
        myData = new HashMap<String, String>();
        columnData = new ArrayList<Map<String, String>>();
        colData = new ArrayList<Map<String, String>>();
        //Para menejo de Monedas
        mapMoneda = new HashMap<String, String>();
        tmMoneda = new tableModel();
        tmMoneda.init("monedas");
        construirTabla();

        //PARA EL DETALLE
        mapProducto = new HashMap<String, String>();
        tmProducto = new tableModel();
        tmProducto.init("productos");

        mapProductoDet = new HashMap<String, String>();
        tmProductoDet = new tableModel();
        tmProductoDet.init("producto_detalle");
        //setLocationRelativeTo(null);
        //Prepara la tabla con los valores iniciales
        //Estos son lísteners, es decir, esta misma clase implementa 
        //las interfaces línener para mouse y key y por tanto, la clase se escucha a sí misma
        jtDetalle.addMouseListener(this);
        jtDetalle.addKeyListener(this);
        this.tfcodbarra.addKeyListener(this);
        this.tfcodbarra.addMouseListener(this);
        jtDetalle.setOpaque(false);
        jtDetalle.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "selectNextColumnCell");
        //seleccionable cell
//        this.jtDetalle.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//        this.jtDetalle.setColumnSelectionAllowed(true);
//        this.jtDetalle.setRowSelectionAllowed(true);
        //Unificamos tipo y tamaño de fuente
        FontUIResource font = new FontUIResource("Times New Roman", Font.PLAIN, 12);
        UIManager.put("Table.font", font);
        UIManager.put("Table.foreground", Color.RED);

        // COMBO BOX DESPLEGABLES DE LAS TABLAS//
        ComboBox.pv_cargar(jcbPlazo, "plazo_pago", " id, plazo ", "id", "");
        ComboBox.pv_cargar(jcbMoneda, "monedas", " id, moneda ", "id", "");
        ComboBox.pv_cargar(jcbProveedor, "proveedores", "cod_proveedor, nombre_proveedor", "cod_proveedor", "");
        ComboBox.pv_cargar(jcbDeposito, "depositos", "id, nombre", "descripcion", "");

        // Inicializamos las fechas
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        dateIns = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        jdcLlega.setDate(new Date());
        jdcProceso.setDate(new Date());
        jdcFactura.setDate(new Date());
        jdcVence.setDate(new Date());
        getMoneda();

        tc = new tableController();
        tc.init("compras");
        tcdet = new tableController();
        tcdet.init("compra_detalle");
    }//fin constructor wCompra

    /**
     * Metodo que permite construir la tabla para el detalle se crean primero
     * las columnas y luego se asigna la información
     */
    private void construirTabla() {
        /**
         * Aquí se inicializan los valores. El metodo consultar tiene la forma
         * como debería ser en el metodo de recuperar registro.
         */
        listaDetalles.clear();
        listaDetalles = consultarListaDetalles();
        //Este array cambiará de valores según la tabla que querramos representar
        //en este caso nuestro detalle tiene esa estructura de columnas

        ArrayList<String> titulosList = new ArrayList<>();
//7 
        titulosList.add("Cod Barra");
        titulosList.add("Descripcion");
        titulosList.add("Precio");
        titulosList.add("Cantidad");
        titulosList.add("Descuento");
        titulosList.add("Bonificado");
        titulosList.add("Total");

        //se asignan las columnas al arreglo para enviarse al momento de construir la tabla
        //Esto es porque la tabla recibe arreglo [] y no un ArrayList, bien se pudo ya contruir de esa manera 
        //System.out.println("lista titulos "+titulosList.toString());
        String titulos[] = new String[titulosList.size()];

        for (int i = 0; i < titulos.length; i++) {
            titulos[i] = titulosList.get(i);
        }
        /*obtenemos los datos de la lista y los guardamos en la matriz
         * que luego se manda a construir la tabla
         */
        Object[][] data = obtenerMatrizDatos(titulosList);
        construirTabla(titulos, data);
        titulosList.clear();
    }

    /**
     * Permite simular el llenado de personas en una lista que posteriormente
     * alimentará la tabla
     *
     * @return
     */
    private ArrayList<pojoCompraDetalle> consultarListaDetalles() {
        //ArrayList<pojoCompraDetalle> lista = new ArrayList<>();
        this.lista.add(new pojoCompraDetalle(0, "0", "Descripcion", 0, 0, 0, 0, 0));
        return lista;
    }

    /**
     * Llena la información de la tabla usando la lista de personas trabajada
     * anteriormente, guardandola en una matriz que se retorna con toda la
     * información para luego ser asignada al modelo
     *
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
            //Poner los nombres de los campos de la tabla de la bd  //7 
            informacion[x][0] = listaDetalles.get(x).getString("cod_barra");
            informacion[x][1] = listaDetalles.get(x).getString("descripcion");
            informacion[x][2] = listaDetalles.get(x).getDouble("precio") + "";
            informacion[x][3] = listaDetalles.get(x).getDouble("cantidad") + "";
            informacion[x][4] = listaDetalles.get(x).getDouble("descuento") + "";
            informacion[x][5] = listaDetalles.get(x).getDouble("bonificado") + "";
            informacion[x][6] = listaDetalles.get(x).getDouble("total") + "";

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
        ArrayList<Integer> noEditable = new ArrayList<Integer>();
        noEditable.add(1);
        noEditable.add(6);
        modelo = new ModeloTabla(data, titulos, noEditable);
        //se asigna el modelo a la tabla
        jtDetalle.setModel(modelo);

        filasTabla = jtDetalle.getRowCount();
        columnasTabla = jtDetalle.getColumnCount();

        //se asigna el tipo de dato que tendrán las celdas de cada columna definida respectivamente para validar su personalización
        //7
        jtDetalle.getColumnModel().getColumn(0).setCellRenderer(new GestionCeldas("texto"));
        jtDetalle.getColumnModel().getColumn(1).setCellRenderer(new GestionCeldas("texto"));
        jtDetalle.getColumnModel().getColumn(2).setCellRenderer(new GestionCeldas("numerico"));
        jtDetalle.getColumnModel().getColumn(3).setCellRenderer(new GestionCeldas("numerico"));
        jtDetalle.getColumnModel().getColumn(4).setCellRenderer(new GestionCeldas("numerico"));
        jtDetalle.getColumnModel().getColumn(5).setCellRenderer(new GestionCeldas("numerico"));
        jtDetalle.getColumnModel().getColumn(6).setCellRenderer(new GestionCeldas("numerico"));

        //se recorre y asigna el resto de celdas que serian las que almacenen datos de tipo texto
        /*for (int i = 0; i < titulos.length-5; i++) {//se resta 5 porque las ultimas 5 columnas se definen arriba
                System.out.println(i);
                jtDetalle.getColumnModel().getColumn(i).setCellRenderer(new GestionCeldas("texto"));
        }*/
        jtDetalle.getTableHeader().setReorderingAllowed(false);
        jtDetalle.setRowHeight(25);//tamaño de las celdas
        jtDetalle.setGridColor(new java.awt.Color(0, 0, 0));
        //Se define el tamaño de largo para cada columna y su contenido
        jtDetalle.getColumnModel().getColumn(0).setPreferredWidth(150);//cod_barra
        jtDetalle.getColumnModel().getColumn(1).setPreferredWidth(200);//descripcion
        jtDetalle.getColumnModel().getColumn(2).setPreferredWidth(150);//precio
        jtDetalle.getColumnModel().getColumn(3).setPreferredWidth(100);//cantidad
        jtDetalle.getColumnModel().getColumn(4).setPreferredWidth(100);//descuento
        jtDetalle.getColumnModel().getColumn(5).setPreferredWidth(100);//bonificado
        jtDetalle.getColumnModel().getColumn(6).setPreferredWidth(200);//total

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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jdcProceso = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jdcVence = new com.toedter.calendar.JDateChooser();
        jdcLlega = new com.toedter.calendar.JDateChooser();
        jdcFactura = new com.toedter.calendar.JDateChooser();
        jcbPlazo = new javax.swing.JComboBox<>();
        jcbMoneda = new javax.swing.JComboBox<>();
        jcbProveedor = new javax.swing.JComboBox<>();
        jftfCotizacion = new javax.swing.JFormattedTextField();
        jftfAdelanto = new javax.swing.JFormattedTextField();
        jftfSerie = new javax.swing.JFormattedTextField();
        jcbTipo = new javax.swing.JComboBox<>();
        jtfTimbrado = new javax.swing.JTextField();
        jftfFactura = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jtfObs = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jcbDeposito = new javax.swing.JComboBox<>();
        tfcodbarra = new javax.swing.JTextField();
        jtfId = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtDetalle = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jftfNeto = new javax.swing.JFormattedTextField();
        jftfImpuesto = new javax.swing.JFormattedTextField();
        jftfExenta = new javax.swing.JFormattedTextField();
        jftfTotal = new javax.swing.JFormattedTextField();
        jSeparator1 = new javax.swing.JSeparator();

        setClosable(true);
        setIconifiable(true);
        setTitle("Compra de Mercaderías");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Cabecera Compra"));

        jLabel1.setText("Id:");

        jLabel2.setText("F. Proceso:");

        jLabel3.setText("F. Legada:");

        jLabel4.setText("Plazo:");

        jLabel5.setText("Moneda:");

        jLabel6.setText("Cotización:");

        jLabel7.setText("Adelanto:");

        jdcProceso.setToolTipText("Fecha/Hora de carga en el sistema");
        jdcProceso.setDateFormatString("dd/MM/yyyy HH:mm");

        jLabel8.setText("Proveedor:");

        jLabel9.setText("Timbrado:");

        jLabel10.setText("Serie:");

        jLabel11.setText("Vence:");

        jLabel12.setText("Nro Factura:");

        jLabel13.setText("F. Factura:");

        jLabel14.setText("Condición :");

        jdcVence.setToolTipText("Fecha/Hora de carga en el sistema");
        jdcVence.setDateFormatString("dd/MM/yyyy HH:mm");

        jdcLlega.setToolTipText("Fecha/Hora de carga en el sistema");
        jdcLlega.setDateFormatString("dd/MM/yyyy HH:mm");

        jdcFactura.setToolTipText("Fecha/Hora de carga en el sistema");
        jdcFactura.setDateFormatString("dd/MM/yyyy HH:mm");

        jcbPlazo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Seleccione Plazo" }));

        jcbMoneda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Seleccione Moneda" }));
        jcbMoneda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbMonedaActionPerformed(evt);
            }
        });

        jcbProveedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Seleccione Proveedor" }));
        jcbProveedor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jcbProveedorFocusGained(evt);
            }
        });
        jcbProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbProveedorActionPerformed(evt);
            }
        });

        jftfCotizacion.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jftfCotizacion.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jftfCotizacion.setText("0.0");
        jftfCotizacion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jftfCotizacionFocusGained(evt);
            }
        });

        jftfAdelanto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jftfAdelanto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jftfAdelanto.setText("0.0");
        jftfAdelanto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jftfAdelantoFocusGained(evt);
            }
        });

        try {
            jftfSerie.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###-###-")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jftfSerie.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jftfSerie.setText("001-001-");
        jftfSerie.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jftfSerieFocusGained(evt);
            }
        });

        jcbTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1-Contado", "2-Crédito" }));

        jtfTimbrado.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtfTimbrado.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtfTimbradoFocusGained(evt);
            }
        });

        jftfFactura.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jftfFactura.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jftfFacturaFocusGained(evt);
            }
        });

        jLabel19.setText("OBS:");

        jLabel20.setText("Depósito:");

        jcbDeposito.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Seleccione Depósito" }));

        tfcodbarra.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfcodbarraFocusGained(evt);
            }
        });
        tfcodbarra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfcodbarraActionPerformed(evt);
            }
        });
        tfcodbarra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfcodbarraKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfcodbarraKeyReleased(evt);
            }
        });

        jtfId.setText("0");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(4, 4, 4))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel6)
                                            .addGap(25, 25, 25)
                                            .addComponent(jftfCotizacion))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel19)
                                                .addComponent(jLabel14))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jftfAdelanto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jcbTipo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGap(32, 32, 32)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel4))
                                .addGap(257, 257, 257)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jftfFactura)
                            .addComponent(jdcFactura, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(85, 85, 85)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jcbPlazo, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jcbMoneda, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(109, 109, 109)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jftfSerie)
                                    .addComponent(jdcVence, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addGap(18, 18, 18)
                                .addComponent(jcbDeposito, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                                .addComponent(jdcLlega, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jLabel1)
                                        .addGap(20, 20, 20)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jtfId)
                                    .addComponent(jdcProceso, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE))))
                        .addGap(43, 43, 43)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jcbProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jtfTimbrado, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jtfObs, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tfcodbarra, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(36, 36, 36))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel20)
                            .addComponent(jcbDeposito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jtfId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(jdcProceso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jcbProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel3)
                    .addComponent(jdcLlega, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jtfTimbrado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jcbPlazo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jftfSerie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel5)
                    .addComponent(jcbMoneda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jdcVence, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel6)
                    .addComponent(jftfCotizacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(jftfFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel7)
                    .addComponent(jftfAdelanto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(jdcFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addComponent(jtfObs, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tfcodbarra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalles Compra"));

        jtDetalle.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jtDetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"0", "Descripcion",  new Double(0.0),  new Double(0.0),  new Double(0.0),  new Double(0.0),  new Double(0.0)}
            },
            new String [] {
                "Cod Barra", "Descripción", "Precio", "Cantidad", "Descuento", "Bonificado", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
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
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Totales:"));

        jLabel15.setText("Neto:");

        jLabel16.setText("Impuesto:");

        jLabel17.setText("Excenta:");

        jLabel18.setText("Total:");

        jftfNeto.setEditable(false);
        jftfNeto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jftfNeto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jftfNeto.setText("0.0");
        jftfNeto.setFocusable(false);

        jftfImpuesto.setEditable(false);
        jftfImpuesto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jftfImpuesto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jftfImpuesto.setText("0.0");
        jftfImpuesto.setFocusable(false);

        jftfExenta.setEditable(false);
        jftfExenta.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jftfExenta.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jftfExenta.setText("0.0");
        jftfExenta.setFocusable(false);

        jftfTotal.setEditable(false);
        jftfTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jftfTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jftfTotal.setText("0.0");
        jftfTotal.setFocusable(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jftfNeto, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jftfImpuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jftfExenta, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jftfTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jSeparator1))
                .addContainerGap(170, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jftfNeto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jftfImpuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jftfExenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jftfTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(112, 112, 112))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(19, 19, 19))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );

        getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jcbProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbProveedorActionPerformed

    private void jtfTimbradoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfTimbradoFocusGained
        jtfTimbrado.selectAll();
    }//GEN-LAST:event_jtfTimbradoFocusGained

    private void jftfAdelantoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jftfAdelantoFocusGained
        jftfAdelanto.selectAll();
    }//GEN-LAST:event_jftfAdelantoFocusGained

    private void jftfSerieFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jftfSerieFocusGained
        jftfSerie.selectAll();
    }//GEN-LAST:event_jftfSerieFocusGained

    private void jftfFacturaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jftfFacturaFocusGained
        jftfFactura.selectAll();
    }//GEN-LAST:event_jftfFacturaFocusGained

    private void jtDetalleFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtDetalleFocusGained
        validarCombo();  
        currentTable = "tabla";
    }//GEN-LAST:event_jtDetalleFocusGained

    private void jcbProveedorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jcbProveedorFocusGained
        currentField = "proveedor";        // TODO add your handling code here:
    }//GEN-LAST:event_jcbProveedorFocusGained

    private void tfcodbarraFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfcodbarraFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_tfcodbarraFocusGained

    private void tfcodbarraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfcodbarraActionPerformed

    }//GEN-LAST:event_tfcodbarraActionPerformed

    private void tfcodbarraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfcodbarraKeyPressed
        if (currentTable.equals("tabla")) {
            //OBS: Aquí debemos llamar a un método que controle que los campos de la cabecera estén completos
            int row = jtDetalle.getSelectedRow();
            int rows = jtDetalle.getRowCount();
            int col = jtDetalle.getSelectedColumn();
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                this.getProducto(row, col);
            }
        } //end if
    }//GEN-LAST:event_tfcodbarraKeyPressed

    private void tfcodbarraKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfcodbarraKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tfcodbarraKeyReleased

    private void jcbMonedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbMonedaActionPerformed
        this.getMoneda();
    }//GEN-LAST:event_jcbMonedaActionPerformed

    private void jftfCotizacionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jftfCotizacionFocusGained
        jftfCotizacion.selectAll();
    }//GEN-LAST:event_jftfCotizacionFocusGained


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JComboBox<String> jcbDeposito;
    private javax.swing.JComboBox<String> jcbMoneda;
    private javax.swing.JComboBox<String> jcbPlazo;
    private javax.swing.JComboBox<String> jcbProveedor;
    private javax.swing.JComboBox<String> jcbTipo;
    private com.toedter.calendar.JDateChooser jdcFactura;
    private com.toedter.calendar.JDateChooser jdcLlega;
    private com.toedter.calendar.JDateChooser jdcProceso;
    private com.toedter.calendar.JDateChooser jdcVence;
    private javax.swing.JFormattedTextField jftfAdelanto;
    private javax.swing.JFormattedTextField jftfCotizacion;
    private javax.swing.JFormattedTextField jftfExenta;
    private javax.swing.JTextField jftfFactura;
    private javax.swing.JFormattedTextField jftfImpuesto;
    private javax.swing.JFormattedTextField jftfNeto;
    private javax.swing.JFormattedTextField jftfSerie;
    private javax.swing.JFormattedTextField jftfTotal;
    public javax.swing.JTable jtDetalle;
    private javax.swing.JTextField jtfId;
    private javax.swing.JTextField jtfObs;
    private javax.swing.JTextField jtfTimbrado;
    private javax.swing.JTextField tfcodbarra;
    // End of variables declaration//GEN-END:variables

    @Override
    public void mouseClicked(MouseEvent e) {
        //capturo fila o columna dependiendo de mi necesidad

        //OBS: Aquí debemos llamar a un método que controle que los campos de la cabecera estén completos
        int fila = jtDetalle.rowAtPoint(e.getPoint());
        int columna = jtDetalle.columnAtPoint(e.getPoint());

        /*uso la columna para valiar si corresponde a la columna de perfil garantizando
         * que solo se produzca algo si selecciono una fila de esa columna
         */
        if (columna == 0) { //0 corresponde a cod barra
            //sabiendo que corresponde a la columna de perfil, envio la posicion de la fila seleccionada
            validarSeleccionMouse(fila);
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
    private void validarSeleccionMouse(int fila) {
        this.filaSeleccionada = fila;
        //teniendo la fila entonces se obtiene el objeto correspondiente para enviarse como parammetro o imprimir la información
        pojoCompraDetalle rowDetalle = new pojoCompraDetalle();
        rowDetalle.setString("cod_barra", jtDetalle.getValueAt(fila, 0).toString());
        rowDetalle.setString("descripcion", jtDetalle.getValueAt(fila, 1).toString());

        String info = "INFO PERSONA\n";
        info += "Código: " + rowDetalle.getString("cod_barra") + "\n";
        info += "Descripción: " + rowDetalle.getString("descripcion") + "\n";
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.validarCabecera();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //System.out.println(" aquí es cuando ya se presiona enter para el cambio");
    }

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
                this.getProducto(row, col);
                return;
            }
            if (col == 2 || col == 3 || col == 4 || col == 5) {
                this.setTotalRow(row);
                this.setTotalGral();
            }
            //System.out.println("Col "+col+ " key "+key);
            if (col == 6 && key == 10 && (row == (rows - 1))) { //Si está en la última columna y presiona enter, inserta una nueva fila
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
    public void keyReleased(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void imGrabar(String crud) {
        this.CRUD = crud;
        int metodo = 3;
        System.out.println("OPCION DE LA VENTANA PRINCIPAL: " + Opcion);
        metodo = Tools.validarPermiso(conexion.getGrupoId(), Opcion, crud);
        System.out.println("METODOOO; " + metodo);
        if (Tools.validarPermiso(conexion.getGrupoId(), Opcion, crud) == 0) {
            String msg = "NO TIENE PERMISO PARA REALIZAR ESTA OPERACIÓN ";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
        }
        String msg;
        if (jtfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos deben ser completados ");
            return;
        }
        this.setData();
        ArrayList<Map<String, String>> alCabecera;         //Declara array de Map, cada Map es para un registro
        alCabecera = new ArrayList<Map<String, String>>(); //Instancia array
        int id, rows = 0;
        id = Integer.parseInt(this.jtfId.getText());
        if (id > 0) {
            alCabecera.add(this.myData);                      //agrega el Map al array, para la cabecera será el mejor de los casos, es decir 1 registro 
            int rowsAffected = this.tc.updateReg(alCabecera);
            //si rowsaffected < 1 return
            if (rowsAffected < 1) {
                msg = "Error al intentar actualizar el registro: " + this.jtfId.getText();
            } else {
                msg = "Se ha actualizado exitosamente el registro: " + this.jtfId.getText();
            }
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
        } else {
            rows = this.tc.createReg(this.myData);
            id = this.tc.getMaxId();
            if (rows < 1) {
                msg = "Error al intentar crear el registro";
            } else {
                msg = "Se ha creado exitosamente el registro: " + id;
            }
            id = rows;
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
            //si rows es menor 1 entonces no grabo return
        }

        //DETALLES
        //si pasó quiere decir que tenemos cabecera y recorremos el detalle
        Map<String, String> where = new HashMap<String, String>();      //Por qué campo buscar los registros
        Map<String, String> fields = new HashMap<String, String>();     //Los campos que vamos a recuperar
        ArrayList<Map<String, String>> alDetalle;                      //Declara array de Map, cada Map es para un registro

        fields.put("*", "*");

        for (Map<String, String> myRow : columnData) {
            where.put("compraid", id + "");
            where.put("cod_barra", myRow.get("cod_barra"));

            myRow.put("compraid", id + "");

            this.colDat = this.tcdet.searchListById(fields, where);//verificar tablaModel 407 cuando no existe el reg

            if (this.colDat.isEmpty()) {
                rows = this.tcdet.createReg(myRow);
                //if rows < 1 no creo debes hacer el control aquí
            } else {
                alDetalle = new ArrayList<Map<String, String>>();
                //myRow.put("ventaid", id+"");      //asignamos el id de la cabecera como el fk del detalle
                alDetalle.add(myRow);
                int affected = this.tcdet.updateReg(alDetalle);   //Recordar que el modelo sólo procesa de a uno los registros
                //controlar affected, no olvidar
            }
        }
        this.imInsDet();
        this.imNuevo();
    }

    @Override
    public void imFiltrar() {
        String sql;
        sql = "";
        wBuscar frame = null;
        
        if (currentField.equals("id")){
        sql = "SELECT id AS codigo, "
                + "CONCAT(numero_factura, ' ',observacion) AS descripcion "
                + "FROM compras "
                + "WHERE LOWER(CONCAT(id, ' ',numero_factura, ' ',observacion)) LIKE '%";
        frame = new wBuscar(sql, this.jtfId);
         }
        
        if (currentTable.equals("tabla")) { //para buscar un producto por filtro
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.DEFAULT_OPTION);
        }
    }

    @Override
    public void imActualizar(String crud) {
        this.CRUD = crud;
        int metodo = 3;
        System.out.println("OPCION DE LA VENTANA PRINCIPAL: " + Opcion);
        metodo = Tools.validarPermiso(conexion.getGrupoId(), Opcion, crud);
        System.out.println("METODOOO; " + metodo);
        if (Tools.validarPermiso(conexion.getGrupoId(), Opcion, crud) == 0) {
            String msg = "NO TIENE PERMISO PARA REALIZAR ESTA OPERACIÓN ";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
        }
        this.setData();
        ArrayList<Map<String, String>> alCabecera;         //Declara array de Map, cada Map es para un registro
        alCabecera = new ArrayList<Map<String, String>>(); //Instancia array
        alCabecera.add(this.myData);                      //agrega el Map al array, para la cabecera será el mejor de los casos, es decir 1 registro 
        int rowsAffected = this.tc.updateReg(alCabecera); //Está guardando igual si en el detalle hay error
        //Para el DETALLE
        ArrayList<Map<String, String>> alDetalle;         //Declara array de Map, cada Map es para un registro
        alDetalle = new ArrayList<Map<String, String>>(); //Instancia array

        for (Map<String, String> myRow : columnData) {       //hay que recorrer el detalle y envira de a uno.
            System.out.println("ENVIAMOS " + myData.get("id"));
            myRow.put("compraid", myData.get("id"));      //asignamos el id de la cabecera como el fk del detalle
            alDetalle.add(myRow);
        }
        //Hay que considerar que el upd puede ser que se envíe una fila nueva
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
        int metodo = 3;
        System.out.println("OPCION DE LA VENTANA PRINCIPAL: " + Opcion);
        metodo = Tools.validarPermiso(conexion.getGrupoId(), Opcion, crud);
        System.out.println("METODOOO; " + metodo);
        if (Tools.validarPermiso(conexion.getGrupoId(), Opcion, crud) == 0) {
            String msg = "NO TIENE PERMISO PARA REALIZAR ESTA OPERACIÓN ";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
        }
        this.setData();
        //Para el DETALLE
        ArrayList<Map<String, String>> alDetalle;         //Declara array de Map, cada Map es para un registro
        alDetalle = new ArrayList<Map<String, String>>(); //Instancia array

        for (Map<String, String> myRow : columnData) {       //hay que recorrer el detalle y envira de a uno.
            myRow.put("compraid", myData.get("id"));      //asignamos el id de la cabecera como el fk del detalle
            alDetalle.add(myRow);
        }
        int affected = this.tcdet.deleteReg(alDetalle);   //Recordar que el modelo sólo procesa de a uno los registros
        ArrayList<Map<String, String>> alCabecera;         //Declara array de Map, cada Map es para un registro
        alCabecera = new ArrayList<Map<String, String>>(); //Instancia array
        alCabecera.add(myData);                           //agrega el Map al array, para la cabecera será el mejor de los casos, es decir 1 registro 
        int rowsAffected = this.tc.deleteReg(alCabecera); //Está guardando igual si en el detalle hay error
        //Invocamos el método deleteReg del Modelo que procesa un array
        if (rowsAffected <= 0) {
            String msg = "NO SE HA PODIDO ELIMINAR EL REGISTRO: " + jtfId.getText();
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
        }
        if (rowsAffected > 0) {
            String msg = "EL REGISTRO: " + jtfId.getText() + " SE HA ELIMINADO CORRECTAMENTE";
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);

        }
        imNuevo();
    }

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
        this.myData = this.tc.searchById(myData);                     //Usa el mismo myData para devolver los valores de la cabecera
        System.out.println("Compras imBuscar " + this.myData.toString());
        this.limpiarTabla();
        if (this.myData.isEmpty()) {
            System.out.println("No hay registros que mostrar");
            this.resetData();
            this.fillView(myData, columnData);
            return;
        }
        Map<String, String> where = new HashMap<String, String>();      //Por qué campo buscar los registros
        where.put("compraid", this.myData.get("id"));
        //Los campos que vamos a recuperar
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("*", "*");
        //verificar tablaModel 407 cuando no existe el reg
        this.columnData = this.tcdet.searchListById(fields, where);
        if (this.columnData.isEmpty()) {
            this.resetData();
            //return
        }
        this.fillView(myData, columnData);
    }

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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void imInsDet() {
        int currentRow = jtDetalle.getSelectedRow();
        if (currentRow == -1) {
            //System.out.println("no hay fila seleccionada imInsDet 1062");
            modelo.addRow(new Object[]{"", "Descripcion", "0.0", "0.0", "0.0", "0.0", "0.0"});
            return;
        }
        String cod = this.jtDetalle.getValueAt(currentRow, 0).toString();

        //System.out.println("Codigo "+cod);
        if (cod.equals("0") || cod.equals("") || cod == null) {
            /*  String msg = "POR FAVOR INGRESE UN PRODUCTO ";
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION); */
        } else {
            //System.out.println("entro en imInsDet");
            modelo.addRow(new Object[]{"", "Descripcion", "0.0", "0.0", "0.0", "0.0", "0.0"});
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
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public int getProducto(int row, int col) {
        String codbar, sql;
        codbar = "";
        sql = "";
         if (this.tfcodbarra.getText().equals("")) {
            codbar = this.jtDetalle.getModel().getValueAt(row, col).toString();
        } else {
            codbar = this.tfcodbarra.getText();
            this.jtDetalle.getModel().setValueAt(codbar, row, 0);
        }
            codbar = this.jtDetalle.getModel().getValueAt(row, col).toString();
        if (codbar.equals("0") || codbar.equals("")) {
            return 0;
        }
        //System.out.println("codigo "+codbar);
        sql = "SELECT CONCAT(p.nombre, ' - ', "
                + "p.descripcion, ' - ', m.nombre, ' - ', "
                + "c.color, ' - ', t.tamano, ' - ', s.diseno) AS descripcion "
                + "FROM productos p, producto_detalle d, marcas m, colores c, tamanos t, disenos s "
                + "WHERE p.id = d.productoid "
                + "AND p.marca = m.id "
                + "AND d.colorid = c.id "
                + "AND d.tamanoid = t.id "
                + "AND d.disenoid = s.id "
                + "AND d.cod_barra = '" + codbar + "'";
        Map<String, String> rtn = new HashMap<String, String>();
        ResultSet rs;

        //System.out.println("sql "+sql);
        try {
            rs = conexion.ejecuteSQL(sql); //Esto devuelve un ResultSet
            ResultSetMetaData metaData = rs.getMetaData();
            int colCount = metaData.getColumnCount();
            // while(rs.next()){
            if (rs.next()) {
                for (int r = 1; r <= colCount; r++) {
                    //System.out.println("column "+metaData.getColumnName(r)+" valor "+rs.getString(metaData.getColumnName(r)));
                    rtn.put(metaData.getColumnName(r), rs.getString("descripcion"));
                    this.jtDetalle.getModel().setValueAt(rs.getString(metaData.getColumnName(r)), row, 1);
                }
            } else {
                this.jtDetalle.getModel().setValueAt("0", row, 0);
                this.jtDetalle.getModel().setValueAt("", row, 1);
            }
        } //fin deleteRegister
        catch (SQLException ex) {
            Logger.getLogger(tableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public void limpiarCelda(JTable tabla) {
        tabla.setValueAt("", tabla.getSelectedRow(), tabla.getSelectedColumn());
    }

    /**
     * Calucula el total para una fila específica
     *
     * @param row int index de la fila a ser procesada
     * @return rtn int que devuelve el estado de la operación, falta completar
     */
    public int setTotalRow(int row) {
        int rtn = 0;
        Double precio, cantidad, descuento, bonificado, totalrow;
        precio = Tools.sGetDecimalStringAnyLocaleAsDouble(this.jtDetalle.getModel().getValueAt(row, 2).toString());
        cantidad = Tools.sGetDecimalStringAnyLocaleAsDouble(this.jtDetalle.getModel().getValueAt(row, 3).toString());
        descuento = Tools.sGetDecimalStringAnyLocaleAsDouble(this.jtDetalle.getModel().getValueAt(row, 4).toString());
        bonificado = Tools.sGetDecimalStringAnyLocaleAsDouble(this.jtDetalle.getModel().getValueAt(row, 5).toString());

        //System.out.println("");
        if (precio <= 0 || cantidad <= 0) {
            return rtn;
        }

        totalrow = (precio - descuento) * (cantidad - bonificado);
        //System.out.println("totalrow sin cambio "+totalrow);        
        String totrow = Tools.decimalFormat(totalrow);
        //System.out.println("totalrow formateado "+totrow);

        String woDot = "";
        woDot = totrow.replace(".", "");
        //System.out.println("Sin puntos "+woDot);
        String woComa = "";
        woComa = woDot.replace(",", ".");
        //System.out.println("Coma por punto "+woComa);
        double tr = Tools.sGetDecimalStringAnyLocaleAsDouble(totrow);

        //System.out.println("total string tr "+tr+" fila "+row);
        this.jtDetalle.getModel().setValueAt(woComa, row, 6);

        return rtn;
    }//fin setTotalRow

    public int setTotalGral() {
        int rtnint = 0, monedaDecimal = 0;
        int rows = this.jtDetalle.getRowCount();
        String codbar, sql;
        codbar = "";
        sql = "";

        Double precio, cantidad, descuento, bonificado, totalrow;
        Double totBruto, totiva, totNeto, totExenta, impuesto, base, iva, exenta;
        totBruto = 0.0;
        totiva = 0.0;
        totNeto = 0.0;
        totExenta = 0.0;
        impuesto = 0.0;
        base = 0.0;
        iva = 0.0;
        exenta = 0.0;

//        monedaDecimal = Integer.parseInt(mapMoneda.get("decimales"));

        Map<String, String> rtn = new HashMap<String, String>();
        Map<String, String> select = new HashMap<String, String>();
        Map<String, String> where = new HashMap<String, String>();
        columnData.clear();
        for (int row = 0; row < rows; row++) {
            codbar = this.jtDetalle.getModel().getValueAt(row, 0).toString();
            if (codbar.equals("0") || codbar.equals("")) { //Si no se especifica códio, no tiene sentido continuar
                continue;
            }
            where.clear();
            //Por cada codigo de barras 
            select.put("*", "*");
            where.put("cod_barra", codbar);
            this.mapProductoDet = this.tmProductoDet.readRegisterById(select, where);//Recupera el id de producto
            //Para recuperar el producto
            where.clear();
            where.put("id", this.mapProductoDet.get("productoid"));

            this.mapProducto = this.tmProducto.readRegisterById(select, where);
            iva = Double.parseDouble(this.mapProducto.get("impuesto"));

            precio = Tools.sGetDecimalStringAnyLocaleAsDouble(this.jtDetalle.getModel().getValueAt(row, 2).toString());
            cantidad = Tools.sGetDecimalStringAnyLocaleAsDouble(this.jtDetalle.getModel().getValueAt(row, 3).toString());
            descuento = Tools.sGetDecimalStringAnyLocaleAsDouble(this.jtDetalle.getModel().getValueAt(row, 4).toString());
            bonificado = Tools.sGetDecimalStringAnyLocaleAsDouble(this.jtDetalle.getModel().getValueAt(row, 5).toString());
            if (precio <= 0 || cantidad <= 0) {
                continue;
            }
            if (iva > 0.0) {
                base = precio / (1 + (iva / 100));
                //Redondea la base a la canitdad de decimales de la moneda
                BigDecimal bd = new BigDecimal(base).setScale(monedaDecimal, RoundingMode.HALF_UP);
                base = bd.doubleValue();
                impuesto = precio - base;
                exenta = 0.0;
            } else {
                base = 0.0;
                impuesto = 0.0;
                exenta = precio;
            }

            totalrow = (precio - descuento) * (cantidad - bonificado);
            totBruto = totBruto + totalrow;
            totNeto = totNeto + ((base - descuento) * (cantidad - bonificado));
            totiva = totiva + (impuesto * cantidad);
            totExenta = totExenta + ((exenta - descuento) * (cantidad - bonificado));

            myDet = new HashMap<String, String>();
            String id = jtfId.getText();
            //System.out.println("el facking id "+id);
            myDet.put("compraid", id);
            myDet.put("cod_barra", codbar);
            myDet.put("cantidad", cantidad + "");
            myDet.put("precio_bruto", precio + "");
            myDet.put("precio_neto", ((base - descuento) * (cantidad - bonificado)) + "");
            myDet.put("cantbonificado", bonificado + "");
            myDet.put("descuento", (descuento * (cantidad - bonificado)) + "");
            myDet.put("impuesto", (impuesto * (cantidad - bonificado)) + "");
            myDet.put("total", totalrow + "");
            myDet.put("descripcion", this.jtDetalle.getModel().getValueAt(row, 1).toString());

            //limpiar
            columnData.add(myDet);
            // System.out.println("DETALLEEE: "+myDet);
        }
        //String totBrut = functions.decimalFormat(totBruto);

        //System.out.println("total "+functions.decimalFormat(totalrow).toString()+" fila "+row);
        this.jftfNeto.setText(Tools.decimalFormat(totNeto));
        this.jftfImpuesto.setText(Tools.decimalFormat(totiva));
        this.jftfExenta.setText(Tools.decimalFormat(totExenta));
        this.jftfTotal.setText(Tools.decimalFormat(totBruto));
        return rtnint;
    }//end setTotalGral

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
        Date fecha;
        fecha = jdcProceso.getDate();
        if (fecha == null) {
            JOptionPane.showMessageDialog(this, "Favor ingrese una fecha de operación válida!", "¡A T E N C I O N!", JOptionPane.WARNING_MESSAGE);
            jdcProceso.requestFocus();
            jdcProceso.setDate(new Date());
            rtn = false;
        }

        fecha = jdcLlega.getDate();

        if (fecha == null) {
            JOptionPane.showMessageDialog(this, "Favor ingrese una fecha de llegada válida!", "¡A T E N C I O N!", JOptionPane.WARNING_MESSAGE);
            jdcLlega.requestFocus();
            jdcLlega.setDate(new Date());
            rtn = false;
        }

        //Aquí verificar que la fecha de llegada no sea inferior que la fecha de proceso
        //continuar con los demás controles
        return rtn;
    }

    /**
     * Recupera los datos de la moneda que se usa en el proceso
     *
     * @return int rtn estado del proceso de recuperación, falta completar
     */
    public int getMoneda() {
        int rtn = 0;
        Map<String, String> where = new HashMap<String, String>();
        String idMoneda;
        idMoneda = "";
        idMoneda = ComboBox.ExtraeCodigo(this.jcbMoneda.getSelectedItem().toString());
        this.mapMoneda.clear();
        this.mapMoneda.put("*", "*");
        where.put("id", idMoneda);
        this.mapMoneda = tmMoneda.readRegisterById(this.mapMoneda, where);
        return rtn;
    }

    /**
     * Carga todos los datos de la cabacera a una estructrua de datos tipo
     * Map<String, String>
     */
    private void setData() {
        java.util.Date df = new java.util.Date();
        String fecha = "";
        myData.put("id", jtfId.getText());

        fecha = (jdcProceso.getDate().getTime() / 1000L) + "";
        myData.put("fecha_proceso", fecha);

        fecha = (jdcFactura.getDate().getTime() / 1000L) + "";
        myData.put("fecha_factura", fecha);

        fecha = (jdcLlega.getDate().getTime() / 1000L) + "";
        myData.put("fecha_llegada", fecha);

        myData.put("nro_documento", jftfFactura.getText());
        myData.put("serie", jftfSerie.getText());
        myData.put("timbrado", jtfTimbrado.getText());

        fecha = (jdcVence.getDate().getTime() / 1000L) + "";
        myData.put("vence", fecha);

        myData.put("proveedorid", ComboBox.ExtraeCodigo(jcbProveedor.getSelectedItem().toString()));
        myData.put("plazoid", ComboBox.ExtraeCodigo(jcbPlazo.getSelectedItem().toString()));
        myData.put("monedaid", ComboBox.ExtraeCodigo(jcbMoneda.getSelectedItem().toString()));
        myData.put("cotizacion", jftfCotizacion.getText().replace(".", "").replace(",", "."));
        myData.put("tot_preciobruto", jftfTotal.getText().replace(".", "").replace(",", "."));
        myData.put("pago_inicial", jftfAdelanto.getText().replace(".", "").replace(",", "."));
        myData.put("tot_exento", jftfExenta.getText().replace(".", "").replace(",", "."));
        myData.put("tot_precioneto", jftfNeto.getText().replace(".", "").replace(",", "."));
        myData.put("tot_impuesto", jftfImpuesto.getText().replace(".", "").replace(",", "."));
        myData.put("tipocompra", ComboBox.ExtraeCodigo(jcbTipo.getSelectedItem().toString()));
        myData.put("observacion", jtfObs.getText());
        myData.put("depositoid", ComboBox.ExtraeCodigo(jcbDeposito.getSelectedItem().toString()));
        //Recorre el detalle y guarda cada fila
        //columndata ya se procesó con el metodo setTotalGral
        this.setTotalGral();
        //Recorre el detalle y guarda cada fila
        //columndata ya se procesó con el metodo setTotalGral
        System.out.println("myData " + myData);
        //  this.setTotalGral();

        /* if(jtfId.getText() == "0"){
             String msg = "POR FAVOR INGRESE UN PRODUCTO ";
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
        } */
    }//fin setData

    /**
     * Limpia
     */
    private void resetData() {
        this.myData = new HashMap<String, String>();
        java.util.Date df = new java.util.Date();
        this.myData.put("id", "0");
        //  this.myData.put("fecha_proceso", df.getTime()+"");
        // this.myData.put("fecha_factura", df.getTime()+"");
        // this.myData.put("fecha_llegada", df.getTime()+"");
        this.myData.put("nro_documento", "0");
        this.myData.put("serie", "001001");
        this.myData.put("timbrado", "0");
       // this.myData.put("vence", df.getTime() + "");
        this.myData.put("proveedorid", "0");
        this.myData.put("plazoid", "0");
        this.myData.put("monedaid", "0");
        this.myData.put("cotizacion", "0.0");
        this.myData.put("tot_preciobruto", "0.0");
        this.myData.put("pago_inicial", "0.0");
        this.myData.put("tot_exento", "0.0");
        this.myData.put("tot_precioneto", "0.0");
        this.myData.put("tot_impuesto", "0.0");
        this.myData.put("tipocompra", "0");
        this.myData.put("observacion", "Obs");
        this.myData.put("depositoid", "0");

       
        jcbMoneda.setSelectedIndex(0);
        jcbPlazo.setSelectedIndex(0);
        jcbDeposito.setSelectedIndex(0); 
        jcbProveedor.setSelectedIndex(0); 
        jcbTipo.setSelectedIndex(0); 
        this.myDet = new HashMap<String, String>();

        this.myDet.put("compraid", "0");
        this.myDet.put("cod_barra", "0");
        this.myDet.put("cantidad", "0");
        this.myDet.put("precio_bruto", "0");
        this.myDet.put("precio_neto", "0");
        this.myDet.put("cantbonificado", "0");
        this.myDet.put("descuento", "0");
        this.myDet.put("impuesto", "0");
        this.myDet.put("total", "0");

        this.columnData.add(this.myDet);

        //fillView(myData, columnData);
    }//fin reset data

    /**
     * Inserta todos los datos seteados en el Map myData y columnData cuyo lista
     * es de tipo también Map myDet
     *
     * @param data Map con los valores de los campos de la cabecera
     * @param colData List cuyos valores son del tipo Map myDet
     */
    private void fillView(Map<String, String> data, List<Map<String, String>> colData) {
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
                case "fecha_proceso":
                    dateLong = Long.parseLong(value) * 1000L;
                    df = new Date(dateLong);
                    jdcProceso.setDate(df);
                    break;
                case "fecha_llegada":
                    dateLong = Long.parseLong(value) * 1000L;
                    df = new Date(dateLong);
                    jdcLlega.setDate(df);
                    break;
                case "fecha_factura":
                    dateLong = Long.parseLong(value) * 1000L;
                    df = new Date(dateLong);
                    jdcFactura.setDate(df);
                    break;
                case "numero_factura":
                    jftfFactura.setText(value);
                    break;
                case "serie":
                    jftfSerie.setText(value);
                    break;
                case "vence":
                    dateLong = Long.parseLong(value) * 1000L;
                    df = new Date(dateLong);
                    jdcVence.setDate(df);
                    break;
                case "timbrado":
                    jtfTimbrado.setText(value);
                    break;
                case "tot_preciobruto":
                    jftfTotal.setText(Tools.decimalFormat(Double.parseDouble(value)));
                    break;
                case "pago_inicial":
                    jftfAdelanto.setText(Tools.decimalFormat(Double.parseDouble(value)));
                    break;
                case "tot_exento":
                    jftfExenta.setText(Tools.decimalFormat(Double.parseDouble(value)));
                    break;
                case "tot_precioneto":
                    jftfNeto.setText(Tools.decimalFormat(Double.parseDouble(value)));
                    break;
                case "tot_impuesto":
                    jftfImpuesto.setText(Tools.decimalFormat(Double.parseDouble(value)));
                    break;
                case "tot_costo":
                    //tfcosto.setText(value);
                    break;
                case "cotizacion":
                    jftfCotizacion.setText(Tools.decimalFormat(Double.parseDouble(value)));
                    break;
                case "observacion":
                    jtfObs.setText(value);
                    break;
                case "tipocompra":
                     jcbTipo.setSelectedItem(Integer.parseInt(value));
                    break;
                case "plazoid":
                    Tools.E_estado(jcbPlazo, "sys_plazo_pago", "id=" + value);
                    break;
                case "proveedorid":
                    Tools.E_estado(jcbProveedor, "sys_proveedores", "cod_proveedor=" + value);
                    break;
                case "monedaid":
                    Tools.E_estado(jcbMoneda, "sys_monedas", "id=" + value);
                    break;
                case "depositoid":
                    Tools.E_estado(jcbDeposito, "sys_depositos", "id=" + value);
                    break;
            }//end switch
        }//end for 1    

        int row;
        row = 0;
        for (Map<String, String> myRow : columnData) {

            this.modelo.addRow(new Object[]{"0", "", 0.0, 0.0, 0.0, 0.0});

            this.jtDetalle.setValueAt(myRow.get("cod_barra"), row, 0);
            this.jtDetalle.setValueAt(myRow.get("descripcion"), row, 1);
            //this.getProducto(row, 1);
            // System.out.println(myRow.get("descripcion"));
            //this.jtDet.editCellAt(row, 0, this.keyPress(KeyEvent.VK_ENTER));
            this.jtDetalle.setValueAt(myRow.get("precio_bruto"), row, 2);
            this.jtDetalle.setValueAt(myRow.get("cantidad"), row, 3);
            this.jtDetalle.setValueAt(myRow.get("descuento"), row, 4);
            //  this.jtDetalle.setValueAt(myRow.get("cantbonificado"), row, 5);
            this.jtDetalle.setValueAt(myRow.get("total"), row, 6);

            this.jtDetalle.setValueAt(decimalFormat(650.75), row, 5);

            int exist = this.getProducto(row, 0);
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
    public void validarCombo(){
           int codigo = 0;
           int condicion = 0;
           codigo = Integer.parseInt(ComboBox.ExtraeCodigo(jcbPlazo.getSelectedItem().toString()));
            System.out.println("codigooo:"+codigo);
            if (codigo == 0){
            this.jcbPlazo.requestFocus();
            JOptionPane.showMessageDialog(this, "Favor Seleccione Plazo!", "¡A T E N C I O N!", JOptionPane.WARNING_MESSAGE);
            return;
            }
            codigo = Integer.parseInt(ComboBox.ExtraeCodigo(jcbMoneda.getSelectedItem().toString()));
            System.out.println("codigooo:"+codigo);
            if (codigo == 0){
            this.jcbMoneda.requestFocus();
            JOptionPane.showMessageDialog(this, "Favor Seleccione Moneda!", "¡A T E N C I O N!", JOptionPane.WARNING_MESSAGE);
            return;
            }
            codigo = Integer.parseInt(ComboBox.ExtraeCodigo(jcbTipo.getSelectedItem().toString()));
            System.out.println("codigooo:"+codigo);
            if (codigo == 0){
             this.jcbTipo.requestFocus();
            JOptionPane.showMessageDialog(this, "Favor Seleccione tipo de Operacion!", "¡A T E N C I O N!", JOptionPane.WARNING_MESSAGE);
            return;
            }
            codigo = Integer.parseInt(ComboBox.ExtraeCodigo(jcbDeposito.getSelectedItem().toString()));
            System.out.println("codigooo:"+codigo);
            if (codigo == 0){
            this.jcbDeposito.requestFocus();
            JOptionPane.showMessageDialog(this, "Favor Seleccione Depósito!", "¡A T E N C I O N!", JOptionPane.WARNING_MESSAGE);
            return;
            }
            
            codigo = Integer.parseInt(ComboBox.ExtraeCodigo(jcbProveedor.getSelectedItem().toString()));
            System.out.println("codigooo:"+codigo);
            if (codigo == 0){
            this.jcbProveedor.requestFocus();
            JOptionPane.showMessageDialog(this, "Favor Seleccione Proveedor!", "¡A T E N C I O N!", JOptionPane.WARNING_MESSAGE);
            return;
            }
            
          condicion = Integer.parseInt(ComboBox.ExtraeCodigo(jcbTipo.getSelectedItem().toString()));
          int plazo = Integer.parseInt(ComboBox.ExtraeCodigo(jcbPlazo.getSelectedItem().toString()));
//          System.out.println("codigo" +codigo);
          if (condicion == 1 && plazo > 1 || plazo == 1 && condicion == 2) {
              this.jcbDeposito.requestFocus();
            String msg = "CONDICIÓN Y PLAZO NO COMPATIBLE";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
            }
           
     }
}//FIN DE LA CLASE
