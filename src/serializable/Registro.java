/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializable;

import java.io.Serializable;
import java.util.TreeMap;

/**
 *
 * @author J.R
 */
public class Registro implements Serializable {
    
    private final TreeMap<String, Object> parametro = new TreeMap<>();
    
    public void addParametro (String chave, Object valor) {
        this.parametro.put(chave, valor);
    }
    
    public Object getParametro (String chave) {
        return this.parametro.get(chave);
    }
    
}
