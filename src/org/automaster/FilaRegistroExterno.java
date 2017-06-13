/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.automaster;

import java.util.ArrayList;

/**
 *
 * @author J.R
 * @param <Registro>
 */
public class FilaRegistroExterno<Registro> extends ArrayList<Registro> {
    
    public Registro first () {
        try {
            return remove(0);
        } catch (IndexOutOfBoundsException exp) {
            Info.filaLog.add(exp.getMessage());
            return null;
        }
    }
    
}
