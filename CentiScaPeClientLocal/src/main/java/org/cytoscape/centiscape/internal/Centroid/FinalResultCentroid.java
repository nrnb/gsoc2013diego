/*
 * FinalResultCentroid.java
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

package org.cytoscape.centiscape.internal.Centroid;

import org.cytoscape.model.CyNode;

public class FinalResultCentroid implements Comparable {
    
    private long nodesuid;
    private int numberidentifier;
    private double []distvector;
    private double Centroid;
    private CyNode node;
    
    /**
     * Creates a new instance of FinalResultCentroid
     */
    public FinalResultCentroid() {
    }
    
    public FinalResultCentroid(CyNode node, double cen, int totalnodecount) {
        
        this.node = node;
        this.nodesuid = node.getSUID();
        this.Centroid = cen;
        this.distvector = new double[totalnodecount];
    }
    
    public void updatevector(int indexofnode, double distvalue) {
        this.distvector[indexofnode] = distvalue;
    }
    
    public int getvectorlenght() {
        return distvector.length;
    }
    
    public double getdistAt(int index) {
        return distvector[index];
    }
    
    public void update(double cenvalue) {
        Centroid = cenvalue;
    }
    
    public boolean nodeequals(long secondnodesuid) {
        return (this.nodesuid == secondnodesuid);
    }
    
    public String toString(){
        String result= "node name= " + nodesuid + " Centroid =" + Centroid;
        return result;
    }
    
   /* public String getName() {
        return nodename;
    }*/
    public long getSUID() {
        return nodesuid;
    }
    
    public double getCentroid() {
        
        return Centroid;
    }
    
    public String getstringCentroid() {
        
        return "" + Centroid;
    }
    
    public CyNode getNode() {
    
    return node;
    }
    
    
    public int compareTo(Object c) {
        FinalResultCentroid c2 = (FinalResultCentroid)c;
        if (this.getCentroid() > c2.getCentroid())
        return -1;
        else if (this.getCentroid() < c2.getCentroid())
        return 1;
        else return 0;
    }
    
}
