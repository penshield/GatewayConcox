/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.automaster.server;

import java.io.ObjectOutputStream;
import java.net.Socket;
import org.automaster.Info;
import serializable.Registro;

/**
 *
 * @author J.R
 */
public class ThreadEnviaRegistroExterno implements Runnable {

    @Override
    public void run() {
        Info.filaLog.add("[ThreadEnviaRegistroExterno] - " + "Iniciando Thread!");
        inicia();
    }
    
    public void inicia () {
        try {
            while (true) {
//                Info.filaLog.add("[ThreadEnviaRegistroExterno] - " + "Primeira linha! While Inicia");
                verifica();
//                Info.filaLog.add("[ThreadEnviaRegistroExterno] - " + "Setima linha! Esperando 2 segundos");
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            Info.filaLog.add("[ThreadEnviaRegistroExterno] - " + "Thread finalizada!");
            Info.filaLog.add("[ThreadEnviaRegistroExterno] - " + e.getMessage());
            run();
        }
    }
    
    private void verifica () {
//        Info.filaLog.add("[ThreadEnviaRegistroExterno] - " + "Seguna linha! Verifica");
        int tamFila = Info.filaDeRegistrosExternos.size();
//        Info.filaLog.add("[ThreadEnviaRegistroExterno] - " + "Terceira linha! depois do tamFila");
        if (tamFila > 0) {
            for (int i = 0; i < tamFila; i++) {
                Registro registro = Info.filaDeRegistrosExternos.first();
//                Info.filaLog.add("[ThreadEnviaRegistroExterno] - " + "Quarta linha! depois do first");
                try {
                    Info.oos.writeObject(registro);
//                    Info.filaLog.add("[ThreadEnviaRegistroExterno] - " + "Quinta linha! depois do write");
                    Info.oos.flush();
//                    Info.filaLog.add("[ThreadEnviaRegistroExterno] - " + "Sexta linha! depois fo flush");
                } catch (Exception e) {
                    Info.filaLog.add("Erro ao enviar no Socket! Tentando reconectar e enviar novamente! Msg de Erro:\n" + e.getMessage());
                    try {
                        Info.cliente = new Socket("85.93.91.19", 1001);
                        Info.oos = new ObjectOutputStream(Info.cliente.getOutputStream());
                        Info.oos.writeObject(registro);
                        Info.oos.flush();
                    } catch (Exception e2) {
                        Info.filaLog.add("Erro novamente! Pacote perdido! Msg de Erro:\n" + e2.getMessage());
                    }
                }
            }
        }
        Info.filaLog.add("[ThreadEnviaRegistroExterno] - " + "Enviados " + tamFila + " registros!");
    }
    
}
