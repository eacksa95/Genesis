package com.genesis.vistas;

import com.genesis.model.conexion;
import com.genesis.model.pojoAjusteDetalle;
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

public class wAjusteStock extends javax.swing.JInternalFrame implements MouseListener, KeyListener, ActiveFrame {

    int tipoopcion = 0;
    
    private Map<String, String> myData;
    private HashMap<String, String> myDet;
    private tableController tc; //tableController Ajuste Stock
    private tableController tcdet; //tableController Ajuste Stock Detalle
    private tableModel tmAjuste;
    private tableModel tmAjusteDet;
    private ArrayList<Map<String, String>> columnData, colDat;
    
    ArrayList<pojoAjusteDetalle> lista;
    ArrayList<pojoAjusteDetalle> listaDetalles;
    ModeloTabla modelo; //modelo de la JTable jtDetalle

    private tableModel tmProducto;
    Map<String, String> mapProducto;
    
    private tableModel tmProductoDet;
    Map<String, String> mapProductoDet;

    String menuName = "";
    String CRUD = "";
    private DateFormat dateFormat, dateTimeFormat, dateIns; //= new SimpleDateFormat("dd/MM/yyyy HH:mm"); 
    
    private String currentTable = "";
    private String currentField = "";
    

    /**
     * Creates new form wAjusteStock
     * @param tipoopcion define si la ventana se ejecuta para solicitar ajuste de stock o para aprobarlo
     * @param menuName menu ejecutado desde wPrincipal
     */
    public wAjusteStock(int tipoopcion, String menuName) {
        initComponents();
        this.menuName = menuName;
        this.tipoopcion = tipoopcion;
        listaDetalles = new ArrayList<>();
        lista = new ArrayList<>();
        myData = new HashMap<>();
        columnData = new ArrayList<>();

        if (tipoopcion == 1) {
            jChAprobado.setEnabled(false);
            this.setTitle("Pedido de Ajuste de Stock");
        }

        if (tipoopcion == 2) {
            jChAprobado.setEnabled(true);
            this.setTitle("Aprobar Pedido de Ajuste de Stock");
        }

        // COMBO BOX DESPLEGABLES DE LAS TABLAS//
        ComboBox.pv_cargar(jcbMoneda, "monedas", " id, moneda ", "id", "");
        ComboBox.pv_cargar(jcbDeposito, "depositos", " id, nombre ", "id", "");

        tc = new tableController();
        tc.init("ajustes_stock");
        tcdet = new tableController();
        tcdet.init("ajustes_stock_detalle");

        //PARA EL DETALLE
        mapProducto = new HashMap<>();
        tmProducto = new tableModel();
        tmProducto.init("productos");

        mapProductoDet = new HashMap<>();
        tmProductoDet = new tableModel();
        tmProductoDet.init("producto_detalle");

        tmAjusteDet = new tableModel();
        tmAjusteDet.init("ajustes_stock_detalle");
        //setLocationRelativeTo(null);
        construirTabla();

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        dateIns = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        jDateFecha.setDate(new Date());

        jtDetalle.addMouseListener(this);
        jtDetalle.addKeyListener(this);
        jtDetalle.setOpaque(false);
        this.tfcodbarra.addKeyListener(this);
        this.tfcodbarra.addMouseListener(this);
        jtDetalle.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "selectNextColumnCell");
    }

    private void construirTabla() {
        listaDetalles = consultarListaDetalles();
        //Este array cambiará de valores según la tabla que querramos representar
        //en este caso nuestro detalle tiene esa estructura de columnas
        ArrayList<String> titulosList = new ArrayList<>();

        titulosList.add("Cod Barra");
        titulosList.add("Descripcion");
        titulosList.add("Cantidad Actual");
        titulosList.add("Cantidad Ajustada");

        String titulos[] = new String[titulosList.size()];

        for (int i = 0; i < titulos.length; i++) {
            titulos[i] = titulosList.get(i);
        }

        Object[][] data = obtenerMatrizDatos(titulosList);
        construirTabla(titulos, data);
    }

    private ArrayList<pojoAjusteDetalle> consultarListaDetalles() {
        //ArrayList<pojoCompraDetalle> lista = new ArrayList<>();
        this.lista.add(new pojoAjusteDetalle(0, "0", "Descripcion", 0, 0));
        return lista;
    }

    private Object[][] obtenerMatrizDatos(ArrayList<String> titulosList) {

        String informacion[][] = new String[listaDetalles.size()][titulosList.size()];

        for (int x = 0; x < informacion.length; x++) {
            //Poner los nombres de los campos de la tabla de la bd
            informacion[x][0] = listaDetalles.get(x).getString("Cod_barra");
            informacion[x][1] = listaDetalles.get(x).getString("Descripcion");
            informacion[x][2] = listaDetalles.get(x).getDouble("Cantidad_Actual") + "";
            informacion[x][3] = listaDetalles.get(x).getDouble("Cantidad_Ajuste") + "";
        }
        return informacion;
    }

    private void construirTabla(String[] titulos, Object[][] data) {
        ArrayList<Integer> noEditable = new ArrayList<>();
        noEditable.add(1);
        noEditable.add(2);
        modelo = new ModeloTabla(data, titulos, noEditable);
        //se asigna el modelo a la tabla
        jtDetalle.setModel(modelo);

        //se asigna el tipo de dato que tendrán las celdas de cada columna definida respectivamente para validar su personalización
        jtDetalle.getColumnModel().getColumn(0).setCellRenderer(new GestionCeldas("texto"));
        jtDetalle.getColumnModel().getColumn(1).setCellRenderer(new GestionCeldas("texto"));
        jtDetalle.getColumnModel().getColumn(2).setCellRenderer(new GestionCeldas("numerico"));
        jtDetalle.getColumnModel().getColumn(3).setCellRenderer(new GestionCeldas("numerico"));

        //Mas Propiedades para la tabla
        jtDetalle.getTableHeader().setReorderingAllowed(false);
        jtDetalle.setRowHeight(25);//Altura de las celdas
        jtDetalle.setGridColor(new java.awt.Color(0, 0, 0));
        //Se define el Ancho para cada columna
        jtDetalle.getColumnModel().getColumn(0).setPreferredWidth(50);//cod_barra
        jtDetalle.getColumnModel().getColumn(1).setPreferredWidth(250);//descripcion
        jtDetalle.getColumnModel().getColumn(2).setPreferredWidth(50);//Cantidad Actual
        jtDetalle.getColumnModel().getColumn(3).setPreferredWidth(50);//Cantidad Ajuste

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
        jtfId = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jcbDeposito = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        tfMotivo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jcbMoneda = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        tfObs = new javax.swing.JTextField();
        jChContabilizado = new javax.swing.JCheckBox();
        jChAprobado = new javax.swing.JCheckBox();
        jDateFecha = new com.toedter.calendar.JDateChooser();
        tfcodbarra = new javax.swing.JTextField();
        jDetalle = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtDetalle = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Ajuste Stock");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Cabecera"));

        jLabel1.setText("ID");

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

        jLabel2.setText("Fecha Ajuste");

        jLabel3.setText("Deposito");

        jcbDeposito.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Seleccione Depósito" }));

        jLabel4.setText("Motivo");

        jLabel5.setText("Moneda");

        jcbMoneda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Seleccione Moneda" }));

        jLabel6.setText("Observación");

        jChContabilizado.setText("Contabilizado");

        jChAprobado.setText("Aprobado");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(59, 59, 59)
                                    .addComponent(jLabel1))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jLabel2))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jtfId)
                            .addComponent(jcbDeposito, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tfMotivo)
                            .addComponent(jcbMoneda, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jDateFecha, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jChContabilizado)
                            .addComponent(jChAprobado)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tfObs)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfcodbarra, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtfId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jChContabilizado))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jChAprobado))
                    .addComponent(jDateFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jcbDeposito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tfMotivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jcbMoneda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(tfObs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfcodbarra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jDetalle.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalle"));

        jtDetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Cod Barra", "Descripción", "Cantidad Actual", "Cantidad Ajuste"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class
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

        javax.swing.GroupLayout jDetalleLayout = new javax.swing.GroupLayout(jDetalle);
        jDetalle.setLayout(jDetalleLayout);
        jDetalleLayout.setHorizontalGroup(
            jDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDetalleLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 632, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );
        jDetalleLayout.setVerticalGroup(
            jDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDetalleLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(243, 243, 243))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jDetalle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(265, 265, 265)))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(jDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtfIdFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfIdFocusGained
        currentField = "id";
    }//GEN-LAST:event_jtfIdFocusGained

    private void jtfIdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfIdKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.imBuscar();
        }
    }//GEN-LAST:event_jtfIdKeyPressed

    private void jtDetalleFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtDetalleFocusGained
    currentTable = "tabla";  
    validarCombo();
    }//GEN-LAST:event_jtDetalleFocusGained


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jChAprobado;
    private javax.swing.JCheckBox jChContabilizado;
    private com.toedter.calendar.JDateChooser jDateFecha;
    private javax.swing.JPanel jDetalle;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> jcbDeposito;
    private javax.swing.JComboBox<String> jcbMoneda;
    private javax.swing.JTable jtDetalle;
    private javax.swing.JTextField jtfId;
    private javax.swing.JTextField tfMotivo;
    private javax.swing.JTextField tfObs;
    private javax.swing.JTextField tfcodbarra;
    // End of variables declaration//GEN-END:variables

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
        pojoAjusteDetalle rowDetalle = new pojoAjusteDetalle();
        rowDetalle.setString("cod_barra", jtDetalle.getValueAt(fila, 0).toString());
        rowDetalle.setString("descripcion", jtDetalle.getValueAt(fila, 1).toString());

        String info = "INFO DETALLE\n";
        info += "Código: " + rowDetalle.getString("cod_barra") + "\n";
        info += "Descripción: " + rowDetalle.getString("descripcion") + "\n";
        System.out.println("validarSeleccionMouse490: " + info);
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
    public void keyReleased(KeyEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            where.put("ajusteid", idAjuste + "");
            where.put("cod_barra", myRow.get("cod_barra"));
            this.colDat = this.tcdet.searchListById(fields, where);

            myRow.put("ajusteid", idAjuste + "");

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
        int idAjuste = Integer.parseInt(this.jtfId.getText());
        rowsAffected = 0;
        for (Map<String, String> myRow : columnData) {
            where.put("ajusteid", idAjuste + "");
            where.put("cod_barra", myRow.get("cod_barra"));
            this.colDat = this.tcdet.searchListById(fields, where);

            myRow.put("ajusteid", idAjuste + "");

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
            myRow.put("ajusteid", myData.get("id"));      //asignamos el id de la cabecera como el fk del detalle
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
                    + "CONCAT(motivo, ' ',depositoid, ' ',monedaid) AS descripcion "
                    + "FROM ajustes_stock "
                    + "WHERE LOWER(CONCAT(id, ' ',motivo, ' ',monedaid)) LIKE '%";
            frame = new wBuscar(sql, this.jtfId);
        }
        
        String depositoid = ComboBox.ExtraeCodigo(jcbDeposito.getSelectedItem().toString());
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
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.DEFAULT_OPTION);
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
        where.put("ajusteid", this.myData.get("id"));
        //Los campos que vamos a recuperar
        Map<String, String> fields = new HashMap<>();
        fields.put("*", "*");
        this.columnData = this.tcdet.searchListById(fields, where);
        if (this.columnData.isEmpty()) {
            this.resetData();
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void imInsDet() {
        int currentRow = jtDetalle.getSelectedRow();
        if (currentRow == -1) {
            System.out.println("no hay fila seleccionada imInsDet 1062");
            modelo.addRow(new Object[]{"0", "Descripcion", 0, 0});
            return;
        }
        String cod = this.jtDetalle.getValueAt(currentRow, 0).toString();
        if (cod.equals("0") || cod.equals("")) {
            /*  String msg = "POR FAVOR INGRESE UN PRODUCTO ";
            System.out.println(msg);
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION); */
        } else {
            //System.out.println("entro en imInsDet");
            modelo.addRow(new Object[]{"0", "Descripcion", 0, 0});
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
        //si no hay row seleccionado
        if (currentRow == -1) { 
            String msg = "imDelDet939: NO hay fila seleccionada ";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            return;
        }
        //si hay row seleccionado
        if (currentRow >= 0) { 
            modelo.removeRow(currentRow);
            //Si al eliminar queda vacía, habrá que insertar una nueva
            int rows = jtDetalle.getRowCount();
            if (rows == 0) {
                String msg = "imDelDet949: Se eliminaron todas las filas ";
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
        String depositoid = ComboBox.ExtraeCodigo(jcbDeposito.getSelectedItem().toString());
        String codbar = this.jtDetalle.getModel().getValueAt(row, 0).toString(); //col == 0
        if (codbar.equals("")) { //para evitar error en el sql
            codbar = "0";
            JOptionPane.showMessageDialog(this, "Proporcione codigo para la fila", "getProducto927", JOptionPane.OK_OPTION);
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
                JOptionPane.showMessageDialog(this, msg, "getProducto1346", JOptionPane.OK_OPTION);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Exito;
    } //Fin getProducto

    private void setData() {
        //CABECERA
        String fecha = (jDateFecha.getDate().getTime() / 1000L) + "";
        myData.put("fecha", fecha);

        myData.put("id", jtfId.getText());
        myData.put("depositoid", ComboBox.ExtraeCodigo(jcbDeposito.getSelectedItem().toString()));
        myData.put("monedaid", ComboBox.ExtraeCodigo(jcbMoneda.getSelectedItem().toString()));
        myData.put("motivo", tfMotivo.getText());
        myData.put("observacion", tfObs.getText());

        int aprobado = 0;
        if (jChAprobado.isSelected()) { aprobado = 1; }
        myData.put("aprobado", aprobado + "");
        
        int contabilizado = 0;
        if (jChContabilizado.isSelected()) { contabilizado = 1; }
        myData.put("contabilizado", contabilizado + "");

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
            Object actualValue = this.jtDetalle.getValueAt(row, 2);
            Object ajusteValue = this.jtDetalle.getValueAt(row, 3);
            String actual = actualValue != null ? actualValue.toString() : "";
            String ajuste = ajusteValue != null ? ajusteValue.toString() : "";
            
            if (ajuste.equals("")) {    
                JOptionPane.showMessageDialog(this, "Debe proporcionar una cantidad valida", "setData1007", JOptionPane.DEFAULT_OPTION);
                continue;
            }
            int cai = Integer.parseInt(ajuste);
            if (cai < 0) {
                JOptionPane.showMessageDialog(this, "Debe proporcionar una cantidad valida", "setData1012", JOptionPane.DEFAULT_OPTION);
                continue;
            }

            myDet = new HashMap<>();
            String id = jtfId.getText();
            myDet.put("ajusteid", id);
            myDet.put("cod_barra", codbar);
            myDet.put("descripcion", this.jtDetalle.getModel().getValueAt(row, 1).toString());
            myDet.put("cantidad_actual", actual + "");
            myDet.put("cantidad_ajuste", ajuste + "");

            this.columnData.add(this.myDet);

        }

    }//fin setData

    private void resetData() {
        this.myData = new HashMap<>();
        this.myData.put("id", "0");
        this.myData.put("monedaid", "0");
        this.myData.put("depositoid", "0");
        this.myData.put("motivo", "");
        this.myData.put("observacion", "");
        this.myData.put("aprobado", "0");
        this.myData.put("contabilizado", "0");

        //Detalle---------------------
        this.myDet = new HashMap<>();
        this.myDet.put("ajusteid", "0");
        this.myDet.put("cod_barra", "");
        this.myDet.put("descripcion", "");
        this.myDet.put("cantidad_actual", "0");
        this.myDet.put("cantidad_ajuste", "0");

        this.columnData.add(this.myDet);

        //fillView(myData, columnData);
        //jC.setSelected(false);
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
                case "observacion":
                    tfObs.setText(value);
                    break;
                case "monedaid":
                    ComboBox.E_estado(jcbMoneda, "monedas", "id, moneda", "id=" + value);
                    break;
                case "depositoid":
                    ComboBox.E_estado(jcbDeposito, "depositos", "id, nombre", "id=" + value);
                    break;
                case "motivo":
                    tfMotivo.setText(value);
                    break;
                case "fecha":
                    dateLong = Long.parseLong(value) * 1000L;
                    df = new Date(dateLong);
                    jDateFecha.setDate(df);
                    break;
                case "aprobado":
                    if (Integer.parseInt(value) == 0) {
                        jChAprobado.setSelected(false);
                    } else {
                        jChAprobado.setSelected(true);
                    }
                    break;
                case "contabilizado":
                    if (Integer.parseInt(value) == 0) {
                        jChContabilizado.setSelected(false);
                    } else {
                        jChContabilizado.setSelected(true);
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
                JOptionPane.showMessageDialog(this, msg, "1145fillView Detalle!", JOptionPane.DEFAULT_OPTION);
                modelo.removeRow(row);
                continue;
            }
            this.jtDetalle.setValueAt(myRow.get("cantidad_actual"), row, 2);
            this.jtDetalle.setValueAt(myRow.get("cantidad_ajuste"), row, 3);
            row++;
        }//end for 2
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
        //Fecha Proceso
        Date fechaProceso = jDateFecha.getDate();
        if (fechaProceso == null) {
            msg = "Favor ingrese una fecha de operación válida!";
            jDateFecha.requestFocus();
            jDateFecha.setDate(new Date());
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
            //CANTIDAD AJUSTE
            String cantidadAjuste = (String) jtDetalle.getValueAt(i, 3);
            if (Integer.parseInt(cantidadAjuste) < 1) {
                msg = "Cantidad no puede ser menor a 1";
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
            codigo = Integer.parseInt(ComboBox.ExtraeCodigo(jcbDeposito.getSelectedItem().toString()));            
            if (codigo == 0){
            this.jcbDeposito.requestFocus();
            JOptionPane.showMessageDialog(this, "Favor Seleccione Depósito!", "¡A T E N C I O N!", JOptionPane.WARNING_MESSAGE);
            return;
            }
            
            codigo = Integer.parseInt(ComboBox.ExtraeCodigo(jcbMoneda.getSelectedItem().toString()));
            if (codigo == 0){
            this.jcbMoneda.requestFocus();
            JOptionPane.showMessageDialog(this, "Favor Seleccione Moneda!", "¡A T E N C I O N!", JOptionPane.WARNING_MESSAGE);
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
