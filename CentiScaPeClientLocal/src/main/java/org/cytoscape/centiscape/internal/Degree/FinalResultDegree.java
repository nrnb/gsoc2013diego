/*
 * FinalResultDegree.java
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
package org.cytoscape.centiscape.internal.Degree;

import org.cytoscape.model.CyNode;


public class FinalResultDegree implements Comparable {
    
    private long nodesuid;  
    private CyNode node;
    private double Degree;
    
    /**
     * Creates a new instance of FinalResultDegree
     */
    public FinalResultDegree() {
    }
    
    public FinalResultDegree(CyNode node, double Degree) {
        this.node = node;
        this.nodesuid = node.getSUID();
        this.Degree = Degree;
    }
    
    public void update(double Degreevalue) {
        Degree = Degreevalue;
    }
    
    public boolean suidequals(long secondnodesuid) {
        return this.nodesuid == secondnodesuid;
    }
    
    public String toString(){
        String result= "node name= " + nodesuid + " Degree =" + Degree;
        return result;
    }
    
    /*public String getName() {
        return nodesuid;
    }*/
    
    public long getSUID() {
        return nodesuid;
    } 
    
    public double getDegree() {
        
        return Degree;
    }
    
    public String getstringDegree() {
        
        return "" + Degree;
    }
    
     public CyNode getNode() {
    
    return node;
    }
    
    public int compareTo(Object c) {
        FinalResultDegree c2 = (FinalResultDegree)c;
        if (this.getDegree() > c2.getDegree())
        return -1;
        else if (this.getDegree() < c2.getDegree())
        return 1;
        else return 0;
    }
    
}
