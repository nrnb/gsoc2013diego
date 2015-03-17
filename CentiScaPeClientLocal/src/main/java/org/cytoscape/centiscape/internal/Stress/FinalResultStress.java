/*
 * FinalResultStress.java
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
package org.cytoscape.centiscape.internal.Stress;

import org.cytoscape.model.CyNode;


public class FinalResultStress implements Comparable {
    
    private long nodeSUID;
    //private CyNode node;
    private double Stress;
    private CyNode node;
    
    /**
     * Creates a new instance of FinalResultStress
     */
    public FinalResultStress() {
    }
    
    public FinalResultStress(CyNode node, double Stress) {
        this.nodeSUID = node.getSUID();
        this.Stress = Stress;
        this.node = node;
    }
    
    public void update(double Stressvalue) {
        Stress = Stress + Stressvalue;
    }
    
    public boolean suidequals(long secondnodesuid) {
        return this.nodeSUID == (secondnodesuid);
    }
    
    public String toString(){
        String result= "node name= " + nodeSUID + " Stress =" + Stress;
        return result;
    }
    
    public CyNode getNode() {
        return node;
    }
    
    public double getStress() {
        return Stress;
    }
    
    
    
    public int compareTo(Object c) {
        FinalResultStress c2 = (FinalResultStress)c;
        if (this.getStress() > c2.getStress())
        return -1;
        else if (this.getStress() < c2.getStress())
        return 1;
        else return 0;
    }
    
}
