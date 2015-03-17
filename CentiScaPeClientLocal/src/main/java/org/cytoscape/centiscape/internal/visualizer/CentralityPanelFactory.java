/*
 * CentralityPanelFactory.java
 *
 * Created on 27 novembre 2007, 16.16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.cytoscape.centiscape.internal.visualizer;

import java.util.Observer;


/**
 *
 * @author admini
 */
public abstract class CentralityPanelFactory {
    
    private static Centrality cent;
    
    public static CentralityPanel allocateCentralityPanel(Observer obs,Centrality c){
        CentralityPanel cp=new CentralityPanel(obs,c);
//        System.out.println("centrality "+c.getName()+
//                " min="+c.getMinValue()+
//                " max="+c.getMaxValue()+
//                " def="+c.getDefaultValue());
        cp.setBorder(javax.swing.BorderFactory.createTitledBorder(c.getName()));
        cp.setSlider(c.getDefaultValue());
        return(cp);
    }
}
