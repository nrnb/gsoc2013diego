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
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.CyTableUtil;
import org.cytoscape.view.model.CyNetworkView;

public class Centroidnew  {
    
    public static String version = "1.21";
  
    public Centroidnew(CyNetwork network,String attributo) {                 
                     
            CyTable nodeTable=network.getDefaultNodeTable();    //attributi
            CyTable networkTable=network.getDefaultNetworkTable();
            
            //Creo la colonna sulla tabella per il Centroide
            nodeTable.deleteColumn("CentiScaPe Centroid");
            nodeTable.createColumn("CentiScaPe Centroid", Integer.class, false);
            
            //Ottengo tutti i nodi
            LinkedList<CyNode> allnode=new LinkedList<CyNode>();
            allnode.addAll(CyTableUtil.getNodesInState(network,"selected",false));             
            allnode.addAll(CyTableUtil.getNodesInState(network,"selected",true));   
                
            // Tabella con sorgente, destinazione, nodi interni e nè di shortest path presenti con quella sorgente e quella destinazione              
            LinkedList<Object[]> sds=new LinkedList<Object[]>();
                
            //Per tutti i nodi            
            for(Iterator u=allnode.iterator();u.hasNext();)
            {
                CyNode nodeU=(CyNode)u.next(); 
                long idnodeU=nodeU.getSUID();  //SUID del nodo U                
                TreeMap tree=new TreeMap();
                Vector vettore_percorsiU= new Vector();
                vettore_percorsiU=CentiScaPeMultiShortestPathTreeAlgorithm.ExecuteMultiShortestPathTreeAlgorithm(network,nodeU,false,false,tree);
                //Elimino i shortest-path che hanno la stessa destinazione, ed essendo shortest-path avranno anche stessa lunghezza
                vettore_percorsiU=eliminadoppioni(vettore_percorsiU); 
                Vector vettore_percorsiV= new Vector(); 
                int listaFvu[]=new int[allnode.size()-1]; //Lista di F(v,u)
                int k=0;
                for(Iterator v=allnode.iterator();v.hasNext();)
                {                    
                    CyNode nodeV=(CyNode)v.next(); 
                    long idnodeV=nodeV.getSUID();  //SUID del nodo V   
                    if(idnodeU!=idnodeV){
                        vettore_percorsiV=CentiScaPeMultiShortestPathTreeAlgorithm.ExecuteMultiShortestPathTreeAlgorithm(network,nodeV,false,false,tree);                       
                        vettore_percorsiV=eliminadoppioni(vettore_percorsiV);
                        int val[]={0,0};                        
                        for(Iterator s=vettore_percorsiU.iterator();s.hasNext();){
                            CentiScaPeShortestPathList lista_percorsi=(CentiScaPeShortestPathList)(s.next());                            
                            CentiScaPeMultiSPath ultimo =(CentiScaPeMultiSPath) lista_percorsi.getLast();
                            //dato che scorro la lista dei shortest path da U a tutti gli altri nodi: U è il nodo di partenza                                
                            if (ultimo.getSUID()!=idnodeV){ 
                                CentiScaPeShortestPathList trovato=trovaElemento(vettore_percorsiV,ultimo.getSUID());
                                int differenza=lista_percorsi.size()-trovato.size();
                                if(differenza<0)
                                    val[0]++;  
                                if(differenza>0)
                                     val[1]++;                                 
                                //System.out.println("V="+idnodeV+" ultimo"+ultimo.getSUID());                                
                            }                            
                        }
                        //System.out.println(" val U="+val[0]+" val V="+val[1]);
                        listaFvu[k]=((val[0]-val[1]));
                        k++;
                    }
                    
                }
                int min=minimo(listaFvu);
                System.out.println("U="+idnodeU+" Il miniomo è="+min);
                LinkedList<CyRow> row=new LinkedList<CyRow>();
                row.addAll( nodeTable.getMatchingRows("SUID", idnodeU));//ottengo tutte le righe che hanno il suid del nodo (per forza 1)
                CyRow readRow=(row.iterator()).next();
                readRow.set("CentiScaPe Centroid", min);
             }             
    }
    public Vector eliminadoppioni(Vector v){
        Vector nuovo= new Vector();
        for(Iterator s=v.iterator();s.hasNext();){
            CentiScaPeShortestPathList lista_percorsi=(CentiScaPeShortestPathList)(s.next());                            
            CentiScaPeMultiSPath ultimo =(CentiScaPeMultiSPath) lista_percorsi.getLast();                        
            if(!presente(nuovo,ultimo.getSUID())){
                nuovo.add(lista_percorsi);
            }
        }
        
        return nuovo;         
    }
    //Fa la somma del num di elementi con stesso primo e ultimo
    public CentiScaPeShortestPathList trovaElemento(Vector v,long elemento){
        for(Iterator s=v.iterator();s.hasNext();){
            CentiScaPeShortestPathList lista_percorsi=(CentiScaPeShortestPathList)(s.next());
            CentiScaPeMultiSPath ultimo =(CentiScaPeMultiSPath) lista_percorsi.getLast();
            if (elemento==ultimo.getSUID()){
                return lista_percorsi;
            }
        }
        return null;
    }
    public boolean presente(Vector v,long confronto){
        boolean val=false;
        for(Iterator s=v.iterator();s.hasNext();){
            CentiScaPeShortestPathList lista_percorsi=(CentiScaPeShortestPathList)(s.next());
            CentiScaPeMultiSPath ultimo =(CentiScaPeMultiSPath) lista_percorsi.getLast();
            if (ultimo.getSUID()==confronto){
                val=true;
                break;
            }
        }
        return val;
    }
    public int minimo(int[] l){
        int min=9999;
        if (l.length>0)
            min=l[0];
        for(int k=1; k<l.length;k++){            
            if (min>l[k]){ 
                min=l[k];
            }
        }
        return min;
    }
}
