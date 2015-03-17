/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cytoscape.centiscape.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.application.swing.CytoPanelState;
import org.cytoscape.centiscape.internal.visualizer.CentVisualizer;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.view.model.CyNetworkView;

/**
 *
 * @author scardoni
 */
public class CentiScaPeCore {
    public CyNetwork network;
    public CyNetworkView view;
    public CyApplicationManager cyApplicationManager;
    public CySwingApplication cyDesktopService;
    public CyServiceRegistrar cyServiceRegistrar;
    public CyActivator cyactivator;
    public CentiScaPeStartMenu centiscapestartmenu;
    public CentVisualizer centvisualizer;
    public ArrayList visualizerlist;
    //  public CentiScaPeListener listener;

    public CentiScaPeCore(CyActivator cyactivator) {
        System.out.println("entrato in centiscape core");        
        this.cyactivator = cyactivator;
        this.cyApplicationManager = cyactivator.getcyApplicationManager();
        this.cyDesktopService = cyactivator.getcytoscapeDesktopService();
        this.cyServiceRegistrar = cyactivator.getcyServiceRegistrar();
        centiscapestartmenu = createCentiScaPeStartMenu();
        visualizerlist = new ArrayList();
        //  centvisualizer = createCentiScaPeVisualizer();
        updatecurrentnetwork();



        /* 
           

         
           
         listener = new CentiScaPeListener(this);
         */
    }

    public void updatecurrentnetwork() {

        //get the network view object
        if (view == null) {
            view = null;
            network = null;
        } else {
            view = cyApplicationManager.getCurrentNetworkView();
            //get the network object; this contains the graph  

            network = view.getModel();
        }

    }
    /*

     public void hideresults() {
    
     CytoscapeDesktop desktop = Cytoscape.getDesktop();
     CytoPanel cytoPaneleast = desktop.getCytoPanel(SwingConstants.EAST);
     cytoPaneleast.setState(CytoPanelState.HIDE);
           
     }

     public void showresults() {
    
     CytoscapeDesktop desktop = Cytoscape.getDesktop();
     CytoPanel cytoPaneleast = desktop.getCytoPanel(SwingConstants.EAST);
     cytoPaneleast.setState(CytoPanelState.DOCK);
    
     }

     public void hideattributes() {
     CytoscapeDesktop desktop = Cytoscape.getDesktop();
     CytoPanel cytoPanelsouth = desktop.getCytoPanel(SwingConstants.SOUTH);
     cytoPanelsouth.setState(CytoPanelState.HIDE);
     cytoPanelsouth.setState(CytoPanelState.DOCK);
     }


     public void removecurrentnetworkattributes() {
        
     CentiScaPeStartMenu startmenu = windows.getstartmenu();
     startmenu.removeattributes();
     }
     */

    public void closecore() {
        // listener = null;
        network = null;
        view = null;

    }
 
    public CyApplicationManager getCyApplicationManager() {
        return this.cyApplicationManager;
    }

    public CySwingApplication getCyDesktopService() {
        return this.cyDesktopService;
    }

    public CentiScaPeStartMenu createCentiScaPeStartMenu() {
        System.out.println("create1");
        CentiScaPeStartMenu startmenu = new CentiScaPeStartMenu(cyactivator, this);
        System.out.println("create2");
        cyServiceRegistrar.registerService(startmenu, CytoPanelComponent.class, new Properties());
        System.out.println("create3");
        CytoPanel cytopanelwest = cyDesktopService.getCytoPanel(CytoPanelName.WEST);
        int index = cytopanelwest.indexOfComponent(startmenu);
        cytopanelwest.setSelectedIndex(index);
        return startmenu;
    }

    public void closeCentiscapeStartMenu() {

        cyServiceRegistrar.unregisterService(centiscapestartmenu, CytoPanelComponent.class);
    }

    //cambiare il create e close result
    public CentVisualizer createCentiScaPeVisualizer() {
        System.out.println("create1");
        centvisualizer = new CentVisualizer(cyApplicationManager);//(cyactivator,this);
        System.out.println("create2");
        cyServiceRegistrar.registerService(centvisualizer, CytoPanelComponent.class, new Properties());
        System.out.println("create3");
        CytoPanel cytopaneleast = cyDesktopService.getCytoPanel(CytoPanelName.EAST);
        cytopaneleast.setState(CytoPanelState.DOCK);
        int index = cytopaneleast.indexOfComponent(centvisualizer);
        cytopaneleast.setSelectedIndex(index);
        visualizerlist.add(centvisualizer);
        return centvisualizer;
    }

    public void closeCentiscapevisualizer() {
        for (Iterator i = visualizerlist.listIterator(); i.hasNext();) {
            CentVisualizer currentvisualizer = (CentVisualizer) i.next();    
 
        cyServiceRegistrar.unregisterService(currentvisualizer, CytoPanelComponent.class);
        }
    }

    public CentVisualizer getvisualizer() {
        return centvisualizer;
    }
}
