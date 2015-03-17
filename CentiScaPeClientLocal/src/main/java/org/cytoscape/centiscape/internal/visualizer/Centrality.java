/*
 * Centrality.java
 *
 * Created on 27 novembre 2007, 10.36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.cytoscape.centiscape.internal.visualizer;

/**
 *
 * @author admini
 */
public interface Centrality {
    public String getName();
    public boolean isOn();
    public double getDefaultValue();
    public double getMinValue();
    public double getMaxValue();
    public String toString();
}
