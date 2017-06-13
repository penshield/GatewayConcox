/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.automaster.gatewayconcox;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.automaster.Info;
import org.automaster.ThreadComandos;
import org.automaster.ThreadEscreve;
import org.automaster.server.ThreadEnviaRegistroExterno;
import org.automaster.server.ThreadRecebeJson;

/**
 *
 * @author J.R
 */
public class GatewayConcox {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        if (args.length > 0) {
            String[] params = args[0].split(";");
            for (int i = 0; i < params.length; i++) {
                String[] valores = params[i].split("=");
                Info.parametros.put(valores[0], valores[1]);
            }
        }
        if (Info.parametros.get("log") == null) {
            Info.parametros.put("log", "1");
        }
        Info.filaLog.add("Iniciando nova instancia do programa GatewayConcox...\n");
        try {
            Info.cliente = new Socket("85.93.91.19", 1001);
            Info.oos = new ObjectOutputStream(Info.cliente.getOutputStream());
        } catch (Exception e) {
            Info.filaLog.add(e.getMessage());
            e.printStackTrace();
        }

        (new Thread(new ThreadEscreve())).start();
//        (new Thread(new ThreadPrincipalTCP())).start();
//        (new Thread(new ThreadPrincipal())).start();
        
//        (new Thread(new ThreadInsereBanco())).start();        
        (new Thread(new ThreadEnviaRegistroExterno())).start();
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(9062), 0);
            server.createContext("/", new MyHandler());
            server.start();
            Info.filaLog.add("Started Server in http://127.0.0.1:9062/");
//            Info.filaLog.add("Crtl + C para encerrar");

        } catch (Exception e) {
            Info.filaLog.add(e.getMessage());
            e.printStackTrace();
        }
        (new Thread(new ThreadComandos())).start();
    }

    static class MyHandler implements HttpHandler {

        public void handle(HttpExchange t) throws IOException {
            //get request information convert to String
            String saida = "";
            InputStream is = t.getRequestBody();
            String requestBody = convertStreamToString(is);
            //convert string in object json
            Gson gson = new Gson();
            JsonElement element = gson.fromJson(requestBody, JsonElement.class);
            JsonObject jsonObj = element.getAsJsonObject();
            (new Thread(new ThreadRecebeJson(jsonObj))).start();
            String response = "Agora cabe a você! Faça a magia";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            //return 200 code if success trated message!
            os.write(response.getBytes());
            os.close();
        }

        public String convertStreamToString(java.io.InputStream is) {
            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }
    }
}
