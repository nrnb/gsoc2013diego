/*
 * FinalResultBetweenness.java
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
package org.cytoscape.centiscape.internal.Betweenness;

import org.cytoscape.model.CyNode;

public class FinalResultBetweenness implements Comparable {
    
    private long nodesuid;;
    private double Betweenness;
    private CyNode node;
    
    /**
     * Creates a new instance of FinalResultBetweenness
     */
    public FinalResultBetweenness() {
    }
    
    public FinalResultBetweenness(CyNode node, double btw) {
        this.nodesuid = node.getSUID();
        this.node = node;
        this.Betweenness = btw;
    }
    
    public void update(double btwvalue) {
        Betweenness = Betweenness + btwvalue;
    }
    
    public boolean Nameequals(long secondnodesuid) {
         System.out.println("node name= " + nodesuid + " secondo node = " + secondnodesuid);
        return this.nodesuid == secondnodesuid;
    }
    
    public String toString(){
        String result= "node name= " + nodesuid + " betweenness =" + Betweenness;
        return result;
    }
    
    /*public String getName() {
        return nodename;
    }*/

    public long getSUID() {
        return nodesuid;
    }
    
    public double getBetweenness() {
        return Betweenness;
    }
    
    public CyNode getNode() {
        return node;
    }
    
    public int compareTo(Object c) {
        FinalResultBetweenness c2 = (FinalResultBetweenness)c;
        if (this.getBetweenness() > c2.getBetweenness())
        return -1;
        else if (this.getBetweenness() < c2.getBetweenness())
        return 1;
        else return 0;
    }
    
}
