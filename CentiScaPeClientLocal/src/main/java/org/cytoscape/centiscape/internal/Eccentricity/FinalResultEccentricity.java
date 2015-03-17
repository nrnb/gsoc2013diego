/*
 * FinalResultEccentricity.java
 *
 * Created on 15 marzo 2007, 18.06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author scardoni
 */
package org.cytoscape.centiscape.internal.Eccentricity;

import org.cytoscape.model.CyNode;


public class FinalResultEccentricity implements Comparable {
    
    private long nodesuid;
    private double eccentricity;
    private CyNode node;
    
    
    /**
     * Creates a new instance of FinalResultEccentricity
     */
    public FinalResultEccentricity() {
    }
    
    public FinalResultEccentricity(CyNode node, double ecc) {
        this.node = node;
        this.nodesuid = node.getSUID();
        this.eccentricity = ecc;
        
    }
    
    public void update(double eccvalue) {
        eccentricity = eccvalue;
    }
    
    public boolean Nameequals(long secondnodesuid) {
        return (this.nodesuid == secondnodesuid);
    }
    
    public String toString(){
        String result= "node name= " + nodesuid + " eccentricity =" + eccentricity;
        return result;
    }
    
    /*public String getName() {
        return nodesuid;
    }*/
    
    public double geteccentricity() {
        
        return eccentricity;
    }
    
    public String getstringeccentricity() {
        
        return "" + eccentricity;
    }
    
     public CyNode getNode() {
    
    return node;
    }
    
    public int compareTo(Object c) {
        FinalResultEccentricity c2 = (FinalResultEccentricity)c;
        if (this.geteccentricity() > c2.geteccentricity())
        return -1;
        else if (this.geteccentricity() < c2.geteccentricity())
        return 1;
        else return 0;
    }
    
}
