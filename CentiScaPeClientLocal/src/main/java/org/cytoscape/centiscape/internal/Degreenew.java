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

public class Degreenew  {
    
    public static String version = "1.21";
  
    public Degreenew(CyNetwork network,String attributo ) {                 
                      
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
                        colonna_selezionata=JOptionPane.showInputDialog("Choose an attribute for Degree: \n"+ lista);
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
                
                //Inizializzare a 0 tutto CentiScaPe Degree
                CyTable tabella=network.getDefaultNodeTable();
                tabella.deleteColumn("CentiScaPe DegreeNew");
                tabella.createColumn("CentiScaPe DegreeNew", Integer.class, false, 0);
                for(Iterator i=allnode.iterator();i.hasNext();){      
                    CyNode nodo=(CyNode)i.next(); 
                    long suid=nodo.getSUID();
                    LinkedList<CyRow> righe=new LinkedList<CyRow>();
                    righe.addAll(tabella.getMatchingRows("SUID",suid));   //ne aggiunge solo 1, quella con l'id
                    CyRow riga=(righe.iterator()).next(); 
                    
                    riga.set("CentiScaPe DegreeNew", 0);                    
                }
                
                //Per tutti i nodi
                for(Iterator j=allnode.iterator();j.hasNext();){
                    CyNode node=(CyNode)j.next(); 
                    long idnode=node.getSUID();  //SUID del nodo i cui attributi sono usati cone stampo
                    TreeMap tree=new TreeMap();                    
                    Vector vettore_percorsi= new Vector();
                    vettore_percorsi=CentiScaPeMultiShortestPathTreeAlgorithm.ExecuteMultiShortestPathTreeAlgorithm(network,node,false,false,tree);
                    
                    //System.out.println("\nNodo="+idnode);
                    LinkedList<String> vicini= new LinkedList<String>();
                    for(Iterator s=vettore_percorsi.iterator();s.hasNext();){
                        CentiScaPeShortestPathList lista_percorsi=(CentiScaPeShortestPathList)(s.next());
                        
                        //Prendo solo il 2° nodo (il 1° è il nodo da cui trovo i vicini)
                        Iterator i=lista_percorsi.iterator();
                        CentiScaPeMultiSPath interno=(CentiScaPeMultiSPath)i.next();
                        interno=(CentiScaPeMultiSPath)i.next();
                        if(!presente(vicini,interno.getSUID()))
                            vicini.add(""+interno.getSUID());
                    }
                    Integer degree=0;
                    LinkedList<CyRow> row=new LinkedList<CyRow>();
                    CyRow readRow;
                    for(Iterator i=vicini.iterator();i.hasNext();){
                        String vicino=(String)i.next();
                        //System.out.println("vicino="+vicino);
                        row=new LinkedList<CyRow>();
                        long idvicino=Long.parseLong(vicino);
                        row.addAll( nodeTable.getMatchingRows("SUID", idvicino));//ottengo tutte le righe che hanno il suid del nodo (per forza 1)
                        readRow=(row.iterator()).next();
                        Object val_attributo=readRow.getRaw(colonna_selezionata); //ottengo il valore dell'attributo su cui mi baso per moltiplicare il nodo
                        if(val_attributo!=null){    // Il valore dell'attributo non è presente salto al nodo interno dopo 
                              Integer num=(Integer) val_attributo;
                              if(num>0)
                               degree=degree+num;
                              else{
                                  if(num==0)
                                      degree++;
                              }
                        }
                        else{
                            degree++;
                        } 
                         //System.out.println("degree="+degree);
                    }
                    row=new LinkedList<CyRow>();
                    row.addAll( nodeTable.getMatchingRows("SUID", idnode));//ottengo tutte le righe che hanno il suid del nodo (per forza 1)
                    readRow=(row.iterator()).next();
                    readRow.set("CentiScaPe DegreeNew", degree);
                                                                  
                  }
             }else{
                    JOptionPane.showMessageDialog(null,"No Integer Attribute for this network"+network.toString());
             }  
     }
   
    public boolean presente(LinkedList<String> v,long daControllare){
        boolean risp=false;
        for(Iterator i=v.iterator();i.hasNext();){
            String elem=(String) i.next();
            if (elem.equals(""+daControllare)){
                risp=true;
                break;
            }
        }
    return risp;
    }
}
    
