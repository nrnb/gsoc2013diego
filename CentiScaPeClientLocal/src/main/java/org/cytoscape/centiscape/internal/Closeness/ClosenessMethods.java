/*
 * ClosenessMethods.java
 *
 * Created on 7 dicembre 2007, 16.15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author scardoni
 */
package org.cytoscape.centiscape.internal.Closeness;


import java.util.Vector;
import org.cytoscape.centiscape.internal.CentiScaPeShortestPathList;
import org.cytoscape.model.CyNode;



public class ClosenessMethods {
    
    /** Creates a new instance of ClosenessMethods */
    public ClosenessMethods() {
    }
    
      public static FinalResultCloseness CalculateCloseness(Vector SingleShortestPathVector,CyNode CurrentRoot ) {
           
            int CurrentClosenessSum=0;
           // Vector ClosenessVector = new Vector();
                CentiScaPeShortestPathList CurrentClosenessList;
                FinalResultCloseness CurrentCloseness = new FinalResultCloseness(CurrentRoot,0);
                       
                for (int j = 0; j < SingleShortestPathVector.size(); j++) {
                      
                     CurrentClosenessList = (CentiScaPeShortestPathList)SingleShortestPathVector.get(j);
                     
                       CurrentClosenessSum = CurrentClosenessSum + CurrentClosenessList.size()-1;
                       
                    }
                   
                double CurrentClosenessValue = (1/(double)CurrentClosenessSum);
                CurrentCloseness.update(CurrentClosenessValue);
                //ClosenessVector.addElement(CurrentCloseness);
                
           return CurrentCloseness;
           
       } 
    
}
