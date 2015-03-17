/*
 * EccentricityCentrality.java
 *
 * Created on 11 dicembre 2007, 11.18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.cytoscape.centiscape.internal.visualizer;

/**
 *
 * @author scardoni
 */
public class ImplCentrality  implements Centrality {
    
    String centralityName;
    boolean isOn;
    double defaultValue;
    double minValue;
    double maxValue;
    
    /** Creates a new instance of EccentricityCentrality */
    public ImplCentrality(String name, boolean ison, double defaultvalue, double min, double max) {
    
        centralityName = name;  
        isOn = ison;
        defaultValue = defaultvalue;
        minValue = min;
        maxValue = max;
        
    
    }
    
    public String getName() {
       return centralityName; 
    }
    public boolean isOn() {
        return isOn;
    }
    public double getDefaultValue() {
        return defaultValue;
    }
    
    public double getMinValue() {
        return minValue;
    }
    
    public double getMaxValue() {
        return maxValue;
    }
    
    public String toString() {
        return centralityName;
    }
    
}
