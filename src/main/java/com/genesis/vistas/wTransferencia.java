package com.genesis.vistas;

import com.genesis.model.conexion;
import com.genesis.model.pojoTransferenciaDetalle;
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
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class wTransferencia extends javax.swing.JInternalFrame implements MouseListener, KeyListener, ActiveFrame {

    private Map<String, String> myData;
    private HashMap<String, String> myDet;
    private tableController tc;
    private tableController tcdet;

    ArrayList<pojoTransferenciaDetalle> lista;// = new ArrayList<>(); //cambiar pojoo

    ArrayList<pojoTransferenciaDetalle> listaDetalles;//lista que simula la información de la BD //cambiar pojoo

    ModeloTabla modelo;//modelo definido en la clase ModeloTabla

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
    String menuName = "";
    String CRUD = "";
    private DateFormat dateFormat, dateTimeFormat, dateIns; //= new SimpleDateFormat("dd/MM/yyyy HH:mm"); 
    private int ped_env;

    /**
     * Creates new form wTransferencia
     * @param pe pedido o confirmar
     * @param menuName menu ejecutado desde wPrincipal
     */
    public wTransferencia(int pe, String menuName) {
        initComponents();
        this.menuName = menuName;
        this.ped_env = pe;
        listaDetalles = new ArrayList<>();
        lista = new ArrayList<>();
        myData = new HashMap<>();
        columnData = new ArrayList<>();

        ComboBox.pv_cargar(jcbDepositoOrigen, "depositos", " id, nombre ", "id", "");
        ComboBox.pv_cargar(jcbDepositoDestino, "depositos", " id, nombre ", "id", "");

        tc = new tableController();
        tc.init("transferencias_stock");
        tcdet = new tableController();
        tcdet.init("transferencia_stock_detalle");

        //PARA EL DETALLE
        mapProducto = new HashMap<>();
        tmProducto = new tableModel();
        tmProducto.init("productos");

        mapProductoDet = new HashMap<>();
        tmProductoDet = new tableModel();
        tmProductoDet.init("producto_detalle");

        mapAjusteDet = new HashMap<>();
        tmAjusteDet = new tableModel();
        tmAjusteDet.init("transferencia_stock_detalle");
        construirTabla();

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        dateIns = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        jDateFechaEnviado.setDate(new Date());
        jDateFechaRecibido.setDate(new Date());

        jtDetalle.addMouseListener(this);
        jtDetalle.addKeyListener(this);
        this.tfcodbarra.addKeyListener(this);
        this.tfcodbarra.addMouseListener(this);
        jtDetalle.setOpaque(false);
        jtDetalle.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "selectNextColumnCell");
        Map<String, String> select = new HashMap<>();
        Map<String, String> where = new HashMap<>();
        currentField = "";
        currentTable = "";
    }

    private void construirTabla() {
        
        listaDetalles = consultarListaDetalles();
        
        ArrayList<String> titulosList = new ArrayList<>();

        titulosList.add("Cod Barra");
        titulosList.add("Descripción");
        titulosList.add("Cantidad Pedido");
        titulosList.add("Cantidad Enviado");

        String titulos[] = new String[titulosList.size()];

        for (int i = 0; i < titulos.length; i++) {
            titulos[i] = titulosList.get(i);
        }
        
        Object[][] data = obtenerMatrizDatos(titulosList);
        construirTabla(titulos, data);
    }

    private ArrayList<pojoTransferenciaDetalle> consultarListaDetalles() {
        //ArrayList<pojoCompraDetalle> lista = new ArrayList<>();
        this.lista.add(new pojoTransferenciaDetalle(0, "0", "Descripcion", 0, 0));
        
        return lista;
    }

    private Object[][] obtenerMatrizDatos(ArrayList<String> titulosList) {

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
        ArrayList<Integer> noEditable = new ArrayList<>();
        noEditable.add(1);
        if (ped_env == 0) {
            noEditable.add(3);
            jChAprobado.setEnabled(false);
        } else {
            noEditable.add(2);
        }

        modelo = new ModeloTabla(data, titulos, noEditable);
        //se asigna el modelo a la tabla
        jtDetalle.setModel(modelo);

        //se asigna el tipo de dato que tendrán las celdas de cada columna definida respectivamente para validar su personalización
        jtDetalle.getColumnModel().getColumn(0).setCellRenderer(new GestionCeldas("texto"));
        jtDetalle.getColumnModel().getColumn(1).setCellRenderer(new GestionCeldas("texto"));
        jtDetalle.getColumnModel().getColumn(2).setCellRenderer(new GestionCeldas("numerico"));
        jtDetalle.getColumnModel().getColumn(3).setCellRenderer(new GestionCeldas("numerico"));

        jtDetalle.getTableHeader().setReorderingAllowed(false);
        jtDetalle.setRowHeight(25);//tamaño de las celdas
        jtDetalle.setGridColor(new java.awt.Color(0, 0, 0));
        //Se define el tamaño de largo para cada columna y su contenido
        jtDetalle.getColumnModel().getColumn(0).setPreferredWidth(50);//cod_barra
        jtDetalle.getColumnModel().getColumn(1).setPreferredWidth(250);//descripcion
        jtDetalle.getColumnModel().getColumn(2).setPreferredWidth(100);//precio
        jtDetalle.getColumnModel().getColumn(3).setPreferredWidth(100);//precio

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
        jtfId = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jtfMotivo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jtfNroDocumento = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jDateFechaEnviado = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        jDateFechaRecibido = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        jcbDepositoOrigen = new javax.swing.JComboBox<>();
        jcbDepositoDestino = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        tfcodbarra = new javax.swing.JTextField();
        jChAprobado = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtDetalle = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Pedido de Transferencia de Producto");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Cabecera"));

        jtfId.setText("0");
        jtfId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtfIdFocusGained(evt);
            }
        });
        jtfId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfIdActionPerformed(evt);
            }
        });
        jtfId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfIdKeyPressed(evt);
            }
        });

        jLabel1.setText("ID");

        jLabel2.setText("Motivo");

        jLabel3.setText("Nro Documento");

        jLabel4.setText("Fecha Envío");

        jLabel5.setText("Fecha Recibido");

        jLabel6.setText("Depósito Origen");

        jcbDepositoOrigen.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Seleccione Depósito" }));

        jcbDepositoDestino.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Seleccione Depósito" }));
        jcbDepositoDestino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbDepositoDestinoActionPerformed(evt);
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

        jChAprobado.setText("Aprobado");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jDateFechaRecibido, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jcbDepositoOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jtfMotivo)
                                    .addComponent(jtfId)
                                    .addComponent(jtfNroDocumento)
                                    .addComponent(jDateFechaEnviado, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jcbDepositoDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(44, 44, 44)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfcodbarra, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jChAprobado))))
                .addContainerGap(162, Short.MAX_VALUE))
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
                    .addComponent(jtfMotivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jChAprobado))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfNroDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jDateFechaEnviado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(jDateFechaRecibido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jcbDepositoOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jcbDepositoDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfcodbarra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
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
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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

    private void jtfIdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfIdKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.imBuscar();
        }            // TODO add your handling code here:
    }//GEN-LAST:event_jtfIdKeyPressed

    private void jcbDepositoDestinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbDepositoDestinoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbDepositoDestinoActionPerformed

    private void jtfIdFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfIdFocusGained
        this.currentField = "id";     
// TODO add your handling code here:
    }//GEN-LAST:event_jtfIdFocusGained

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

    private void jtfIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfIdActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jChAprobado;
    private com.toedter.calendar.JDateChooser jDateFechaEnviado;
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
    private javax.swing.JComboBox<String> jcbDepositoDestino;
    private javax.swing.JComboBox<String> jcbDepositoOrigen;
    private javax.swing.JTable jtDetalle;
    private javax.swing.JTextField jtfId;
    private javax.swing.JTextField jtfMotivo;
    private javax.swing.JTextField jtfNroDocumento;
    private javax.swing.JTextField tfcodbarra;
    // End of variables declaration//GEN-END:variables

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("mouseClicked 524 source: " + e.getSource());
        //Verificar si se ejecuto desde Tabla o jtfCodigo
        Object source = e.getSource();
        if (source == jtDetalle) {
            //Capturamos fila y columna del evento click e.getPoint()
            int fila = jtDetalle.rowAtPoint(e.getPoint());
            int columna = jtDetalle.columnAtPoint(e.getPoint());
            System.out.println("mouseClicked tabla: fila: " + fila + " columna: " + columna);
            //si columna Seleccionada corresponde a Cod_barra
            if (columna == 0) { //0 corresponde a codigo barra
                validarSeleccionMouse(fila);
            }
        }
        if (source == tfcodbarra) {
            System.out.println("el click se realizo en jTextFieldCodBarra");
        }
    } // Fin mouseClicked
    
    /**
     * Esta funcion falta modificar y deberia capturar los datos de la fila y
     * cargarlos a un mapa para manejar los datos la row en un mapa
     *
     * @param fila
     */
    private void validarSeleccionMouse(int fila) {
        //se puede mostrar en consola el codigo de barras y descripcion del producto al seleccionar
        //con el mouse una fila de la tabla
        pojoTransferenciaDetalle rowDetalle = new pojoTransferenciaDetalle();
        rowDetalle.setString("cod_barra", jtDetalle.getValueAt(fila, 0).toString());
        rowDetalle.setString("descripcion", jtDetalle.getValueAt(fila, 1).toString());

        String info = "INFO DETALLE\n";
        info += "Código: " + rowDetalle.getString("cod_barra") + "\n";
        info += "Descripción: " + rowDetalle.getString("descripcion") + "\n";
        System.out.println("validarSeleccionMouse558: " + info);
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
    public void keyReleased(KeyEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            boolean celdasNumericas = (col == 2 || col == 3);
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
                }
            }

            //Si Enter o Tab para col == 0 Codigo Barra
            if (col == 0 && (enter || tabulacion)) {
                jtDetalle.getCellEditor().stopCellEditing();
                this.getProducto(row, col);
                return;
            }

            //Enter para Ultima Columna. si ultima fila de la tabla Inserta una nueva fila
            if (col == 3 && enter && lastRow) {
                //Verifica que se haya ingresado cod_barra Correcto en la fila
                String cod = this.jtDetalle.getValueAt(row, 0).toString();
                if (cod.equals("0") || cod.equals("")) {
                    JOptionPane.showMessageDialog(this, "keyPressed960: Favor ingrese un producto Valido!", "¡A T E N C I O N!", JOptionPane.INFORMATION_MESSAGE);

                } else {
                    this.imInsDet();
                }
            }
        }
    } //Fin KeyPressed

    public void limpiarCelda(JTable tabla) {
        tabla.setValueAt("", tabla.getSelectedRow(), tabla.getSelectedColumn());
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
        //Cargar Datos en los Maps e inicializar estructuras de datos para el proceso
        this.setData();
        int idAjuste = Integer.parseInt(this.jtfId.getText());
        if (idAjuste > 0) {
            this.imActualizar("U");
        }
        if (idAjuste == 0) {
            int rowsAffected = this.tc.createReg(this.myData);
            if (rowsAffected <= 0) {
                msg = "Error al intentar crear el registro";
                JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
                return;
            } else {
                msg = "Se ha creado exitosamente el registro";
                JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.INFORMATION_MESSAGE);
                this.jtfId.requestFocus();
                idAjuste = this.tc.getMaxId();
            }
        } //Fin Cabecera
        
        //DETALLES--------------------------------------------------------
        //si pasó quiere decir que tenemos cabecera y recorremos el detalle
        Map<String, String> where = new HashMap<>();      //Por qué campo buscar los registros
        Map<String, String> fields = new HashMap<>();     //Los campos que vamos a recuperar
        ArrayList<Map<String, String>> alDetalle;         //Declara array de Map, cada Map es para un registro
        fields.put("*", "*");

        for (Map<String, String> myRow : columnData) {
            where.put("transferenciaid", idAjuste + "");
            where.put("cod_barra", myRow.get("cod_barra"));
            this.colDat = this.tcdet.searchListById(fields, where);

            myRow.put("transferenciaid", idAjuste + "");

            if (this.colDat.isEmpty()) {   // si no existe un detalle con este cod_barra para esta compra
                myRow.put("id", "0");
                int rowsAffected = this.tcdet.createReg(myRow);
                if (rowsAffected <= 0) {
                    msg = "No se ha podido grabar el Detalle Codigo:" + myRow.get("cod_barra");
                    JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
                } else {
                    msg = "Se ha creado el Detalle:" + myRow.get("cod_barra") + "para este producto";
                    JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.INFORMATION_MESSAGE);
                }
            } else { //si ya existe un detalle con este cod_barra para esta compra
                myRow.put("id", colDat.get(0).get("id"));
                alDetalle = new ArrayList<>(); //necesitamos el alDetalle por la estructura de la funcion tcdet.updateReg
                alDetalle.add(myRow);
                int rowsAffected = this.tcdet.updateReg(alDetalle);   //Recordar que el modelo sólo procesa de a uno los registros
                if (rowsAffected <= 0) {
                    msg = "No se ha podido actualizar el detalle: " + myRow.get("cod_barra") + " del producto. Por favor verifique";
                    JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
                    return;
                } else {
                    msg = "Ya existe un Detalle con este codigo de barra:" + myRow.get("cod_barra") + "para este producto";
                    JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
                }
            }
        }
        this.imNuevo();
    }//fin imGrabar
    
    @Override
    public void imActualizar(String crud) {
        this.CRUD = crud;
        String msg;
        
        if (Tools.validarPermiso(conexion.getGrupoId(), menuName, crud) == 0) {
            msg = "NO TIENE PERMISO PARA REALIZAR ESTA OPERACIÓN ";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
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
        //DETALLES--------------------------------------------------------
        //si pasó quiere decir que tenemos cabecera y recorremos el detalle
        Map<String, String> where = new HashMap<>();      //Por qué campo buscar los registros
        Map<String, String> fields = new HashMap<>();     //Los campos que vamos a recuperar
        ArrayList<Map<String, String>> alDetalle;         //Declara array de Map, cada Map es para un registro
        fields.put("*", "*");
        int idTransferencia = Integer.parseInt(this.jtfId.getText());
        rowsAffected = 0;
        for (Map<String, String> myRow : columnData) {
            where.put("transferenciaid", idTransferencia + "");
            where.put("cod_barra", myRow.get("cod_barra"));
            this.colDat = this.tcdet.searchListById(fields, where);

            myRow.put("transferenciaid", idTransferencia + "");

            if (this.colDat.isEmpty()) {   // si no existe un detalle con este cod_barra para esta compra
                myRow.put("id", "0");
                rowsAffected = this.tcdet.createReg(myRow);
                if (rowsAffected <= 0) {
                    msg = "No se ha podido grabar el Detalle Codigo:" + myRow.get("cod_barra");
                    JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
                } else {
                    msg = "Se ha creado el Detalle:" + myRow.get("cod_barra") + "para este producto";
                    JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.INFORMATION_MESSAGE);
                }
            } else { //si ya existe un detalle con este cod_barra para esta compra
                myRow.put("id", colDat.get(0).get("id"));
                alDetalle = new ArrayList<>(); //necesitamos el alDetalle por la estructura de la funcion tcdet.updateReg
                alDetalle.add(myRow);
                rowsAffected = this.tcdet.updateReg(alDetalle);   //Recordar que el modelo sólo procesa de a uno los registros
                if (rowsAffected <= 0) {
                    msg = "No se ha podido actualizar el detalle: " + myRow.get("cod_barra") + " del producto. Por favor verifique";
                    JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
                    return;
                } else {
                    msg = "Ya existe un Detalle con este codigo de barra:" + myRow.get("cod_barra") + "para este producto";
                    JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
                }
            }
        }
        this.imNuevo();
    }//fin imActualizar
    
    @Override
    public void imBorrar(String crud) {
        this.CRUD = crud;
        String msg;
        
        if (Tools.validarPermiso(conexion.getGrupoId(), menuName, crud) == 0) {
            msg = "NO TIENE PERMISO PARA REALIZAR ESTA OPERACIÓN ";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
        }
        
        this.setData();
        ArrayList<Map<String, String>> alCabecera;         
        alCabecera = new ArrayList<>();
        alCabecera.add(myData);                          
        int rowsAffected = this.tc.deleteReg(alCabecera);
        if (rowsAffected <= 0) {
            msg = "Error al intentar crear el registro";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);
            return;
        } else {
            msg = "Se ha creado exitosamente el registro";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.INFORMATION_MESSAGE);
            this.jtfId.requestFocus();
        }
            
        //DETALLES---------------------------------
        ArrayList<Map<String, String>> alDetalle;         
        alDetalle = new ArrayList<>(); 
        //Recorremos los detalles
        for (Map<String, String> myRow : columnData) {       //hay que recorrer los detalles y enviar de a uno.
            myRow.put("transferenciaid", myData.get("id"));      //asignamos el id de la cabecera como el fk del detalle
            alDetalle.add(myRow);
        }
        rowsAffected = this.tcdet.deleteReg(alDetalle);   
        //Invocamos el método deleteReg del Modelo que procesa un array
        if (rowsAffected <= 0) {
            msg = "NO SE HA PODIDO ELIMINAR EL REGISTRO: " + jtfId.getText();
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
        }
        if (rowsAffected > 0) {
            msg = "EL REGISTRO: " + jtfId.getText() + " SE HA ELIMINADO CORRECTAMENTE";
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.DEFAULT_OPTION);

        }
        imNuevo();
    }

    @Override
    public void imFiltrar() {
        String sql;
        wBuscar frame = new wBuscar();
        if (currentField.equals("id")){
            sql = "SELECT id AS codigo, "
                    + "CONCAT(motivo, ' ',nro_documento) AS descripcion "
                    + "FROM transferencias_stock "
                    + "WHERE LOWER(CONCAT(motivo, ' ',nro_documento)) LIKE '%";
            frame = new wBuscar(sql, this.jtfId);
        }
        
        String depositoid = ComboBox.ExtraeCodigo(jcbDepositoOrigen.getSelectedItem().toString());        
         if (currentTable.equals("tabla")) { //para buscar un producto por filtro
            sql = "SELECT DISTINCT d.cod_barra as codigo, "
                    + "CONCAT(p.nombre, ' - ', "
                    + "m.nombre, ' - ', "
                    + "c.color, ' - ', t.tamano, ' - ', s.diseno) AS descripcion "
                    + "FROM PRODUCTOS p, PRODUCTO_DETALLE d, MARCAS m, COLORES c, tamanos t, disenos s, stock, depositos "
                    + "WHERE p.marca = m.id "
                    + "AND d.colorid = c.id "
                    + "AND d.tamanoid = t.id "
                    + "AND d.disenoid = s.id "
                    + "AND p.id = d.productoid "
                    + "AND stock.cod_barra = d.cod_barra "
                    + "AND stock.depositoid = '" + depositoid + "' "
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
        this.myData = this.tc.searchById(myData);
        System.out.println("AjusteStock imBuscar " + this.myData.toString());
        this.limpiarTabla();
        if (this.myData.isEmpty()) {
            System.out.println("No hay registros que mostrar");
            this.resetData();
            this.fillView(myData, columnData);
            return;
        }
        Map<String, String> where = new HashMap<>();      //Por qué campo buscar los registros
        where.put("transferenciaid", this.myData.get("id"));
        //Los campos que vamos a recuperar
        Map<String, String> fields = new HashMap<>();
        fields.put("*", "*");
        this.columnData = this.tcdet.searchListById(fields, where);
        if (this.columnData.isEmpty()) {
            this.resetData();
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
        System.out.println("wTransferencias964 imInsDet currentRow: " + currentRow);
        //si no hay fila seleccionada
        if (currentRow == -1) {
            modelo.addRow(new Object[]{"0", "Descripcion", 0, 0});
            return;
        }
        if (currentRow != -1) {
            jtDetalle.getSelectionModel().clearSelection();
        }
        //Si hay fila seleccionada
        String cod = this.jtDetalle.getValueAt(currentRow, 0).toString();
        if (cod.equals("0") || cod.equals("")) {
            String msg = "imInsDet976: POR FAVOR INGRESE UN PRODUCTO ";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
        } else {
            System.out.println("imInsDet979: Exito");
            modelo.addRow(new Object[]{"0", "Descripcion", 0, 0});

            this.jtDetalle.requestFocus(); //devolver el foco a la tabla
            //hacer foco en la col=0 de la nueva fila
            int lastRow = this.jtDetalle.getRowCount() - 1;
            this.jtDetalle.changeSelection(lastRow, 0, false, false);
            //this.jtDetalle.requestFocusInWindow(); // Asegura que el foco esté en la tabla
        }
    }

    @Override
    public void imDelDet() {
        int currentRow = jtDetalle.getSelectedRow();
        //si no hay row seleccionado
        if (currentRow == -1) { 
            String msg = "imDelDet995: NO hay fila seleccionada ";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
        }
        //si hay row seleccionado
        if (currentRow >= 0) { 
            modelo.removeRow(currentRow);
            //Si al eliminar queda vacía, habrá que insertar una nueva
            int rows = jtDetalle.getRowCount();
            if (rows == 0) {
                String msg = "imDelDet1006: Se eliminaron todas las filas ";
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
    
    public int getProducto(int row, int col) { 
        int Exito = 0;
        //recuperar cod_barra de la row y DepositoId del ComboBox cabecera
        String depositoid = ComboBox.ExtraeCodigo(jcbDepositoOrigen.getSelectedItem().toString());
        String codbar = this.jtDetalle.getModel().getValueAt(row, 0).toString(); //col == 0
        if (codbar.equals("")) { //para evitar error en el sql
            codbar = "0";
            JOptionPane.showMessageDialog(this, "Proporcione codigo para la fila", "getProducto1025", JOptionPane.OK_OPTION);
            Exito = 0;
        }
        String sql = "SELECT CONCAT(p.nombre, ' ', "
                + "m.nombre, ' , ', "
                + "c.color, ' ', t.tamano, ' ', s.diseno) AS descripcion, "
                + "stock.cantidad AS CANTIDAD_ACTUAL "
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

        try {
            rs = conexion.ejecuteSQL(sql); //Esto devuelve un ResultSet
            if (rs.next()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int colCount = metaData.getColumnCount();
                Exito = 1;
                System.out.println("getProducto INFO: ");
                System.out.println("column "+metaData.getColumnName(1)+" valor "+rs.getString("descripcion"));
                for (int r = 1; r <= colCount; r++) {                    
                    //PRODUCTO DESCRIPCION
                    String descripcion = rs.getString("descripcion");
                    this.jtDetalle.getModel().setValueAt(descripcion, row, 1); //Descripcion en jtDetalle
                    
                    //PRODUCTO CANTIDAD ACTUAL STOCK
                    int cantidad_actual = rs.getInt("CANTIDAD_ACTUAL");
                    this.jtDetalle.getModel().setValueAt(cantidad_actual, row, 2);
                }
            } else {
                this.jtDetalle.getModel().setValueAt("0", row, 0);
                this.jtDetalle.getModel().setValueAt("", row, 1);
                String msg = "No se ha encontrado Producto con el Codigo: " + codbar;
                JOptionPane.showMessageDialog(this, msg, "getProducto1065", JOptionPane.OK_OPTION);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Exito;
    } //Fin getProducto

    private void setData() {
        //CABECERA
        String fechaEnviado = (jDateFechaEnviado.getDate().getTime() / 1000L) + "";
        myData.put("fecha_envio", fechaEnviado);
        
        String fechaRecibido = (jDateFechaRecibido.getDate().getTime() / 1000L) + "";
        myData.put("fecha_recibo", fechaRecibido);

        myData.put("id", jtfId.getText());
        myData.put("deposito_origen", ComboBox.ExtraeCodigo(jcbDepositoOrigen.getSelectedItem().toString()));
        myData.put("deposito_destino", ComboBox.ExtraeCodigo(jcbDepositoDestino.getSelectedItem().toString()));
        myData.put("motivo", jtfMotivo.getText());
        myData.put("nro_documento", jtfNroDocumento.getText());

        int aprobado = 0;
        if (jChAprobado.isSelected()) { aprobado = 1; }
        myData.put("aprobado", aprobado + "");
        
        System.out.println("myData " + myData);

        //DETALLE------------------------------

        columnData.clear();
        int rows = this.jtDetalle.getRowCount();
        //recorremos todas las filas de la tabla
        for (int row = 0; row < rows; row++) {
            String codbar = this.jtDetalle.getModel().getValueAt(row, 0).toString();
            if (codbar.equals("0") || codbar.equals("")) { //Si no se especifica códio, no tiene sentido continuar
                continue;
            }
            //carputando valores de la tabla, verificando que no sean null y casteando
            Object cantidadPedido = this.jtDetalle.getValueAt(row, 2);
            Object cantidadEnviado = this.jtDetalle.getValueAt(row, 3);
            String pedido = cantidadPedido != null ? cantidadPedido.toString() : "";
            String enviado = cantidadEnviado != null ? cantidadEnviado.toString() : "";
            
            if (enviado.equals("")) {    
                JOptionPane.showMessageDialog(this, "Debe proporcionar una cantidad valida", "setData1007", JOptionPane.DEFAULT_OPTION);
                continue;
            }
            int cantEnviado = Integer.parseInt(enviado);
            if (cantEnviado < 0) {
                JOptionPane.showMessageDialog(this, "Debe proporcionar una cantidad valida", "setData1012", JOptionPane.DEFAULT_OPTION);
                continue;
            }

            myDet = new HashMap<>();
            String id = jtfId.getText();
            myDet.put("ajusteid", id);
            myDet.put("cod_barra", codbar);
            myDet.put("descripcion", this.jtDetalle.getModel().getValueAt(row, 1).toString());
            myDet.put("cantidad_pedido", pedido + "");
            myDet.put("cantidad_enviado", enviado + "");

            this.columnData.add(this.myDet);

        }

    }//fin setData

    private void resetData() {
        this.myData = new HashMap<>();
        this.myData.put("id", "0");
        this.myData.put("motivo", "Motivo de Transferencia");
        this.myData.put("nro_documento", "0");
        this.myData.put("aprobado", "0");
        this.myData.put("deposito_origen", "0");
        this.myData.put("deposito_destino", "0");
        //this.myData.put("fecha_envio", title);
        //this.myData.put("fecha_recibo", title);

        //Detalle---------------------------------
        this.myDet = new HashMap<>();
        this.myDet.put("ajusteid", "0");
        this.myDet.put("cod_barra", "");
        this.myDet.put("descripcion", "");
        this.myDet.put("cantidad_pedido", "0");
        this.myDet.put("cantidad_origen", "0");

        this.columnData.add(this.myDet);
    }//fin reset data

    private void fillView(Map<String, String> data, List<Map<String, String>> columnData) {
        Date df;
        long dateLong;
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
                case "id":
                    jtfId.setText(value);
                    break;
                case "motivo":
                    jtfMotivo.setText(value);
                    break;
                case "nro_documento":
                    jtfNroDocumento.setText(value);
                    break;
                case "deposito_origen":
                    ComboBox.E_estado(jcbDepositoOrigen, "depositos", "id, nombre", "id=" + value);
                    break;
                case "deposito_destino":
                    ComboBox.E_estado(jcbDepositoDestino, "depositos", "id, nombre", "id=" + value);
                    break;
                case "fecha_envio":
                    dateLong = Long.parseLong(value) * 1000L;
                    df = new Date(dateLong);
                    jDateFechaEnviado.setDate(df);
                    break;
                case "fecha_recibido":
                    dateLong = Long.parseLong(value) * 1000L;
                    df = new Date(dateLong);
                    jDateFechaRecibido.setDate(df);
                    break;
                case "aprobado":
                    if(Integer.parseInt(value) == 0) {
                        jChAprobado.setSelected(false);
                    } else {
                        jChAprobado.setSelected(true);
                    }
                    break;

            }//end switch
        }//end CABECERA   

       //DETALLE TABLA
        this.modelo.setRowCount(0); // Limpiar la tabla antes de llenar
        
        for (Map<String, String> myRow : columnData) { //Detalles
            // Añadir una nueva fila vacía al modelo
            this.modelo.addRow(new Object[]{"0", "Descripcion", 0, 0});
            int row = modelo.getRowCount() - 1; // Índice de la última fila añadida
            //cargar codigo de barra en la nueva fila
            this.jtDetalle.setValueAt(myRow.get("cod_barra"), row, 0);
            //verificar que el codigo de barra corresponda a un producto existente en la DB antes de continuar
            int exist = this.getProducto(row, 0);
            if(exist == 0){
                String msg = "Producto: " + myRow.get("cod_barra") + " no encontrado";
                JOptionPane.showMessageDialog(this, msg, "1211fillView Detalle!", JOptionPane.DEFAULT_OPTION);
                modelo.removeRow(row);
                continue;
            }
            this.jtDetalle.setValueAt(myRow.get("cantidad_pedido"), row, 2);
            this.jtDetalle.setValueAt(myRow.get("cantidad_enviado"), row, 3);
            row++;
        }//end for Detalle
    }//Fin FillView
    
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
        //Fecha Enviado
        Date fechaEnviado = jDateFechaEnviado.getDate();
        if (fechaEnviado == null) {
            msg = "Favor ingrese una fecha de operación válida!";
            jDateFechaEnviado.requestFocus();
            jDateFechaEnviado.setDate(new Date());
            rtn = false;
        }
        //continuar con los demás controles
        if (!rtn) {
            JOptionPane.showMessageDialog(this, msg, "¡Favor Verificar!", JOptionPane.INFORMATION_MESSAGE);
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
            }
            //CANTIDAD ENVIADO
            String cantidadEnviado = jtDetalle.getValueAt(i, 3).toString();
            if (Integer.parseInt(cantidadEnviado) < 0) {
                msg = "Cantidad no puede ser menor a 0";
                jtDetalle.changeSelection(i, 3, false, false);
                rtn = false;
            }
        }
        if (!rtn) {
            JOptionPane.showMessageDialog(this, msg, "Validacion de Campos Detalle!", JOptionPane.DEFAULT_OPTION);
        }
        return rtn;
    } //Fin validarDetalles
    
    
  public void validarCombo(){
           int codigo = 0;
            codigo = Integer.parseInt(ComboBox.ExtraeCodigo(jcbDepositoOrigen.getSelectedItem().toString()));            
            if (codigo == 0){
            this.jcbDepositoOrigen.requestFocus();
            JOptionPane.showMessageDialog(this, "Favor Seleccione Depósito Origen!", "¡A T E N C I O N!", JOptionPane.WARNING_MESSAGE);
            return;
            }
            
            codigo = Integer.parseInt(ComboBox.ExtraeCodigo(jcbDepositoDestino.getSelectedItem().toString()));
            if (codigo == 0){
            this.jcbDepositoDestino.requestFocus();
            JOptionPane.showMessageDialog(this, "Favor Seleccione Depósito Destino!", "¡A T E N C I O N!", JOptionPane.WARNING_MESSAGE);
            }
     } //Fin validarCombo
  
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

} //Fin Clase
