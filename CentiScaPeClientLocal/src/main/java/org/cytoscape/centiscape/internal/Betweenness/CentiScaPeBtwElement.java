/*
 * CentiScaPeBtwElement.java
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
package org.cytoscape.centiscape.internal.Betweenness;

public class CentiScaPeBtwElement {
    
    private long nodesuid;
    private int SPcount;
    private double Btwcount;
    
    /**
     * Creates a new instance of CentiScaPeBtwElement
     */
    public CentiScaPeBtwElement() {
    }
    
    public CentiScaPeBtwElement(long nodesuid) {
        this.nodesuid = nodesuid;
        SPcount = 1;
        Btwcount = 0;
    }
    
    public void incrementSPcount() {
        SPcount++;
    }
    
     
    
 /*   public String getName() {
        return nodename;
    }
   */ 
    public int getSPcount() {
        return SPcount;
    }
    
    public double getBtwcount() {
        return Btwcount;
    }
    
    public void calculateBtwcount(double totalSP) {
       Btwcount = ((double)SPcount)/totalSP; 
    }
    
    public long getSUID() {
        return nodesuid;
    }
    
    public String toString() {
        return "nodoname = " + nodesuid + " spcount= " +SPcount + " btwcount= " + Btwcount;
    }
}
