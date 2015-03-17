/*
 * FinalResultRadiality.java
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
package org.cytoscape.centiscape.internal.Radiality;
 
import java.util.Vector;
import org.cytoscape.model.CyNode;



public class FinalResultRadiality implements Comparable {
    
    private long nodesuid;
    private CyNode node;
    private double Radiality;
    private Vector distvector = new Vector();
    
    /**
     * Creates a new instance of FinalResultRadiality
     */
    public FinalResultRadiality() {
    }
    
    public FinalResultRadiality(CyNode node, double rad) {
        this.nodesuid = node.getSUID();
        this.Radiality = rad;
        this.node = node;
    }
    
    public void update(double radvalue) {
        Radiality = Radiality + radvalue;
    }
    
    public void updatesizevector(Integer size) {
        distvector.addElement(size);
    }
    
    public int getlistsizeat(int place) {
        int listsize; 
        listsize = (Integer)(distvector.elementAt(place));
        return (int)listsize;
    }
    
    public void finalcalculus(int nodecount) {
        Radiality = Radiality/nodecount;
    }
    public int getVectorSize() {
        return distvector.size();
    }
    
    public boolean suidequals(long secondnodesuid) {
        return this.nodesuid == secondnodesuid;
    }
    
    public String toString(){
        String result= "node name= " + nodesuid + " Radiality =" + Radiality;
        return result;
    }
    
   /* public String getName() {
        return nodename;
    }*/
    
    public long getSuid() {
        return nodesuid;
    }
    
    public double getRadiality() {
        
        return Radiality;
    }
    
    public String getstringRadiality() {
        
        return "" + Radiality;
    }
    
     public CyNode getNode() {
    
    return node;
    }
    
    public int compareTo(Object c) {
        FinalResultRadiality c2 = (FinalResultRadiality)c;
        if (this.getRadiality() > c2.getRadiality())
        return -1;
        else if (this.getRadiality() < c2.getRadiality())
        return 1;
        else return 0;
    }
    
}
