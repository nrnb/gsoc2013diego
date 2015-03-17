package com.mycompany.objectserver;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;



/**
 * Hello world!
 *
 */





public class App  {
    
    
  
    

public static void main(String[] args) throws IOException, ClassNotFoundException {

    
    Connection conn = null;    // Connection variable 
    //TODO impostare i valori del server
    // Ip, porta e database da usare per la connessione a MySQL
    String dbConnection = "jdbc:mysql://localhost:3306/Centiscape";
    // Utente e password per accesso a db MySQL
    String dbUser = "Centiscape";
    String dbPwd = "centiscape162012";
   
    try {
           
                System.out.println("Request0: Creating connection");
               // Class.forName("com.mysql.jdbc.Driver");
              
        }
        catch (Exception e) {
            System.out.println("Request1: " + e.getMessage() + e.getLocalizedMessage() );
        }
      
           //System.out.println("Request: " + "Connection = " + conn.toString());
    try {
       conn = DriverManager.getConnection(dbConnection,dbUser,dbPwd);
             System.out.println("Request2: " + "Connection = ");
    }
     catch (SQLException ex) {
    // handle any errors
    System.out.println("SQLException: " + ex.getMessage());
    System.out.println("SQLState: " + ex.getSQLState());
    System.out.println("VendorError: " + ex.getErrorCode());
}
            
            
      
  
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(4444);
            System.out.println("Waiting for connection");
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
            System.exit(1);
        }

        
        int count = 0;
        while (true) {
        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
   BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        
        
  /*      InputStream is = clientSocket.getInputStream();   
   ObjectInputStream ois = new ObjectInputStream(is);   
    Pappo obj = (Pappo)ois.readObject();  
  */
          
   
        
        String inputLine, outputLine;
                  ObjectProtocol kkp = new ObjectProtocol();
  
                  
                         
        outputLine = kkp.processInput(null);
        //mando conferma connessione
        
        out.println(outputLine);
        
        //ricevo e-mail
        
        inputLine = in.readLine();
         
        
        outputLine = kkp.processInput(inputLine);
      
        // mettere codice per assegnare ID va in lngID
        
         // Long lngID = new Long(33);
          
          // mando emailricevuta
        //Gson gsonID = new Gson();
         // String jsonID = gsonID.toJson(lngID);
        out.println(outputLine);
        
        // ricevo lngCent
        
        inputLine = in.readLine();
        outputLine = kkp.processInput(inputLine);
        
        
        // mando lngCent
        out.println(outputLine);
        
        // ricevo network SUID
        
         inputLine = in.readLine();
        outputLine = kkp.processInput(inputLine);
        out.println(outputLine);
        
        //ricevo network
        
        inputLine = in.readLine();
        outputLine = kkp.processInput(inputLine);
        
          System.out.println("mandorc");
        // mettere codice per inviare conferma ricezione network lngrc=0
        
         out.println(outputLine);  
          
       /* Gson gsonrc = new Gson();
        Long lngrc = new Long(0);
        String jsonrc = gsonrc.toJson(lngrc);
        out.println(jsonrc);*/
        
         
         
        out.close();
       // is.close();
        
        in.close();
        clientSocket.close();
        count ++;
        System.out.println("count è= " + count);
        System.out.println("waiting for connections");
        }
        //serverSocket.close();
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
            File fl = new File("/utenti/centiscape/fastcent/"+ strNome);
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
    
    

    /** 
     * lngFileDisponibile
     * Aggiorna tabella richieste segnalando che i dati sono a disposizione 
     * 
     */
    
   
    

  

}

