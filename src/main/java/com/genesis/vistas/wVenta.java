package com.genesis.vistas;

import com.genesis.model.conexion;
import com.genesis.model.pojoCompraDetalle;
import com.genesis.model.pojoVentaDetalle;
import com.genesis.model.tableModel;
import com.genesis.tabla.GestionCeldas;
import com.genesis.tabla.GestionEncabezadoTabla;
import com.genesis.tabla.ModeloTabla;
import com.genesis.controladores.tableController;
import util.ComboBox;
import util.Tools;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrinterName;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.view.JasperViewer;
import reportes.Reporte;
import reportes.vFactura;
import com.genesis.vistas.wPrincipal;
import java.beans.PropertyVetoException;


public class wVenta extends javax.swing.JInternalFrame implements MouseListener, KeyListener, ActiveFrame {

    //Controladores
    private tableController tc;
    private tableController tcdet;
    String menuName = "";
    String CRUD = "";

    private int precioid;
    int nrodocnuevo = 0;
    int nrodoc = 0;
    int nrovent = 0;
    int nroventnuevo = 0;
    int condicion = 0;
    int iddeposito = 0;
    double cantdeposito = 0.0;
    int tipoopcion = 0;
    int IDVENTA = 0;
    int cotizacionGlobal = 0;
    int operacionCotizacion = 0;
    double limite = 0; //Limite de Credito del cliente. Revisar setTotalGral
    String varserie = "";
    String vartimbrado = "";
    int iRow = 0;
    int iColumn = 0;
    //Modelos y estructuras de datos
    private tableModel tmMoneda;
    Map<String, String> mapMoneda;
    
    private tableModel tmCotizacion;
    Map<String, String> mapCotizacion;

    private tableModel tmProducto;
    Map<String, String> mapProducto;

    private tableModel tmProductoDet;
    Map<String, String> mapProductoDet;

    ArrayList<pojoVentaDetalle> lista;
    ArrayList<pojoVentaDetalle> listaDetalles;//lista que simula la información de la BD

    private boolean jcbClienteSelectItem = false; // Bandera JcbCliente itemStateChanged
    
    // Estructura de datos de Cabecera y Detalles
    private Map<String, String> myData;
    private Map<String, String> myDataAux;
    private HashMap<String, String> myDet;
    private ArrayList<Map<String, String>> columnData, colDat;

    ModeloTabla modelo;//modelo definido en la clase ModeloTabla
    
    private DateFormat dateFormat, dateTimeFormat, dateIns; //= new SimpleDateFormat("dd/MM/yyyy HH:mm"); 
    //private static ResultSet rs_suc;
    private int cajaid;
    static FileInputStream inputStream = null;
    private String currentField = "";
    private String currentTable = "";
    
    public JInternalFrame[] w_abierto;

    /**
     * Creates new form wVenta
     * @param tipoopcion Tipo de documento que se va a realizar en la venta
     * @param menuName nombre del Menu ejecutado desde la vista Principal
     */
    public wVenta(int tipoopcion, String menuName) {
        initComponents();
        this.tipoopcion = tipoopcion;
        this.menuName = menuName;
        listaDetalles = new ArrayList<>();
        lista = new ArrayList<>();
        myData = new HashMap<>();
        myDataAux = new HashMap<>();
        columnData = new ArrayList<>();
        //Para menejo de Moneda de operacion y cotizacion
        mapMoneda = new HashMap<>();
        tmMoneda = new tableModel();
        tmMoneda.init("monedas");
        //Cotizacion
        mapCotizacion = new HashMap<>();
        tmCotizacion = new tableModel();
        tmCotizacion.init("cotizaciones");
        construirTabla();

        //Para Producto Cabecera Se utiliza en setTotalGral para recuperar datos de Productos
        mapProducto = new HashMap<>();
        tmProducto = new tableModel();
        tmProducto.init("productos");
        //Para Producto Detalle. se Utiliza en setTotalGral para recuperar datos de Productos Detalle
        mapProductoDet = new HashMap<>();
        tmProductoDet = new tableModel();
        tmProductoDet.init("producto_detalle");
        
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
        
        // COMBO BOX DESPLEGABLES DE LAS TABLAS//
        ComboBox.pv_cargar(jcbPlazo, "plazo_pago", " id, plazo ", "id", "");
        ComboBox.pv_cargar(jcbMoneda, "monedas", " id, moneda ", "id", "");
        ComboBox.pv_cargar(jcbCliente, "clientes c, personas p", "c.id, CONCAT(p.nombres,' ', p.apellidos, ':', c.ruc) AS cliente", "c.id", "c.personaid = p.id");
        ComboBox.pv_cargar(jcbDeposito, "depositos", "id, nombre", "id", "");
        ComboBox.pv_cargar(jcbTipo, "tipos_documento", "id, tipodocumento", "id", "tipoopcion=" + tipoopcion);

        // Inicializamos las fechas
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        dateIns = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        jdcProceso.setDate(new Date());
        jdcFactura.setDate(new Date());
        jdcVencimiento.setDate(new Date());
        this.getMoneda();
        if (tipoopcion == 1) {
            jcbtipoventa.setSelectedItem("1-Venta Mercadería");
            jcbtipoventa.setEnabled(false);
        }

        if (tipoopcion == 2) {
            jcbtipoventa.setSelectedItem("2-Nota de Crédito");
            jcbCondicion.setEnabled(false);
            jcbtipoventa.setEnabled(false);
            jcbTipo.setEnabled(false);
            jcbPlazo.setEnabled(false);
            jcbPlazo.setSelectedItem("1-1 Dia");
            jcbTipo.setSelectedItem("2-Nota de Crédito");
            this.setTitle("Nota de Crédito");
        }

        tc = new tableController();
        tc.init("ventas");
        tcdet = new tableController();
        tcdet.init("venta_detalle");
        //Para acceder a los datos del .properties usaremos esta instruccion: 
        try {
            //inputStream = new FileInputStream("genesis.properties");
            inputStream = new FileInputStream("C:\\Users\\eacks\\OneDrive\\Documentos\\NetBeansProjects\\genesis\\genesis.properties");
            //Ahora inicializamos el properties:
            Properties properties = new Properties();
            properties.load(inputStream);
            inputStream.close();
            //Y ahora si queremos los valores del properties:
            this.cajaid = Integer.parseInt(properties.getProperty("caja"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (this.tipoopcion == 1) {
            tfventareg.setEnabled(false);
        }

    }// fin constructor

    private void construirTabla() {
        listaDetalles.clear();
        listaDetalles = consultarListaDetalles();
        
        ArrayList<String> titulosList = new ArrayList<>();
        titulosList.add("Cod Barra");
        titulosList.add("Descripcion");
        titulosList.add("Precio");
        titulosList.add("Cantidad");
        titulosList.add("Descuento");
        titulosList.add("Bonificado");
        titulosList.add("Total");

        String titulos[] = new String[titulosList.size()];

        for (int i = 0; i < titulos.length; i++) {
            titulos[i] = titulosList.get(i);
        }
        
        Object[][] data = obtenerMatrizDatos(titulosList);
        construirTabla(titulos, data);
        titulosList.clear();
    }

    private ArrayList<pojoVentaDetalle> consultarListaDetalles() {
        this.lista.add(new pojoVentaDetalle(0, "0", "Descripcion", 0, 0, 0, 0, 0));
        return lista;
    }

    private Object[][] obtenerMatrizDatos(ArrayList<String> titulosList) {

        String informacion[][] = new String[listaDetalles.size()][titulosList.size()];

        for (int x = 0; x < informacion.length; x++) {
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

    private void construirTabla(String[] titulos, Object[][] data) {
        ArrayList<Integer> noEditable = new ArrayList<>();
        noEditable.add(1);
        noEditable.add(2);
        noEditable.add(6);
        modelo = new ModeloTabla(data, titulos, noEditable);
        //se asigna el modelo a la tabla
        jtDetalle.setModel(modelo);

        //se asigna el tipo de dato que tendrán las celdas de cada columna definida respectivamente para validar su personalización
        jtDetalle.getColumnModel().getColumn(0).setCellRenderer(new GestionCeldas("texto"));
        jtDetalle.getColumnModel().getColumn(1).setCellRenderer(new GestionCeldas("texto"));
        jtDetalle.getColumnModel().getColumn(2).setCellRenderer(new GestionCeldas("numerico"));
        jtDetalle.getColumnModel().getColumn(3).setCellRenderer(new GestionCeldas("numerico"));
        jtDetalle.getColumnModel().getColumn(4).setCellRenderer(new GestionCeldas("numerico"));
        jtDetalle.getColumnModel().getColumn(5).setCellRenderer(new GestionCeldas("numerico"));
        jtDetalle.getColumnModel().getColumn(6).setCellRenderer(new GestionCeldas("numerico"));

        jtDetalle.getTableHeader().setReorderingAllowed(false);
        jtDetalle.setRowHeight(25);//tamaño de las celdas
        jtDetalle.setGridColor(new java.awt.Color(0, 0, 0));
        //Se define el tamaño de largo para cada columna y su contenido
        jtDetalle.getColumnModel().getColumn(0).setPreferredWidth(100);//cod_barra
        jtDetalle.getColumnModel().getColumn(1).setPreferredWidth(350);//descripcion
        jtDetalle.getColumnModel().getColumn(2).setPreferredWidth(100);//precio
        jtDetalle.getColumnModel().getColumn(3).setPreferredWidth(100);//cantidad
        jtDetalle.getColumnModel().getColumn(4).setPreferredWidth(100);//descuento
        jtDetalle.getColumnModel().getColumn(5).setPreferredWidth(100);//bonificado
        jtDetalle.getColumnModel().getColumn(6).setPreferredWidth(150);//total

        //personaliza el encabezado
        JTableHeader jtableHeader = jtDetalle.getTableHeader();
        jtableHeader.setDefaultRenderer(new GestionEncabezadoTabla());
        jtDetalle.setTableHeader(jtableHeader);

        //se asigna la tabla al scrollPane
        jScrollPane1.setViewportView(jtDetalle);
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
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jtfId = new javax.swing.JTextField();
        jftfNroDocu = new javax.swing.JTextField();
        jdcFactura = new com.toedter.calendar.JDateChooser();
        jdcProceso = new com.toedter.calendar.JDateChooser();
        jftfSerie = new javax.swing.JTextField();
        jtfTimbrado = new javax.swing.JTextField();
        jcbCliente = new javax.swing.JComboBox<>();
        jcbPlazo = new javax.swing.JComboBox<>();
        jcbMoneda = new javax.swing.JComboBox<>();
        jcbCondicion = new javax.swing.JComboBox<>();
        jftfCotizacion = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jftfAdelanto = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jcbDeposito = new javax.swing.JComboBox<>();
        jdcVencimiento = new com.toedter.calendar.JDateChooser();
        jtfObs = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jcbTipo = new javax.swing.JComboBox<>();
        tfventareg = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jcbtipoventa = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jftfTotal = new javax.swing.JTextField();
        jftfNeto = new javax.swing.JTextField();
        jftfExenta = new javax.swing.JTextField();
        jftfImpuesto = new javax.swing.JTextField();
        tfcodbarra = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtDetalle = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Venta de Mercaderías");
        setPreferredSize(new java.awt.Dimension(1150, 650));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Cabecera"));

        jLabel1.setText("ID");

        jLabel2.setText("Fecha Proceso");

        jLabel3.setText("Fecha Factura");

        jLabel5.setText("Nro. Documento");

        jLabel6.setText("Serie");

        jLabel7.setText("Timbrado");

        jLabel8.setText("Vence");

        jLabel9.setText("Cliente");

        jLabel10.setText("Plazo");

        jLabel11.setText("Moneda");

        jLabel12.setText("Cotización");

        jLabel13.setText("Tipo Doc");

        jLabel14.setText("Observaciones");

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

        jftfNroDocu.setEditable(false);

        jftfSerie.setEditable(false);

        jtfTimbrado.setEditable(false);

        jcbCliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Seleccione Cliente" }));
        jcbCliente.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbClienteItemStateChanged(evt);
            }
        });

        jcbPlazo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Seleccione Plazo" }));

        jcbMoneda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Seleccione Moneda" }));

        jcbCondicion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Crédito", "1-Contado" }));
        jcbCondicion.setSelectedIndex(1);
        jcbCondicion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbCondicionActionPerformed(evt);
            }
        });

        jftfCotizacion.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jftfCotizacion.setText("0.0");

        jLabel19.setText("Pago Inicial");

        jftfAdelanto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jftfAdelanto.setText("0.0");

        jLabel4.setText("Depósito");

        jcbDeposito.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Seleccione Depósito" }));

        jLabel20.setText("Condición");

        jcbTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Seleccione Tipo Doc" }));

        tfventareg.setText("0");
        tfventareg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfventaregFocusGained(evt);
            }
        });
        tfventareg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfventaregKeyPressed(evt);
            }
        });

        jLabel21.setText("Venta Reg.");

        jLabel22.setText("Tipo Venta");

        jcbtipoventa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1-Venta Mercadería", "2-Nota de Crédito" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jdcProceso, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel8)
                                                .addGap(9, 9, 9))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(jLabel12)
                                                    .addComponent(jLabel19)
                                                    .addComponent(jLabel14)
                                                    .addComponent(jLabel22))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jftfCotizacion)
                                            .addComponent(jftfAdelanto)
                                            .addComponent(jtfObs, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jdcVencimiento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jcbtipoventa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel21)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(tfventareg, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(18, 18, 18)
                                        .addComponent(jtfId, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(4, 4, 4))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jdcFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 4, Short.MAX_VALUE)))
                        .addGap(7, 7, 7)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel11)
                                        .addComponent(jLabel10)
                                        .addComponent(jLabel6)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel7))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jftfSerie)
                    .addComponent(jtfTimbrado)
                    .addComponent(jftfNroDocu)
                    .addComponent(jcbCondicion, 0, 162, Short.MAX_VALUE)
                    .addComponent(jcbMoneda, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcbPlazo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcbCliente, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcbDeposito, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcbTipo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(23, 23, 23))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(jcbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jtfId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jcbPlazo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tfventareg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jdcProceso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel11)
                                    .addComponent(jcbMoneda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(5, 5, 5))))
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jcbDeposito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4))
                            .addGap(1, 1, 1))
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(jdcFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(jcbCondicion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8)
                            .addComponent(jdcVencimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jcbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)
                            .addComponent(jLabel19)
                            .addComponent(jftfAdelanto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jftfSerie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel12)
                    .addComponent(jftfCotizacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfTimbrado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel14)
                    .addComponent(jtfObs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jftfNroDocu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel22)
                    .addComponent(jcbtipoventa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Totales"));

        jLabel15.setText("Total Neto");

        jLabel16.setText("Total Bruto");

        jLabel17.setText("Exento");

        jLabel18.setText("Impuesto");

        jftfTotal.setEditable(false);
        jftfTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jftfTotal.setText("0.0");
        jftfTotal.setEnabled(false);

        jftfNeto.setEditable(false);
        jftfNeto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jftfNeto.setText("0.0");

        jftfExenta.setEditable(false);
        jftfExenta.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jftfExenta.setText("0.0");

        jftfImpuesto.setEditable(false);
        jftfImpuesto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jftfImpuesto.setText("0.0");

        tfcodbarra.setEditable(false);
        tfcodbarra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfcodbarraKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(18, 18, 18)
                                .addComponent(jftfTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel18))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jftfImpuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jftfNeto, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jftfExenta, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(tfcodbarra, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jftfTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jftfNeto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jftfExenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jftfImpuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tfcodbarra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalles"));

        jtDetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Cod Barra", "Descripción", "Cantidad", "Cant. Bonificado", "Descuento", "Total"
            }
        ));
        jtDetalle.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtDetalleFocusGained(evt);
            }
        });
        jScrollPane1.setViewportView(jtDetalle);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 70, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtfIdFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfIdFocusGained
        currentField = "id";
        jtfId.selectAll();
    }//GEN-LAST:event_jtfIdFocusGained

    private void jtfIdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfIdKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.imBuscar();
        }
    }//GEN-LAST:event_jtfIdKeyPressed

    private void jcbCondicionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbCondicionActionPerformed
//        jcbTipo.removeAllItems();
//        String tip = "";
//        tip = functions.ExtraeCodigo(jcbCondicion.getSelectedItem().toString());
//        cargaComboBox.pv_cargar(jcbTipo, "sys_tipos_documento", "id, tipodocumento", "id", "contadocredito="+tip);
    }//GEN-LAST:event_jcbCondicionActionPerformed

    private void tfventaregFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfventaregFocusGained
        currentField = "ventaid";
        tfventareg.selectAll();          // TODO add your handling code here:
    }//GEN-LAST:event_tfventaregFocusGained

    private void tfventaregKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfventaregKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.imBuscar();
        }
    }//GEN-LAST:event_tfventaregKeyPressed

    private void jtDetalleFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtDetalleFocusGained
        currentTable = "tabla";
        validarCombo();
    }//GEN-LAST:event_jtDetalleFocusGained

    private void tfcodbarraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfcodbarraKeyPressed
        if (currentTable.equals("tabla")) {
            //OBS: Aquí debemos llamar a un método que controle que los campos de la cabecera estén completos
            int row = jtDetalle.getSelectedRow();
            int rows = jtDetalle.getRowCount();
            int col = jtDetalle.getSelectedColumn();
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                this.getProducto(row, col);
            }
        }
    }//GEN-LAST:event_tfcodbarraKeyPressed

    private void jcbClienteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbClienteItemStateChanged
        // recuperar la lista de precio correspondiente al cliente
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED && jcbClienteSelectItem) {
            currentField = "cliente"; 
            int clienteid;
            clienteid = Integer.parseInt(ComboBox.ExtraeCodigo(jcbCliente.getSelectedItem().toString()));
            ResultSet rs;
            String sql;
            sql = "SELECT * FROM clientes WHERE id = " + clienteid;
            //System.out.println(sql);
            rs = conexion.ejecuteSQL(sql);
            try {
                if (rs.next()) {
                    precioid = rs.getInt("precioid");
                } else {
                    precioid = 0;
                }
            } catch (SQLException ex) {
                precioid = 0;
                Logger.getLogger(wVenta.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        jcbClienteSelectItem = true; // Marcar que el usuario seleccionó un item
    }//GEN-LAST:event_jcbClienteItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
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
    private javax.swing.JComboBox<String> jcbCliente;
    private javax.swing.JComboBox<String> jcbCondicion;
    private javax.swing.JComboBox<String> jcbDeposito;
    private javax.swing.JComboBox<String> jcbMoneda;
    private javax.swing.JComboBox<String> jcbPlazo;
    private javax.swing.JComboBox<String> jcbTipo;
    private javax.swing.JComboBox<String> jcbtipoventa;
    private com.toedter.calendar.JDateChooser jdcFactura;
    private com.toedter.calendar.JDateChooser jdcProceso;
    private com.toedter.calendar.JDateChooser jdcVencimiento;
    private javax.swing.JTextField jftfAdelanto;
    private javax.swing.JTextField jftfCotizacion;
    private javax.swing.JTextField jftfExenta;
    private javax.swing.JTextField jftfImpuesto;
    private javax.swing.JTextField jftfNeto;
    private javax.swing.JTextField jftfNroDocu;
    private javax.swing.JTextField jftfSerie;
    public javax.swing.JTextField jftfTotal;
    private javax.swing.JTable jtDetalle;
    private javax.swing.JTextField jtfId;
    private javax.swing.JTextField jtfObs;
    private javax.swing.JTextField jtfTimbrado;
    private javax.swing.JTextField tfcodbarra;
    public javax.swing.JTextField tfventareg;
    // End of variables declaration//GEN-END:variables

    @Override
    public void mouseClicked(MouseEvent e) {
        //capturo fila o columna dependiendo de mi necesidad

        //OBS: Aquí debemos llamar a un método que controle que los campos de la cabecera estén completos
        int fila = jtDetalle.rowAtPoint(e.getPoint());
        int columna = jtDetalle.columnAtPoint(e.getPoint());

        if (columna == 0) { //0 corresponde a cod barra
            validarSeleccionMouse(fila);
        } else if (columna == 2) {//se valida que sea la columna del otro evento 2 que corresponde a precio
        }
        if (currentTable.equals("tabla")) {
            //OBS: Aquí debemos llamar a un método que controle que los campos de la cabecera estén completos
            int row = jtDetalle.getSelectedRow();
            int rows = jtDetalle.getRowCount();
            int col = jtDetalle.getSelectedColumn();

            iRow = row;
            iColumn = col;
            if (e.getSource() == this.tfcodbarra) {
                this.getProducto(iRow, iColumn);
            }

        }
    }

    private void validarSeleccionMouse(int fila) {
        pojoCompraDetalle rowDetalle = new pojoCompraDetalle();
        rowDetalle.setString("cod_barra", jtDetalle.getValueAt(fila, 0).toString());
        rowDetalle.setString("descripcion", jtDetalle.getValueAt(fila, 1).toString());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        ///throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            boolean celdasNumericas = (col == 2 || col == 3 || col == 4 || col == 5);
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

            //Si Enter o Tab y col < 6 entonces dejar de editar la celda y pasar a la siguiente celda
            if (celdasNumericas && (enter || tabulacion)) {
                if (jtDetalle.isEditing()) {
                    jtDetalle.getCellEditor().stopCellEditing();
                    this.setTotalRow(row);
                    this.setTotalGral();
                }
            }

            //Si Enter o Tab para col == 0 Codigo Barra
            if (col == 0 && (enter || tabulacion)) {
                jtDetalle.getCellEditor().stopCellEditing();
                this.getMoneda();
                this.getCotizacion();
                this.getProducto(row, col);
                return;
            }

            //Enter para Ultima Columna. si ultima fila de la tabla Inserta una nueva fila
            if (col == 6 && enter && lastRow) {
                //Verifica que se haya ingresado cod_barra Correcto en la fila
                String cod = this.jtDetalle.getValueAt(row, 0).toString();
                if (cod.equals("0") || cod.equals("")) {
                    JOptionPane.showMessageDialog(this, "keyPressed960: Favor ingrese un producto Valido!", "¡A T E N C I O N!", JOptionPane.INFORMATION_MESSAGE);

                } else {
                    this.imInsDet();
                }

            }

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void imGrabar(String crud) {
        this.CRUD = crud;
        String msg;
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

        recuperarTalonario();

        int idVenta = Integer.parseInt(this.jtfId.getText());
        if (idVenta > 0) {
            imActualizar("U");
            return;
        } else {
            int rowsAffected = this.tc.createReg(this.myData);
            if (rowsAffected <= 0) {
                msg = "Error al intentar crear el registro";
                JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.ERROR_MESSAGE);
                return; // si tfId > 0 y no grabo cambios, entonces return
            } else {
                msg = "Se ha creado exitosamente el registro: " + idVenta;
                JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.INFORMATION_MESSAGE);
                this.jtfId.requestFocus();
                idVenta = this.tc.getMaxId();
            }
        }

        //DETALLES --------------------------------
        //si pasó quiere decir que tenemos cabecera y recorremos el detalle
        for (Map<String, String> myRow : columnData) {
            myRow.put("id", "0");
            myRow.put("ventaid", idVenta + "");
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
    } //Fin imGrabar

    @Override
    public void imActualizar(String crud) {
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
        //-------DETALLES-----------------
        //si pasó quiere decir que tenemos cabecera y recorremos el detalle
        int idVenta = Integer.parseInt(jtfId.getText()); //id de la cabecera
        Map<String, String> where = new HashMap<>();      //Por qué campo buscar los registros
        Map<String, String> fields = new HashMap<>();     //Los campos que vamos a recuperar
        ArrayList<Map<String, String>> alDetalle;         //Declara array de Map, cada Map es para un registro
        fields.put("*", "*");
        
        for (Map<String, String> myRow : columnData) {
            where.put("ventaid", idVenta + "");
            where.put("cod_barra", myRow.get("cod_barra"));
            //Buscar si ya existe un detalle de este cod_barra para esta compra.
            this.colDat = this.tcdet.searchListById(fields, where);
            // si no existe un detalle con este cod_barra para esta compra
            if (this.colDat.isEmpty()) { 
                myRow.put("id", "0");
                myRow.put("ventaid", idVenta + "");
                rowsAffected = this.tcdet.createReg(myRow);
                if (rowsAffected <= 0) {
                    msg = "No se ha podido grabar el Detalle Codigo:" + myRow.get("cod_barra");
                    JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
                } else {
                    msg = "Se ha creado el Detalle:" + myRow.get("cod_barra");
                    JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.INFORMATION_MESSAGE);
                }
            } else { //si ya existe un detalle con este cod_barra para esta compra
                myRow.put("id", colDat.get(0).get("id")); // id del detalle
                myRow.put("compraid", idVenta + "");
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
        int idVenta = Integer.parseInt(jtfId.getText());
        if(idVenta <= 0){
            msg = "NO SE HA ENCONTRADO EL REGISTRO";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
        }
        if(idVenta > 0){
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
        //Por defecto el buscador buscara Registros de venta por ID
        if (currentField.equals("id") && (this.tipoopcion == 1)) { //estando en venta para recuperar una nota de venta
            sql = "SELECT v.id AS codigo, "
                    + " CONCAT(v.serie, ' ', v.nro_documento, ' ',p.nombres, ' ', p.apellidos, ' ', c.ruc) AS descripcion "
                    + " FROM ventas v, clientes c, personas p "
                    + " WHERE v.tipodoc = 1 "
                    + " AND c.personaid = p.id "
                    + " AND v.clienteid = c.id "
                    + " AND v.tipoventa =  " + this.tipoopcion
                    + " AND CONCAT(v.id, v.nro_documento, v.observacion) LIKE '%";
            frame = new wBuscar(sql, this.jtfId);
        }

        if (currentField.equals("id") && (this.tipoopcion == 2)) { //estando como nota de credito para recuperar la nota credito
            sql = "SELECT v.id AS codigo, "
                    + " CONCAT(v.serie, ' ', v.nro_documento, ' ',p.nombres, ' ', p.apellidos, ' ', c.ruc) AS descripcion "
                    + " FROM ventas v, clientes c, personas p "
                    + " WHERE v.tipodoc = 2 "
                    + " AND v.clienteid = c.id "
                    + " AND c.personaid = p.id "
                    + " AND v.tipoventa =  " + this.tipoopcion
                    + " AND LOWER(CONCAT(v.id, v.nro_documento, v.observacion)) LIKE '%";
            frame = new wBuscar(sql, this.jtfId);

        }

        //Buscar Cliente
//        if (currentField.equals("cliente")){
//        sql = "SELECT c.id AS codigo, "
//         + " CONCAT(p.nombres, ' ', p.apellidos) as descripcion " 
//         + " FROM clientes c, personas p " 
//         + " WHERE c.personaid = p.id "
//         + " AND LOWER(c.id) LIKE '%" ;
//        frame = new wBuscar(sql, this.jcbCliente);
//        }
        if (currentField.equals("ventaid") && (this.tipoopcion == 2)) { //estando como nota de credito para recuperar una venta 
            //Verificar que se haya seleccionado cliente
            int clienteid;
            clienteid = Integer.parseInt(ComboBox.ExtraeCodigo(this.jcbCliente.getSelectedItem().toString()));
            if (clienteid <= 0) {
                String msg;
                msg = "¡Selecciones un Cliente válido!";
                this.jcbCliente.requestFocus();
                JOptionPane.showMessageDialog(null, msg, "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }
            sql = "SELECT v.id AS codigo, "
                    + " CONCAT(v.serie, ' ', v.nro_documento, ' ',p.nombres, ' ', p.apellidos, ' ', c.ruc) AS descripcion "
                    + " FROM ventas p, tipos_documento td, clientes c, personas g "
                    + " WHERE v.tipodoc = td.id "
                    + " AND v.clienteid = c.id"
                    + " AND c.personaid = p.id"
                    + " AND v.clienteid = " + clienteid
                    + " AND v.tipoventa = 1"
                    + " AND LOWER(CONCAT(v.serie, ' ', v.nro_documento, ' ',p.nombres, ' ', p.apellidos, ' ', c.ruc)) LIKE '%";
            frame = new wBuscar(sql, this.tfventareg);
        }

        if (currentTable.equals("tabla")) { //para buscar un producto por filtro
            sql = "SELECT d.cod_barra as codigo, "
                    + "CONCAT(p.nombre, ' ', m.nombre, ' ', "
                    + "c.color, ' - ', t.tamano, ' - ', s.diseno) AS descripcion "
                    + "FROM productos p, producto_detalle d, marcas m, colores c, tamanos t, disenos s "
                    + "WHERE p.marca = m.id "
                    + "AND d.colorid = c.id "
                    + "AND d.tamanoid = t.id "
                    + "AND d.disenoid = s.id "
                    + "AND p.id = d.productoid "
                    + "AND LOWER(CONCAT(p.descripcion, ' ', m.nombre, ' ',c.color, ' ', t.tamano, ' ', s.diseno, ' ',p.nombre)) LIKE '%";
            frame = new wBuscar(sql, this.tfcodbarra);
        }

        frame.setVisible(true);
        wPrincipal.desktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.DEFAULT_OPTION);
        }
    }

    @Override
    public void imNuevo() {
        this.resetData();
        this.limpiarTabla();
        this.fillView(myData, columnData);
        this.imInsDet();
    }

    @Override
    public void imBuscar() {
        String msg = "";
        Map<String, String> where = new HashMap<>();  //Por qué campo buscar los registros
        Map<String, String> fields = new HashMap<>(); //Los campos que vamos a recuperar
        this.setData(); //Hace tomar los datos de la vista
        
//=================Buscar por id de Cabecera ===================================      
        if (currentField.equals("id")) {
            this.myData = this.tc.searchById(myData); //Usa el mismo myData para devolver los valores de la cabecera
            System.out.println("Ventas imBuscar " + this.myData.toString());
            this.limpiarTabla();
            if (this.myData.isEmpty()) {
                System.out.println("No hay registros que mostrar");
                this.resetData();
                //this.fillView(myData, columnData);
                return;
            }

            where.put("ventaid", this.myData.get("id"));
            fields.put("*", "*");
            
            //verificar tablaModel 407 cuando no existe el reg
            this.columnData = this.tcdet.searchListById(fields, where);
            if (this.columnData.isEmpty()) {
                this.resetData();
                return;
            }
            this.fillView(myData, columnData);
            return;
        }
//=================================Buscar por id de Cabecera ============================================================
        if (currentField.equals("ventaid") && (this.tipoopcion == 2)) {
            where.clear();
            fields.clear();
            this.limpiarTabla();
            //Verificar cliente, si no se cargó = 0, return
            int clienteid;
            clienteid = Integer.parseInt(ComboBox.ExtraeCodigo(this.jcbCliente.getSelectedItem().toString()));
            if (clienteid <= 0) {
                msg = "¡Selecciones un Cliente válido!";
                this.jcbCliente.requestFocus();
                JOptionPane.showMessageDialog(null, msg, "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }
            //verificar tablaModel 407 cuando no existe el reg
            //===========================cabecera======================
            where.put("id", this.tfventareg.getText());
            where.put("monedaid", "0");
            where.put("depositoid", "0");
            where.put("plazoid", "0");
            this.myData = this.tc.searchById(where);
            System.out.println("Valor devuelto "+this.myData);
            //Siempre hay que verificar si trajo o nó registro alguno, si no trajo, return
            if (this.myData.isEmpty()) {
                msg = "¡No se ha encontrado el Registro Nro: " + where.get("id") + "!";
                this.tfventareg.requestFocus();
                JOptionPane.showMessageDialog(null, msg, "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }
            this.myDataAux.put("monedaid", myData.get("monedaid"));
            this.myDataAux.put("depositoid", myData.get("depositoid"));
            //this.myDataAux.put("plazoid", myData.get("plazoid"));

            //===========================detalle=======================
            where.clear();
            where.put("ventaid", this.tfventareg.getText());
            //Los campos que vamos a recuperar            
            fields.put("*", "*");
            this.columnData = this.tcdet.searchListById(fields, where);
            if (this.columnData.isEmpty()) {
                msg = "¡No se ha encontrado Detalle para el Registro Nro: " + where.get("id") + "!";
                this.tfventareg.requestFocus();
                JOptionPane.showMessageDialog(null, msg, "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }
            //myData.clear();
            this.fillView(this.myDataAux, columnData);
            setTotalGral();
            //invocar metodo set totales //
        }//fin si busca venta
    }//Fin Buscar

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
        if (tipoopcion == 1) {
            Reporte r = new Reporte("ReporteFactura", "vFactura", null);
            r.setTitle("Factura");
            r.setClosable(true);
            r.setVisible(true);
        }
        if (tipoopcion == 2) {
            Reporte r = new Reporte("NotaDeCredito", "vNotaDeCredito", null);
            r.setTitle("NotaDeCredito");
            r.setClosable(true);
            r.setVisible(true);
        }
//        String sql = "SELECT MAX(id) AS nro_documento FROM  sys_ventas ";
////                + "WHERE serie = '"
////                + varserie + "' AND id = '"
////                + jtfId.getText() + "' AND timbrado = '"
////                + vartimbrado + "' AND tipodoc = '" + tipoopcion + "'";
////              
//        //System.out.println(sql);
//        ResultSet rs = conexion.ejecuteSQL(sql);
//
//        try {
//            if (rs.next()) {
//                nrovent = Integer.parseInt(rs.getString("nro_documento"));
//
//                //System.out.println("numero de documentooooooooooooooooo" + nrovent);
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(wVenta.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        if (tipoopcion == 1) {
//            JasperReport jr;
//            int id = this.tc.getMaxId();
//            //JasperPrint jp;
//            JRViewer jv;
//            String RUTALOCAL = System.getProperty("user.dir");
//            try {
//                HashMap parametros = new HashMap();
//
//                parametros.put("numeroven", nrovent);
//                jr = JasperCompileManager.compileReport(new File("").getAbsolutePath() + "/src/reportes/ReporteFactura.jrxml");
//                JasperPrint jasperPrint = JasperFillManager.fillReport(jr, parametros, conexion.con);
//                // JasperPrint jp = JasperFillManager.fillReport(RUTALOCAL + "/src/reportes/Productos.jrxml", parametros, con);
//                PrinterJob printerJob = PrinterJob.getPrinterJob();
//                PageFormat pageFormat = PrinterJob.getPrinterJob().defaultPage();
//                printerJob.defaultPage(pageFormat);
//                JasperViewer view = new JasperViewer(jasperPrint, false);
//                view.setTitle("FACTURA");
//                // view.setExtendedState(Frame.MAXIMIZED_BOTH);
//                view.setVisible(true);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//        if (tipoopcion == 2) {
//            JasperReport jr;
//            //JasperPrint jp;
//            JRViewer jv;
//            String RUTALOCAL = System.getProperty("user.dir");
//            try {
//                HashMap parametros = new HashMap();
//
//                parametros.put("numeroven", nrovent);
//                jr = JasperCompileManager.compileReport(new File("").getAbsolutePath() + "/src/reportes/NotaDeCredito.jrxml");
//                JasperPrint jasperPrint = JasperFillManager.fillReport(jr, parametros, conexion.con);
//                // JasperPrint jp = JasperFillManager.fillReport(RUTALOCAL + "/src/reportes/Productos.jrxml", parametros, con);
//                PrinterJob printerJob = PrinterJob.getPrinterJob();
//                PageFormat pageFormat = PrinterJob.getPrinterJob().defaultPage();
//                printerJob.defaultPage(pageFormat);
//                JasperViewer view = new JasperViewer(jasperPrint, false);
//                view.setTitle("NOTA DE CRÉDITO");
//                // view.setExtendedState(Frame.MAXIMIZED_BOTH);
//                view.setVisible(true);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }

    }

    @Override
    public void imInsDet() {
        int currentRow = jtDetalle.getSelectedRow();
        //Si no hay fila seleccionada
        if (currentRow == -1) {
            //System.out.println("no hay fila seleccionada imInsDet 1062");
            modelo.addRow(new Object[]{"", "Descripcion", "0.0", "0.0", "0.0", "0.0", "0.0"});
            return;
        }
        if (currentRow != -1) {
            jtDetalle.getSelectionModel().clearSelection();
        }
        //Si hay fila seleccionada
        String cod = this.jtDetalle.getValueAt(currentRow, 0).toString();
        if (cod.equals("0") || cod.equals("")) {
            String msg = " imInsDet1606: POR FAVOR INGRESE UN PRODUCTO ";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
        } else {
            System.out.println("imInsDet1609: Exito");
            modelo.addRow(new Object[]{"", "Descripcion", "0.0", "0.0", "0.0", "0.0", "0.0"});
            this.jtDetalle.requestFocus(); //devolver el foco a la tabla
            //hacer foco en la col=0 de la nueva fila
            int lastRow = this.jtDetalle.getRowCount() - 1;
            this.jtDetalle.changeSelection(lastRow, 0, false, false);
        }
    }

    @Override
    public void imDelDet() {
        int currentRow = jtDetalle.getSelectedRow();
        //si no hay row seleccionado
        if (currentRow == -1) {
            String msg = "imDelDet1623: NO hay fila seleccionada ";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
        }
        //si hay row seleccionado
        if (currentRow >= 0) { 
            modelo.removeRow(currentRow);
            //Si al eliminar queda vacía, habrá que insertar una nueva
            int rows = jtDetalle.getRowCount();
            if (rows == 0) {
                String msg = "imDelDet1291: Se eliminaron todas las filas ";
                JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
                this.imInsDet();
            }
        }
    }//Fin imDelDet

    @Override
    public void imCerrar() {
        setVisible(false);
        dispose();
    }

    /**
     * Calucula el total para una fila específica
     *
     * @param row fila a ser procesada
     * @return rtn int que devuelve el estado de la operación, falta completar
     */
    public int setTotalRow(int row) {
        int rtn = 0;
        Double precio, cantidad, descuento, bonificado, totalrow;
        precio = Tools.sGetDecimalStringAnyLocaleAsDouble(this.jtDetalle.getModel().getValueAt(row, 2).toString());
        cantidad = Tools.sGetDecimalStringAnyLocaleAsDouble(this.jtDetalle.getModel().getValueAt(row, 3).toString());
        descuento = Tools.sGetDecimalStringAnyLocaleAsDouble(this.jtDetalle.getModel().getValueAt(row, 4).toString());
        bonificado = Tools.sGetDecimalStringAnyLocaleAsDouble(this.jtDetalle.getModel().getValueAt(row, 5).toString());

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

        monedaDecimal = Integer.parseInt(mapMoneda.get("decimales"));
        Map<String, String> select = new HashMap<>();
        Map<String, String> where = new HashMap<>();
        columnData.clear();
        //Recorrer Rows de Detalles
        for (int row = 0; row < rows; row++) {
            codbar = this.jtDetalle.getModel().getValueAt(row, 0).toString();
            if (codbar.equals("0") || codbar.equals("")) { //Si no se especifica códio, no tiene sentido continuar
                JOptionPane.showMessageDialog(this, "Favor ingrese un producto!", "¡setTotalGral 1893!", JOptionPane.INFORMATION_MESSAGE);
                continue;
            }
            where.clear();
            //Por cada codigo de barras 
            select.put("*", "*");
            where.put("cod_barra", codbar);
            this.mapProductoDet = this.tmProductoDet.readRegisterById(select, where);//Recupera el id de productoDetalle
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
                System.out.println("setTotalGral 1913: precio o cantidad <= 0");
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
            
            //para CONTROLAR LIMITE DE CREDITO DEL CLIENTE
            sql = "SELECT (c.credito) AS limite "
                    + "FROM precio_detalle pd, clientes c "
                    + "WHERE pd.precioid = c.precioid "
                    + "AND pd.cod_barra = '" + codbar + "'";
            ResultSet rs;

            try {
                rs = conexion.ejecuteSQL(sql);
                if (rs.next()) {
                    System.out.println("Credito Cliente INFO: ");
                    System.out.println("Limite de Credito Cliente: " + " valor " + rs.getString("limite"));
                    
                    String limi = rs.getString("limite");
                    limite = Double.parseDouble(limi);
                    System.out.println("Limite de Credito de Cliente: " + limite);
                    
                } else {
                    //PENDIENTE como se deberia manejar el caso en el que el cliente haya alcanzado
                    // el limite maximo de credito
                    // this.jtDetalle.getModel().setValueAt("0", row, 2);
                }
            }
            catch (SQLException ex) {
                Logger.getLogger(tableModel.class.getName()).log(Level.SEVERE, null, ex);
            }
            //CONTROLA LIMITE DE CRÉDITO //
            String condi = ComboBox.ExtraeCodigo(jcbCondicion.getSelectedItem().toString());
            condicion = Integer.parseInt(condi);
            System.out.println("condicion: " + condicion);
            System.out.println("limite: " + limite);
            System.out.println("totalventa " + totNeto);
            if (totBruto > limite && condicion == 0) {
                //this.jftfNeto.setText("0");
                //this.jftfImpuesto.setText("0");
                //this.jftfExenta.setText("0");
                //this.jftfTotal.setText("0");
                //imNuevo();
                JOptionPane.showMessageDialog(this, "LIMITE DE CRÉDITO DEL CLIENTE SUPERADO!", "¡A T E N C I O N!", JOptionPane.WARNING_MESSAGE);

            }

            myDet = new HashMap<>();
            String id = jtfId.getText();
            //System.out.println("el facking id "+id);
            myDet.put("ventaid", id);
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
            //System.out.println("DETALLEEE: " + myDet);
        }
        // String totBrut = functions.decimalFormat(totBruto);

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
     * Tambien carga los datos de moneda y cotizacion en sus maps correspondientes
     * Este método se llama en el evento focus de la tabla
     *
     * @return boolean que indica si las condiciones se cumplen o no, true/false
     */
    public boolean validarCabecera() {
        boolean rtn = true;
        String msg = "Defecto Cabecera";
        //validar id
        if (jtfId.getText().isEmpty() || "".equals(jtfId.getText())) {
            msg = "El campo Id no debe ser vacio";
            rtn = false;
            jtfId.setText("0");
            jtfId.requestFocus();
        }
         //Fecha Proceso
        Date fechaProceso = jdcProceso.getDate();
        if (fechaProceso == null) {
            msg = "Favor ingrese una fecha de operación válida!";
            jdcProceso.requestFocus();
            jdcProceso.setDate(new Date());
            rtn = false;
        }
        //Fecha Factura
        Date fechaFactura = jdcFactura.getDate();
        if (fechaFactura == null) {
            msg = "Favor ingrese una fecha de operación válida!";
            jftfNroDocu.requestFocus();
            jdcFactura.setDate(new Date());
            rtn = false;
        }
        //Cliente
        String clienteid = ComboBox.ExtraeCodigo(jcbCliente.getSelectedItem().toString());
        if (clienteid.isEmpty()) {
            msg = "favor seleccione un cliente";
            jcbCliente.requestFocus();
            jcbCliente.showPopup();
            rtn = false;
        }
        //Deposito
        String depositoid = ComboBox.ExtraeCodigo(jcbDeposito.getSelectedItem().toString());
        if (depositoid.isEmpty()) {
            msg = "favor seleccione un deposito";
            jcbDeposito.requestFocus();
            jcbDeposito.showPopup();
            rtn = false;
        }
        //Moneda
        String monedaid = ComboBox.ExtraeCodigo(jcbMoneda.getSelectedItem().toString());
        if (monedaid.isEmpty()) {
            msg = "favor seleccione un deposito";
            jcbMoneda.requestFocus();
            jcbMoneda.showPopup();
            rtn = false;
        }
        
        this.getMoneda(); //Carga los datos de la moneda operacion en el mapMoneda
        this.getCotizacion(); //Carga los datos de la cotizacion en el mapCotizacion
        
        if (!rtn) {
            JOptionPane.showMessageDialog(this, msg, "¡Favor Verificar!", JOptionPane.INFORMATION_MESSAGE);
        }
        return rtn;
    } //Fin validarCabecera

    public boolean validarDetalles() {
        //Validar Campos de los Detalles
        boolean valor = true;
        String msg = "Defecto Detalle";
        int rows = this.jtDetalle.getRowCount();
        for (int i = 0; i < rows; i++) {
            //celda codigo de barra
            String codigo_barra = (String) jtDetalle.getValueAt(i, 0);
            if (codigo_barra.equals("0") || "".equals(codigo_barra)) {
                msg = "Proporcione codigo de barra para el registro";
                jtDetalle.changeSelection(i, 0, false, false);
                valor = false;
            }

            String cantidad = (String) jtDetalle.getValueAt(i, 3);
            if (Integer.parseInt(cantidad) < 1) {
                msg = "Cantidad no puede ser menor a 1";
                jtDetalle.changeSelection(i, 3, false, false);
                valor = false;
            }

            String descuento = (String) jtDetalle.getValueAt(i, 4);
            if ("".equals(descuento)) {
                jtDetalle.setValueAt("0", i, 4);
                jtDetalle.changeSelection(i, 4, false, false);
                valor = false;
            }
        }
        if (!valor) {
            JOptionPane.showMessageDialog(this, msg, "Validacion de Campos Detalle!", JOptionPane.DEFAULT_OPTION);
        }
        return valor;
    } //Fin validarDetalles
    
    public int getProducto(int row, int col) {
        int Exito = 0;
        //recuperar cod_barra de la row y DepositoId del ComboBox cabecera
        String codbar = this.jtDetalle.getModel().getValueAt(row, 0).toString(); //col == 0
        String depositoid = ComboBox.ExtraeCodigo(jcbDeposito.getSelectedItem().toString());
        
        if (codbar.equals("")) { //para evitar error en el sql
            //this.jtDetalle.getModel().setValueAt("0", row, 0); //cod_bar 0
            codbar = "0";
            JOptionPane.showMessageDialog(this, "Proporcione codigo", "getProducto1623", JOptionPane.OK_OPTION);
            Exito = 0;
            return Exito;
        }
        // DESCRIPCION DEL PRODUCTO -------------------
        String sql = "SELECT CONCAT(p.nombre, ' ', "
                + "m.nombre, ' , ', "
                + "c.color, ' ', t.tamano, ' ', s.diseno) AS descripcion, "
                + "stock.cantidad AS cantidad_actual "
                + "FROM productos p, producto_detalle d, marcas m, colores c, tamanos t, disenos s, stock, depositos dep "
                + "WHERE p.id = d.productoid "
                + "AND p.marca = m.id "
                + "AND d.colorid = c.id "
                + "AND d.tamanoid = t.id "
                + "AND d.disenoid = s.id "
                + "AND stock.cod_barra = '" + codbar + "' "
                + "AND stock.depositoid = '" + depositoid + "' "
                + "AND d.cod_barra = '" + codbar + "'";
        ResultSet rs;

                //SELECT CONCAT(p.nombre, ' ', m.nombre, ' , ', c.color, ' ', t.tamano, ' ', s.diseno) AS descripcion, 
                //	stock.cantidad AS cantidad_actual,
                //	pd.precio AS precio
                //FROM productos p
                //JOIN producto_detalle d ON p.id = d.productoid
                //JOIN marcas m ON p.marca = m.id 
                //JOIN colores c ON d.colorid = c.id
                //JOIN tamanos t ON d.tamanoid = t.id
                //JOIN disenos s ON d.disenoid = s.id
                //JOIN stock ON stock.cod_barra = d.cod_barra
                //JOIN depositos dep ON stock.depositoid = dep.id
                //join clientes cl on cl.id = :clienteid
                //join precios pr on cl.precioid = pr.id
                //join precio_detalle pd on pd.cod_barra = d.cod_barra and pd.precioid = pr.id
                //WHERE stock.cod_barra = :codbar
                //AND stock.depositoid = :depositoid
                //AND d.cod_barra = :codbar
                //and cl.id = :clienteid;
        try {
            rs = conexion.ejecuteSQL(sql); //Esto devuelve un ResultSet
            if (rs.next()) {
                Exito = 1;
                //INFORMACION DEL PRODUCTO EN CONSOLA
                ResultSetMetaData metaData = rs.getMetaData();
                System.out.println("Producto INFO:");
                System.out.println("column "+metaData.getColumnName(1)+" valor "+rs.getString("descripcion"));

                //PRODUCTO DESCRIPCION
                String descripcion = rs.getString("descripcion");
                this.jtDetalle.getModel().setValueAt(descripcion, row, 1); //Descripcion

                //PRODUCTO CANTIDAD ACTUAL STOCK
                int cantidad_actual = rs.getInt("cantidad_actual");
                this.jtDetalle.getModel().setValueAt(cantidad_actual, row, 3); //Cantidad en Deposito Stock
                
            } else {
                this.jtDetalle.getModel().setValueAt("0", row, 0);
                this.jtDetalle.getModel().setValueAt("Descripcion", row, 1);
                String msg = "No se ha encontrado Producto con el Codigo: " + codbar;
                JOptionPane.showMessageDialog(this, msg, "getProducto1696", JOptionPane.OK_OPTION);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tableModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        //PARA EL PRECIO DEL PRODUCTO --------------------------------
        String clienteid = ComboBox.ExtraeCodigo(jcbCliente.getSelectedItem().toString());
        sql = "SELECT (pd.precio) AS precio"
                + " FROM PRECIOS p, PRECIO_DETALLE pd, CLIENTES c"
                + " WHERE p.id = pd.precioid"
                + " AND pd.precioid = c.precioid"
                + " AND c.id = " + clienteid
                + " AND pd.cod_barra = '" + codbar + "'";
        ResultSet rss;
        try {
            Double precio = 0.0;
            rss = conexion.ejecuteSQL(sql);
            if (rss.next()) {
                    precio = rss.getDouble("precio"); //Precio de lista
                    //Double precioCotizado = this.cotizarPrecio(precio);
                    String totrow = Tools.decimalFormat(precio); //precioCotizado
                    String woDot = "";
                    woDot = totrow.replace(".", "");
                    //System.out.println("Sin puntos "+woDot);
                    String woComa = "";
                    woComa = woDot.replace(",", ".");
                    //System.out.println("Coma por punto "+woComa);
                    double tr = Tools.sGetDecimalStringAnyLocaleAsDouble(totrow);

                    //System.out.println("total string tr "+tr+" fila "+row);
                    this.jtDetalle.getModel().setValueAt(woComa, row, 2);
                
            } else {
                JOptionPane.showMessageDialog(this, "EL PRODUCTO NO TIENE PRECIO DEFINIDO PARA EL CLIENTE!", "¡A T E N C I O N!", JOptionPane.WARNING_MESSAGE);
                this.jtDetalle.getModel().setValueAt("0", row, 2);
//PENDIENTE
//Aqui debe haber una manera de gestionar la falta de precio para el producto para esta lista de precios en particular
                this.limpiarTabla();
                this.imInsDet();
            }
            if (precio == 0.0) {
                JOptionPane.showMessageDialog(this, "CARGAR COTIZACIÓN DEl DIA", "¡A T E N C I O N!", JOptionPane.WARNING_MESSAGE);
                limpiarTabla();
                imInsDet();
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(tableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Exito;
    } //Fin getProducto
    
    /**
     * Recupera los datos de la moneda que se usa en el proceso
     */
    public void getMoneda() {
        String idMoneda = ComboBox.ExtraeCodigo(this.jcbMoneda.getSelectedItem().toString());
        if (Integer.parseInt(idMoneda) > 0) {
            this.mapMoneda.clear();
            Map<String, String> fields = new HashMap<>();
            Map<String, String> where = new HashMap<>();
            fields.put("*", "*");
            where.put("id", idMoneda);
            this.mapMoneda = tmMoneda.readRegisterById(fields, where);
        }
    } // Fin getMoneda
    
    /**
     * Cargar los datos de la cotizacion en mapCotizacion
    */
    public void getCotizacion() {
        this.mapCotizacion.clear();
        //monedaOrigen es la moneda en la lista de precios.
        //para conseguir esta moneda realizamos una consulta SQL de la lista de precios asociada al cliente
        String clienteid = ComboBox.ExtraeCodigo(jcbCliente.getSelectedItem().toString());
        int monedaOrigen = 1; //moneda definida en PRECIOS. Por defecto 1 que disponemos como Guaranies
        String sql = "SELECT p.moneda as MONEDAORIGEN "
                    + "FROM clientes c, precios p "
                    + "WHERE c.precioid = p.id "
                    + "AND c.id = '" + clienteid + "'";
        ResultSet rs;
        try {
            rs = conexion.ejecuteSQL(sql);
            ResultSetMetaData metaData = rs.getMetaData();
            int colCount = metaData.getColumnCount();
            if (rs.next()) {
                for (int r = 1; r <= colCount; r++) {
                    monedaOrigen = rs.getInt("MONEDAORIGEN");
                }
            }
        }catch(SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se pudo recuperar el registro. - ERROR: " + ex);
        }
        //monedaDestino es la moneda de OPERACION. Esta definida en mapMoneda
        int monedaDestino = Integer.parseInt(ComboBox.ExtraeCodigo(jcbMoneda.getSelectedItem().toString()));
        //fechaProceso es necesario para verificar la cotizacion el dia de la operacion
        long fechaProceso = (jdcProceso.getDate().getTime() / 1000L);
        //nuevo sql, esta vez consultamos la cotizacion entre monedaOrigen: moneda en listado de precios
        // y monedaDestino: moneda definida en jcbMoneda para la fecha de la operacion definida en jdcProceso
        sql = "SELECT c.cotizacion, c.operacion "
                + "FROM cotizaciones c "
                + "WHERE c.moneda_origen =  '" + monedaOrigen + "' "
                + "AND c.moneda_destino = '" + monedaDestino + "' "
                + "AND c.fecha = '" + fechaProceso + "'";
        ResultSet rss;
        try {
            rss = conexion.ejecuteSQL(sql);
            if (rss.next()) {
                //si existe registro con estas condiciones entonces
                //cargar valor de cotizacion en cotizacionGlobal
                cotizacionGlobal = rss.getInt("cotizacion");
                operacionCotizacion = rss.getInt("opearcion");
            }
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "No se pudo recuperar el registro. - ERROR: " + erro);
        }
    } //Fin getCotizacion

    /**
    * Prepara los Map Cabecera y Detalle con los valores de los campos
    */
    private void setData() {
        java.util.Date df = new java.util.Date();
        String fecha = "";
        myData.put("id", jtfId.getText());

        fecha = (jdcProceso.getDate().getTime() / 1000L) + "";
        myData.put("fecha_proceso", fecha);

        fecha = (jdcFactura.getDate().getTime() / 1000L) + "";
        myData.put("fecha_factura", fecha);

        myData.put("nro_documento", jftfNroDocu.getText());
        myData.put("serie", jftfSerie.getText());
        myData.put("timbrado", jtfTimbrado.getText());

        fecha = (jdcVencimiento.getDate().getTime() / 1000L) + "";
        myData.put("vence", fecha);

        myData.put("clienteid", ComboBox.ExtraeCodigo(jcbCliente.getSelectedItem().toString()));
        myData.put("plazoid", ComboBox.ExtraeCodigo(jcbPlazo.getSelectedItem().toString()));
        myData.put("monedaid", ComboBox.ExtraeCodigo(jcbMoneda.getSelectedItem().toString()));
        myData.put("cotizacion", jftfCotizacion.getText().replace(".", "").replace(",", "."));
        myData.put("tot_preciobruto", jftfTotal.getText().replace(".", "").replace(",", "."));
        myData.put("pago_inicial", jftfAdelanto.getText().replace(".", "").replace(",", "."));
        myData.put("tot_exento", jftfExenta.getText().replace(".", "").replace(",", "."));
        myData.put("tot_precioneto", jftfNeto.getText().replace(".", "").replace(",", "."));
        myData.put("tot_impuesto", jftfImpuesto.getText().replace(".", "").replace(",", "."));
        myData.put("condicion", ComboBox.ExtraeCodigo(jcbCondicion.getSelectedItem().toString()));
        myData.put("tipoventa", ComboBox.ExtraeCodigo(jcbtipoventa.getSelectedItem().toString()));
        myData.put("tipodoc", ComboBox.ExtraeCodigo(jcbTipo.getSelectedItem().toString()));
        myData.put("observacion", jtfObs.getText());
        myData.put("ventareg", tfventareg.getText());
        myData.put("depositoid", ComboBox.ExtraeCodigo(jcbDeposito.getSelectedItem().toString()));
        //System.out.println("myData " + myData);

        this.setTotalGral();

        //  this.setTotalGral();
    }//fin setData

    /**
     * Limpia Los mapas de datos
     */
    private void resetData() {
        this.myData = new HashMap<String, String>();
        java.util.Date df = new java.util.Date();
        this.myData.put("id", "0");
        this.myData.put("nro_documento", "0");
        this.myData.put("serie", "0");
        this.myData.put("timbrado", "0");
        this.myData.put("clienteid", "0");
        this.myData.put("plazoid", "0");
        this.myData.put("monedaid", "0");
        this.myData.put("cotizacion", "0.0");
        this.myData.put("tot_preciobruto", "0.0");
        this.myData.put("pago_inicial", "0.0");
        this.myData.put("tot_exento", "0.0");
        this.myData.put("tot_precioneto", "0.0");
        this.myData.put("tot_impuesto", "0.0");
        this.myData.put("tipoventa", "0");
        this.myData.put("tipoventa", "1");
        this.myData.put("observacion", "");
        this.myData.put("depositoid", "0");
        this.myData.put("ventareg", "0");
        jcbCliente.setSelectedIndex(0);
        jcbMoneda.setSelectedIndex(0);
        jcbPlazo.setSelectedIndex(0);
        jcbDeposito.setSelectedIndex(0);
        jcbCondicion.setSelectedIndex(0);
        jcbTipo.setSelectedIndex(0);

        this.myDet = new HashMap<>();

        this.myDet.put("ventaid", "0");
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

    public int getStock(int row, int col) {
        String codbar, sql, sqll, sqlll, sqllll, limi, msg = "";
        double cantidad;
        cantidad = 0.0;
        int rows = this.jtDetalle.getRowCount();
        codbar = "";
        sql = "";
        codbar = this.jtDetalle.getModel().getValueAt(row, 0).toString();
        System.out.println("CODIGO DE BARRA de GETSTOCK:" + codbar);
        cantidad = Tools.sGetDecimalStringAnyLocaleAsDouble(this.jtDetalle.getModel().getValueAt(row, 3).toString());

        //para CONTROLAR stock en los depositos/*********************************
        String iddepo = ComboBox.ExtraeCodigo(jcbDeposito.getSelectedItem().toString());
        iddeposito = Integer.parseInt(iddepo);
        sqllll = "SELECT cantidad AS CANTIDAD "
                + "FROM STOCK "
                + "WHERE depositoid = " + iddeposito
                + " AND cod_barra = '" + codbar + "'";

        Map<String, String> rtnnnn = new HashMap<>();
        ResultSet rssss;

        try {
            rssss = conexion.ejecuteSQL(sqllll); //Esto devuelve un ResultSet
            ResultSetMetaData metaData = rssss.getMetaData();
            int colCount = metaData.getColumnCount();
            // while(rs.next()){
            if (rssss.next()) {
                for (int r = 1; r <= colCount; r++) {
                    //System.out.println("column "+metaData.getColumnName(r)+" valor "+rs.getString(metaData.getColumnName(r)));
                    limi = rssss.getString("CANTIDAD");
                    cantdeposito = Double.parseDouble(limi);
                    System.out.println("getStock2226 CANTIDAD stock: " + cantdeposito);
                }
            } else {
                // this.jtDetalle.getModel().setValueAt("0", row, 2);

            }
        }catch (SQLException ex) {
//            Logger.getLogger(tableModel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
        }
        
        if (cantdeposito == 0.0) {
            msg = "PRODUCTO SIN STOCK EN EL DEPOSITO SELECCIONADO";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            this.jtDetalle.getModel().setValueAt("0", row, 3);
            this.jtDetalle.getModel().setValueAt("0", row, 6);
            this.jftfTotal.setText("0.0");
            this.jftfNeto.setText("0.0");
            this.jftfExenta.setText("0.0");
            this.jftfImpuesto.setText("0.0");
            return 0;
        }
        if (cantdeposito < cantidad) {
            msg = "CANTIDAD INGRESADA NO DISPONIBLE";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            this.jtDetalle.getModel().setValueAt("0", row, 3);
            this.jtDetalle.getModel().setValueAt("0", row, 6);
            this.jftfTotal.setText("0.0");
            this.jftfNeto.setText("0.0");
            this.jftfExenta.setText("0.0");
            this.jftfImpuesto.setText("0.0");
            return 0;
        }
        return 0;
    }

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
            String key = entry.getKey();
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
                case "fecha_factura":
                    dateLong = Long.parseLong(value) * 1000L;
                    df = new Date(dateLong);
                    jdcFactura.setDate(df);
                    break;
                case "nro_documento":
                    jftfNroDocu.setText(value);
                    break;
                case "serie":
                    jftfSerie.setText(value);
                    break;
                case "timbrado":
                    jtfTimbrado.setText(value);
                    break;
                case "vence":
                    dateLong = Long.parseLong(value) * 1000L;
                    df = new Date(dateLong);
                    jdcVencimiento.setDate(df);
                    break;
                case "clienteid":
                    ComboBox.E_estado(jcbCliente, "clientes c, personas p", "c.id, CONCAT(p.nombres, ' ', p.apellidos) as nombres", "c.personaid = p.id AND c.id = " + value);
                    break;
                case "plazoid":
                    ComboBox.E_estado(jcbPlazo, "plazo_pago","id, plazo", "id=" + value);
                    break;
                case "monedaid":
                    ComboBox.E_estado(jcbMoneda, "monedas", "id, moneda", "id=" + value);
                    break;
                case "cotizacion":
                    jftfCotizacion.setText(Tools.decimalFormat(Double.parseDouble(value)));
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
                case "observacion":
                    jtfObs.setText(value);
                    break;
                case "tipoventa":
                    jcbtipoventa.setSelectedItem(Integer.parseInt(value));
                    break;
                case "depositoid":
                    ComboBox.E_estado(jcbDeposito, "depositos", "id, nombre", "id=" + value);
                    break;
                case "condicion":
                    jcbCondicion.setSelectedItem(Integer.parseInt(value));
                    break;
                case "tipodoc":
                    ComboBox.E_estado(jcbTipo, "tipos_documento", "id, tipodocumento", "id=" + value);
                    break;
                case "ventareg":
                    tfventareg.setText(value);
                    break;
            }//end switch
        }//end for Cabecera 

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
                JOptionPane.showMessageDialog(this, msg, "2365Validacion de Campos Detalle!", JOptionPane.DEFAULT_OPTION);
                modelo.removeRow(row);
                continue;
            }
            //System.out.println(myRow.get("descripcion"));
            //this.jtDetalle.setValueAt(myRow.get("descripcion"), row, 1);
            //this.jtDet.editCellAt(row, 0, this.keyPress(KeyEvent.VK_ENTER));
            this.jtDetalle.setValueAt(Tools.decimalFormat(Double.parseDouble(myRow.get("precio_bruto"))), row, 2);
            this.jtDetalle.setValueAt(myRow.get("cantidad"), row, 3);
            this.jtDetalle.setValueAt(Tools.decimalFormat(Double.parseDouble(myRow.get("descuento"))), row, 4);
            this.jtDetalle.setValueAt(myRow.get("cantbonificado"), row, 5);
            this.jtDetalle.setValueAt(Tools.decimalFormat(Double.parseDouble(myRow.get("total"))), row, 6);

            //this.jtDetalle.setValueAt(decimalFormat(650.75), row, 5);
            
            row++;
        } //end for Detalles
    } //end fillView

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

    public void recuperarTalonario() {
        
        int tipodocid = Integer.parseInt(ComboBox.ExtraeCodigo(jcbTipo.getSelectedItem().toString()));
        String sql = "SELECT * FROM talonarios "
                + "WHERE tipodocid = '"+ tipodocid + "' "
                + "AND cajaid = '" + this.cajaid + "' "
                + "AND activo = 1";
        //System.out.println(sql);
        ResultSet rs = conexion.ejecuteSQL(sql);
        try {
            if (rs.next()) {
                this.jftfSerie.setText(rs.getString("serie"));
                this.jtfTimbrado.setText(rs.getString("timbrado"));
                long fechavence = Long.parseLong(rs.getString("vence")) * 1000L;
                Date df = new Date(fechavence);
                jdcVencimiento.setDate(df);

                varserie = (rs.getString("serie"));
                vartimbrado = (rs.getString("timbrado"));

                nrovent = Integer.parseInt(rs.getString("cant_usado"));
                nroventnuevo = nrovent + 1;
                this.jftfNroDocu.setText(nroventnuevo + "");
            }
        } catch (SQLException ex) {
            Logger.getLogger(wVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//fin recuperarTalonario

    public void validartipodocumento() {
        int tipodoc;
        int nrofin = 0;
        tipodoc = Integer.parseInt(ComboBox.ExtraeCodigo(jcbTipo.getSelectedItem().toString()));
        String sql = "SELECT * FROM talonarios WHERE tipodocid = "
                + tipodoc + " AND cajaid = "
                + this.cajaid
                + " AND activo = 1";
        //System.out.println(sql);
        ResultSet rs = conexion.ejecuteSQL(sql);
        try {
            if (rs.next()) {

                nrofin = Integer.parseInt(rs.getString("nrofin"));

                //System.out.println(+nrofin);
            }
        } catch (SQLException ex) {
            Logger.getLogger(wVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (nrodocnuevo > nrofin) {  //cambiar nrodocnuevo
            JOptionPane.showMessageDialog(this, "Talonario Lleno!", "¡A T E N C I O N!", JOptionPane.WARNING_MESSAGE);
        }
    } //fin validar tipo documento

    public void validarCombo() {
        int codigo = 0;
        int condicion = 0;
        codigo = Integer.parseInt(ComboBox.ExtraeCodigo(jcbCliente.getSelectedItem().toString()));
        if (codigo == 0) {
            this.jcbCliente.requestFocus();
            JOptionPane.showMessageDialog(this, "Favor Seleccione Cliente!", "¡A T E N C I O N!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        codigo = Integer.parseInt(ComboBox.ExtraeCodigo(jcbPlazo.getSelectedItem().toString()));
        if (codigo == 0) {
            this.jcbPlazo.requestFocus();
            JOptionPane.showMessageDialog(this, "Favor Seleccione Plazo!", "¡A T E N C I O N!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        codigo = Integer.parseInt(ComboBox.ExtraeCodigo(jcbMoneda.getSelectedItem().toString()));
        if (codigo == 0) {
            this.jcbMoneda.requestFocus();
            JOptionPane.showMessageDialog(this, "Favor Seleccione Moneda!", "¡A T E N C I O N!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        codigo = Integer.parseInt(ComboBox.ExtraeCodigo(jcbDeposito.getSelectedItem().toString()));
        if (codigo == 0) {
            this.jcbDeposito.requestFocus();
            JOptionPane.showMessageDialog(this, "Favor Seleccione Depósito!", "¡A T E N C I O N!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        codigo = Integer.parseInt(ComboBox.ExtraeCodigo(jcbTipo.getSelectedItem().toString()));
        if (codigo == 0) {
            this.jcbTipo.requestFocus();
            JOptionPane.showMessageDialog(this, "Favor Seleccione tipo de Documento!", "¡A T E N C I O N!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        condicion = Integer.parseInt(ComboBox.ExtraeCodigo(jcbCondicion.getSelectedItem().toString()));
        int plazo = Integer.parseInt(ComboBox.ExtraeCodigo(jcbPlazo.getSelectedItem().toString()));
//          System.out.println("codigo" +codigo);
        if (condicion == 1 && plazo > 1 || plazo == 1 && condicion == 0) {
            this.jcbCondicion.requestFocus();
            String msg = "CONDICIÓN Y PLAZO NO COMPATIBLE";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
        }
    }


}// fin clase
