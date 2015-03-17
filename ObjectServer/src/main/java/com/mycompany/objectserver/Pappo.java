/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.objectserver;

import java.io.Serializable;

/**
 *
 * @author scardoni
 */
public class Pappo implements Serializable {
    
    String frase;
    
    public Pappo (String frase) {
        this.frase = frase;
    }
    
    public String toString() {
    return frase;
}
}


