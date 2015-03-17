/*
 * StressElement.java
 *
 * Created on 13 marzo 2007, 14.41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author scardoni
 */
package org.cytoscape.centiscape.internal.Stress;

import org.cytoscape.model.CyNode;

public class StressElement {
    
    private long nodeSUID;
    private CyNode node;
    private int SPcount;
    private double Stresscount;
    
    /**
     * Creates a new instance of StressElement
     */
    public StressElement() {
    }
    
    public StressElement(CyNode node) {
        this.node = node;
        this.nodeSUID = node.getSUID();
        SPcount = 1;
        Stresscount = 0;
    }
    
    public void incrementSPcount() {
        SPcount++;
    }
    
     
    
    public CyNode getNode() {
        return node;
    }
    
    public int getSPcount() {
        return SPcount;
    }
    
    public double getStresscount() {
        return Stresscount;
    }
    
    public void calculateStresscount(double totalSP) {
       Stresscount = ((double)SPcount)/totalSP; 
    }
    
    public String toString() {
        return "nodoname = " + nodeSUID + " spcount= " +SPcount + " Stresscount= " + Stresscount;
    }
}
