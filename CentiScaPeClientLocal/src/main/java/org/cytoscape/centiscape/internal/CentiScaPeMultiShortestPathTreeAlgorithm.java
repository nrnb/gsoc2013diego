
package org.cytoscape.centiscape.internal;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;

/*
 * CentiScaPeMultiShortestPathTreeAlgorithm.java
 *
 * Created on 1 febbraio 2007, 12.35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
/**editor
 *
 * @author scardoni
 */


public class CentiScaPeMultiShortestPathTreeAlgorithm {

    static Boolean StressisOn;
    static Boolean BtwisOn;
    static TreeMap currentmap = new TreeMap();
    static CyNode  nodotarget;
    static double  numberofpaths;
    //modificare perch?? non si cancella a ogni nuova iterazione
    //public static TreeMap Stressmap = new TreeMap();

    /**
     * Creates a new instance of CentiScaPeMultiShortestPathTreeAlgorithm
     */
    public CentiScaPeMultiShortestPathTreeAlgorithm() {
    }

   // public static Vector ExecuteMultiShortestPathTreeAlgorithm(CyNetwork network, CyNetworkView view, CyNode root, Boolean Stressvalue, Boolean Btwvalue, TreeMap zeromap) {
 public static Vector ExecuteMultiShortestPathTreeAlgorithm(CyNetwork network, CyNode root, Boolean Stressvalue, Boolean Btwvalue, TreeMap zeromap) {

        //  Stressmap.clear();
        //Declaration of set and list used in the algorithm
        //PathSet is the set of the shortest path
        HashSet PathSet;
        //TempSet is the set of temporary Shortest path
        HashSet TempSet;
        //Queue is the list of the temporary shortest path
        LinkedList Queue;

        StressisOn = Stressvalue;
        BtwisOn = Btwvalue;

        TreeMap perinizializza = (TreeMap) zeromap.clone();
        //The vector of the final results
        Vector ShortestPathVector = new Vector();
        //get the list of selected nodes
      //  List lista = view.getSelectedNodes();




        // Create the set of the Shortest Path
        PathSet = new HashSet();
        //create the set of temporary Shortest path
        TempSet = new HashSet();
        //create the list of the temporary shortest path
        Queue = new LinkedList();
        //initialize shortest path Queue and Tempset
      //  initialize(TempSet, Queue, network, view, root);
         initialize(TempSet, Queue, network,  root);
        // Start the Core of the multi shortestpath algorithm
      //  ShortestPathCore(PathSet, TempSet, Queue, network, view, root);
        ShortestPathCore(PathSet, TempSet, Queue, network,  root);
        //now go through our PathSet and select the CentiScaPeMultiSPath corresponding to the target node
        //System.out.println("il nodo root  " + root.getIdentifier());
        boolean result = false;
        numberofpaths = 0;
        double startingpathsnumber = 0;
        ShortestPathVector.size();
        double finalpathsnumber = 0;

        //if (BtwisOn) {
        //  currentmap = (TreeMap) perinizializza.clone();
        // }
        // System.out.println("mapprima =" + currentmap.toString());
        for (Iterator i = PathSet.iterator(); i.hasNext();) {
            CentiScaPeMultiSPath tmpspath = (CentiScaPeMultiSPath) i.next();
        //    System.out.println("parto col nodo " + tmpspath.getName());
            startingpathsnumber = ShortestPathVector.size();
        //    System.out.println("ShortestPathVector.size iniziale = " + startingpathsnumber);
            CentiScaPeShortestPathList prova = new CentiScaPeShortestPathList();
            ShortestPathVector.addElement(prova);
            if (BtwisOn) {
                currentmap = (TreeMap) perinizializza.clone();
            }

            // currentmap = (TreeMap)perinizializza.clone();
            nodotarget = tmpspath.getNode();

            createShortestPathVector(prova, tmpspath, ShortestPathVector, root);
         //   System.out.println("currentmap del ciclo =" + currentmap.toString());
            result = true;
         //   System.out.println("il nodo root  " + root.getIdentifier());
         //   System.out.println("currentmap finale =" + currentmap.toString());
         //   System.out.println("dimensione ShortestPathVector iniziale" + startingpathsnumber);
            finalpathsnumber = ShortestPathVector.size();
         //   System.out.println("dimensione ShortestPathVector finale= " + finalpathsnumber);
            numberofpaths = finalpathsnumber - startingpathsnumber;

          // AGGIUNGERE QUI ALLA MAPPA BETWEENNESS IL VALORE ATTUALE DIVISO IL VALORE NUMBEROFPATHS.
            //QUINDI FARE UPDATEBETWEENNESS SULL'ORIGINALE.



          //  System.out.println("numero di percorsi da nodo root = " + root.getIdentifier() + "a nodo target = " + nodotarget.getIdentifier() + "?? uguale a " + numberofpaths);

        }
           return ShortestPathVector;
    }

    //initialize shortest path
   // public static void initialize(HashSet TempSet, LinkedList Queue, CyNetwork network, CyNetworkView view, CyNode root) {
      public static void initialize(HashSet TempSet, LinkedList Queue, CyNetwork network, CyNode root) {

        //create the root Shortest Path (cost=o, predecessor = null)
        CentiScaPeMultiSPath primospath = new CentiScaPeMultiSPath(root, 0,network);
        //initialize shortest path for each node
        // predecessor is null and shortestpath cost is the maximum
        //i.e. number of node plus 1. for root node cost is 0;
        int numberofnodes = network.getNodeCount();
        //add the root Shortest Path to the Queue
        Queue.add(primospath);
        //iterate on all the nodes of the view
       /*for (Iterator i = view.getNodeViewsIterator(); i.hasNext();) {
            NodeView nView = (NodeView) i.next();
            CyNode currentnode = (CyNode) nView.getNode();*/

            for (Iterator i = network.getNodeList().iterator(); i.hasNext();) {
               CyNode currentnode = (CyNode)i.next();


            // initialize all the node except of root
            if (!currentnode.equals(root)) {
                CentiScaPeMultiSPath currentSPath = new CentiScaPeMultiSPath(currentnode, numberofnodes + 1, network);
                TempSet.add(currentSPath);
            }
        }
    }

    // public static void ShortestPathCore(HashSet PathSet, HashSet TempSet, LinkedList Queue, CyNetwork network, CyNetworkView view, CyNode root) {
      public static void ShortestPathCore(HashSet PathSet, HashSet TempSet, LinkedList Queue, CyNetwork network,  CyNode root) {

        //Iterate the minimum path algorithm on the Queue list
        for (Iterator i = Queue.iterator(); i.hasNext();) {
            //get the first element of the Queue and add it to the set of the Shortest Path
            CentiScaPeMultiSPath currentSPath = (CentiScaPeMultiSPath) Queue.remove(0);
            if (!currentSPath.getNode().equals(root)) {
                PathSet.add(currentSPath);
            }

            // get the neighbors of the selected CentiScaPeMultiSPath node
            List neighbors = network.getNeighborList(currentSPath.getNode(),CyEdge.Type.ANY);
            // and iterate over the neighbors
            for (Iterator ni = neighbors.iterator(); ni.hasNext();) {
                CyNode neighbor = (CyNode) ni.next();
                //relax the currentSPath with its neighbors

                relax(currentSPath, neighbor, TempSet, Queue);
            }
        }
    }

    public static void relax(CentiScaPeMultiSPath CurrentSPath, CyNode NeighBor, HashSet TempSet, LinkedList Queue) {
        // verify if NeighBor is in the TempSet end put the corresponding CentiScaPeMultiSPath in NeighborSPAth
        CentiScaPeMultiSPath NeighborSPath = findSPath(NeighBor, TempSet);
        // if yes then add NeighborSPath to the Queue and set the cost and predecessor
        if (NeighborSPath != null) {
            NeighborSPath.setCost(CurrentSPath.getCost() + 1);
            NeighborSPath.addPredecessor(CurrentSPath);
            Queue.addLast(NeighborSPath);
            // then remove it from the TempSet
            TempSet.remove(NeighborSPath);
        } else {
            // if Neighbor is not in TempSet verify if it is in Queue
            NeighborSPath = findSPath(NeighBor, Queue);
            // if yes put it in NeighborSPath
            if (NeighborSPath != null) {
                // then verify if its cost is greater then the one of the current SPath
                if (NeighborSPath.getCost() > CurrentSPath.getCost()) {
                    // if yes we have found a new minimium shortestpath so we have another predecessor
                    // for neighbor. we update cost(useless) and we add the new predecessor
                    NeighborSPath.setCost(CurrentSPath.getCost() + 1);
                    NeighborSPath.addPredecessor(CurrentSPath);
                }
            }
        }
    }

    // Verify if Node is in TempSet and return the corresponding
    // CentiScaPeMultiSPath element
    public static CentiScaPeMultiSPath findSPath(CyNode Node, Collection TempSet) {
        CentiScaPeMultiSPath foundSPath = null;
        for (Iterator i = TempSet.iterator(); i.hasNext();) {
            CentiScaPeMultiSPath tempSPath = (CentiScaPeMultiSPath) i.next();
            if (Node.equals(tempSPath.getNode())) {
                foundSPath = tempSPath;
                break;
            }
        }
        return foundSPath;
    }

 
    public static double createShortestPathVector(CentiScaPeShortestPathList spathlist, CentiScaPeMultiSPath tmpspath, Vector ShortestPathVector, CyNode root) {
        // set vectorsize equals to the size of the vector predecessor
        int vectorsize = tmpspath.PredecessorVectorSize();
        // add the element tmpspath to the spathlist representing the current shortestpath
        spathlist.addFirst(tmpspath);
        // if node is not root we enter to recorsive calls
        if (vectorsize != 0) {
            // we create a copy of spathlist
            CentiScaPeShortestPathList tmplist = (CentiScaPeShortestPathList) spathlist.clone();
            double currentstressvalue = 0;
            CyNode currentfathernode = tmpspath.getNode();
            long currentfathernodename = currentfathernode.getSUID();
            for (int i = 0; i < vectorsize; i++) {
                // the first predecessor build the Shortestpath in the current element of the vector
                //  CyNode currentfathernode = tmpspath.getNode();
                // String currentfathernodename = currentfathernode.getIdentifier();
                //double currentstressvalue = (Double)currentmap.get(currentfathernodename);
                if (i == 0) {
                    CentiScaPeMultiSPath newMultiSPath = tmpspath.getPredecessor(i);
                    // predecessor is a root
                    if (newMultiSPath.getNode().equals(root)) {
                        // System.out.println("entro nel preroot");
                        if (!(currentfathernode.getSUID() == nodotarget.getSUID())) {
                            currentstressvalue = createShortestPathVector(spathlist, newMultiSPath, ShortestPathVector, root);
                        //currentmap.put(currentfathernodename, currentstressvalue);
                        } else {
                            createShortestPathVector(spathlist, newMultiSPath, ShortestPathVector, root);
                            currentstressvalue = 0;
                        }
                    } else {
                        // System.out.println("corrente stressvalue per allandata" + currentfathernodename + " con predecessore " +
                        // newMultiSPath.getName() + " = " + currentstressvalue);
                        if (currentfathernode.getSUID() == nodotarget.getSUID()) {
                            createShortestPathVector(spathlist, newMultiSPath, ShortestPathVector, root);
                        } else {
                            currentstressvalue = currentstressvalue + createShortestPathVector(spathlist, newMultiSPath, ShortestPathVector, root);
                        
                        }
                    }

                } else {
                    //if there are more predecessors we have to build a new vector element
                    //for each predecessor exceeding the first. This because each predecessor contributes
                    //for a different shortest path
                    CentiScaPeMultiSPath newMultiSPath = tmpspath.getPredecessor(i);
                    CentiScaPeShortestPathList newlist = (CentiScaPeShortestPathList) tmplist.clone();
                    ShortestPathVector.addElement(newlist);
                    //    System.out.println("corrente stressvalue per allandata altro predec" + currentfathernodename + " con predecessore " +
                    //     newMultiSPath.getName() + " = " + currentstressvalue);
                    if (currentfathernode.getSUID() == nodotarget.getSUID()) {
                        createShortestPathVector(newlist, newMultiSPath, ShortestPathVector, root);
                    } else {

                        currentstressvalue = currentstressvalue + createShortestPathVector(newlist, newMultiSPath, ShortestPathVector, root);
                      }

                }
            }
            if (StressisOn) {
                // double valuetoput = (Double) currentmap.get(currentfathernodename) + currentstressvalue;

                CentiScaPeAlgorithm.Stressmap.put(currentfathernodename, (Double) CentiScaPeAlgorithm.Stressmap.get(currentfathernodename) + currentstressvalue);
              }
            if (BtwisOn) {
                
                  currentmap.put(currentfathernodename, (Double) currentmap.get(currentfathernodename) + currentstressvalue);
            }
            return currentstressvalue;
        } else {
            //System.out.println("currentmap prima di return 1 =" + currentmap.toString());
            return 1;
        }
    }
}


