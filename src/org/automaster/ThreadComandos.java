/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.automaster;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;
import org.automaster.db.ComandoBancoPostgresSQL;

/**
 *
 * @author Renato
 */
public class ThreadComandos implements Runnable {
    
    public void run() {
        Info.filaLog.add("[COMANDOS POSTGRESQL] - " + "Iniciando Thread!");
        Info.continuaComandos = true;
        inicia();
    }
    
    public void inicia () {
        try {
            while (Info.continuaComandos) {                
                verificaPostgresSQL();
                Thread.sleep(1000);
//                verifica();
            }
            Info.filaLog.add("[COMANDOS POSTGRESQL] 01 - " + "T hread finalizada!");
        } catch (Exception e) {
            Info.filaLog.add("[COMANDOS POSTGRESQL] 02 - " + e.getMessage());
            run();
        }
    }
    ///////////////////////////////////////////////////////////
    private void verificaPostgresSQL() {
        ArrayList<TreeMap<String, String>> comandos = ComandoBancoPostgresSQL.verificaComandos();
        if (!comandos.isEmpty()) {
            for (TreeMap<String, String> comando : comandos) {
                long id = Long.parseLong(comando.get("id"));
                int modelo = -1;
                Info.filaLog.add("[Linha -1 MODELO COMANDOS PostgreSQL] : " + comando.get("modelo"));
                if (Integer.parseInt(comando.get("modelo")) > 0) {
                    modelo = Integer.parseInt(comando.get("modelo"));
                    Info.filaLog.add("[Linha 00 ID COMANDOS PostgreSQL] : " + String.valueOf(id));
                }
                Info.filaLog.add("[Linha 01 ID COMANDOS PostgresSQL] : " + String.valueOf(id));
                if (modelo == 11) {
                    String msg = "0";
                    // Teste para 5 pinos //
                    if (comando.get("comando").equalsIgnoreCase("bloquear")) {
                        msg = "Bloquear";
                    } else if (comando.get("comando").equalsIgnoreCase("desbloquear")) {
                        msg = "Desbloquear";
                    } else if (comando.get("comando").equalsIgnoreCase("SolicitarGPS")) {
                        msg = "Solicitargps";
                    }
                    Info.filaLog.add("[Linha 02 ID COMANDOS PostgresSQL] : " + String.valueOf(id));
                    // Fim do teste para 5 pinos //
                    if (msg.equalsIgnoreCase("0")) {
                        //Comando de configuração não implementado!
                        ComandoBancoPostgresSQL.atualizaComandos(1, comando.get("cod"), "10", null, (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()), null, -1);
                        Info.filaLog.add("[COMANDOS POSTEGRESQL 03 ] - Comando de configuração não implementado Concox\n");
                    }  else {
                        Info.filaLog.add("[Linha 03 ID COMANDOS PostgresSQL] : " + String.valueOf(id));
                        if (msg.length() > 3) {
                            try {
                                Info.sendPost(comando.get("id"), msg);
                                //Mensagem enviada ao servidor!
                                ComandoBancoPostgresSQL.atualizaComandos(1, comando.get("cod"), "14", null, (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()), null, -1);
                                //ComandoBanco.atualizaComandos(1, comando.get("cod"), comando.get("id"), "301", "Mensagem enviada ao servidor!", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()), null, -1);                                
                                Info.filaLog.add("[COMANDOS POSTEGRESQL 05] - Comando enviado:\n" + msg);
                            } catch (Exception e) {
                                Info.filaLog.add("[COMANDOS POSTEGRESQL 06] "+ e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                        //ComandoBanco.atualizaComandos(1, comando.get("cod"), null, "99", "Erro! Equipamento desconhecido, consulte a Auto Master!", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()), null, -1);
                    //Erro! Equipamento desconhecido, consulte a Auto Master!
                    Info.filaLog.add("[COMANDOS POSTEGRESSQL 07 ] - Equipamento desconhecido, consulte a Auto Master Concox\n");
                    ComandoBancoPostgresSQL.atualizaComandos(1, comando.get("cod"), "12", null, (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()), null, -1);
                }
            }
        }
    }
    
    
    
    
}
    

