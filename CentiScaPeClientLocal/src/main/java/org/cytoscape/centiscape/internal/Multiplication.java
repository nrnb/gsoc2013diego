/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cytoscape.centiscape.internal;

import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.CyTableUtil;
import org.cytoscape.view.model.CyNetworkView;

/**
 *
 * @author Diego Magagna
 */
public class Multiplication  {
    public Multiplication(CyNetwork network ) {                 
                     
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
                do{
                    colonna_selezionata=JOptionPane.showInputDialog("Choose an attribute: \n"+ lista);
                    for(Iterator j=attributi_corretti.iterator();j.hasNext();)
                    {
                        String name=(String)j.next();                    
                        if(name.equals(colonna_selezionata)){     //controllo se la nome dell'attributo è nell'elenco
                            inclusa=false;
                        }
                    }  
                }while(inclusa);              
              
                //Ottengo tutti i nodi
                LinkedList<CyNode> allnode=new LinkedList<CyNode>();
                allnode.addAll(CyTableUtil.getNodesInState(network,"selected",false));             
                allnode.addAll(CyTableUtil.getNodesInState(network,"selected",true));              
                //Per ogni nodo controllo il valore dell'attributo per moltiplicarlo
                for(Iterator j=allnode.iterator();j.hasNext();)
                {
                    CyNode node=(CyNode)j.next(); 
                    long idnode=node.getSUID();  //SUID del nodo i cui attributi sono usati cone stampo
                    LinkedList<CyNode> nodiClonati=new LinkedList<CyNode>();    //lista di tutti i nodi che poi verranno collegati tra loro usando gli archi
                    LinkedList<CyRow> row=new LinkedList<CyRow>();
                    row.addAll( nodeTable.getMatchingRows("SUID", idnode));//ottengo tutte le righe che hanno il suid del nodo (per forza 1)
                    CyRow readRow=(row.iterator()).next();
                    Object val_attributo=readRow.getRaw(colonna_selezionata); //ottengo il valore dell'attributo su cui mi baso per moltiplicare il nodo
                    if(!(val_attributo==null)){    // Il valore dell'attributo non è presente salto al nodo dopo 
                        Integer num=(Integer) val_attributo; 
                        nodiClonati.add(node);
                        for (int i=1;i<num;i++)  //Moltiplico i nodi solo se hanno un valore dell'attributo >1
                        {        
                            CyNode add=network.addNode();
                            nodiClonati.add(add);
                            long idadd=add.getSUID();    //SUID del nodo in cui devo copiare gli attributi               
                            LinkedList<CyRow> rowadd=new LinkedList<CyRow>();               
                            rowadd.addAll( nodeTable.getMatchingRows("SUID", idadd));//ottengo tutte le righe che hanno il suid del nodo nuovo(per forza 1)
                            CyRow newRow=(rowadd.iterator()).next();      
                            //Copio tutti gli attributi al nuovo nodo                                               
                            //Errore con linked list, quindi passata con 
                            LinkedList<String> nome_colonne=new LinkedList<String>();      
                            nome_colonne.addAll(CyTableUtil.getColumnNames(network.getDefaultNodeTable()));                                                                   
                            for(int k=0; k<attributi.length; k++)
                            {
                                String name=(String) attributi[k];   
                                Object oggetto=readRow.getRaw(name);
                                newRow.set(name, oggetto);
                            }
                            //Copio gli archi al nuovo nodo.
                            LinkedList<CyNode> vicini=new LinkedList<CyNode>();
                            vicini.addAll(network.getNeighborList(node, CyEdge.Type.ANY));
                            for(Iterator z=vicini.iterator();z.hasNext();)
                            {
                                CyNode vicino=(CyNode)z.next();
                                CyEdge arco_nuovo=network.addEdge(add, vicino, false); //creo l'arco                                                                                                
                            }                            
                        }
                        //Aggiungo gli archi ai nodi "GEMELLI"
                        int k=0;
                        for(Iterator z=nodiClonati.iterator();z.hasNext();)
                        {
                            CyNode vicino1=(CyNode)z.next();
                            k++;
                            int x=0;
                            for(Iterator s=nodiClonati.iterator();s.hasNext();)
                            {                                
                                CyNode vicino2=(CyNode)s.next();
                                x++;
                                if((vicino1.getSUID()!=vicino2.getSUID())&&(x>k)){  //Così evito di mettere un arco tra A e B se ne ho gia messo uno tra B e A
                                    CyEdge arco_nuovo=network.addEdge(vicino1, vicino2, false); //creo l'arco 
                                }
                            }                                                                                               
                        }                        
                    }
                } 
            }else{
                JOptionPane.showMessageDialog(null, "No Attribute of Type Integer");
            }
        //  CentiScaPeCore centiscapecore = new CentiScaPeCore();              
                   
    }
} 
