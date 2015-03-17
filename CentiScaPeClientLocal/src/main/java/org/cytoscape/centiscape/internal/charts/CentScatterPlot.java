package org.cytoscape.centiscape.internal.charts;



// Referenced classes of package demo:
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RefineryUtilities;
//            SampleXYDataset2
public class CentScatterPlot extends JFrame {

    //private static Centrality x;
    private static String x;
    private static String y;
    public static CyNetwork currentnetwork;

    public CentScatterPlot(String s, CyNetwork currentnetwork) {
        super(s);
        JPanel jpanel = createDemoPanel();
        this.currentnetwork = currentnetwork;
        //jpanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(jpanel);
    }

    //public ScatterPlot(Centrality x, String y) {
      public CentScatterPlot(String x, String y, CyNetwork currentnetwork) {
       // super("scatter plot for " + x.getName() + "/" + y);
        super("scatter plot for " + x + "/" + y);
        this.currentnetwork = currentnetwork;
        this.x = x;
        this.y = y;
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        JPanel jpanel = createDemoPanel();
        setContentPane(jpanel);
    }

    private static JFreeChart createChart(XYDataset xydataset) {
        //JFreeChart jfreechart = ChartFactory.createScatterPlot("Centiscape Scatter Plot view", x.getName(), y, xydataset, PlotOrientation.VERTICAL, false, true, true);
         JFreeChart jfreechart = ChartFactory.createScatterPlot("Centiscape Scatter Plot view", x, y, xydataset, PlotOrientation.VERTICAL, false, true, true);
        
        XYPlot xyplot = (XYPlot) jfreechart.getPlot();
        xyplot.setNoDataMessage("NO DATA");
        xyplot.setDomainZeroBaselineVisible(true);
        xyplot.setRangeZeroBaselineVisible(true);
        XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyplot.getRenderer();
        xylineandshaperenderer.setSeriesOutlinePaint(0, Color.black);
        xylineandshaperenderer.setUseOutlinePaint(true);
        NumberAxis numberaxis = (NumberAxis) xyplot.getDomainAxis();
        numberaxis.setAutoRangeIncludesZero(false);
        numberaxis.setTickMarkInsideLength(2.0F);
        numberaxis.setTickMarkOutsideLength(0.0F);
        NumberAxis numberaxis1 = (NumberAxis) xyplot.getRangeAxis();
        numberaxis1.setTickMarkInsideLength(2.0F);
        numberaxis1.setTickMarkOutsideLength(0.0F);
        return jfreechart;
    }

    // create an XYDataSet for scatter plot from x-y centralities information
    private static DefaultXYDataset createDataSet() {
        double vx, vy;
        DefaultXYDataset dxy = new DefaultXYDataset();
      
     //   CyAttributes cattr = Cytoscape.getNodeAttributes();
       // for (Iterator it = Cytoscape.getCurrentNetwork().nodesIterator(); it.hasNext();) {
             for (Iterator it = currentnetwork.getNodeList().listIterator(); it.hasNext();) {
            double[][] val = new double[2][1];
            CyNode node = (CyNode) it.next();
   // if      (currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).get(x, Double.class) != null) {
     //         vx =currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).get(x, Double.class);
                if      (currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).getRaw(x) != null) {  
                vx = (Double)currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).getRaw(x);
        //  if ((cattr.getAttribute(node.getSUID(), x)) != null) {
               /* if (cattr.getType(x)==cattr.TYPE_INTEGER) {
                   vx = cattr.getIntegerAttribute(node.getSUID(), x);
                }
                else {
            vx = cattr.getDoubleAttribute(node.getSUID(), x);
                }*/
                
                
            val[0][0] = vx;
            String nodename = currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).get("name", String.class);
            dxy.addSeries(nodename, val);
            }
   //  if      (currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).get(y, Double.class) != null) {
     //      vy =currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).get(y, Double.class);
       
           if      (currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).getRaw(y) != null) {  
                vy = (Double)currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).getRaw(y);
               val[1][0] = vy;
                String nodename = currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).get("name", String.class);
           
                dxy.addSeries(nodename, val);
            }
             // dxy.addSeries(node.getSUID(), val);
        }
        return (dxy);
        //return null;
    }

    public JPanel createDemoPanel() {
        jfreechart = createChart(createDataSet());
        chartpanel = new ChartPanel(jfreechart);
        chartpanel.setVerticalAxisTrace(true);
        chartpanel.setHorizontalAxisTrace(true);
        
        chartpanel.setPopupMenu(null);
        chartpanel.setDomainZoomable(true);
        chartpanel.setRangeZoomable(true);
        
         //CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();
        // MyBarRenderer mybarrenderer = (MyBarRenderer) categoryplot.getRenderer();
        //DemoPanel demopanel = new DemoPanel(mybarrenderer);
      //  chartpanel.add;
       chartpanel.setLayout(new GridBagLayout());
      //chartpanel.setBounds(0, 0, 600, 500);
        JButton menu = new JButton("export image");
        
        GridBagConstraints constraint = new GridBagConstraints();
        constraint.weightx = 1.0;
        
        constraint.weighty = 1.0;
        constraint.anchor = GridBagConstraints.LAST_LINE_END;
        constraint.gridx = 0;
        constraint.gridy = 0;
        chartpanel.add(menu, constraint);
        menu.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveIt();
            }
        });
        return chartpanel;
    }
    
    static File f;
    static JFreeChart jfreechart;
     ChartPanel chartpanel;
    
    private  void saveIt() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("export image");
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == 0) {
            f = fc.getSelectedFile();
            if (!f.getAbsolutePath().endsWith(".png") && !f.getAbsolutePath().endsWith(".jpg")) {
                f = new File((new StringBuilder()).append(f.getAbsolutePath()).append(".jpg").toString());
            }
            if (f.exists() && 1 == JOptionPane.showConfirmDialog(this, (new StringBuilder()).append(f.getName()).append(" exists. Overwrite?").toString(), "Confirm export current chart as an image...", 0, 2)) {
                return;
            }
                if (f.getAbsolutePath().endsWith(".png")) {
                    savePng();
                }
                if (f.getAbsolutePath().endsWith(".jpg")) {
                    savePng();
                }
        }
    }
    
    private  void savePng(){
        
    try{
        ChartUtilities.saveChartAsPNG(f, jfreechart, chartpanel.getWidth(), chartpanel.getHeight());
    } catch (IOException exc) {
                exc.printStackTrace();
            }
    }

   public static void main(String args[]) {
   //       public  void main(String args[]) {
        CentScatterPlot scatterplotdemo1 = new CentScatterPlot("Scatter Plot", currentnetwork);
        scatterplotdemo1.pack();
        RefineryUtilities.centerFrameOnScreen(scatterplotdemo1);
        scatterplotdemo1.setVisible(true);
    }
}
