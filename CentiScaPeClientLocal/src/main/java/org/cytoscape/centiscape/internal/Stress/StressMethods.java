/*
 * StressMethods.java
 *
 * Created on 3 dicembre 2007, 11.38
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
import org.cytoscape.centiscape.internal.CentiScaPeMultiSPath;
import org.cytoscape.centiscape.internal.CentiScaPeShortestPathList;
import org.cytoscape.model.CyNode;


public class StressMethods {
    
    /**
     * Creates a new instance of StressMethods
     */
    public StressMethods() {
    }
    
       public static void updateStress(Vector sptree, Vector StressVectorResults) {
        
           CentiScaPeShortestPathList currentlist;
           String currentsource;
           String currenttarget;
           int currentindex;
           Vector StressVector = new Vector(); 
           StressResult currentStressresult;
           Vector newresults = new Vector();
           
           for (int i = 0; i < sptree.size(); i++) {
                
               currentlist = (CentiScaPeShortestPathList)sptree.get(i);
               currentsource = ((CentiScaPeMultiSPath)currentlist.getFirst()).getName();
               currenttarget = ((CentiScaPeMultiSPath)currentlist.getLast()).getName();
               currentindex = Stressindexof(currentsource,currenttarget,StressVector);
               
               if (currentindex == -1) {
                    currentStressresult  = new StressResult(currentsource,currenttarget); 
                    StressVector.addElement(currentStressresult);
                    currentindex = StressVector.size() -1;
               }
               else {
                    currentStressresult = (StressResult)StressVector.elementAt(currentindex);
                    currentStressresult.incrementSPcount();
               }
               for (int h = 1; h < (currentlist.size() - 1); h++ ) {
                    CentiScaPeMultiSPath currentmultispath = (CentiScaPeMultiSPath)currentlist.get(h);
                    CyNode a = currentmultispath.getNode();
                    currentStressresult.update(a);
               }
           }
           
           for (int i=0; i< StressVector.size(); i++) {
               StressResult currentresult = (StressResult)StressVector.elementAt(i);
               currentresult.calculateStresscount();
               newresults.addAll(currentresult.getVector());
           }
              // adesso devo aggiungere gli elementi 
           StressupdateVectorResults(newresults, StressVectorResults);
           
       } 
       
         public static int Stressindexof(String source, String target, Vector Stressvector) {
         int result = -1; 
         StressResult currentStressresult;
         for (int i = 0; i < Stressvector.size(); i++) {
             currentStressresult = (StressResult)Stressvector.elementAt(i);
             if (currentStressresult.exist(source,target)) {
                 result = i;
                 break;
             }
         } 
         return result;
     }
     
     public static void Stressupdatevector(Vector finalvector, StressElement element) {
         int position = Stressindexof(element.getNode().getSUID(),finalvector);
         if ( position == -1) {
             FinalResultStress newfinalresult = new FinalResultStress(element.getNode(),element.getStresscount());
             finalvector.addElement(newfinalresult);
         }
         else {
              FinalResultStress currentfinalresult = (FinalResultStress)finalvector.elementAt(position);
              currentfinalresult.update(element.getStresscount());
         }
         
     }
     
     public static int Stressindexof(long nodeSUID, Vector finalvector) {
         int result = -1;
         FinalResultStress currentfinalresult;
         for (int i = 0; i < finalvector.size(); i++) {
             currentfinalresult = (FinalResultStress)finalvector.elementAt(i);
             if (currentfinalresult.suidequals(nodeSUID)) {
                 result = i;
                 break;
             }
         }
         return result;
     }
       
       
       
       
       
       public static void StressupdateVectorResults(Vector newresult, Vector StressVectorResults) {
           StressElement currentelement;
           for (int i = 0; i < newresult.size(); i++) {
                currentelement = (StressElement)newresult.elementAt(i);
                Stressinsertnewvalue(currentelement, StressVectorResults);
           }
       }
       
       public static void Stressinsertnewvalue(StressElement newelement, Vector StressVectorResults) {
           FinalResultStress current;
           for (Iterator i = StressVectorResults.iterator(); i.hasNext();) {
                current = (FinalResultStress)i.next();
                if ( newelement.getNode().equals(current.getNode()) ) {
                    current.update(newelement.getStresscount());
               }
           }
       }
       
    
}
