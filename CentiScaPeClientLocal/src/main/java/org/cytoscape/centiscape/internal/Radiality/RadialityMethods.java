/*
 * RadialityMethods.java
 *
 * Created on 10 dicembre 2007, 12.05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */



/**
 *
 * @author scardoni
 */
package org.cytoscape.centiscape.internal.Radiality;

import java.util.Iterator;
import java.util.Vector;



public class RadialityMethods {
    
    /** Creates a new instance of RadialityMethods */
    public RadialityMethods() {
    }
    
    
    public static void calculateRadiality(Vector RadialityVectorResults, double Diameter, int totalnodecount) {
        
        for (Iterator i = RadialityVectorResults.iterator() ;i.hasNext(); ) {
            
            FinalResultRadiality currentRadiality;
            currentRadiality = (FinalResultRadiality)i.next();
            double Diametervalue = Diameter;
            int currentDist = 0;
            double currentRadialityvalue, parziale1, parziale2;
            for (int j = 0; j < currentRadiality.getVectorSize(); j++) {
                currentDist = currentRadiality.getlistsizeat(j);
                if (currentDist != 0) {
                    parziale1 = Diametervalue+1-currentDist;
                    parziale2 = totalnodecount-1;
                    currentRadialityvalue = ((Diametervalue+1-currentDist));
                    currentRadiality.update(currentRadialityvalue);
                }
            }
            currentRadiality.finalcalculus(totalnodecount-1);
        }
    }
    
}
