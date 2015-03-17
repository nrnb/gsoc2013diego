/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.objectserver;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author scardoni
 */
public class ObjectProtocol {

    private static final int WAITING = 0;
    private static final int CONNECTIONACCEPTED = 1;
    private static final int LNGCENTRECEIVED = 3;
    private static final int NETWORKNAMERECEIVED = 4;
    private static final int EMAILRECEIVED = 2;
   
    private int state = WAITING;
    
     private Connection conn;    // Connection variable 
    //TODO impostare i valori del server
    // Ip, porta e database da usare per la connessione a MySQL
    private String dbConnection = "jdbc:mysql://localhost:3306/Centiscape";
    // Utente e password per accesso a db MySQL
    private String dbUser = "Centiscape";
    private String dbPwd = "centiscape162012";
    // Cartella destinazione file .sif da elaborare
    private String strRepository = "";    
    private String email,networkString,networkSUID;
    private long lngIdReq;
    

    public String processInput(String theInput) {
        String theOutput = null;

        if (state == WAITING) {
            theOutput = "connection accepted" + " Hello!";
            state = CONNECTIONACCEPTED;

        } else if (state == CONNECTIONACCEPTED) {


            System.out.println("email received " + theInput);
            email = theInput;
            theOutput = "received1= " + email;
            state = EMAILRECEIVED;
        } else if (state == EMAILRECEIVED) {
              Gson gsonLngCent = new Gson();
              Long receivedLngCent = gsonLngCent.fromJson(theInput, Long.class);
               System.out.println("ho ricevuto lngCent= " + receivedLngCent);
               
               Long answer = new Long(Request(email,receivedLngCent));
             lngIdReq = answer;
               System.out.println("prodotto risposta = " + answer);
             
               //per server su server
               theOutput = gsonLngCent.toJson(new Long(answer));
               // per locale
               
            //   theOutput = gsonLngCent.toJson(new Long(44));
              
               
               
        state = LNGCENTRECEIVED;
        }
        else if (state == LNGCENTRECEIVED) {
         // ricevonomenetwork   
             Gson gsonNetworksuid = new Gson();
               System.out.println("ho ricevuto" + theInput);
               Long networksuid  = gsonNetworksuid.fromJson(theInput, Long.class);
               networkSUID = networksuid.toString();
               System.out.println("il network suid = " + networkSUID);
               theOutput = gsonNetworksuid.toJson(new Long(networksuid));
            state = NETWORKNAMERECEIVED;
        }
        else if (state == NETWORKNAMERECEIVED) {    
            Gson gsonNetwork = new Gson();
               System.out.println("ho ricevuto" + theInput);

               
                Type type = new TypeToken<List<String>>(){}.getType();
    List<String> edgelist =  new Gson().fromJson(theInput, type);
   /* for (int i=0;i<edgelist.size();i++) {
      String x = edgelist.get(i);
      System.out.println(x);
    }*/
     System.out.println("ho ricevuto2" + theInput);
                String receivedstring = theInput.replaceAll(",","\n");
                System.out.println("ho ricevuto2" + receivedstring);
               receivedstring = receivedstring.replaceAll("\\(", "");
                System.out.println("ho ricevuto2" + receivedstring);
               receivedstring = receivedstring.replaceAll("\\)", "");
                System.out.println("ho ricevuto2" + receivedstring);
               receivedstring = receivedstring.substring(1, receivedstring.length()-1);
                System.out.println("ho ricevuto2" + receivedstring);
               receivedstring =receivedstring.replaceAll("\"","");
                System.out.println("ho ricevuto2" + receivedstring);
               receivedstring = receivedstring + "\n";
          System.out.println("ho ricevuto2" + receivedstring);
                networkString = receivedstring;
              System.out.println("convertito" );    
          /*  String receivedstring = gsonNetwork.fromJson(theInput, String.class);*/
                
                //ciclo troppo lento sostituire con replace nella stringa o converitre la lista
                
       /*       for (int i=0;i<edgelist.size();i++) {
      receivedstring = receivedstring + edgelist.get(i) + "\n";
     // System.out.println(receivedstring);
    }*/
              
           // System.out.println("ho ricevuto3" + receivedstring);

       Long loadnetworkanswer =  LoadNetwork(lngIdReq, email,networkSUID, networkString);
              System.out.println("esco da loadnetwork" );
             // mettere codice di LoadNetwork
                theOutput = gsonNetwork.toJson(new Long(loadnetworkanswer));
           // theOutput = "received2= " + receivedstring;

        }
        System.out.println("Server state= " + state);
        return theOutput;
    }

    public void writenetworktofile(String edgelist, String networkname) {

        try {
            File networkfile = new File(networkname);
            PrintWriter out = new PrintWriter(networkfile);
            out.println(edgelist);
            out.close();
            
        } catch (IOException e) {
            System.out.println("error writing file");
        }

    }
    
    
    public long Request(String pUser, long lngCent) {
        try {
            if(conn==null) {
                ScriviLog("Request: Creating connection");
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(dbConnection,dbUser,dbPwd);
            }
        }
        catch (Exception e) {
            ScriviLog("Request: " + e.getMessage());
        }
        if(conn==null) {
            ScriviLog("Request: " + "Connection error");
            return -1;
        }
        try {
            String strSerial,strData,strUser = "";
            java.util.Calendar data = new GregorianCalendar();
            strUser = pUser;
           /*questo c'era se usavo byte 
            * 
            * int i = 0;
            while ((pUser[i] != 0) && (i<=255)){
                strUser = strUser + (char)pUser[i];
                i++;
            }*/
            
            
            ScriviLog("Request from " + strUser);
            strData = String.format("%04d%02d%02d%02d%02d%02d",
                data.get(Calendar.YEAR),
                data.get(Calendar.MONTH)+1,
                data.get(Calendar.DAY_OF_MONTH ),
                data.get(Calendar.HOUR_OF_DAY),
                data.get(Calendar.MINUTE),
                data.get(Calendar.SECOND));
            PreparedStatement stmt = conn.prepareStatement("SELECT count(*) FROM users " +
               "WHERE us_mail=?");
            stmt.setString(1, strUser);
            java.sql.ResultSet rs;
            rs = stmt.executeQuery();
            rs.first();
            if (rs.getInt(1) == 0) {
                rs.close();
                stmt = conn.prepareStatement("INSERT INTO users " +
                    "(us_mail,us_datain,us_stato)" +
                    "VALUES (?,?,0)");
                stmt.setString(1, strUser);
                stmt.setString(2, strData);
                ScriviLog("Inserting user: "+ stmt);
                stmt.executeUpdate();
                ScriviLog("New User inserted");
            }
            // inserimento in richieste
            strSerial = String.format("%04d%02d%02d%02d%02d%02d%03d",
               data.get(Calendar.YEAR),
               data.get(Calendar.MONTH)+1,
               data.get(Calendar.DAY_OF_MONTH ),
               data.get(Calendar.HOUR_OF_DAY),
               data.get(Calendar.MINUTE),
               data.get(Calendar.SECOND),
               data.get(Calendar.MILLISECOND));
            stmt = conn.prepareStatement("INSERT INTO requests " +
               "(re_us_mail,re_data,re_cent,re_serial)" +
               "VALUES (?,?,?,?)");
            stmt.setString(1, strUser);
            stmt.setString(2, strData);
            stmt.setLong(3, lngCent);
            stmt.setString(4, strSerial);
            stmt.executeUpdate();
            System.out.println(" New request inserted for " + strUser);
            stmt = conn.prepareStatement("SELECT re_id FROM requests " +
               "WHERE re_us_mail=? AND re_serial=?");
            stmt.setString(1, strUser);
            stmt.setString(2, strSerial);
            rs = stmt.executeQuery();
            rs.first();
            long rc = (long)rs.getLong(1);
            ScriviLog("Get request id: " + rs.getString(1));
            rs.close();
            conn.close();
            conn = null;
            if (rc >= 0)
                return rc;
            else
                return -3;
        } 
        catch (SQLException ex) {
            ScriviLog("Request: " + ex.getMessage());
            return -2;
        }
    }
    
    static void ScriviLog(String strLine) {
        try {
            Calendar data = new GregorianCalendar();
            String strData = String.format("%04d-%02d-%02d %02d:%02d:%02d ",
                data.get(Calendar.YEAR),
                data.get(Calendar.MONTH)+1,
                data.get(Calendar.DAY_OF_MONTH ),
                data.get(Calendar.HOUR_OF_DAY),
                data.get(Calendar.MINUTE),
                data.get(Calendar.SECOND));
            String strNome = String.format("ws%04d%02d.log",data.get(Calendar.YEAR),
                data.get(Calendar.MONTH)+1);
            // rimettere file corretto prima di mandare al server
            
            
            File fl = new File("/utenti/centiscape/fastcent/"+ strNome);
          //  File fl = new File("/home/scardoni/" + strNome);
            
            FileOutputStream fo;
            if (fl.createNewFile()){
                // file creato, apre in scrittura
                fo = new FileOutputStream(fl,false);
            }
            else {
                 //esisteva, apre in append
                fo = new FileOutputStream(fl,true);
            }
            java.io.PrintStream scrivi = new java.io.PrintStream(fo);
            scrivi.println(strData + strLine);
            scrivi.flush();
            scrivi.close();
            fo.close();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());   
        }
    }
    
  
    public long LoadNetwork(long lngIdRequest, String pUser,
        String pNetworkName, String networkString
        //,@WebParam(name = "pNetworkStream") byte[] pFileSif,
       // @WebParam(name = "intStreamLength") int intStreamLength
        ) {
        long rc = -1;
        String strUser = pUser;
        String strNetworkName = pNetworkName;
        
        
        try {
                                  
            // controlla se esiste richiesta con id dato per l'utente
            // da ripristinare su server
            if (!blnVerifyRequest(strUser,lngIdRequest)) {
                ScriviLog("Not authenticate user for this request: " +
                    strUser + " - " + String.format("%d",lngIdRequest));
                return rc;
            }
          
            String strDir;
            try {
               
                strDir = "/utenti/centiscape/fastcent/";   
                //mettere per prova locale
            //strDir = "/home/scardoni/";
            }
            finally {
            }
            if (!strDir.isEmpty())
                strRepository = strDir;
            // nome file da elaborare
            String strFileDest = strRepository + String.format("%s.sif",strNetworkName);
                
          /*  if ((pFileSif[0] == 0) || (pFileSif == null) || 
                (intStreamLength == 0)) {
                // file inviato via ftp, farà solo l'aggiornamento del record
            }*/
            /*else {
                FileOutputStream file = new FileOutputStream(strFileDest);
                file.write(pFileSif,0,intStreamLength);
                file.flush();
                file.close();
                ScriviLog(strFileDest + " file received");
            }*/
            // scrivo il file
            
              writenetworktofile(networkString, strFileDest);
              
              System.out.println("scrivo su" + strFileDest);
            
            writenetworktofile(networkString,"/utenti/centiscape/fastcent/prova");
            // File pronto o trasferito; aggiorno il database scrivendo nome della rete
            rc = lngFileDisponibile(lngIdRequest,strNetworkName);
            // da eliminare su server
          //  rc = 0;
           
            if (rc == 0) {
                ScriviLog(strFileDest + " file ready");
                // lancio programma di gestione code presente nel repository
                // assieme a FastCent
                String strCmd = String.format("java -jar %sFastCentMonitor.jar %d %s",
                    strRepository,lngIdRequest,strRepository);
                ScriviLog(strCmd);
                LanciaElaborazione(strCmd);
            }
            else
                ScriviLog("PROBLEM in receiving file; can't start FastCentMonitor");
            ScriviLog("Web Service End");
            return rc;
        }
        catch(Exception ex) {
            ScriviLog("LoadNetwork: " + ex.getMessage());
            return rc;   
        }
    }
     boolean blnVerifyRequest(String strUser,long lngID) {
        boolean rc = false;
        try {
            if(conn==null) {
                ScriviLog("blnVerifyRequest: Creating connection");
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(dbConnection, dbUser,dbPwd);
            }
        }
        catch (Exception e) {
            ScriviLog("blnVerifyRequest: " + e.getMessage());
        }
        if(conn == null) {
            ScriviLog("blnVerifyRequest: Connection error");
            return rc;
        }
        // connessione OK, verifico esistenza coppia id/User
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT count(*) FROM requests " +
               "WHERE re_id=? AND re_us_mail=? AND re_stato=0");
            stmt.setLong(1, lngID);
            stmt.setString(2, strUser);
            java.sql.ResultSet rs;
            rs = stmt.executeQuery();
            rs.first();
            rc = (rs.getInt(1) == 1);
            rs.close();
            conn.close();
            conn = null;
        }    
        catch (Exception e) {
            ScriviLog("blnVerifyRequest: " + e.getMessage());
        }
        return rc;
    }
     long lngFileDisponibile(long lngIdRequest,String strFileName) {
        long rc = -1;
        try {
            if(conn == null) {
                ScriviLog("lngFileDisponibile: Creating connection");
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(dbConnection, dbUser,dbPwd);
            }
        }
        catch (Exception e) {
            ScriviLog("lngFileDisponibile: " + e.getMessage());
        }
        if(conn==null) {
            ScriviLog("lngFileDisponibile: " +  "Connection error");
            return rc;
        }
        try {
            PreparedStatement stmt = conn.prepareStatement("UPDATE requests " +
               "SET re_network=?, re_stato=1 WHERE re_id=?");
            stmt.setString(1, strFileName);
            stmt.setLong(2, lngIdRequest);
            stmt.executeUpdate();
            rc = 0;
            conn.close();
            conn = null;
        }    
        catch (Exception e) {
            ScriviLog("lngFileDisponibile: " + e.getMessage());
        }
        return rc;
    }
     
      void LanciaElaborazione(String strCommand) {
        try {
            ScriviLog("LanciaElaborazione: " + "Exec " + strCommand);
            Process p = Runtime.getRuntime().exec(strCommand); 
            if (p == null)
                ScriviLog(" Can't exec " + strCommand);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    
}
