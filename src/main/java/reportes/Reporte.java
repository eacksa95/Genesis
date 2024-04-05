package reportes;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.genesis.model.conexion;
import java.awt.BorderLayout;
import java.io.File;
import java.util.HashMap;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.swing.JRViewer;


/**
 *
 * @author RC
 */
public class Reporte extends javax.swing.JInternalFrame {
    String rp, vf;
 
    javax.swing.JFrame padre; 
    /**
     * Creates new form Reporte
     * @param String r: es un tipo texto con el nombre del reporte sin la extenxi�n .jrxml
     * @param String f: es el nombre de la ventana de filtrado del reporte asociada al mismo 
     * @param String c: es el objeto conexion que se le pasa desde la ventana principal.
     */
    public Reporte(String r, String f, javax.swing.JFrame p) {
        initComponents();
        this.rp = r;
        this.vf = f;

        this.padre = p;
        //filtrarListado();
    }
    public void filtrarListado(){
        JasperReport jr;
        JasperPrint jp;
        JRViewer jv;
        //JasperViewer jv ;
        HashMap par = new HashMap();
        //Map<String,Object> par = new HashMap<String,Object>();

        if(vf.length() > 0){
            switch(vf){
                   
                    
                    case "vFactura":
                    vFactura k = new vFactura(this.padre, true);
                    k.setParams(par);
                    k.setVisible(true);
                    break;
                   
                    case "vFiltroCompras":
                    vFiltroCompras j = new vFiltroCompras(this.padre, true);
                    j.setParams(par);
                    j.setVisible(true);
                    break;
                    
                    
                    case "vFiltroVentas":
                    vFiltroVentas l = new vFiltroVentas(this.padre, true);
                    l.setParams(par);
                    l.setVisible(true);
                    break;
                    
                     case "vFiltroProductos":
                    vFiltroProductos p = new vFiltroProductos(this.padre, true);
                    p.setParams(par);
                    p.setVisible(true);
                    break;
                    
                     case "vFiltroCtaCobrar":
                    vFiltroCtaCobrar z = new vFiltroCtaCobrar(this.padre, true);
                    z.setParams(par);
                    z.setVisible(true);
                    break;
                    

                     case "vFiltroCtaPagar":
                    vFiltroCtaPagar x = new vFiltroCtaPagar(this.padre, true);
                    x.setParams(par);
                    x.setVisible(true);
                    break;
                    
                      case "vRecibo":
                    vRecibo r = new vRecibo(this.padre, true);
                    r.setParams(par);
                    r.setVisible(true);
                    break;
                    
                     case "vFiltroSeguimientoProducto":
                    vFiltroSeguimientoProducto f = new vFiltroSeguimientoProducto(this.padre, true);
                    f.setParams(par);
                    f.setVisible(true);
                    break;
                    
                        case "vNotaDeCredito":
                    vNotaDeCredito m = new vNotaDeCredito(this.padre, true);
                    m.setParams(par);
                    m.setVisible(true);
                    break;
                    
            }
        }else{
            par.put("ardesc", "a7");
            par.put("arid", "7");
            par.put("argdepo", "1");
            par.put("numerofac", "1");
            par.put("compraid", 1);
            par.put("ventaid", 1);
            par.put("numeroven", 1);
            par.put("IDCTACOBRAR", 1);
            
        }

        try {
 
            jr = JasperCompileManager.compileReport(new File("").getAbsolutePath() +"/src/reportes/"+this.rp+".jrxml");
            if(jr != null){
                System.out.println("El reporte fue compilado satisfactoriamente");
            }
            
            jp = JasperFillManager.fillReport(jr, par, conexion.con);

            if(jp != null){
                 System.out.println("El reporte fue rellenado");
            }
            System.out.println(jp.toString());
            jv = new JRViewer(jp);
            //JasperViewer.viewReport(jp);
            jpReporte.setLayout(new BorderLayout());
            jpReporte.repaint();
            jpReporte.add(jv);
            jpReporte.revalidate();
            setTitle("Reporte");
            setClosable(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jpReporte = new javax.swing.JPanel();
        jbBuscar = new javax.swing.JButton();

        javax.swing.GroupLayout jpReporteLayout = new javax.swing.GroupLayout(jpReporte);
        jpReporte.setLayout(jpReporteLayout);
        jpReporteLayout.setHorizontalGroup(
            jpReporteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jpReporteLayout.setVerticalGroup(
            jpReporteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 493, Short.MAX_VALUE)
        );

        jbBuscar.setText("Buscar");
        jbBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jbBuscar)
                .addContainerGap(859, Short.MAX_VALUE))
            .addComponent(jpReporte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jbBuscar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpReporte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>                        

    private void jbBuscarActionPerformed(java.awt.event.ActionEvent evt) {                                         
        filtrarListado();
    }                                        


    // Variables declaration - do not modify                     
    private javax.swing.JButton jbBuscar;
    private javax.swing.JPanel jpReporte;
    // End of variables declaration                   
}
