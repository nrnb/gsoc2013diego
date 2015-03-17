
package org.cytoscape.centiscape.internal;
/*
 * CentiScaPeThreadEngine.java
 *
 * Created on 20 marzo 2007, 14.47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author scardoni
 */

import javax.swing.*;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.view.model.CyNetworkView;



public class CentiScaPeThreadEngine extends Thread {
    
    private CentiScaPeAlgorithm CentiScaPealg;
    public CyNetwork currentnetwork;
    public CyNetworkView currentnetworkview;
    //private JFrame c;
    private JPanel c;
    /**
     * Creates a new instance of CentiScaPeThreadEngine
     */
    public CentiScaPeThreadEngine(CentiScaPeAlgorithm centiscapealgorithm, CyNetwork currentnetwork, CyNetworkView currentnetworkview) {
     this.currentnetwork = currentnetwork;
     this.currentnetworkview = currentnetworkview;
      this.CentiScaPealg= centiscapealgorithm;
    
    }
    
 
    
    public void run(){
        
        CentiScaPealg.ExecuteCentiScaPeAlgorithm(currentnetwork, currentnetworkview, c);
        c.setEnabled(true);
    }
    
    public void setCaller(JPanel caller){
        c=caller;
    }

    public void endprogram() {
        CentiScaPealg.endalgorithm();
    }
    
}

    

