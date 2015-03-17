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

public class Radialitynew  {
    
    public static String version = "1.21";
  
    public Radialitynew(CyNetwork network,String attributo ) {
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
            Vector vettore_percorsi= new Vector();              
            if(min){
                String colonna_selezionata="";
                boolean inclusa=true;
                if(attributi_corretti.contains(attributo)){
                    colonna_selezionata=attributo;
                }else{
                    System.out.println("Attributo "+attributo+" non presente");
                    do{
                        colonna_selezionata=JOptionPane.showInputDialog("Choose an attribute for Radiality: \n"+ lista);
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
                
                //Inizializzare a 0 tutto CentiScaPe RadialityNew
                CyTable tabella=network.getDefaultNodeTable();
                tabella.deleteColumn("CentiScaPe RadialityNew");
                tabella.createColumn("CentiScaPe RadialityNew", Double.class, false, 0.0);
                for(Iterator i=allnode.iterator();i.hasNext();){      
                    CyNode nodo=(CyNode)i.next(); 
                    long suid=nodo.getSUID();
                    LinkedList<CyRow> righe=new LinkedList<CyRow>();
                    righe.addAll(tabella.getMatchingRows("SUID",suid));   //ne aggiunge solo 1, quella con l'id
                    CyRow riga=(righe.iterator()).next();                     
                    riga.set("CentiScaPe RadialityNew", 0.0);                    
                }                
                
                //Trovo la longest shortest path (Diametro)
                int diametro=0;                
                for(Iterator j=allnode.iterator();j.hasNext();)
                {
                    CyNode node=(CyNode)j.next();
                    TreeMap tree=new TreeMap();                    
                    vettore_percorsi=CentiScaPeMultiShortestPathTreeAlgorithm.ExecuteMultiShortestPathTreeAlgorithm(network,node,false,false,tree);
                    for(Iterator s=vettore_percorsi.iterator();s.hasNext();){
                        CentiScaPeShortestPathList lista_percorsi=(CentiScaPeShortestPathList)(s.next());          
                        if (diametro<lista_percorsi.size())
                            diametro=lista_percorsi.size();                                      
                    }
                }
                diametro=diametro-1;
                System.out.println("Diametro="+diametro);
                //Ottengo il num di nodi totali
                CyTable tab=network.getDefaultNodeTable();
                int numNodi=tab.getAllRows().size();
                System.out.println("Num nodi="+numNodi);
                //Per ogni nodo faccio il calcolo
                for(Iterator j=allnode.iterator();j.hasNext();)
                {
                    CyNode node=(CyNode)j.next(); 
                    long idnode=node.getSUID();  //SUID del nodo i cui attributi sono usati cone stampo
                                        
                    TreeMap tree=new TreeMap();
                    vettore_percorsi=CentiScaPeMultiShortestPathTreeAlgorithm.ExecuteMultiShortestPathTreeAlgorithm(network,node,false,false,tree);
                    vettore_percorsi=eliminadoppioni(vettore_percorsi);
                    
                    //Somma delle distanze da tutti i nodi
                    double dist=0;
                    for(Iterator s=vettore_percorsi.iterator();s.hasNext();){
                        CentiScaPeShortestPathList lista_percorsi=(CentiScaPeShortestPathList)(s.next()); 
                        CentiScaPeMultiSPath ultimo =(CentiScaPeMultiSPath) lista_percorsi.getLast();
                        
                        //Ottengo l'attributo
                        LinkedList<CyRow> row=new LinkedList<CyRow>();                        
                        row.addAll( nodeTable.getMatchingRows("SUID", ultimo.getSUID()));//ottengo tutte le righe che hanno il suid del nodo (per forza 1)
                        CyRow readRow=(row.iterator()).next(); 
                        Object val_attributo=readRow.getRaw(colonna_selezionata);
                        double k=1;
                        if(val_attributo!=null){                          
                            k=(Integer) val_attributo;
                            if(k<=0){
                                k=1;
                            }
                        }
                        dist=dist+(lista_percorsi.size()-1)*k;
                    }  
                    LinkedList<CyRow> righe=new LinkedList<CyRow>();
                    righe.addAll(tabella.getMatchingRows("SUID",idnode));  
                    CyRow riga=(righe.iterator()).next(); 
                    System.out.println("Suid="+idnode+" distTot="+dist);
                    riga.set("CentiScaPe RadialityNew", ((diametro+1)*numNodi-dist)/(numNodi-1) );
                    //NUOVA DEFINIZIONE DI RADIALITY
                    //riga.set("CentiScaPe RadialityNew", 1/(diametro*numNodi-1/dist));                   
                }                      
            }else{
                JOptionPane.showMessageDialog(null,"No Integer Attribute for this network"+network.toString());
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
}
