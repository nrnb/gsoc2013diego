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

import java.util.ArrayList;
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

public class AverageDistnew  {
    
    public static String version = "1.21";
  
    public AverageDistnew(CyNetwork network,String attributo ) {
                      
            CyTable nodeTable=network.getDefaultNodeTable();    //attributi
            CyTable networkTable=network.getDefaultNetworkTable();
                          
            //Ottengo i nomi degli attributi del nodo             
            LinkedList<String> columname=new LinkedList<String>();      
            columname.addAll(CyTableUtil.getColumnNames(nodeTable));
            LinkedList<String> attributi_corretti=new LinkedList<String>(); //Tengo solo gli attributi validi
            Object [] attributi= columname.toArray();  //Vettore di stringhe con tutti gli attributi
            CyColumn colonna;
            //stampo il nome delle colonne (nomi degli attributi)
            String lista="";
            boolean min=false;
            for(Iterator j=columname.iterator();j.hasNext();)
            {
                String n2=(String)j.next();
                colonna=nodeTable.getColumn(n2);
                if((""+colonna.getType()).equals("class java.lang.Integer")){   //controllo che l'attributo abbia tipo integer
                    attributi_corretti.add(n2);
                    lista=n2+"\n"+lista;    //Creo una lista di soli attributi interi                    
                    min=true;   //Se c'è almeno 1 attributo di tipo intero
                }
            }             
            if(min){            
                String colonna_selezionata="";
                boolean inclusa=true;
                if(attributi_corretti.contains(attributo)){
                    colonna_selezionata=attributo;
                }else{
                    System.out.println("Attributo "+attributo+" non presente");
                    do{
                        colonna_selezionata=JOptionPane.showInputDialog("Choose an attribute for Average Distance: \n"+ lista);
                        for(Iterator j=attributi_corretti.iterator();j.hasNext();)
                        {
                            String name=(String)j.next();                    
                            if(name.equals(colonna_selezionata)){     //controllo se la nome dell'attributo è nell'elenco
                                inclusa=false;
                            }
                        }  
                    }while(inclusa);
                }
                
                //Ottengo tutti i nodi
                LinkedList<CyNode> allnode=new LinkedList<CyNode>();
                allnode.addAll(CyTableUtil.getNodesInState(network,"selected",false));             
                allnode.addAll(CyTableUtil.getNodesInState(network,"selected",true));   
                
                //Inizializzare a 0 tutto CentiScaPe AverageDistanceNew
                CyTable tabella=network.getDefaultNodeTable();
                tabella.deleteColumn("CentiScaPe AverageDistanceNew");
                tabella.createColumn("CentiScaPe AverageDistanceNew", Double.class, false, 0.0);
                for(Iterator i=allnode.iterator();i.hasNext();){      
                    CyNode nodo=(CyNode)i.next(); 
                    long suid=nodo.getSUID();
                    LinkedList<CyRow> righe=new LinkedList<CyRow>();
                    righe.addAll(tabella.getMatchingRows("SUID",suid));   //ne aggiunge solo 1, quella con l'id
                    CyRow riga=(righe.iterator()).next();                     
                    riga.set("CentiScaPe AverageDistanceNew", 0.0);                    
                } 
                int somma_distanze=0;
                //Per tutti i nodi
                for(Iterator j=allnode.iterator();j.hasNext();){
                    CyNode node=(CyNode)j.next(); 
                    long idnode=node.getSUID();  //SUID del nodo i cui attributi sono usati cone stampo
                    TreeMap tree=new TreeMap();                    
                    Vector vettore_percorsi= new Vector();
                    vettore_percorsi=CentiScaPeMultiShortestPathTreeAlgorithm.ExecuteMultiShortestPathTreeAlgorithm(network,node,false,false,tree);
                    //System.out.println("\nNodo="+idnode);
                    double numpercorsi=0;
                    int k=1;
                    somma_distanze=0;
                    for(Iterator s=vettore_percorsi.iterator();s.hasNext();){
                        CentiScaPeShortestPathList lista_percorsi=(CentiScaPeShortestPathList)(s.next());                        
                        //Prendo solo il 2° nodo (il 1° è il nodo da cui trovo i vicini)
                        k=1;
                        for(Iterator i=lista_percorsi.iterator();i.hasNext();){
                            CentiScaPeMultiSPath interno=(CentiScaPeMultiSPath)i.next();                             
                            if(interno.getSUID()!=idnode){
                                LinkedList<CyRow> row=new LinkedList<CyRow>();
                                row.addAll( nodeTable.getMatchingRows("SUID", interno.getSUID()));//ottengo tutte le righe che hanno il suid del nodo (per forza 1)
                                CyRow readRow=(row.iterator()).next();
                                Object val_attributo=readRow.getRaw(colonna_selezionata); //ottengo il valore dell'attributo su cui mi baso per moltiplicare il nodo
                                if(val_attributo!=null){    // Il valore dell'attributo non è presente salto al nodo interno dopo
                                    Integer num=(Integer) val_attributo;
                                    if(num>0)
                                            k=k*num; 
                                }
                            }    
                        }
                        somma_distanze=somma_distanze+(lista_percorsi.size()-1);
                        numpercorsi=numpercorsi+k;
                        CentiScaPeMultiSPath ultimo=(CentiScaPeMultiSPath)lista_percorsi.getLast(); 
                        
                        //System.out.println("num perc="+numpercorsi+" somma dist="+somma_distanze+" ultimo"+ultimo.getSUID());
                    }
                    LinkedList<CyRow> row=new LinkedList<CyRow>();                   
                    row.addAll( nodeTable.getMatchingRows("SUID", idnode));//ottengo tutte le righe che hanno il suid del nodo (per forza 1)
                    CyRow readRow=(row.iterator()).next();
                    readRow.set("CentiScaPe AverageDistanceNew", somma_distanze/numpercorsi);                                                                  
                  }
                
             }else{
                    JOptionPane.showMessageDialog(null,"No Integer Attribute for this network"+network.toString());
             } 
    }    
}
    
