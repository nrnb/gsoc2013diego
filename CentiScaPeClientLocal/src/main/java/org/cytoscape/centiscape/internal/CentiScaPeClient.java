/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cytoscape.centiscape.internal;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTableUtil;

/**
 *
 * @author scardoni
 */
public class CentiScaPeClient {

    CyNetwork network;

    public CentiScaPeClient(CyNetwork currentnetwork) {

        this.network = currentnetwork;
    }

    /*public void startclient(String hostname) throws IOException {

        Socket CentiscapeSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;


        try {
            CentiscapeSocket = new Socket(hostname, 4444);
            out = new PrintWriter(CentiscapeSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(CentiscapeSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.out.println("Don't know about host:" + hostname);
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Couldn't get I/O for the connection to:" + hostname);
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser;


        fromServer = in.readLine();
        System.out.println("Server: " + fromServer);

        System.out.println("prima di copy");
        // Pappo pappo = new Pappo("scardovari");
        System.out.println("dopo copy");

        List<CyNode> rootlist = CyTableUtil.getNodesInState(network, "selected", true);

        CyNode node = (CyNode) rootlist.get(0);

        String mynode = network.getRow(node).get(CyNetwork.NAME, String.class);
        List prova = network.getEdgeList();

        String edgelist = "";
        for (Iterator i = prova.listIterator(); i.hasNext();) {
            CyEdge edge = (CyEdge) i.next();
            edgelist = edgelist + network.getRow(edge).get(CyNetwork.NAME, String.class) + "\n";
           
        }
        System.out.println("edge list " + edgelist);
        System.out.println("il node è " + mynode);
        Gson gson = new Gson();
        String json = gson.toJson(edgelist);


        out.println(json);



        //   out.println(networkinfo);;
        fromServer = in.readLine();
        System.out.println("Server: " + fromServer);

        out.close();
        in.close();
        stdIn.close();
        CentiscapeSocket.close();
    }*/

    public long lngUseCentiscapeWS(long lngCent, String strNetworkName, CyNetwork network, CentiScaPeStartMenu centiscapestartmenu) throws IOException {

        CentiScaPeStartMenu csm = centiscapestartmenu;
        long rc = -1;
        long lngID;     // ID richiesta andata a buon fine
        Socket CentiscapeSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;


        String strUser = JOptionPane.showInputDialog(null, "Please insert your mail for the answer");

        if (strUser.isEmpty() || !strUser.contains("@")) {
            JOptionPane.showMessageDialog(null, "Please insert a valid @mail", "CentiScaPe", JOptionPane.INFORMATION_MESSAGE);

            return -2;
        }
        csm.jTextPanelsetText("Connecting to CentiScaPe web service");

        //cambiare con server vero

        String hostname = ""; 
        hostname = "157.27.10.103";
        // prima connessione
       /* try {
            
           
            hostname = java.net.InetAddress.getLocalHost().getHostName();
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to:");
        }*/
            
        try {
            CentiscapeSocket = new Socket(hostname, 4444);
            out = new PrintWriter(CentiscapeSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(CentiscapeSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.out.println("Don't know about host:" + hostname);
            //System.exit(1);
            return -1;
        } catch (IOException e) {
            System.out.println("Couldn't get I/O for the connection to:" + hostname);
            //System.exit(1);
            return -1;
        }

        String fromServer;
        String fromUser;
        fromServer = in.readLine();
        System.out.println("Server1: " + fromServer);

        // passa la richiesta al web service:
        // verifica l'utente e rende il numero ID di richiesta
        csm.jTextPanelsetText("Sending contact data");

        // invio e-mail
        out.println(strUser);
        // mando direttamente la stringa strUser
        fromServer = in.readLine();
        csm.jTextPanelsetText("Getting server confirm");

      //  Gson gsonID = new Gson();

        //lngID = gsonID.fromJson(fromServer, Long.class);
        System.out.println("Server2: id service" + fromServer);



        //  lngID = port.request(pUser, lngCent);
        // per provare metto lngid = 10
        
         // invia lngCent
         csm.jTextPanelsetText("Sending request");
            System.out.println("lng Cent=" + lngCent);
             Gson gsonCent = new Gson();
             String jsonLngCent = gsonCent.toJson(lngCent);
             out.println(jsonLngCent);
             
              csm.jTextPanelsetText("Getting request id");
              fromServer = in.readLine();
            Gson gsonCentreceived = new Gson();

            long lngCentreceived = gsonCentreceived.fromJson(fromServer, Long.class);
          
             System.out.println("Server3: received lngCent" + lngCentreceived);
             lngID = lngCentreceived;
        
        

        if (lngID >= 0) {
            
            // mando networkSUID
            long networksuid = network.getSUID();
            Gson gsonnetworksuid = new Gson();
             String jsonnetworksuid = gsonnetworksuid.toJson(networksuid);
             out.println(jsonnetworksuid);
             
             // ricevo risposta
              fromServer = in.readLine();
            Gson gsonsuidreceived = new Gson();

            long suidreceived = gsonsuidreceived.fromJson(fromServer, Long.class);
          
             System.out.println("Server4: received lngCent" + suidreceived);
             
            
            // mando la rete
            // esporta la rete come stringa 
            CyColumn prova = network.getDefaultEdgeTable().getColumn("name");
              System.out.println("colonna " + prova.toString());
          //  Object[] edgearray =   prova.toArray();
            String edgelist = "";
            List prova2 = prova.getValues(String.class);
           /*
            int pppp=0;
            for (Iterator i = prova2.iterator(); i.hasNext(); pppp++) {
                if ((pppp % 100) == 0) {
                System.out.println(pppp);}
                CyEdge edge = (CyEdge) i.next();
                edgelist = edgelist + network.getRow(edge).get(CyNetwork.NAME, String.class) + "\n";
           
               
            }
            System.out.println("edge list " + edgelist);
             */      
          
            Gson gsonNetwork = new Gson();
            String jsonNetwork = gsonNetwork.toJson(prova2);

            csm.jTextPanelsetText("Sending network data, please wait...");

            out.println(jsonNetwork);

System.out.println("fatto");
            // ricevo conferma network ricevuta 
             fromServer = in.readLine();
            Gson gsonrc = new Gson();

            rc = gsonrc.fromJson(fromServer, Long.class);
            System.out.println("Server5: received network outcome= " + rc);





            if (rc == 0) {
                csm.jTextPanelsetText("Network successfully transferred");
                System.out.println("Network transferred");
            } else {
                JOptionPane.showMessageDialog(null, "Can't load network in server", "CentiScaPe", JOptionPane.ERROR_MESSAGE);

                csm.jTextPanelsetText("Can't load network in server");
                System.out.println("Can't load network in server");
            }


        } else {
            JOptionPane.showMessageDialog(null,
                    "Can't get request number from webservice", "CentiScaPe",
                    JOptionPane.ERROR_MESSAGE);
            System.out.println("Can't get request number from webservice");
        }
        out.close();
        in.close();
        CentiscapeSocket.close();


        if (rc == 0) {
            return lngID;   //codice richiesta
        } else {
            return -1;
        }

    }
    public long lngUseCentiscapeWS(String strUser, long lngCent, String strNetworkName, CyNetwork network, CentiScaPeStartMenu centiscapestartmenu) throws IOException {

        CentiScaPeStartMenu csm = centiscapestartmenu;
        long rc = -1;
        long lngID;     // ID richiesta andata a buon fine
        Socket CentiscapeSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;


        //String strUser = JOptionPane.showInputDialog(null, "Please insert your mail for the answer");

        if (strUser.isEmpty() || !strUser.contains("@")) {
            JOptionPane.showMessageDialog(null, "Please insert a valid @mail", "CentiScaPe", JOptionPane.INFORMATION_MESSAGE);

            return -2;
        }
        csm.jTextPanelsetText("Connecting to CentiScaPe web service");

        //cambiare con server vero

        String hostname = ""; 
        hostname = "157.27.10.103";
        // prima connessione
       /* try {
            
           
            hostname = java.net.InetAddress.getLocalHost().getHostName();
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to:");
        }*/
            
        try {
            CentiscapeSocket = new Socket(hostname, 4444);
            out = new PrintWriter(CentiscapeSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(CentiscapeSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.out.println("Don't know about host:" + hostname);
            //System.exit(1);
            return -1;
        } catch (IOException e) {
            System.out.println("Couldn't get I/O for the connection to:" + hostname);
            //System.exit(1);
            return -1;
        }

        String fromServer;
        String fromUser;
        fromServer = in.readLine();
        System.out.println("Server1: " + fromServer);

        // passa la richiesta al web service:
        // verifica l'utente e rende il numero ID di richiesta
        csm.jTextPanelsetText("Sending contact data");

        // invio e-mail
        out.println(strUser);
        // mando direttamente la stringa strUser
        fromServer = in.readLine();
        csm.jTextPanelsetText("Getting server confirm");

      //  Gson gsonID = new Gson();

        //lngID = gsonID.fromJson(fromServer, Long.class);
        System.out.println("Server2: id service" + fromServer);



        //  lngID = port.request(pUser, lngCent);
        // per provare metto lngid = 10
        
         // invia lngCent
         csm.jTextPanelsetText("Sending request");
            System.out.println("lng Cent=" + lngCent);
             Gson gsonCent = new Gson();
             String jsonLngCent = gsonCent.toJson(lngCent);
             out.println(jsonLngCent);
             
              csm.jTextPanelsetText("Getting request id");
              fromServer = in.readLine();
            Gson gsonCentreceived = new Gson();

            long lngCentreceived = gsonCentreceived.fromJson(fromServer, Long.class);
          
             System.out.println("Server3: received lngCent" + lngCentreceived);
             lngID = lngCentreceived;
        
        

        if (lngID >= 0) {
            
            // mando networkSUID
            long networksuid = network.getSUID();
            Gson gsonnetworksuid = new Gson();
             String jsonnetworksuid = gsonnetworksuid.toJson(networksuid);
             out.println(jsonnetworksuid);
             
             // ricevo risposta
              fromServer = in.readLine();
            Gson gsonsuidreceived = new Gson();

            long suidreceived = gsonsuidreceived.fromJson(fromServer, Long.class);
          
             System.out.println("Server4: received lngCent" + suidreceived);
             
            
            // mando la rete
            // esporta la rete come stringa 
            CyColumn prova = network.getDefaultEdgeTable().getColumn("name");
              System.out.println("colonna " + prova.toString());
          //  Object[] edgearray =   prova.toArray();
            String edgelist = "";
            List prova2 = prova.getValues(String.class);
           /*
            int pppp=0;
            for (Iterator i = prova2.iterator(); i.hasNext(); pppp++) {
                if ((pppp % 100) == 0) {
                System.out.println(pppp);}
                CyEdge edge = (CyEdge) i.next();
                edgelist = edgelist + network.getRow(edge).get(CyNetwork.NAME, String.class) + "\n";
           
               
            }
            System.out.println("edge list " + edgelist);
             */      
          
            Gson gsonNetwork = new Gson();
            String jsonNetwork = gsonNetwork.toJson(prova2);

            csm.jTextPanelsetText("Sending network data, please wait...");

            out.println(jsonNetwork);

System.out.println("fatto");
            // ricevo conferma network ricevuta 
             fromServer = in.readLine();
            Gson gsonrc = new Gson();

            rc = gsonrc.fromJson(fromServer, Long.class);
            System.out.println("Server5: received network outcome= " + rc);





            if (rc == 0) {
                csm.jTextPanelsetText("Network successfully transferred");
                System.out.println("Network transferred");
            } else {
                JOptionPane.showMessageDialog(null, "Can't load network in server", "CentiScaPe", JOptionPane.ERROR_MESSAGE);

                csm.jTextPanelsetText("Can't load network in server");
                System.out.println("Can't load network in server");
            }


        } else {
            JOptionPane.showMessageDialog(null,
                    "Can't get request number from webservice", "CentiScaPe",
                    JOptionPane.ERROR_MESSAGE);
            System.out.println("Can't get request number from webservice");
        }
        out.close();
        in.close();
        CentiscapeSocket.close();


        if (rc == 0) {
            return lngID;   //codice richiesta
        } else {
            return -1;
        }

    }
}
