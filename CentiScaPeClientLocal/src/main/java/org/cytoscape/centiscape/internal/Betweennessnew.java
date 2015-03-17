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

public class Betweennessnew  {
    
    public static String version = "1.21";
  
    public Betweennessnew(CyNetwork network,String attributo ) {                 
                     
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
                    min=true;   //Se c'e' almeno 1 attributo di tipo intero
                }
            }              
            Vector vettore= new Vector();              
            if(min){
            
                String colonna_selezionata="";
                boolean inclusa=true;
                if(attributi_corretti.contains(attributo)){
                    colonna_selezionata=attributo;
                }else{
                    System.out.println("Attributo "+attributo+" non presente");
                    do{
                        colonna_selezionata=JOptionPane.showInputDialog("Choose an attribute for Betweenness: \n"+ lista);
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
                
                //Inizializzare a 0 tutto CentiScaPe BetweennessNew
                CyTable tabella=network.getDefaultNodeTable();
                tabella.deleteColumn("CentiScaPe BetweennessNew");
                tabella.createColumn("CentiScaPe BetweennessNew", Double.class, false, 0.0);
                for(Iterator i=allnode.iterator();i.hasNext();){      
                    CyNode nodo=(CyNode)i.next(); 
                    long suid=nodo.getSUID();
                    LinkedList<CyRow> righe=new LinkedList<CyRow>();
                    righe.addAll(tabella.getMatchingRows("SUID",suid));   //ne aggiunge solo 1, quella con l'id
                    CyRow riga=(righe.iterator()).next();                     
                    riga.set("CentiScaPe BetweennessNew", 0.0);                    
                } 
                
                // Tabella con sorgente, destinazione, nodi interni e nè di shortest path presenti con quella sorgente e quella destinazione              
                LinkedList<Object[]> sds=new LinkedList<Object[]>();
                
                //Per tutti i nodi
                for(Iterator j=allnode.iterator();j.hasNext();)
                {
                    CyNode node=(CyNode)j.next(); 
                    long idnode=node.getSUID();  //SUID del nodo i cui attributi sono usati cone stampo
                    TreeMap tree=new TreeMap();                    
                    Vector vettore_percorsi= new Vector();
                    vettore_percorsi=CentiScaPeMultiShortestPathTreeAlgorithm.ExecuteMultiShortestPathTreeAlgorithm(network,node,false,false,tree);
                    vettore.addAll(vettore_percorsi);                    
                    for(Iterator s=vettore_percorsi.iterator();s.hasNext();){
                        CentiScaPeShortestPathList lista_percorsi=(CentiScaPeShortestPathList)(s.next());
                        CentiScaPeMultiSPath primo =(CentiScaPeMultiSPath) lista_percorsi.getFirst();
                        CentiScaPeMultiSPath ultimo =(CentiScaPeMultiSPath) lista_percorsi.getLast();
                        int shrtherstpath=1;
                        //Scorro i nodi interni
                        for (Iterator i=lista_percorsi.iterator();i.hasNext();){
                            CentiScaPeMultiSPath interno=(CentiScaPeMultiSPath)i.next(); 
                            //if((interno.getSUID()!=primo.getSUID())  && (interno.getSUID()!=ultimo.getSUID())){    con questa riga escludo l'attributo del primo e ultimo nodo di quel percorso
                                LinkedList<CyRow> row=new LinkedList<CyRow>();
                                row.addAll( nodeTable.getMatchingRows("SUID", interno.getSUID()));//ottengo tutte le righe che hanno il suid del nodo (per forza 1)
                                CyRow readRow=(row.iterator()).next();
                                Object val_attributo=readRow.getRaw(colonna_selezionata); //ottengo il valore dell'attributo su cui mi baso per moltiplicare il nodo
                                if(val_attributo!=null){    // Il valore dell'attributo non e' presente salto al nodo interno dopo 
                                    Integer num=(Integer) val_attributo;
                                    shrtherstpath=shrtherstpath*num;                                    
                                }
                            //}                            
                        }
                        Object nuova_col[]={""+primo.getSUID(),  ""+ultimo.getSUID(),lista_percorsi, ""+shrtherstpath};
                        sds.add(nuova_col);                                             
                        //System.out.println("primo="+primo.getSUID()+" ultimo="+ultimo.getSUID()+" presente="+!presente);                                                     
                    }
                }   
                
                //Stampo il contenuto di sds
             /*  for(Iterator i=sds.iterator();i.hasNext();)       
                 {
                    Object s[]=(Object[])i.next();
                    System.out.println(""+(String)s[0]+" "+(String)s[1]+" "+(String)s[3]);                    
                 } 
             */    
                //Per ogni nodo controllo se sta nel percorso per calcolare la BetweennessNew
                for(Iterator i=allnode.iterator();i.hasNext();){      
                    CyNode node=(CyNode)i.next();                    
                    //Controllo se il nodo e' nel percorso
                    LinkedList<String> gia_passati=new LinkedList<String>();
                    for(Iterator s=sds.iterator();s.hasNext();){
                        Object element_sds[]=(Object[])s.next();
                        CentiScaPeShortestPathList lista_percorsi=(CentiScaPeShortestPathList)element_sds[2];
                        CentiScaPeMultiSPath primo=(CentiScaPeMultiSPath)lista_percorsi.getFirst();
                        CentiScaPeMultiSPath ultimo=(CentiScaPeMultiSPath)lista_percorsi.getLast();
                        for(Iterator l=lista_percorsi.iterator();l.hasNext();){
                            CentiScaPeMultiSPath singolo=(CentiScaPeMultiSPath)l.next();                            
                            if((singolo.getSUID()==node.getSUID())&&(primo.getSUID()!=node.getSUID())&&(ultimo.getSUID()!=node.getSUID())){                                   
                                if (!passato(gia_passati,primo.getSUID(),ultimo.getSUID(),node.getSUID())){
                                    gia_passati.add(""+primo.getSUID()+ultimo.getSUID()+node.getSUID());
                                    double ripetizione=trova(sds,primo.getSUID(),ultimo.getSUID());
                                    //Aggiungo il valore
                                    LinkedList<CyRow> righe=new LinkedList<CyRow>();
                                    righe.addAll(tabella.getMatchingRows("SUID",node.getSUID()));   //ne aggiunge solo 1, quella con l'id
                                    CyRow riga=(righe.iterator()).next();
                                    String valore=""+riga.get("CentiScaPe BetweennessNew", java.lang.Double.class);                                    
                                    
                                    double val=Double.parseDouble(valore)+1/ripetizione;
                                    riga.set("CentiScaPe BetweennessNew", val);                                    
                                }
                            }
                        }
                    }
                }                
            }else{
                JOptionPane.showMessageDialog(null,"No Integer Attribute for this network"+network.toString());
            }
    }
    public boolean passato(LinkedList<String> v,long primo, long ultimo,long daControllare){
        boolean risp=false;
        for(Iterator i=v.iterator();i.hasNext();){
            String elem=(String) i.next();
            if (elem.equals(""+primo+ultimo+daControllare)){
                risp=true;
                break;
            }
        }
    return risp;
    }
    //Fa la somma del num di elementi con stesso primo e ultimo
    public double trova(LinkedList<Object[]> sds,long primo, long ultimo){
        double val=0;
        for(Iterator i=sds.iterator();i.hasNext();){
            Object s[]=(Object[])i.next();            
            if (((String)s[0]).equals(""+primo)&&((String)s[1]).equals(""+ultimo)){
                val=val+Double.parseDouble((String)s[3]);
            }
        }
        return val;
    }
}
