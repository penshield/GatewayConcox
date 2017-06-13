/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.automaster.client;

import java.net.Socket;

/**
 *
 * @author Renato
 */
public class Cliente {
    
    private Integer id;
    private Socket conexao;
    
    public Cliente (Integer id, Socket conexao) {
        setId(id);
        setConexao(conexao);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Socket getConexao() {
        return conexao;
    }

    public void setConexao(Socket conexao) {
        this.conexao = conexao;
    }

}
