/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.net.Socket;
import java.nio.channels.SocketChannel;

/**
 *
 * @author Renato
 */
public class Cliente {
    
    private long id;
    private Socket conexao;
    
    public Cliente (long id, Socket conexao) {
        this.id = id;
        this.conexao = conexao;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Socket getConexao() {
        return conexao;
    }

    public void setConexao(Socket conexao) {
        this.conexao = conexao;
    }
}
