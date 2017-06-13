/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.automaster;

import java.util.ArrayList;

/**
 *
 * @author J.R
 * @param <TreeMap>
 */
public class FilaReg<TreeMap> extends ArrayList<TreeMap> {
    
    public TreeMap first () {
        try {
            return remove(0);
        } catch (IndexOutOfBoundsException exp) {
            Info.filaLog.add(exp.getMessage());
            return null;
        }
    }
    
}
