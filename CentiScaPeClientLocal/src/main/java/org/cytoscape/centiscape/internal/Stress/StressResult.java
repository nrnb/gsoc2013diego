/*
 * StressResult.java
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
package org.cytoscape.centiscape.internal.Stress;

import java.util.Iterator;
import java.util.Vector;
import org.cytoscape.model.CyNode;


public class StressResult {
    
    private String source;
    private String target;
    private Vector Stresselements;
    private int SPcount;
    
    /**
     * Creates a new instance of StressResult
     */
    public StressResult() {
    }
    
    public StressResult(String source, String target) {
        this.source = source;
        this.target = target;
        Stresselements = new Vector();
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
         return Stresselements;
     }
     public void update(CyNode node) {
         boolean found = false;
         for (Iterator i = Stresselements.iterator(); i.hasNext();) {
             StressElement currentelement = (StressElement)i.next();
             if (node.getSUID() == (currentelement.getNode().getSUID())) {
                 currentelement.incrementSPcount();
                 found = true;
             }
         }
         if (found == false) {
            StressElement currentelement = new StressElement(node);
            Stresselements.addElement(currentelement);
             
         }
     }
     
     public void calculateStresscount() {
         for (int i=0; i<Stresselements.size(); i++) {
            StressElement currentelement = (StressElement)Stresselements.elementAt(i);
            currentelement.calculateStresscount(1);
         }
     }
     
     public String toString() {
         String result = "source = " + source + "target = " + target + " " + 
                 "numero SP = " + SPcount ;
         
         for (Iterator i = Stresselements.iterator(); i.hasNext();) {
             StressElement currentelement = (StressElement)i.next();
             result = result + " " + currentelement.getNode().getSUID() + 
                     "spcount = " + currentelement.getSPcount() + "Stresscount" + currentelement.getStresscount();
         }
         return result;
     }
}
