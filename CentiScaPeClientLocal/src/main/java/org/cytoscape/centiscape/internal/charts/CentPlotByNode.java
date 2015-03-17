
package org.cytoscape.centiscape.internal.charts;

import java.awt.Color;
import java.awt.GradientPaint;
import java.util.Iterator;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.cytoscape.centiscape.internal.visualizer.CentVisualizer;
import org.cytoscape.centiscape.internal.visualizer.Centrality;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;




public class CentPlotByNode extends JFrame {

    public static String nodeName;
   // public long nodesuid;
    public CyNode node;
    public static DefaultCategoryDataset dds = new DefaultCategoryDataset();
    public static MyBarRenderer mybarrenderer;
    public CyNetwork currentnetwork;

    // polymorphic constructor
    public CentPlotByNode(CyNode node, CyNetwork currentnetwork) {
       // super("Plot By Node visualization for " + node.getIdentifier());
        
        super("Plot By Node visualization for " + currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).get("name", String.class));
        this.currentnetwork = currentnetwork;
        this.nodeName = currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).get("name", String.class);
        this.setDefaultCloseOperation(this.HIDE_ON_CLOSE);
      //  nodeName = currentnetwork.getRow(currentnetwork).get("name", String.class);
        
        
        this.node = node;
        JPanel jpanel = createDemoPanel();
        //jpanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(jpanel);
    }

    private CategoryDataset createDataset() {
        //String s1 = node.getIdentifier() + " centrality value";
        String s1 = nodeName + " centrality value";
        DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
String centralityname;
        for (Iterator it = CentVisualizer.centralities.iterator(); it.hasNext();) {
            Centrality cent = (Centrality) it.next();
            // add min,max,average value for centrality cent
            centralityname = cent.getName().split(" ")[1];
            if (centralityname.equals("Node")) {
                centralityname = cent.getName().split(" ")[2];
            }
           
            double v1 = (Math.abs(cent.getDefaultValue()-cent.getMinValue()) / Math.abs(cent.getMaxValue()-cent.getMinValue()))*100;
            double v2 = 0;
            double v3 = 100;
            // add node value for centrality cent
          //  CyAttributes cattr = Cytoscape.getNodeAttributes();
            
            //double vtmp = cattr.getDoubleAttribute(node.getIdentifier(), cent.getName());
          //  System.out.println(" scrivo  il nodo " + currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).toString());
            double vtmp = currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).get(cent.getName(), Double.class);
            double v4 = Math.abs(vtmp -cent.getMinValue())/ Math.abs(cent.getMaxValue()-cent.getMinValue())*100;
            
            if ((!Double.isInfinite(v1)) &&
                    (!Double.isInfinite(v2)) &&
                    (!Double.isInfinite(v3)) &&
                    (!Double.isInfinite(v4))) {
                defaultcategorydataset.addValue(v1, "average value", centralityname);
                defaultcategorydataset.addValue(v2, "min value", centralityname);
                defaultcategorydataset.addValue(v3, "max value", centralityname);
                defaultcategorydataset.addValue(v4,
                        s1,
                     centralityname );

                //tooltip real values
                dds.addValue(cent.getDefaultValue(), "average value", centralityname);
                dds.addValue(cent.getMinValue(), "min value",centralityname );
                dds.addValue(cent.getMaxValue(), "max value", centralityname);
                dds.addValue(
                  currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).get(cent.getName(), Double.class), s1,centralityname);
                //dds.addValue(cattr.getDoubleAttribute(node.getSUID(), cent.getName()), s1,                       centralityname );
            }
        }
        return defaultcategorydataset;
   // return null;
    }

    static JFreeChart createChart(CategoryDataset categorydataset) {
        JFreeChart jfreechart = ChartFactory.createBarChart(nodeName, "centrality statistics for " + nodeName, "Percentage", categorydataset, PlotOrientation.VERTICAL, true, true, false);
        jfreechart.setBackgroundPaint(Color.white);

        // plotting setup
        CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();
        categoryplot.setBackgroundPaint(Color.lightGray);
        categoryplot.setDomainGridlinePaint(Color.white);
        categoryplot.setDomainGridlinesVisible(true);
        categoryplot.setRangeGridlinePaint(Color.white);

        NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
        numberaxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
        //bar renderer setup
        mybarrenderer = new MyBarRenderer();
        // permit bar outline marking
        mybarrenderer.setDrawBarOutline(true);
        categoryplot.setRenderer(mybarrenderer);
        // gradients for plots
        GradientPaint gradientpaint = new GradientPaint(0.0F, 0.0F, Color.blue, 0.0F, 0.0F, new Color(0, 0, 64));
        GradientPaint gradientpaint1 = new GradientPaint(0.0F, 0.0F, Color.green, 0.0F, 0.0F, new Color(0, 64, 0));
        GradientPaint gradientpaint2 = new GradientPaint(0.0F, 0.0F, Color.red, 0.0F, 0.0F, new Color(64, 0, 0));
        GradientPaint gradientpaint3 = new GradientPaint(0.0F, 0.0F, Color.WHITE, 0.0F, 0.0F, new Color(64, 64, 0));
        mybarrenderer.setSeriesPaint(0, gradientpaint);
        mybarrenderer.setSeriesPaint(1, gradientpaint1);
        mybarrenderer.setSeriesPaint(2, gradientpaint2);
        mybarrenderer.setSeriesPaint(2, gradientpaint3);
        // tool tips!
//        mybarrenderer.setLegendItemToolTipGenerator(new CategorySeriesLabelGenerator(){
//            public String generateLabel(CategoryDataset ds,int series){
//                return("Ciao!");
//            }
//        });

        //categoryplot.addAnnotation(annotation);

        return jfreechart;
    }

    public JPanel createDemoPanel() {
        JFreeChart jfreechart = createChart(createDataset());
        CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();
        MyBarRenderer mybarrenderer = (MyBarRenderer) categoryplot.getRenderer();
        DemoPanel demopanel = new DemoPanel(mybarrenderer);
        ChartPanel chartpanel = new ChartPanel(jfreechart);
        chartpanel.addChartMouseListener(demopanel);
        demopanel.add(chartpanel);
        //tooltip setup
                mybarrenderer.setToolTipGenerator(new CategoryToolTipGenerator() {

                    public CategoryDataset realValues = dds;

                    public String generateToolTip(CategoryDataset arg0, int arg1, int arg2) {
                        return (realValues.getValue(arg1, arg2).toString());
                    }
                });
        return demopanel;
    }
}
