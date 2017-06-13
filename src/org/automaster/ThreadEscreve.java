/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.automaster;

import java.io.*;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Renato
 */
public class ThreadEscreve implements Runnable{
    
    private SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public void run() {
        inicia();
    }
    
    public void inicia () {
        try {
            while (true) {
                while (!Info.filaLog.isEmpty()) {
                    escreve(Info.filaLog.get(0));
                    Info.filaLog.remove(0);
                }
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            escreve(e.getMessage());
        }
    }
    
    public void escreve (String texto) {
        File pasta = new File("log");
        if (!pasta.exists()) {
            pasta.mkdir();
        }
        boolean index = false;
        boolean vazio = false;
        Date now = new Date();
        String data = now.getDate() + "_" + (now.getMonth() + 1) + "_" + ((now.getYear() + 1900) + "").substring(2, 4);
        String nomeArquivo = "log/log.log";
        File arquivo = new File(nomeArquivo);
        try {
            if (arquivo.exists()) {
                BufferedReader bf = new BufferedReader(new FileReader(nomeArquivo));
                String dataArquivo = "";
                dataArquivo = bf.readLine();
                bf.close();
                if (dataArquivo.equalsIgnoreCase(data)) {
                    index = true;
                } else {
                    File pasta2 = new File("log/antigos");
                    if (!pasta2.exists()) {
                        pasta2.mkdir();
                    }
                    FileChannel oriChannel = new FileInputStream("log/log.log").getChannel();
                    FileChannel destChannel = new FileOutputStream("log/antigos/" + dataArquivo + ".log").getChannel();
                    destChannel.transferFrom(oriChannel, 0, oriChannel.size());
                    oriChannel.close();
                    destChannel.close();
                    vazio = true;
                    try {
                        Runtime.getRuntime().exec("gzip -9 log/antigos/" + dataArquivo + ".log");
                    } catch (Exception e) {
                        Info.filaLog.add(e.getMessage());
                    }
                }
            }
            PrintWriter pw = null;
            if (vazio) {
                pw = new PrintWriter(new FileWriter(arquivo));
            } else {
                pw = new PrintWriter(new FileWriter(arquivo, true));
            }
            if (!index) {
                pw.println(data);
            }
            pw.println("[" + formataData.format(now) + "] - " + texto);
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
}
