package org.cytoscape.centiscape.internal;

import java.util.Properties;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.io.read.CyNetworkReaderManager;
import org.cytoscape.io.util.StreamUtil;
import org.cytoscape.io.write.CyNetworkViewWriterManager;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.work.TaskManager;
import org.osgi.framework.BundleContext;
import org.cytoscape.task.read.LoadNetworkFileTaskFactory;


public class CyActivator extends AbstractCyActivator {

    public CyApplicationManager cyApplicationManager;
    public CySwingApplication cyDesktopService;
    public CyServiceRegistrar cyServiceRegistrar;
    public CentiScaPeMenuAction menuaction;
    public CyNetworkViewWriterManager networkViewWriterManager;
    public TaskManager taskManager;
    public CyNetworkManager cyNetworkManager;
    CyNetworkReaderManager cyNetworkReaderManager;
    public StreamUtil streamUtil;
    public LoadNetworkFileTaskFactory loadNetworkFileTaskFactory;
    
    @Override
    public void start(BundleContext context) throws Exception {

        String version = new String("2.3");    
        this.cyApplicationManager = getService(context, CyApplicationManager.class);
        this.cyDesktopService = getService(context, CySwingApplication.class);
        this.cyServiceRegistrar = getService(context, CyServiceRegistrar.class);
        networkViewWriterManager = getService(context, CyNetworkViewWriterManager.class);
        streamUtil = getService(context, StreamUtil.class);
        taskManager = getService(context, TaskManager.class);
        menuaction = new CentiScaPeMenuAction(cyApplicationManager, "Centiscape" + version, this);
        Properties properties = new Properties();
        registerAllServices(context, menuaction, properties);
        this.loadNetworkFileTaskFactory= getService(context, LoadNetworkFileTaskFactory.class);
        cyNetworkReaderManager=getService(context, CyNetworkReaderManager.class);
        
        
    }
    public CyNetworkReaderManager getcyNetworkReaderManager() {
        return cyNetworkReaderManager;
    }
    
    public CyServiceRegistrar getcyServiceRegistrar() {
        return cyServiceRegistrar;
    }
     
    public LoadNetworkFileTaskFactory getLoadNetworkFileTaskFactory() {
        return loadNetworkFileTaskFactory;
    }
    
    public CyNetworkManager getCyNetworkManager() {
        return cyNetworkManager;
    }
    public TaskManager getTaskManager() {
        return taskManager;
    }
    
    public CyApplicationManager getcyApplicationManager() {
        return cyApplicationManager;
    }

    public CySwingApplication getcytoscapeDesktopService() {
        return cyDesktopService;
    }

    public CentiScaPeMenuAction getmenuaction() {
        return menuaction;
    }
    /*  

     public void createmenustart(CentiScaPeCore centiscapecore) {
     menustart = new CentiScaPeStartMenu(centiscapecore);
     registerService(context, menustart, CytoPanelComponent.class, new Properties());
      
     //return panel;
     }
    
     public CentiScaPeStartMenu getmenustart() {
     return menustart;
     }
    
     public void closemenustart(CentiScaPeCore centiscapecore) {
     System.out.println("chiudooooo menustart");
     CytoPanel cytoPanelWest = this.cyDesktopService.getCytoPanel(CytoPanelName.WEST);
     System.out.println("stoperchiudere menustart");
     ///   int index = cytoPanelWest.indexOfComponent(centiscapestartmenu);
       
        
     centiscapecore = null;    
     // menustart = null;
     menustart.setVisible(false);
     // menustart = null;
     System.out.println("ho chiusooooo menustart");
     //unregisterService(context, menustart, CytoPanelComponent.class, new Properties());
      
     }*/
}
