package org.cytoscape.centiscape.internal;

/*
 * CentiScaPe.java
 *
 * Created on 17 gennaio 2007, 11.21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
/**
 *
 * @author Diego Magagna
 */

import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.JOptionPane;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.CyTableUtil;
import org.cytoscape.view.model.CyNetworkView;

public class Eccentricitynew  {
    
    public static String version = "1.21";
  
        public Eccentricitynew(CyNetwork network ) {                 
                    
            CyTable nodeTable=network.getDefaultNodeTable();    //attributi
            CyTable networkTable=network.getDefaultNetworkTable();
            
            //Creo la colonna sulla tabella per il Centroide 
            nodeTable.deleteColumn("CentiScaPe Eccentricity");
            nodeTable.createColumn("CentiScaPe Eccentricity", Double.class, false);
            Vector vettore= new Vector();              
            
                //Ottengo tutti i nodi
                LinkedList<CyNode> allnode=new LinkedList<CyNode>();
                allnode.addAll(CyTableUtil.getNodesInState(network,"selected",false));             
                allnode.addAll(CyTableUtil.getNodesInState(network,"selected",true));   
                                
                //Per tutti i nodi            
                for(Iterator u=allnode.iterator();u.hasNext();)
                {
                    CyNode nodeU=(CyNode)u.next(); 
                    long idnodeU=nodeU.getSUID();  //SUID del nodo U                
                    TreeMap tree=new TreeMap();
                    Vector vettore_percorsi= new Vector();
                    vettore_percorsi=CentiScaPeMultiShortestPathTreeAlgorithm.ExecuteMultiShortestPathTreeAlgorithm(network,nodeU,false,false,tree);
                    double max=0;
                    for(Iterator s=vettore_percorsi.iterator();s.hasNext();){                                
                        CentiScaPeShortestPathList lista_percorsi=(CentiScaPeShortestPathList)(s.next());                        
                        if(max<(lista_percorsi.size()-1)){
                            max=lista_percorsi.size()-1;
                        }
                    }
                    LinkedList<CyRow> row=new LinkedList<CyRow>();
                    row.addAll( nodeTable.getMatchingRows("SUID", idnodeU));//ottengo tutte le righe che hanno il suid del nodo (per forza 1)
                    CyRow readRow=(row.iterator()).next();
                    readRow.set("CentiScaPe Eccentricity", 1/max);                    
                }
        }
    }
    
   