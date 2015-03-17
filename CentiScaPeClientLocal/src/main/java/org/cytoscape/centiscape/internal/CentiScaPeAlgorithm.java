/*
 * CentiScaPeAlgorithm.java
 *
 * Created on 7 marzo 2007, 10.01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
/**
 *
 * @author scardoni
 */
package org.cytoscape.centiscape.internal; 

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.cytoscape.view.model.CyNetworkView;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.SwingConstants; 
import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelState;
import org.cytoscape.centiscape.internal.Betweenness.BetweennessMethods;
import org.cytoscape.centiscape.internal.Betweenness.FinalResultBetweenness;
import org.cytoscape.centiscape.internal.Centroid.CentroidMethods;
import org.cytoscape.centiscape.internal.Centroid.FinalResultCentroid;
import org.cytoscape.centiscape.internal.Closeness.ClosenessMethods;
import org.cytoscape.centiscape.internal.Closeness.FinalResultCloseness;
import org.cytoscape.centiscape.internal.Degree.FinalResultDegree;
import org.cytoscape.centiscape.internal.Eccentricity.EccentricityMethods;
import org.cytoscape.centiscape.internal.Eccentricity.FinalResultEccentricity;
import org.cytoscape.centiscape.internal.Radiality.FinalResultRadiality;
import org.cytoscape.centiscape.internal.Stress.FinalResultStress;
import org.cytoscape.centiscape.internal.visualizer.CentVisualizer;
import org.cytoscape.centiscape.internal.visualizer.ImplCentrality;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;

public class CentiScaPeAlgorithm {

    private boolean stop = false;
    // one Vector for each centrality
    private Vector ClosenessVectorResults;
    private Vector EccentricityVectorResults;
    private static Vector StressVectorResults;
    private Vector RadialityVectorResults;
    private static Vector BetweennessVectorResults;
    private static Vector CentroidVectorResults;
    private static Vector CentroidVectorofNodes;
    private Vector DegreeVectorResults;
    private double Diameter = 0;
    private double average = 0;
    private double totaldist;
    // variables to verify the selected centralities
    private boolean StressisOn = false;
    private boolean ClosenessisOn = false;
    private boolean EccentricityisOn = false;
    private boolean RadialityisOn = false;
    private boolean DiameterisOn = false;
    private boolean DiameterisSelected = false;
    private boolean BetweennessisOn = false;
    private boolean CentroidisOn = false;
    private boolean DegreeisOn = false;
    private boolean AverageisOn = false;
    public static TreeMap Stressmap = new TreeMap();
    //static CyNetworkView vista = Cytoscape.getCurrentNetworkView();
    Vector vectorOfNodeAttributes = new Vector();
    Vector vectorOfNetworkAttributes = new Vector();
    //   public Vector<Centrality> VectorResults = new Vector();
    public Vector VectorResults = new Vector();
    public boolean openResultPanel;
    public String networkname;
    public CyNetwork network;
    public CentiScaPeCore centiscapecore;

    /**
     * Creates a new instance of CentiScaPeAlgorithm
     */
    public CentiScaPeAlgorithm(CentiScaPeCore centiscapecore) {
        this.centiscapecore = centiscapecore; 
    }

    public void ExecuteCentiScaPeAlgorithm(CyNetwork network, CyNetworkView view, JPanel c) {

        stop = false;
        this.network = network;

        openResultPanel = false;

        CentiScaPeStartMenu menustart = (CentiScaPeStartMenu) c;
        // JOptionPane.showMessageDialog(view.getComponent(),
        //          "comincio1 = " );

        System.out.println(network.toString());
        String networkname = network.getDefaultNetworkTable().getRow(network.getSUID()).get("name", String.class);
        //  network.getRow(network).get("name", String.class);
        //    System.out.println("la network selected e' " + currentnetwork.getTable(CyNode.class, currentnetworkname).toString());
   //     System.out.println("la network e' " + networkname);

        int totalnodecount = network.getNodeCount();
        Long networksuid = network.getSUID();


        int nodeworked = 0;
        Vector CentiScaPeMultiShortestPathVector = null;

        CyTable nodeTable = network.getDefaultNodeTable();
        CyTable networkTable = network.getDefaultNetworkTable();

        // CyAttributes currentNetworkAttributes = Cytoscape.getNetworkAttributes();
        // CyAttributes currentNodeAttributes = Cytoscape.getNodeAttributes();

        for (Iterator i = vectorOfNodeAttributes.iterator(); i.hasNext();) {

            String attributetoremove = (String) i.next();
            //  System.out.println("rimuovo" + attributetoremove );

            if (nodeTable.getColumn(attributetoremove) != null) {
                nodeTable.deleteColumn(attributetoremove);
            }
            //currentNodeAttributes.deleteAttribute(attributetoremove);

            //  System.out.println("ho rimosso" + attributetoremove );
        }

        for (Iterator i = vectorOfNetworkAttributes.iterator(); i.hasNext();) {

            String attributetoremove = (String) i.next();
            //  System.out.println("rimuovo" + attributetoremove );
            if (networkTable.getColumn(attributetoremove) != null) {
                nodeTable.deleteColumn(attributetoremove);
            }

            //currentNetworkAttributes.deleteAttribute(attributetoremove);
            //System.out.println("ho rimosso" + attributetoremove );
        }

        // Create the data structure for each selected centralities
        if (ClosenessisOn) {
            ClosenessVectorResults = new Vector();
            openResultPanel = true;
        }
        if (EccentricityisOn) {
            EccentricityVectorResults = new Vector();
            openResultPanel = true;
        }
        if (DegreeisOn) {
            DegreeVectorResults = new Vector();
            openResultPanel = true;
        }
        if (RadialityisOn) {
            RadialityVectorResults = new Vector();
            openResultPanel = true;
        }
        if (DiameterisOn) {
            //double currentDiametervalue = 0;
        }

        if (AverageisOn) {
            totaldist = 0;
        }

        if (BetweennessisOn || StressisOn || CentroidisOn) {
            openResultPanel = true;
            //  JOptionPane.showMessageDialog(view.getComponent(),
            //     "Betweennesson " );
            if (BetweennessisOn) {
                BetweennessVectorResults = new Vector();
            }
            if (StressisOn) {
                StressVectorResults = new Vector();
            }
            if (CentroidisOn) {

                CentroidVectorResults = new Vector();
                CentroidVectorofNodes = new Vector();
            }
            Stressmap.clear();
            //    for (Iterator i = view.getNodeViewsIterator(); i.hasNext();) {
            for (Iterator i = network.getNodeList().iterator(); i.hasNext();) {
                if (stop) {
                    unselectallnodes(network);
                    return;
                }

                // NodeView nView = (NodeView) i.next();
                CyNode root = (CyNode) i.next();
                if (BetweennessisOn) {
                    BetweennessVectorResults.add(new FinalResultBetweenness(root, 0));
                }
                if (StressisOn || BetweennessisOn) {

                    // StressVectorResults.add(new FinalResultStress(root.getSUID(), 0));
                    Stressmap.put(root.getSUID(), new Double(0));
                    //            System.out.println("la Stressmap iniziale ?? questa" + Stressmap.toString());
                }
                if (CentroidisOn) {

                    CentroidVectorofNodes.addElement(root.getSUID());
                }

            }
        }
        TreeMap inizializedmap = new TreeMap(Stressmap);
        // Start iteration on each node
        Diameter = 0;
        //   for (Iterator i = view.getNodeViewsIterator(); i.hasNext();) {
        for (Iterator i = network.getNodeList().iterator(); i.hasNext();) {

            if (stop) {
                unselectallnodes(network);
                return;
            }
            nodeworked++;
            menustart.updatenodecounting(nodeworked, totalnodecount);
//            NodeView nView = (NodeView) i.next();

            CyNode root = (CyNode) i.next();

            // Execute the multi shortest path algorithm for node root and put the results on the
            // vector called ShortestPathVector
            //CentiScaPeMultiShortestPathTreeAlgorithm newalgorithm = new CentiScaPeMultiShortestPathTreeAlgorithm();
            //  CentiScaPeMultiShortestPathVector = CentiScaPeMultiShortestPathTreeAlgorithm.ExecuteMultiShortestPathTreeAlgorithm(network, view, root, StressisOn, BetweennessisOn,inizializedmap);
            CentiScaPeMultiShortestPathVector = CentiScaPeMultiShortestPathTreeAlgorithm.ExecuteMultiShortestPathTreeAlgorithm(network, root, StressisOn, BetweennessisOn, inizializedmap);

            // Create a Single Shortest Path Vector

            Vector CentiScaPeSingleShortestPathVector = new Vector();
            Vector NodesFound = new Vector();
            CentiScaPeShortestPathList CurrentList;
            for (int j = 0; j < CentiScaPeMultiShortestPathVector.size(); j++) {

                CurrentList = (CentiScaPeShortestPathList) CentiScaPeMultiShortestPathVector.get(j);
                String nodename = ((CentiScaPeMultiSPath) CurrentList.getLast()).getName();
                if (!NodesFound.contains(nodename)) {
                    NodesFound.add(nodename);
                    CentiScaPeSingleShortestPathVector.add(CurrentList);
                }
            }
            // Calculate each properties

            if (ClosenessisOn) {

                ClosenessVectorResults.add((FinalResultCloseness) ClosenessMethods.CalculateCloseness(CentiScaPeSingleShortestPathVector, root));
            }

            if (EccentricityisOn) {
                EccentricityVectorResults.add(EccentricityMethods.CalculateEccentricity(CentiScaPeSingleShortestPathVector, root));

            }
            if (DegreeisOn) {
                FinalResultDegree currentDegree = new FinalResultDegree(root, 0);

                double currentDegreevalue = network.getNeighborList(root, CyEdge.Type.ANY).size();
                currentDegree.update(currentDegreevalue);
                DegreeVectorResults.addElement(currentDegree);
            }

            if (RadialityisOn) {
                CentiScaPeShortestPathList currentlist;
                //RadialityVectorResults.add (CalculateRadiality(CentiScaPeSingleShortestPathVector,root));
                FinalResultRadiality currentRadiality = new FinalResultRadiality(root, 0);
                for (int j = 0; j < CentiScaPeSingleShortestPathVector.size(); j++) {
                    currentlist = (CentiScaPeShortestPathList) CentiScaPeSingleShortestPathVector.elementAt(j);
                    currentRadiality.updatesizevector(new Integer(currentlist.size() - 1));
                }
                RadialityVectorResults.add(currentRadiality);
            }
            if (DiameterisOn || DiameterisSelected) {

                double currentdiametervalue;
                currentdiametervalue = CalculateDiameter(CentiScaPeSingleShortestPathVector);
                if (Diameter < currentdiametervalue) {
                    Diameter = currentdiametervalue;
                }
            }
            if (AverageisOn) {
                CentiScaPeShortestPathList currentlist;
                for (int j = 0; j < CentiScaPeSingleShortestPathVector.size(); j++) {
                    currentlist = (CentiScaPeShortestPathList) CentiScaPeSingleShortestPathVector.elementAt(j);
                    totaldist = totaldist + currentlist.size() - 1;
                }

                //  JOptionPane.showMessageDialog(view.getComponent(),
                //       "totaldist = " + totaldist  );

            }


            if (BetweennessisOn) {
               // BetweennessVectorResults.add(new FinalResultBetweenness(root,0));
                BetweennessMethods.updateBetweenness(CentiScaPeMultiShortestPathVector, BetweennessVectorResults);
            }

            if (StressisOn) {
                // StressMethods.updateStress(CentiScaPeMultiShortestPathVector, StressVectorResults);
            }

            if (CentroidisOn) {

                CentroidMethods.updateCentroid(CentiScaPeSingleShortestPathVector, root, totalnodecount, CentroidVectorofNodes, CentroidVectorResults);


            }
        }


        unselectallnodes(network);

        if (RadialityisOn) {

            //  JOptionPane.showMessageDialog(view.getComponent(),
            //     "calcolo finale radiality" );
            for (Iterator i = RadialityVectorResults.iterator(); i.hasNext();) {
                if (stop) {
                    unselectallnodes(network);
                    return;
                }
                FinalResultRadiality currentRadiality;
                currentRadiality = (FinalResultRadiality) i.next();
                double Diametervalue = Diameter;
                int currentDist = 0;
                double currentRadialityvalue, parziale1, parziale2;
                for (int j = 0; j < currentRadiality.getVectorSize(); j++) {
                    currentDist = currentRadiality.getlistsizeat(j);
                    if (currentDist != 0) {
                        parziale1 = Diametervalue + 1 - currentDist;
                        parziale2 = totalnodecount - 1;
                        currentRadialityvalue = ((Diametervalue + 1 - currentDist));
                        currentRadiality.update(currentRadialityvalue);
                    }
                }
                currentRadiality.finalcalculus(totalnodecount - 1);
            }
            //RadialityVectorResults.add (CalculateRadiality(CentiScaPeSingleShortestPathVector,root));

        }

        if (CentroidisOn) {

            CentroidMethods.calculateCentroid(CentroidVectorResults, totalnodecount, CentroidVectorofNodes);



        }


        menustart.endcalculus(totalnodecount);
        // CyAttributes currentNetworkAttributes = Cytoscape.getNetworkAttributes();


        VectorResults.clear();


        if (DiameterisSelected) {
            // JOptionPane.showMessageDialog(view.getComponent(),
            // "diametro " + Diameter);
            networkTable.createColumn("CentiScaPe Diameter", Double.class, false);
            network.getRow(network).set("CentiScaPe Diameter", new Double(Diameter));

            vectorOfNetworkAttributes.addElement("CentiScaPe Diameter");
        }

        if (AverageisOn) {

            average = totaldist / (totalnodecount * (totalnodecount - 1));
            networkTable.createColumn("CentiScaPe Average Distance", Double.class, false);
            network.getRow(network).set("CentiScaPe Average Distance", new Double(average));

            vectorOfNetworkAttributes.addElement("CentiScaPe Average Distance");
            //JOptionPane.showMessageDialog(view.getComponent(),
            //     "average distance = " + average );
        }


        // CyAttributes currentNodeAttributes = Cytoscape.getNodeAttributes();

        if (EccentricityisOn) {
            nodeTable.createColumn("CentiScaPe Eccentricity", Double.class, false);
            vectorOfNodeAttributes.addElement("CentiScaPe Eccentricity");
            double min = Double.MAX_VALUE, max = -Double.MAX_VALUE, totalsum = 0, currentvalue;
            for (Iterator i = EccentricityVectorResults.iterator(); i.hasNext();) {

                FinalResultEccentricity currentnodeeccentricity = (FinalResultEccentricity) i.next();
                double currenteccentricity = currentnodeeccentricity.geteccentricity();
                if (currenteccentricity < min) {
                    min = currenteccentricity;
                }
                if (currenteccentricity > max) {
                    max = currenteccentricity;
                }
                totalsum = totalsum + currenteccentricity;

                CyRow row = nodeTable.getRow(currentnodeeccentricity.getNode().getSUID());
                row.set("CentiScaPe Eccentricity", new Double(currenteccentricity));




            }

            //currentNodeAttributes.setUserVisible("CentiScaPe Eccentricity", true);


            networkTable.createColumn("CentiScaPe Eccentricity Max value", Double.class, false);
            networkTable.createColumn("CentiScaPe Eccentricity min value", Double.class, false);
            double mean = totalsum / totalnodecount;
            networkTable.createColumn("CentiScaPe Eccentricity mean value", Double.class, false);
            network.getRow(network).set("CentiScaPe Eccentricity Max value", new Double(max));
            network.getRow(network).set("CentiScaPe Eccentricity min value", new Double(min));
            network.getRow(network).set("CentiScaPe Eccentricity mean value", new Double(mean));

            vectorOfNetworkAttributes.addElement("CentiScaPe Eccentricity Max value");
            vectorOfNetworkAttributes.addElement("CentiScaPe Eccentricity min value");
            vectorOfNetworkAttributes.addElement("CentiScaPe Eccentricity mean value");

          //  System.out.println("aggiungo eccentricity il network identifiere e' = " + networkname);
            ImplCentrality eccentricityCentrality = new ImplCentrality("CentiScaPe Eccentricity", true, mean, min, max);
            //    JOptionPane.showMessageDialog(view.getComponent(),
            //           "ecc. totalsum " + totalsum / totalnodecount + "min " + min + "max" + max);
            VectorResults.add(eccentricityCentrality);
        }

        if (ClosenessisOn) {
            nodeTable.createColumn("CentiScaPe Closeness", Double.class, false);
            vectorOfNodeAttributes.addElement("CentiScaPe Closeness");
            double min = Double.MAX_VALUE, max = -Double.MAX_VALUE, totalsum = 0, currentvalue;
            for (Iterator i = ClosenessVectorResults.iterator(); i.hasNext();) {
                FinalResultCloseness currentnodecloseness = (FinalResultCloseness) i.next();
                double currentcloseness = currentnodecloseness.getCloseness();


                if (currentcloseness < min) {
                    min = currentcloseness;
                }
                if (currentcloseness > max) {
                    max = currentcloseness;
                }
                totalsum = totalsum + currentcloseness;

                CyRow row = nodeTable.getRow(currentnodecloseness.getNode().getSUID());
                row.set("CentiScaPe Closeness", new Double(currentcloseness));
                ///  currentNodeAttributes.setAttribute(getName(currentnodecloseness.getNode(),network), "CentiScaPe Closeness", currentcloseness);
            }
            ///currentNodeAttributes.setUserVisible("CentiScaPe Closeness", true);


            networkTable.createColumn("CentiScaPe Closeness Max value", Double.class, false);
            networkTable.createColumn("CentiScaPe Closeness min value", Double.class, false);
            double mean = totalsum / totalnodecount;
            networkTable.createColumn("CentiScaPe Closeness mean value", Double.class, false);
            network.getRow(network).set("CentiScaPe Closeness Max value", new Double(max));
            network.getRow(network).set("CentiScaPe Closeness min value", new Double(min));
            network.getRow(network).set("CentiScaPe Closeness mean value", new Double(mean));

            ///currentNetworkAttributes.setAttribute(networkname, "CentiScaPe Closeness mean value", mean);
            vectorOfNetworkAttributes.addElement("CentiScaPe Closeness Max value");
            vectorOfNetworkAttributes.addElement("CentiScaPe Closeness min value");
            vectorOfNetworkAttributes.addElement("CentiScaPe Closeness mean value");
            ImplCentrality closenessCentrality = new ImplCentrality("CentiScaPe Closeness", true, mean, min, max);
            VectorResults.add(closenessCentrality);
        }


        if (RadialityisOn) {
            nodeTable.createColumn("CentiScaPe Radiality", Double.class, false);
            vectorOfNodeAttributes.addElement("CentiScaPe Radiality");
            double min = Double.MAX_VALUE, max = -Double.MAX_VALUE, totalsum = 0, currentvalue;
            for (Iterator i = RadialityVectorResults.iterator(); i.hasNext();) {

                FinalResultRadiality currentnoderadiality = (FinalResultRadiality) i.next();

                double currentradiality = currentnoderadiality.getRadiality();

                if (currentradiality < min) {
                    min = currentradiality;
                }
                if (currentradiality > max) {
                    max = currentradiality;
                }
                totalsum = totalsum + currentradiality;
                CyRow row = nodeTable.getRow(currentnoderadiality.getNode().getSUID());
                row.set("CentiScaPe Radiality", new Double(currentradiality));
                /// currentNodeAttributes.setAttribute(getName(currentnoderadiality.getNode(),network), "CentiScaPe Radiality", currentradiality);

            }
            networkTable.createColumn("CentiScaPe Radiality Max value", Double.class, false);
            networkTable.createColumn("CentiScaPe Radiality min value", Double.class, false);
            double mean = totalsum / totalnodecount;
            networkTable.createColumn("CentiScaPe Radiality mean value", Double.class, false);
            network.getRow(network).set("CentiScaPe Radiality Max value", new Double(max));
            network.getRow(network).set("CentiScaPe Radiality min value", new Double(min));
            network.getRow(network).set("CentiScaPe Radiality mean value", new Double(mean));
            vectorOfNetworkAttributes.addElement("CentiScaPe Radiality Max value");
            vectorOfNetworkAttributes.addElement("CentiScaPe Radiality min value");
            vectorOfNetworkAttributes.addElement("CentiScaPe Radiality mean value");
            ImplCentrality radialityCentrality = new ImplCentrality("CentiScaPe Radiality", true, mean, min, max);
            VectorResults.add(radialityCentrality);

        }

        if (BetweennessisOn) {
            nodeTable.createColumn("CentiScaPe Betweenness", Double.class, false);
            vectorOfNodeAttributes.addElement("CentiScaPe Betweenness");
            double min = Double.MAX_VALUE, max = -Double.MAX_VALUE, totalsum = 0, currentvalue;

            for (Iterator i = BetweennessVectorResults.iterator(); i.hasNext();) {

                FinalResultBetweenness currentnodebetweenness = (FinalResultBetweenness) i.next();

                double currentbetweenness = currentnodebetweenness.getBetweenness();

                if (currentbetweenness < min) {
                    min = currentbetweenness;
                }
                if (currentbetweenness > max) {
                    max = currentbetweenness;
                }
                totalsum = totalsum + currentbetweenness;
                
                
            CyRow row = nodeTable.getRow(currentnodebetweenness.getNode().getSUID());
             row.set("CentiScaPe Betweenness", new Double(currentbetweenness));
              }
            networkTable.createColumn("CentiScaPe Betweenness Max value", Double.class, false);
            networkTable.createColumn("CentiScaPe Betweenness min value", Double.class, false);
            double mean = totalsum / totalnodecount;
            networkTable.createColumn("CentiScaPe Betweenness mean value", Double.class, false);
            network.getRow(network).set("CentiScaPe Betweenness Max value", new Double(max));
            network.getRow(network).set("CentiScaPe Betweenness min value", new Double(min));
            network.getRow(network).set("CentiScaPe Betweenness mean value", new Double(mean));
            vectorOfNetworkAttributes.addElement("CentiScaPe Betweenness Max value");
            vectorOfNetworkAttributes.addElement("CentiScaPe Betweenness min value");
            vectorOfNetworkAttributes.addElement("CentiScaPe Betweenness mean value");
            ImplCentrality betweennessCentrality = new ImplCentrality("CentiScaPe Betweenness", true, mean, min, max);
            VectorResults.add(betweennessCentrality);

        }

        if (DegreeisOn) {
            nodeTable.createColumn("CentiScaPe Node Degree", Double.class, false);

            vectorOfNodeAttributes.addElement("CentiScaPe Node Degree");
            double min = Double.MAX_VALUE, max = -Double.MAX_VALUE, totalsum = 0, currentvalue;
            for (Iterator i = DegreeVectorResults.iterator(); i.hasNext();) {

                FinalResultDegree currentnodeDegree = (FinalResultDegree) i.next();

                double currentdegree = currentnodeDegree.getDegree();

                if (currentdegree < min) {
                    min = currentdegree;
                }
                if (currentdegree > max) {
                    max = currentdegree;
                }
                totalsum = totalsum + currentdegree;
                CyRow row = nodeTable.getRow(currentnodeDegree.getNode().getSUID());
                row.set("CentiScaPe Node Degree", new Double(currentdegree));
                /// currentNodeAttributes.setAttribute(currentnodeDegree.getNode(), "CentiScaPe Node Degree", currentdegree);

            }
            networkTable.createColumn("CentiScaPe Degree Max value", Double.class, false);
            networkTable.createColumn("CentiScaPe Degree min value", Double.class, false);
            double mean = totalsum / totalnodecount;
            networkTable.createColumn("CentiScaPe Degree mean value", Double.class, false);
            network.getRow(network).set("CentiScaPe Degree Max value", new Double(max));
            network.getRow(network).set("CentiScaPe Degree min value", new Double(min));
            network.getRow(network).set("CentiScaPe Degree mean value", new Double(mean));
            vectorOfNetworkAttributes.addElement("CentiScaPe Degree Max value");
            vectorOfNetworkAttributes.addElement("CentiScaPe Degree min value");
            vectorOfNetworkAttributes.addElement("CentiScaPe Degree mean value");
            ImplCentrality degreeCentrality = new ImplCentrality("CentiScaPe Node Degree", true, mean, min, max);
            VectorResults.add(degreeCentrality);
        }


        if (StressisOn) {
            nodeTable.createColumn("CentiScaPe Stress", Double.class, false);
            vectorOfNodeAttributes.addElement("CentiScaPe Stress");



            double min = Double.MAX_VALUE, max = -Double.MAX_VALUE, totalsum = 0, currentvalue;
            Set stressSet = Stressmap.entrySet();
            //  System.out.println("il set stress ?? " + stressSet.toString() );
            for (Iterator i = stressSet.iterator(); i.hasNext();) {
                Map.Entry currentmapentry = (Map.Entry) i.next();
                long currentnodeSUID = (Long) currentmapentry.getKey();
                CyNode currentnode = network.getNode(currentnodeSUID);
                // String currentnodename = (String) currentmapentry.getKey();
                double currentstress = (double) (Double) (currentmapentry.getValue());
                StressVectorResults.add(new FinalResultStress(currentnode, currentstress));
                //  for (Iterator i = StressVectorResults.iterator(); i.hasNext();) {

                //FinalResultStress currentnodeStress = (FinalResultStress) i.next();

                //double currentstress = currentnodeStress.getStress();

                if (currentstress < min) {
                    min = currentstress;
                }
                if (currentstress > max) {
                    max = currentstress;
                }
                totalsum = totalsum + currentstress;
                CyRow row = nodeTable.getRow(currentnodeSUID);
                row.set("CentiScaPe Stress", new Double(currentstress));
                /// currentNodeAttributes.setAttribute(currentnodename, "CentiScaPe Stress", currentstress);

            }
            networkTable.createColumn("CentiScaPe Stress Max value", Double.class, false);
            networkTable.createColumn("CentiScaPe Stress min value", Double.class, false);
            double mean = totalsum / totalnodecount;
            networkTable.createColumn("CentiScaPe Stress mean value", Double.class, false);
            network.getRow(network).set("CentiScaPe Stress Max value", new Double(max));
            network.getRow(network).set("CentiScaPe Stress min value", new Double(min));
            network.getRow(network).set("CentiScaPe Stress mean value", new Double(mean));
            vectorOfNetworkAttributes.addElement("CentiScaPe Stress Max value");
            vectorOfNetworkAttributes.addElement("CentiScaPe Stress min value");
            vectorOfNetworkAttributes.addElement("CentiScaPe Stress mean value");
            ImplCentrality stressCentrality = new ImplCentrality("CentiScaPe Stress", true, mean, min, max);
            VectorResults.add(stressCentrality);

        }
        if (CentroidisOn) {
            nodeTable.createColumn("CentiScaPe Centroid", Double.class, false);
            vectorOfNodeAttributes.addElement("CentiScaPe Centroid");
            double min = Double.MAX_VALUE, max = -Double.MAX_VALUE, totalsum = 0, currentvalue;
            for (Iterator i = CentroidVectorResults.iterator(); i.hasNext();) {


                FinalResultCentroid currentnodeCentroid = (FinalResultCentroid) i.next();

                double currentcentroid = currentnodeCentroid.getCentroid();

                if (currentcentroid < min) {
                    min = currentcentroid;
                }
                if (currentcentroid > max) {
                    max = currentcentroid;
                }
                totalsum = totalsum + currentcentroid;
                CyRow row = nodeTable.getRow(currentnodeCentroid.getNode().getSUID());
                row.set("CentiScaPe Centroid", new Double(currentcentroid));
                /// currentNodeAttributes.setAttribute(getName(currentnodeCentroid.getNode(),network), "CentiScaPe Centroid", currentcentroid);

            }
             networkTable.createColumn("CentiScaPe Centroid Max value", Double.class, false);
            networkTable.createColumn("CentiScaPe Centroid min value", Double.class, false);
            double mean = totalsum / totalnodecount;
            networkTable.createColumn("CentiScaPe Centroid mean value", Double.class, false);
            network.getRow(network).set("CentiScaPe Centroid Max value", new Double(max));
            network.getRow(network).set("CentiScaPe Centroid min value", new Double(min));
            network.getRow(network).set("CentiScaPe Centroid mean value", new Double(mean));
      
            vectorOfNetworkAttributes.addElement("CentiScaPe Centroid Max value");
            vectorOfNetworkAttributes.addElement("CentiScaPe Centroid min value");
            vectorOfNetworkAttributes.addElement("CentiScaPe Centroid mean value");
            ImplCentrality centroidCentrality = new ImplCentrality("CentiScaPe Centroid", true, mean, min, max);
            VectorResults.add(centroidCentrality);
        }

        centiscapecore.createCentiScaPeVisualizer();
//System.out.println("si vede visualizer?");
     
       // System.out.println("il vectore result ?? " + VectorResults.toString());
        centiscapecore.getvisualizer().setEnabled(VectorResults);
        ///CytoscapeDesktop desktop = Cytoscape.getDesktop();
        //CytoPanel cytoPaneleast = desktop.getCytoPanel(SwingConstants.EAST);

        //int index = cytoPaneleast.indexOfComponent(visualizer);
        //cytoPaneleast.setSelectedIndex(index);
        //cytoPaneleast.add("CentiScaPe Results", visualizer);
        if (openResultPanel) {
            /// cytoPaneleast.setState(CytoPanelState.DOCK);
        } else {
            ///cytoPaneleast.setState(CytoPanelState.HIDE);
        }


    }

    public void endalgorithm() {
        stop = true;
    }

    public void setChecked(boolean[] ison) {


        // Diameter = checkbox 0
        DiameterisOn = ison[4];
        DiameterisSelected = ison[0];
        // Average-Distance = checkbox 1
        AverageisOn = ison[1];
        // Degree = checkbox 2
        DegreeisOn = ison[2];
        // Eccentricity = checkbox 3
        EccentricityisOn = ison[3];
        // Radiality = checkbox 4
        RadialityisOn = ison[4];
        // Closeness = checkbox 5
        ClosenessisOn = ison[5];
        // Stress = checkbox 6
        StressisOn = ison[6];
        // Betweenness = checkbox 7
        BetweennessisOn = ison[7];
        // Centroid = checkbox[8];
        CentroidisOn = ison[8];


    }

    public void unselectallnodes(CyNetwork network) {
        for (Iterator i = network.getNodeList().iterator(); i.hasNext();) {
            CyNode tmpnode = (CyNode) i.next();
            CyRow row = network.getRow(tmpnode);
            row.set("selected", true);
        }
    }

    public double CalculateDiameter(Vector SingleShortestPathVector) {
        CentiScaPeShortestPathList currentdiameterlist;
        int currentmaxvalue = 0;
        double currentvalue = 0;
        for (int j = 0; j < SingleShortestPathVector.size(); j++) {
            currentdiameterlist = (CentiScaPeShortestPathList) SingleShortestPathVector.elementAt(j);
            //    currentRadiality.updatesizevector(new Integer(currentlist.size()-1));
            currentmaxvalue = Math.max(currentmaxvalue, currentdiameterlist.size() - 1);
        }
        if (currentmaxvalue > currentvalue) {
            currentvalue = ((double) currentmaxvalue);
            //   JOptionPane.showMessageDialog(view.getComponent(),
            //   "diametro current" + currentDiametervalue);
        }
        return currentvalue;
    }

    public String getName(CyNode currentnode, CyNetwork currentnetwork) {
        return currentnetwork.getRow(currentnode).get("name", String.class);

    }
}
