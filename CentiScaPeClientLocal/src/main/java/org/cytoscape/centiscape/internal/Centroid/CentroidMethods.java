/*
 * CentroidMethods.java
 *
 * Created on 7 dicembre 2007, 17.15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
 
/**
 *
 * @author scardoni
 */

package org.cytoscape.centiscape.internal.Centroid;

import java.util.Iterator;
import java.util.Vector;
import org.cytoscape.centiscape.internal.CentiScaPeMultiSPath;
import org.cytoscape.centiscape.internal.CentiScaPeShortestPathList;
import org.cytoscape.model.CyNode;



public class CentroidMethods {
    
    /** Creates a new instance of CentroidMethods */
    public CentroidMethods() {
    }
    
    public static int numberofnearnode(FinalResultCentroid centroidnode1, FinalResultCentroid centroidnode2, Vector VectorOfNode) {
         long node1 = centroidnode1.getSUID();
         long node2 = centroidnode2.getSUID();
         int node1count=0;
         int node2count=0;
         int node1index = VectorOfNode.indexOf(node1);
         int node2index = VectorOfNode.indexOf(node2);
         for (int i=0; i < centroidnode1.getvectorlenght(); i++) {
             if (node1index != i & node2index != i) {
                 //JOptionPane.showMessageDialog(view.getComponent(),
                  //      "nodo1= " + node1 + "nodo2 = " + node2 + "distanza1 = " + centroidnode1.getdistAt(i)
                   //    + "distanza2 = " + centroidnode2.getdistAt(i))  ;
                 if (centroidnode1.getdistAt(i) < centroidnode2.getdistAt(i)) {
                     node1count++;
                 }
                 else if (centroidnode1.getdistAt(i) > centroidnode2.getdistAt(i)) {
                     node2count++;
                 }
                
             }
         
     }
         
     return node1count-node2count;
}
    
     public static void updateCentroid(Vector SingleShortestPathVector, CyNode root, int totalnodecount, Vector CentroidVectorofNodes, Vector CentroidVectorResults) {
    CentiScaPeShortestPathList currentlist;
                FinalResultCentroid currentCentroid = new FinalResultCentroid(root,0,totalnodecount);
                
               // String nodename;
                long nodesuid;
                int currentlistsize;
                int indexofnode;
                for (int j = 0; j < SingleShortestPathVector.size(); j++) {
                    //prendo un percorso
                    currentlist = (CentiScaPeShortestPathList)SingleShortestPathVector.elementAt(j);
                    //prendo il nome del nodo destinazione
                    nodesuid =  ((CentiScaPeMultiSPath)currentlist.getLast()).getSUID();
                    
                    // prendo la distanza
                    currentlistsize = currentlist.size()-1;
                    // guardo a che indice e' il nodo nel vettore
                    //indexofnode = CentroidVectorofNodes.indexOf(nodename);
                    indexofnode = CentroidVectorofNodes.indexOf(nodesuid);
                    // aggiorno l'elemento centroide di root con la distanza
                    currentCentroid.updatevector(indexofnode,currentlistsize);
                    
                    //currentsum = currentsum + currentlist.size()-1;
             
                }
                //double currentCentroidvalue = (double)currentsum;
                //totalCentroidindex = totalCentroidindex + currentsum;
                //currentCentroid.update(currentCentroidvalue);
                
                
                // da rivedere
                CentroidVectorResults.addElement(currentCentroid);
                //JOptionPane.showMessageDialog(view.getComponent(),
                //            "aggio finito" + ShortestPathVector.toString()+ " Centroid= " +
                //      currentCentroid.toString());
                
            }
     
     public static void calculateCentroid(Vector CentroidVectorResults, int totalnodecount, Vector CentroidVectorofNodes ) {
     
     int currentcentroidvalue;
            int currentcentroidmin;
            for (Iterator i = CentroidVectorResults.iterator();i.hasNext(); ) {
                
                currentcentroidmin = totalnodecount + 1;
               
                FinalResultCentroid centroidnode1 = (FinalResultCentroid)i.next();
                //  JOptionPane.showMessageDialog(view.getComponent(),
                //          "inizio con Centroid= " + centroidnode1.toString());
                for (Iterator j = CentroidVectorResults.iterator();j.hasNext(); ) {
                    FinalResultCentroid centroidnode2 = (FinalResultCentroid)j.next();
                    if (centroidnode1.getSUID() != centroidnode2.getSUID()) {
                        currentcentroidvalue =  CentroidMethods.numberofnearnode(centroidnode1,centroidnode2,CentroidVectorofNodes);
                        
                        if (currentcentroidvalue < currentcentroidmin) {
                            currentcentroidmin = currentcentroidvalue;
                        }
                        //JOptionPane.showMessageDialog(view.getComponent(),
                        //        "nodo corrente= " + centroidnode1.getName() + "valore di centroidecorrente = "
                        //    + currentcentroidvalue + "valore di centroid min = " + currentcentroidmin );
                    }
                    
                }
                
                centroidnode1.update(currentcentroidmin);
                
            }
     }
     
    
}
