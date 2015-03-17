/*
 * CentiScaPeBtwResult.java
 *
 * Created on 13 marzo 2007, 14.34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author scardoni
 */
package org.cytoscape.centiscape.internal.Betweenness;

import java.util.Iterator;
import java.util.Vector;





public class CentiScaPeBtwResult {
    
    private String source;
    private String target;
    private Vector btwelements;
    private int SPcount;
    
    /**
     * Creates a new instance of CentiScaPeBtwResult
     */
    public CentiScaPeBtwResult() {
    }
    
    public CentiScaPeBtwResult(String source, String target) {
        this.source = source;
        this.target = target;
        btwelements = new Vector();
        SPcount = 1;
        
    }
    
    public boolean exist(String Source, String Target) {
        if ((Source.equals(this.source)) && (Target.equals(this.target))) {
            return true;
        }
        else return false;
    }
    
    public void incrementSPcount() {
        SPcount++;
    }
    
    public String getsource() {
        return source;
    }
    
     public String gettarget() {
        return target;
    }
     
     public Vector getVector() {
         return btwelements;
     }
     public void update(long nodesuid) {
         // System.out.println("btwelements =  " + btwelements.toString());
         boolean found = false;
         for (Iterator i = btwelements.iterator(); i.hasNext();) {
             CentiScaPeBtwElement currentelement = (CentiScaPeBtwElement)i.next();
             if (nodesuid == currentelement.getSUID()) {
                 currentelement.incrementSPcount();
           //        System.out.println("incremento sp count di " + nodesuid);
                 found = true;
             }
         }
         if (found == false) {
            CentiScaPeBtwElement currentelement = new CentiScaPeBtwElement(nodesuid);
            btwelements.addElement(currentelement);
             
         }
     }
     
     
     
     
     public void calculateBtwcount() {
         for (int i=0; i<btwelements.size(); i++) {
            CentiScaPeBtwElement currentelement = (CentiScaPeBtwElement)btwelements.elementAt(i);
            currentelement.calculateBtwcount(SPcount);
         }
     }
     
     public String toString() {
         String result = "source = " + source + "target = " + target + " " + 
                 "numero SP = " + SPcount ;
         
         for (Iterator i = btwelements.iterator(); i.hasNext();) {
             CentiScaPeBtwElement currentelement = (CentiScaPeBtwElement)i.next();
             result = result + " " + currentelement.getSUID() + 
                     "spcount = " + currentelement.getSPcount() + "btwcount" + currentelement.getBtwcount();
         }
         return result;
     }
}
