/*
 * BetweennessMethods.java
 *
 * Created on 3 dicembre 2007, 11.48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.cytoscape.centiscape.internal.Betweenness;


import java.util.*;
import org.cytoscape.centiscape.internal.CentiScaPeMultiSPath;
import org.cytoscape.centiscape.internal.CentiScaPeShortestPathList;
/**
 *
 * @author scardoni 
 */
public class BetweennessMethods {
    
    /** Creates a new instance of BetweennessMethods */
    public BetweennessMethods() {
    }
   
    public static void updateBetweenness(Vector sptree, Vector BetweennessVectorResults ) {
        
           CentiScaPeShortestPathList currentlist;
           String currentsource;
           String currenttarget; 
           int currentindex; 
           Vector BtwVector = new Vector(); 
           CentiScaPeBtwResult currentbtwresult;
           Vector newresults = new Vector();
           
           for (int i = 0; i < sptree.size(); i++) {
               
               currentlist = (CentiScaPeShortestPathList)sptree.get(i);
               currentsource = ((CentiScaPeMultiSPath)currentlist.getFirst()).getName();
               currenttarget = ((CentiScaPeMultiSPath)currentlist.getLast()).getName();
               currentindex = Betweennessindexof(currentsource,currenttarget,BtwVector);
               
               if (currentindex == -1) {
                    currentbtwresult  = new CentiScaPeBtwResult(currentsource,currenttarget); 
                    BtwVector.addElement(currentbtwresult);
                    currentindex = BtwVector.size() -1;
               }
               else {
                    currentbtwresult = (CentiScaPeBtwResult)BtwVector.elementAt(currentindex);
                    currentbtwresult.incrementSPcount();
               }
               for (int h = 1; h < (currentlist.size() - 1); h++ ) {
                    CentiScaPeMultiSPath currentmultispath = (CentiScaPeMultiSPath)currentlist.get(h);
                    long currentsuid = currentmultispath.getSUID();
                    currentbtwresult.update(currentsuid);
               }
           }
           
           for (int i=0; i< BtwVector.size(); i++) {
               CentiScaPeBtwResult currentresult = (CentiScaPeBtwResult)BtwVector.elementAt(i);
               currentresult.calculateBtwcount();
               newresults.addAll(currentresult.getVector());
           }
              // adesso devo aggiungere gli elementi 
           BetweennessupdateVectorResults(newresults, BetweennessVectorResults);
           
       }
        
         public static int Betweennessindexof(String source, String target, Vector btwvector) {
         int result = -1;
    //     System.out.println("confronto source " + source + "target " + target);
         CentiScaPeBtwResult currentbtwresult;
         for (int i = 0; i < btwvector.size(); i++) {
             currentbtwresult = (CentiScaPeBtwResult)btwvector.elementAt(i);
             if (currentbtwresult.exist(source,target)) {
      //             System.out.println("confronto source " + source + "target " + target + "trovato = " +i);
                 result = i;
                 break;
             }
         }
         return result;
     }
     
        public static void BetweennessupdateVectorResults(Vector newresult, Vector BetweennessVectorResults) {
           CentiScaPeBtwElement currentelement;
           for (int i = 0; i < newresult.size(); i++) {
                currentelement = (CentiScaPeBtwElement)newresult.elementAt(i);
                Betweennessinsertnewvalue(currentelement, BetweennessVectorResults);
           } 
       }
        
           public static void Betweennessinsertnewvalue(CentiScaPeBtwElement newelement, Vector BetweennessVectorResults) {
           FinalResultBetweenness current;
           for (Iterator i = BetweennessVectorResults.iterator(); i.hasNext();) {
                current = (FinalResultBetweenness)i.next();
                if ( newelement.getSUID() == (current.getSUID()) ) {
                    current.update(newelement.getBtwcount());
               }
           }
       }
   
}
