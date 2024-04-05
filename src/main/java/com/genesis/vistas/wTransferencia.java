/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genesis.vistas;

import com.genesis.model.conexion;
import com.genesis.model.pojoAjusteDetalle;
import com.genesis.model.pojoTransferenciaDetalle;
import com.genesis.model.tableModel;
import com.genesis.tabla.GestionCeldas;
import com.genesis.tabla.GestionEncabezadoTabla;
import com.genesis.tabla.ModeloTabla;
import com.genesis.controladores.tableController;
import util.cargaComboBox;
import util.Tools;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author RC
 */
public class wTransferencia extends javax.swing.JInternalFrame implements MouseListener, KeyListener, ActiveFrame {

    private Map<String, String> myData;
    private HashMap<String, String> myDet;
    private tableController tc;
    private tableController tcdet;

    ArrayList<pojoTransferenciaDetalle> lista;// = new ArrayList<>(); //cambiar pojoo

    ArrayList<pojoTransferenciaDetalle> listaDetalles;//lista que simula la información de la BD //cambiar pojoo

    ModeloTabla modelo;//modelo definido en la clase ModeloTabla
    private int filasTabla;
    private int columnasTabla;
    public static int filaSeleccionada;

    private ArrayList<Map<String, String>> columnData, colDat;

    private tableModel tMPrecio;
    Map<String, String> mapPrecio;// = new HashMap<String, String>();
    String currentField;
    String currentTable;
    private tableModel tmAjusteDet;
    Map<String, String> mapAjusteDet;

    private tableModel tmProducto;
    Map<String, String> mapProducto;// = new HashMap<String, String>();

    private tableModel tmProductoDet;
    Map<String, String> mapProductoDet;

    private tableModel tmCompra;
    Map<String, String> mapCompra;// = new HashMap<String, String>();

    private tableModel tmCompraDet;
    Map<String, String> mapCompraDet;
    String Opcion = "";
    String CRUD = "";
    private Date fecha; //= jdcFechaProceso.getDate();
    private DateFormat dateFormat, dateTimeFormat, dateIns; //= new SimpleDateFormat("dd/MM/yyyy HH:mm"); 
    private int ped_env;

    /**
     * Creates new form wTransferencia
     */
    public wTransferencia(int pe, String Opcion) {
        initComponents();
        this.Opcion = Opcion;
        this.ped_env = pe;
        listaDetalles = new ArrayList<pojoTransferenciaDetalle>();
        lista = new ArrayList<>();
        myData = new HashMap<String, String>();
        columnData = new ArrayList<Map<String, String>>();

        cargaComboBox.pv_cargar(jcbDeposito, "depositos", " id, nombre ", "id", "");
        cargaComboBox.pv_cargar(jcbDepositoo, "depositos", " id, nombre ", "id", "");

        tc = new tableController();
        tc.init("transferencias_producto");
        tcdet = new tableController();
        tcdet.init("transferencias_producto_det");

        //PARA EL DETALLE
        mapProducto = new HashMap<String, String>();
        tmProducto = new tableModel();
        tmProducto.init("productos");

        mapProductoDet = new HashMap<String, String>();
        tmProductoDet = new tableModel();
        tmProductoDet.init("producto_det");

        mapAjusteDet = new HashMap<String, String>();
        tmAjusteDet = new tableModel();
        tmAjusteDet.init("transferencias_producto_det");
        //setLocationRelativeTo(null);
        construirTabla();

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        dateIns = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        jDateFecha.setDate(new Date());
        jDateFechaRecibido.setDate(new Date());

        jtDetalle.addMouseListener(this);
        jtDetalle.addKeyListener(this);
        this.tfcodbarra.addKeyListener(this);
        this.tfcodbarra.addMouseListener(this);
        jtDetalle.setOpaque(false);
        jtDetalle.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "selectNextColumnCell");
        Map<String, String> select = new HashMap<String, String>();
        Map<String, String> where = new HashMap<String, String>();
        currentField = "";
        currentTable = "";
    }

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
        titulosList.add("Descripción");
        titulosList.add("Cantidad Pedido");
        titulosList.add("Cantidad Enviado");

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
    }

    private ArrayList<pojoTransferenciaDetalle> consultarListaDetalles() {
        //ArrayList<pojoCompraDetalle> lista = new ArrayList<>();
        this.lista.add(new pojoTransferenciaDetalle(0, "0", "Descripcion", 0, 0)); //cambiar pojo
        //productoid, "cod_barra", colorid, disenoid, tamanoid, uxb, stock
        return lista;
    }

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
            informacion[x][1] = listaDetalles.get(x).getString("Descripcion");
            informacion[x][2] = listaDetalles.get(x).getInteger("cantidad_pedido") + "";
            informacion[x][3] = listaDetalles.get(x).getInteger("cantidad_envio") + "";

        }
        return informacion;
    }

    private void construirTabla(String[] titulos, Object[][] data) {
        ArrayList<Integer> noEditable = new ArrayList<Integer>();
        noEditable.add(1);
        if (ped_env == 0) {
            noEditable.add(3);
        } else {
            noEditable.add(2);
        }

        modelo = new ModeloTabla(data, titulos, noEditable);
        //se asigna el modelo a la tabla
        jtDetalle.setModel(modelo);

        filasTabla = jtDetalle.getRowCount();
        columnasTabla = jtDetalle.getColumnCount();

        //se asigna el tipo de dato que tendrán las celdas de cada columna definida respectivamente para validar su personalización
        jtDetalle.getColumnModel().getColumn(0).setCellRenderer(new GestionCeldas("texto"));
        jtDetalle.getColumnModel().getColumn(1).setCellRenderer(new GestionCeldas("texto"));
        jtDetalle.getColumnModel().getColumn(2).setCellRenderer(new GestionCeldas("numerico"));
        jtDetalle.getColumnModel().getColumn(3).setCellRenderer(new GestionCeldas("numerico"));

        //se recorre y asigna el resto de celdas que serian las que almacenen datos de tipo texto
        /*for (int i = 0; i < titulos.length-5; i++) {//se resta 5 porque las ultimas 5 columnas se definen arriba
                System.out.println(i);
                jtDetalle.getColumnModel().getColumn(i).setCellRenderer(new GestionCeldas("texto"));
        }*/
        jtDetalle.getTableHeader().setReorderingAllowed(false);
        jtDetalle.setRowHeight(25);//tamaño de las celdas
        jtDetalle.setGridColor(new java.awt.Color(0, 0, 0));
        //Se define el tamaño de largo para cada columna y su contenido
        jtDetalle.getColumnModel().getColumn(0).setPreferredWidth(80);//cod_barra
        jtDetalle.getColumnModel().getColumn(1).setPreferredWidth(200);//descripcion
        jtDetalle.getColumnModel().getColumn(2).setPreferredWidth(100);//precio
        jtDetalle.getColumnModel().getColumn(3).setPreferredWidth(100);//precio

        //personaliza el encabezado
        JTableHeader jtableHeader = jtDetalle.getTableHeader();
        jtableHeader.setDefaultRenderer(new GestionEncabezadoTabla());
        jtDetalle.setTableHeader(jtableHeader);

        //se asigna la tabla al scrollPane
        //scrollPaneTabla.setViewportView(jtDetalle);
    }

    private void validarSeleccionMouse(int fila) {
        this.filaSeleccionada = fila;
        //teniendo la fila entonces se obtiene el objeto correspondiente para enviarse como parammetro o imprimir la información
        pojoTransferenciaDetalle rowDetalle = new pojoTransferenciaDetalle();
        rowDetalle.setString("cod_barra", jtDetalle.getValueAt(fila, 0).toString());
        rowDetalle.setString("descripcion", jtDetalle.getValueAt(fila, 1).toString());

        String info = "INFO PERSONA\n";
        info += "Código: " + rowDetalle.getString("cod_barra") + "\n";
        info += "Descripción: " + rowDetalle.getString("descripcion") + "\n";
    }

    public int getProducto(int row, int col) {

        //recorrer tabla de arriba a abajo y verircar si ya no existe el cod_barra
        String codbar, sql, sqll, sqlll, bruto, iddeposito;
        codbar = "";
        sql = "";

        double iva, totaliva, total = 0, totalneto, totalbruto, base, preciobruto, precioneto = 0, impuesto = 0;

        iddeposito = Tools.ExtraeCodigo(jcbDeposito.getSelectedItem().toString());

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
                + "p.descripcion, ' - ', m.nombre_marca, ' - ', "
                + "c.color, ' - ', t.tamano, ' - ', s.diseno) AS descripcion "
                + "FROM SYS_PRODUCTOS p, SYS_PRODUCTO_DET d, SYS_MARCAS m, SYS_COLORES c, SYS_tamanos t, sys_disenos s "
                + "WHERE p.id = d.productoid "
                + "AND p.marca = m.cod_marca "
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        tfid = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        Transferencia = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        documento = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jDateFecha = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        jDateFechaRecibido = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        jcbDeposito = new javax.swing.JComboBox<>();
        jcbDepositoo = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        tfcodbarra = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtDetalle = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Pedido de Transferencia de Producto");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Cabecera"));

        tfid.setText("0");
        tfid.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfidFocusGained(evt);
            }
        });
        tfid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfidActionPerformed(evt);
            }
        });
        tfid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfidKeyPressed(evt);
            }
        });

        jLabel1.setText("ID");

        jLabel2.setText("Transferencia");

        jLabel3.setText("Nro Documento");

        jLabel4.setText("Fecha Envío");

        jLabel5.setText("Fecha Recibido");

        jLabel6.setText("Depósito Origen");

        jcbDeposito.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Seleccione Depósito" }));

        jcbDepositoo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Seleccione Depósito" }));
        jcbDepositoo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbDepositooActionPerformed(evt);
            }
        });

        jLabel7.setText("Depósito Destino");

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(Transferencia)
                            .addComponent(tfid)
                            .addComponent(documento)
                            .addComponent(jDateFecha, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jDateFechaRecibido, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jcbDeposito, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfcodbarra, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcbDepositoo, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(94, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tfid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Transferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(documento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jDateFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(jDateFechaRecibido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jcbDeposito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jcbDepositoo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfcodbarra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalle"));

        jtDetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Cod Barra", "Descripción", "Cantidad Pedido", "Cantidad Envío"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tfidKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfidKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.imBuscar();
        }            // TODO add your handling code here:
    }//GEN-LAST:event_tfidKeyPressed

    private void jcbDepositooActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbDepositooActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbDepositooActionPerformed

    private void tfidFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfidFocusGained
        this.currentField = "id";     
// TODO add your handling code here:
    }//GEN-LAST:event_tfidFocusGained

    private void jtDetalleFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtDetalleFocusGained
    currentTable = "tabla";    
    validarCombo();// TODO add your handling code here:
    }//GEN-LAST:event_jtDetalleFocusGained

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
        } // TODO add yo        // TODO add your handling code here:
    }//GEN-LAST:event_tfcodbarraKeyPressed

    private void tfcodbarraKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfcodbarraKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tfcodbarraKeyReleased

    private void tfidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfidActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfidActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Transferencia;
    private javax.swing.JTextField documento;
    private com.toedter.calendar.JDateChooser jDateFecha;
    private com.toedter.calendar.JDateChooser jDateFechaRecibido;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> jcbDeposito;
    private javax.swing.JComboBox<String> jcbDepositoo;
    private javax.swing.JTable jtDetalle;
    private javax.swing.JTextField tfcodbarra;
    private javax.swing.JTextField tfid;
    // End of variables declaration//GEN-END:variables

    @Override
    public void mouseClicked(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
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
                //  this.setTotalGral();
            }

            //System.out.println("Col "+col+ " key "+key);
            if (col == 3 && key == 10 && (row == (rows - 1))) { //Si está en la última columna y presiona enter, inserta una nueva fila
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

    public void limpiarCelda(JTable tabla) {
        tabla.setValueAt("", tabla.getSelectedRow(), tabla.getSelectedColumn());
    }

    @Override
    public void keyReleased(KeyEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        if (tfid.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos deben ser completados ");
            return;
        }
        this.setData();
        ArrayList<Map<String, String>> alCabecera;         //Declara array de Map, cada Map es para un registro
        alCabecera = new ArrayList<Map<String, String>>(); //Instancia array
        int id, rows = 0;
        id = Integer.parseInt(this.tfid.getText());
        if (id > 0) {
            alCabecera.add(this.myData);                      //agrega el Map al array, para la cabecera será el mejor de los casos, es decir 1 registro 
            int rowsAffected = this.tc.updateReg(alCabecera); //Aquí estaba mal, tcdet no es

            //si rowsaffected < 1 return
            if (rowsAffected < 1) {
                msg = "Error al intentar actualizar el registro: " + this.tfid.getText();
            } else {
                id = rowsAffected;
                msg = "Se ha actualizado exitosamente el registro: " + id;
            }
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
        } else {
            rows = this.tc.createReg(this.myData);
            //id = this.tc.getMaxId();
            if (rows < 1) {
                msg = "Error al intentar actualizar el registro: " + this.tfid.getText();
            } else {
                id = rows;
                msg = "Se ha Creado exitosamente el registro: " + id;
            }
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
            //si rows es menor 1 entonces no grabo return
        }

        System.out.println("id encontrado es: " + id);
        //si pasó quiere decir que tenemos cabecera y recorremos el detalle
        Map<String, String> where = new HashMap<String, String>();      //Por qué campo buscar los registros
        Map<String, String> fields = new HashMap<String, String>();     //Los campos que vamos a recuperar
        ArrayList<Map<String, String>> alDetalle;                      //Declara array de Map, cada Map es para un registro

        fields.put("*", "*");

        for (Map<String, String> myRow : columnData) {
            where.clear();
            where.put("transferenciaid", id + "");
            where.put("cod_barra", myRow.get("cod_barra"));        //03/08/22 agregado
            this.colDat = this.tcdet.searchListById(fields, where);//verificar tablaModel 407 cuando no existe el reg
            if (this.colDat.isEmpty()) {
                myRow.put("transferenciaid", id + "");
                rows = this.tcdet.createReg(myRow);
                //if rows < 1 no creo debes hacer el control aquí
            } else {
                alDetalle = new ArrayList<Map<String, String>>();
                myRow.put("transferenciaid", id + "");      //asignamos el id de la cabecera como el fk del detalle
                alDetalle.add(myRow);
                int affected = this.tcdet.updateReg(alDetalle);   //Recordar que el modelo sólo procesa de a uno los registros
                //controlar affected, no olvidar
            }
        }
        imNuevo();
        // this.fillView(myData, columnData);
    }

    @Override
    public void imFiltrar() {
        String sql;
        sql = "";
        wBuscar frame = null;
        if (currentField.equals("id")){
        sql = "SELECT id AS codigo, "
                + "CONCAT(transferencia, ' ',nro_documento) AS descripcion "
                + "FROM sys_transferencias_producto "
                + "WHERE LOWER(CONCAT(transferencia, ' ',nro_documento)) LIKE '%";

        frame = new wBuscar(sql, this.tfid);
        }
         if (currentTable.equals("tabla")) { //para buscar un producto por filtro
            sql = "SELECT d.cod_barra as codigo, "
                    + "CONCAT(p.nombre, ' - ', "
                    + "p.descripcion, ' - ', m.nombre_marca, ' - ', "
                    + "c.color, ' - ', t.tamano, ' - ', s.diseno) AS descripcion "
                    + "FROM SYS_PRODUCTOS p, SYS_PRODUCTO_DET d, SYS_MARCAS m, SYS_COLORES c, SYS_tamanos t, sys_disenos s "
                    + "WHERE p.marca = m.cod_marca "
                    + "AND d.colorid = c.id "
                    + "AND d.tamanoid = t.id "
                    + "AND d.disenoid = s.id "
                    + "AND p.id = d.productoid "
                    + "AND LOWER(CONCAT(p.descripcion, ' ', m.nombre_marca, ' ',c.color, ' ', t.tamano, ' ', s.diseno, ' ',p.nombre)) LIKE '%";
            //System.out.println("ENVIAMOS " + sql);
            frame = new wBuscar(sql, this.tfcodbarra);
        }
        frame.setVisible(true);
        wPrincipal.desktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.OK_OPTION);
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
            myRow.put("transferenciaid", myData.get("id"));      //asignamos el id de la cabecera como el fk del detalle
            alDetalle.add(myRow);
        }
        // int affected = this.tcdet.updateReg(alDetalle);   //Recordar que el modelo sólo procesa de a uno los registros

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
            myRow.put("transferenciaid", myData.get("id"));      //asignamos el id de la cabecera como el fk del detalle
            alDetalle.add(myRow);
        }
        int affected = this.tcdet.deleteReg(alDetalle);   //Recordar que el modelo sólo procesa de a uno los registros
        ArrayList<Map<String, String>> alCabecera;         //Declara array de Map, cada Map es para un registro
        alCabecera = new ArrayList<Map<String, String>>(); //Instancia array
        alCabecera.add(myData);                           //agrega el Map al array, para la cabecera será el mejor de los casos, es decir 1 registro 
        int rowsAffected = this.tc.deleteReg(alCabecera); //Está guardando igual si en el detalle hay error
        //Invocamos el método deleteReg del Modelo que procesa un array
        if (rowsAffected <= 0) {
            String msg = "NO SE HA PODIDO ELIMINAR EL REGISTRO: " + tfid.getText();
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
        }
        if (rowsAffected > 0) {
            String msg = "EL REGISTRO: " + tfid.getText() + " SE HA ELIMINADO CORRECTAMENTE";
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);

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
        where.put("transferenciaid", this.myData.get("id"));
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
        this.myData = this.tc.navegationReg(tfid.getText(), "FIRST");
        this.fillView(this.myData, columnData);
        this.setData();
        imBuscar();
    }

    @Override
    public void imSiguiente() {
        this.myData = this.tc.navegationReg(tfid.getText(), "NEXT");
        this.fillView(this.myData, columnData);
        this.setData();
        imBuscar();
    }

    @Override
    public void imAnterior() {
        this.myData = this.tc.navegationReg(tfid.getText(), "PRIOR");
        this.fillView(this.myData, columnData);
        this.setData();
        imBuscar();
    }

    @Override
    public void imUltimo() {
        this.myData = this.tc.navegationReg(tfid.getText(), "LAST");
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
            modelo.addRow(new Object[]{"", "0.0", "0.0", "0.0", "0.0", "0.0", "0.0"});
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
            modelo.addRow(new Object[]{"0", "Descripcion", "0.0", "0.0", "0.0", "0.0", "0.0"});
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void imCerrar() {
        setVisible(false);
        dispose();
    }

    private void setData() {
        java.util.Date df = new java.util.Date();
        String fecha = "";
        String fecharecibo = "";
        //cabecera
        fecha = (jDateFecha.getDate().getTime() / 1000L) + "";
        myData.put("fecha_envio", fecha);

        fecharecibo = (jDateFechaRecibido.getDate().getTime() / 1000L) + "";
        myData.put("fecha_recibo", fecharecibo);

        myData.put("id", tfid.getText());
        myData.put("transferencia", Transferencia.getText());
        myData.put("nro_documento", documento.getText());
        myData.put("depositoid_origen", Tools.ExtraeCodigo(jcbDeposito.getSelectedItem().toString()));
        myData.put("depositoid_destino", Tools.ExtraeCodigo(jcbDepositoo.getSelectedItem().toString()));

        int estado = 0;
        myData.put("aprobado", estado + "");

        //Recorre el detalle y guarda cada fila
        //columndata ya se procesó con el metodo setTotalGral
        //Recorre el detalle y guarda cada fila
        //columndata ya se procesó con el metodo setTotalGral
        System.out.println("myData " + myData);
        //  this.setTotalGral();

        /* if(jtfId.getText() == "0"){
             String msg = "POR FAVOR INGRESE UN PRODUCTO ";
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
        } */
        //DETALlE
        int row = this.jtDetalle.getRowCount();
        int i;
        String codbar, sql;
        codbar = "";
        sql = "";

        Map<String, String> rtn = new HashMap<String, String>();
        Map<String, String> select = new HashMap<String, String>();
        Map<String, String> where = new HashMap<String, String>();
        columnData.clear();
        int rows = this.jtDetalle.getRowCount();

        for (row = 0; row < rows; row++) {
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
            where.put("id", this.mapAjusteDet.get("transferenciaid"));

            double pedido = Tools.sGetDecimalStringAnyLocaleAsDouble(this.jtDetalle.getModel().getValueAt(row, 2).toString());
            double enviado = Tools.sGetDecimalStringAnyLocaleAsDouble(this.jtDetalle.getModel().getValueAt(row, 3).toString());

            myDet = new HashMap<String, String>();
            String id = tfid.getText();
            myDet.put("transferenciaid", id);
            myDet.put("cod_barra", codbar);
            myDet.put("descripcion", this.jtDetalle.getModel().getValueAt(row, 1).toString());
            myDet.put("cantidad_pedido", pedido + "");
            myDet.put("cantidad_envio", enviado + "");

            this.columnData.add(this.myDet);

            System.out.println("myDet " + myDet);

            System.out.println("pedido " + pedido);
            System.out.println("recibo " + enviado);
        }

    }//fin setData

    private void resetData() {
        this.myData = new HashMap<String, String>();
        java.util.Date df = new java.util.Date();
        this.myData.put("id", "0");
        this.myData.put("transferencia", "");
        this.myData.put("nro_documento", "0");
        this.myData.put("aprobado", "0");
        this.myData.put("depositoid_origen", "0");
        this.myData.put("depositoid_destino", "0");

        //Detalle
//        DefaultTableModel dm = (DefaultTableModel) this.jtDetalle.getModel();
//        dm.getDataVector().removeAllElements();
        this.myDet = new HashMap<String, String>();
        this.myDet.put("ajusteid", "0");
        this.myDet.put("cod_barra", "");
        this.myDet.put("descripcion", "");
        this.myDet.put("cantidad_pedido", "0");
        this.myDet.put("cantidad_origen", "0");

        this.columnData.add(this.myDet);

        //fillView(myData, columnData);
        //jC.setSelected(false);
    }//fin reset data

    private void fillView(Map<String, String> data, List<Map<String, String>> colData) {
        Date df;
        long dateLong;
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey(); //end for
            String value = entry.getValue();
//            System.out.println("fecha "+dateTimeFormat.format(value.toString()));
            switch (key) {
                case "id":
                    tfid.setText(value);
                    break;
                case "transferencia":
                    Transferencia.setText(value);
                    break;
                case "nro_documento":
                    Transferencia.setText(value);
                    break;
                case "depositoid_origen":
                    Tools.E_estado(jcbDeposito, "sys_depositos", "id=" + value);
                    break;
                case "depositoid_destino":
                    Tools.E_estado(jcbDepositoo, "sys_depositos", "id=" + value);
                    break;
                case "fecha_envio":
                    dateLong = Long.parseLong(value) * 1000L;
                    df = new Date(dateLong);
                    jDateFecha.setDate(df);
                    break;
                case "fecha_recibido":
                    dateLong = Long.parseLong(value) * 1000L;
                    df = new Date(dateLong);
                    jDateFechaRecibido.setDate(df);
                    break;

            }//end switch
        }//end CABECERA   

        //DETALLE    
        int row;
        row = 0;
        for (Map<String, String> myRow : columnData) {
            //this.imInsDet();  
            this.modelo.addRow(new Object[]{
                myRow.get("cod_barra"),
                "Descripcion",
                myRow.get("cantidad_pedido"),
                myRow.get("cantidad_envio")
            });
            this.jtDetalle.getSelectionModel().setSelectionInterval(row, 0);
            int exist = this.getProducto(row, 0);
            row++;
        }//end for 2
    }//end fill
  public void validarCombo(){
           int codigo = 0;
            codigo = Integer.parseInt(Tools.ExtraeCodigo(jcbDeposito.getSelectedItem().toString()));            
            if (codigo == 0){
            this.jcbDeposito.requestFocus();
            JOptionPane.showMessageDialog(this, "Favor Seleccione Depósito Origen!", "¡A T E N C I O N!", JOptionPane.WARNING_MESSAGE);
            return;
            }
            
            codigo = Integer.parseInt(Tools.ExtraeCodigo(jcbDepositoo.getSelectedItem().toString()));
            if (codigo == 0){
            this.jcbDepositoo.requestFocus();
            JOptionPane.showMessageDialog(this, "Favor Seleccione Depósito Destino!", "¡A T E N C I O N!", JOptionPane.WARNING_MESSAGE);
            return;
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
