/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cytoscape.centiscape.internal.charts;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.data.category.CategoryDataset;


/**
 *
 * @author mp
 */
class DemoPanel extends JPanel
        implements ChartMouseListener
    {

        public void chartMouseMoved(ChartMouseEvent chartmouseevent)
        {
            org.jfree.chart.entity.ChartEntity chartentity = chartmouseevent.getEntity();
            if(!(chartentity instanceof CategoryItemEntity))
            {
                renderer.setHighlightedItem(-1, -1);
                return;
            } else
            {
                CategoryItemEntity categoryitementity = (CategoryItemEntity)chartentity;
                CategoryDataset categorydataset = categoryitementity.getDataset();
                renderer.setHighlightedItem(categorydataset.getRowIndex(categoryitementity.getRowKey()), categorydataset.getColumnIndex(categoryitementity.getColumnKey()));
                return;
            }
        }

        public void chartMouseClicked(ChartMouseEvent chartmouseevent)
        {
        }

        private MyBarRenderer renderer;

        public DemoPanel(MyBarRenderer mybarrenderer)
        {
            super(new BorderLayout());
            renderer = mybarrenderer;
        }
    }