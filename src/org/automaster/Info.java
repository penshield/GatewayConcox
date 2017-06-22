/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.automaster;

import client.Cliente;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.TreeMap;
import serializable.Registro;

/**
 *
 * @author JoãoRenato
 */
public abstract class Info {

    public static ArrayList<String> filaLog = new ArrayList<>();
//    public static TreeMap<Long, Cliente> clientes = new TreeMap<>();
    public static TreeMap<Long, Integer> comandosHydra = new TreeMap<>();
    public static TreeMap<Integer, Cliente> clientesPostgresSQL = new TreeMap<>();
    public static FilaReg<TreeMap<String, Object>> filaDeRegistros = new FilaReg<>();
    public static FilaRegistroExterno<Registro> filaDeRegistrosExternos = new FilaRegistroExterno<>();
    public static boolean continuaComandosPostgresSQL;
    public static ServerSocket serverSocket;
    //public static DatagramSocket serverSocketPostgresSQL;
    public static boolean continuaComandos;
    public static TreeMap<String, String> parametros = new TreeMap<>();
    public static Socket cliente;
    public static ObjectOutputStream oos;
    public static TreeMap<Long, String> accCrx1 = new TreeMap<Long, String>();

    // URL de Comandos da API Hydra
//    private static final String urlHydra = "http://hydra.dipsystem.com.br/ws-command";
    private static final String urlHydra = "http://104.131.86.187/ws-command";
    // Minha chave de acesso do Hydra Concox
    private static final String hashKey = "5efc3a30f1bd7caace5cd4e70b8bff38891ecd9a1a73f2fc27b6d9444dd26c38";
    //Agente responsável por enviar a requisição
    private static final String USER_AGENT = "Mozilla/5.0";

    public static TreeMap<String, String> sendPost(String imei, String comando) throws Exception {

        try {
            URL obj = new URL(urlHydra);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            String urlParameters = "imei_sn=" + imei + "&command=" + comando + "&hashkey=" + hashKey;
            Info.filaLog.add("URL: " + urlParameters);
            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
//            System.out.println("\nSending 'POST' request to URL : " + urlHydra);
//            System.out.println("Post parameters : " + urlParameters);
//            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            //print result
//            System.out.println(response.toString());
            Gson gson = new Gson();
            JsonElement element = gson.fromJson(response.toString(), JsonElement.class);
            JsonObject jsonObj = element.getAsJsonObject();
            if (jsonObj.get("success").getAsBoolean() && jsonObj.has("command_id")) {
                int comando_id = jsonObj.get("command_id").getAsInt();
                Info.comandosHydra.put(Long.parseLong(imei), comando_id);
                String msg = jsonObj.get("msg").getAsString();
                boolean success = jsonObj.get("success").getAsBoolean();
                TreeMap<String, String> cmd = new TreeMap<String, String>();
                cmd.put("command_id", String.valueOf(comando_id));
                cmd.put("msg", msg);
                cmd.put("success", String.valueOf(success));
                Info.filaLog.add("[COMANDOS sendPost]: " + cmd);
//                System.out.println("MSG: "+jsonObj.get("msg").getAsString());
//                System.out.println("SUCESS: "+jsonObj.get("success").getAsBoolean());
//                System.out.println("Command_ID: "+comando_id);
            } else {
                Info.filaLog.add("JSON sendPost: " + jsonObj);
            }
        } catch (Exception e) {
            Info.filaLog.add("Error [sendPost]: " + e.toString());
            return null;
        }
        return null;
//                String requestBody = Util.convertStreamToString(in);
//                JSONObject docJson = new JSONArray(response.toString()).getJSONObject(0);

    }

    // HTTP GET request
    public static TreeMap<String, String> sendGet(String imei, String comandoId) throws Exception {

        try {
            String url = urlHydra + "?hashkey=" + hashKey + "&imei_sn=" + imei + "&command_id=" + comandoId;
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();
//        System.out.println("\nSending 'GET' request to URL : " + url);
//        System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            //print result
//        System.out.println(response.toString());
            Gson gson = new Gson();
            JsonElement element = gson.fromJson(response.toString(), JsonElement.class);
            JsonObject jsonObj = element.getAsJsonObject();
            if (jsonObj.get("success").getAsBoolean()) {
                boolean success = jsonObj.get("success").getAsBoolean();
                String msg = jsonObj.get("msg").getAsString();
                JsonArray command_detail = jsonObj.getAsJsonArray("command_detail");
                JsonObject dataObj = command_detail.get(0).getAsJsonObject();
                String id = String.valueOf(dataObj.get("id"));
                String name = dataObj.get("name").getAsString();
                String command = dataObj.get("command").getAsString();
                String status = dataObj.get("status").getAsString(); // To send
                String dateTime = dataObj.get("last_update").getAsString();
                String dataSql = Util.formatarDataHoraSQL(Util.formatarDataHora(dateTime));
                TreeMap<String, String> cmd = new TreeMap<String, String>();
                cmd.put("success", String.valueOf(success));
                cmd.put("msg", msg);
                cmd.put("id", String.valueOf(id));
                cmd.put("name", name);
                cmd.put("command", command);
                cmd.put("status", status);
                cmd.put("dateTime", dataSql);
                Info.filaLog.add("[COMANDOS sendGet]: " + cmd);
                return cmd;
            }

        } catch (Exception e) {
            return null;
        }
        Info.filaLog.add("JSON sendGet: ");
        return null;
    }

}
