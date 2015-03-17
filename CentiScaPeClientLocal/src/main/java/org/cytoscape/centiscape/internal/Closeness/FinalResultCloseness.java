/*
 * FinalResultCloseness.java
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
package org.cytoscape.centiscape.internal.Closeness;

import org.cytoscape.model.CyNode;

public class FinalResultCloseness implements Comparable {
    
    private long nodesuid;
    private CyNode node;
    private double Closeness;
    
    /**
     * Creates a new instance of FinalResultCloseness
     */
    public FinalResultCloseness() {
    }
    
    public FinalResultCloseness(CyNode node, double clo) {
        
        this.node = node;
        this.nodesuid = node.getSUID();
        this.Closeness = clo;
    }
    
    public void update(double clovalue) {
        Closeness = Closeness + clovalue;
    }
    
    public boolean Nameequals(long secondnodesuid) {
        return (this.nodesuid  == secondnodesuid);
    }
    
    public String toString(){
        String result= "node name= " + nodesuid + " Closeness =" + Closeness;
        return result;
    }
    
  /*  public String getName() {
        return nodename;
    }*/
    
    public double getCloseness() {
        
        return Closeness;
    }
    
    public String getstringCloseness() {
        
        return "" + Closeness;
    }
    
     public CyNode getNode() {
    
    return node;
    }
    
    public int compareTo(Object c) {
        FinalResultCloseness c2 = (FinalResultCloseness)c;
        if (this.getCloseness() > c2.getCloseness())
        return -1;
        else if (this.getCloseness() < c2.getCloseness())
        return 1;
        else return 0;
    }
    
}
