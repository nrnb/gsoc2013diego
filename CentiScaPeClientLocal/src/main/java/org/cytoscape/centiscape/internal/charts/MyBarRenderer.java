/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cytoscape.centiscape.internal.charts;

import java.awt.Color;
import java.awt.Paint;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.renderer.category.BarRenderer3D;



/**
 *
 * @author mp
 */
 class MyBarRenderer extends BarRenderer3D
    {

        public void setHighlightedItem(int i, int j)
        {
            if(highlightRow == i && highlightColumn == j)
            {
                return;
            } else
            {
                highlightRow = i;
                highlightColumn = j;
                notifyListeners(new RendererChangeEvent(this));
                return;
            }
        }

        public Paint getItemOutlinePaint(int i, int j)
        {
            if(i == highlightRow && j == highlightColumn)
                return Color.yellow;
            else
                return super.getItemOutlinePaint(i, j);
        }

        private int highlightRow;
        private int highlightColumn;

        MyBarRenderer()
        {
            highlightRow = -1;
            highlightColumn = -1;
        }
    }