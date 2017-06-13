/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.automaster.server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.automaster.Info;
import org.automaster.Util;
import org.automaster.db.ComandoBancoPostgresSQL;



/**
 *
 * @author automaster
 */
public class Teste {
    
    // URL de Comandos da API Hydra
    private static final String urlHydra = "http://hydra.dipsystem.com.br/ws-command";
    // Minha chave de acesso do Hydra Concox
    private static final String hashKey = "5efc3a30f1bd7caace5cd4e70b8bff38891ecd9a1a73f2fc27b6d9444dd26c38";
    private static final String USER_AGENT = "Mozilla/5.0";
    
    public static void main(String[] args) throws ParseException {
        
//        String data = "\"2017-06-06T13:05:30\"".replaceAll("T", " ").replaceAll("-", "/").replaceAll("\"", "");
//        System.out.println("Data padrão America/Belem: "+formatarDataHora(data));
//        System.out.println("Data Atual SQL: "+formatarDataHoraSQL(formatarDataHora(data)));
        try {
//            sendPost("351608083818943", "Desbloquear");
            sendGet("351608083818943", "199");
//            ComandoBancoPostgresSQL.verificaComandos();
        } catch (Exception e) {
            System.out.println("Error 01: "+e.toString());
        }

    }
    
    // HTTP GET request
    private static void sendGet(String imei, String comandoId) throws Exception {
        
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
            String status = dataObj.get("status").getAsString();
            String dateTime = dataObj.get("last_update").getAsString();
            String dataSql = Util.formatarDataHoraSQL(Util.formatarDataHora(dateTime));
//            System.out.println("SUCESS: " + success);
//            System.out.println("MSG: " + msg);
//            System.out.println("Command_ID: " + id);
//            System.out.println("name: " + name);
//            System.out.println("Command: " + command);
//            System.out.println("Status: " + status);
//            System.out.println("Date e time: " + dataSql);
            System.out.println("JSON: "+jsonObj);
        }
            
        } catch (Exception e) {
        } 

    }
    
    private static void sendPost(String imei, String comando) throws Exception {

        try {
            URL obj = new URL(urlHydra);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            String urlParameters = "imei_sn=" + imei + "&command="+comando+"&hashkey="+hashKey;
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
                String msg = jsonObj.get("msg").getAsString();
                boolean sucess = jsonObj.get("success").getAsBoolean();
//                System.out.println("MSG: "+jsonObj.get("msg").getAsString());
//                System.out.println("SUCESS: "+jsonObj.get("success").getAsBoolean());
//                System.out.println("Command_ID: "+comando_id);
            }
        } catch (Exception e) {
            Info.filaLog.add("Error [sendPost]: "+e.toString());
        }

//                String requestBody = Util.convertStreamToString(in);
//                JSONObject docJson = new JSONArray(response.toString()).getJSONObject(0);

	}
    
    public static String formatarDataHora(String data) {
        try {
            String dataHora = data.replaceAll("T", " ").replaceAll("-", "/");
            TimeZone timeZoneUTC = TimeZone.getTimeZone("UTC");
            TimeZone timeZoneBelem = TimeZone.getTimeZone("America/Belem");
            SimpleDateFormat formatadorData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            formatadorData.setTimeZone(timeZoneUTC);

            Date d1 = formatadorData.parse(dataHora.substring(8, 10) + "/" + dataHora.substring(5, 7) + "/" + dataHora.substring(0, 4) + " " + dataHora.substring(11, dataHora.length()));
            formatadorData.setTimeZone(timeZoneBelem);
            return formatadorData.format(d1);
        } catch (ParseException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            Info.filaLog.add("[Formatar Data] "+ex.toString());
            return null;
        }

    }
    
    public static String formatarDataHoraSQL(String data) {
        try {
            String dataHora = data;
            TimeZone timeZoneBelem = TimeZone.getTimeZone("America/Belem");
            SimpleDateFormat formatadorData = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatadorData.setTimeZone(timeZoneBelem);

            Date d1 = formatadorData.parse(dataHora.substring(6, 10) + "-" + dataHora.substring(3, 5) + "-" + dataHora.substring(0, 2) + " " + dataHora.substring(11, dataHora.length()));
            
            return formatadorData.format(d1);
        } catch (ParseException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            Info.filaLog.add("[Formatar Data] "+ex.toString());
            return null;
        }

    }
    
}
