/*
 * StressFinalResult.java
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

public class StressFinalResult implements Comparable {
    
    private String nodename;
    private double Stress;
    
    /**
     * Creates a new instance of StressFinalResult
     */
    public StressFinalResult() {
    }
    
    public StressFinalResult(String Nodename, double Stress) {
        this.nodename = Nodename;
        this.Stress = Stress;
    }
    
    public void update(double Stressvalue) {
        Stress = Stress + Stressvalue;
    }
    
    public boolean Nameequals(String name) {
        return this.nodename.equals(name);
    }
    
    public String toString(){
        String result= "node name= " + nodename + " Stress =" + Stress;
        return result;
    }
    
    public String getName() {
        return nodename;
    }
    
    public double getStress() {
        return Stress;
    }
    
    public int compareTo(Object c) {
        StressFinalResult c2 = (StressFinalResult)c;
        if (this.getStress() > c2.getStress())
        return -1;
        else if (this.getStress() < c2.getStress())
        return 1;
        else return 0;
    }
    
}
