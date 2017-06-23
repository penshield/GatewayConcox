/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.automaster.server;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.TreeMap;
import org.automaster.Info;
import org.automaster.Util;
import org.automaster.db.ComandoBancoPostgresSQL;
import serializable.Registro;

/**
 *
 * @author automaster
 */
public class ThreadRecebeJson implements Runnable {

    private JsonObject jsonObj;

    public ThreadRecebeJson(JsonObject jsonObj) {
        this.jsonObj = jsonObj;
    }

    @Override
    public void run() {
        try {

//            Info.filaLog.add("Linha 01");
            JsonArray data = jsonObj.getAsJsonArray("data");
//            Info.filaLog.add("Linha 02");
            JsonObject dataObj = data.get(0).getAsJsonObject();
//            Info.filaLog.add("Linha 03");
//            JsonObject length = jsonObj.getAsJsonObject("lenght");
//            Info.filaLog.add("Linha 04");
            String protocolNumber = String.valueOf(dataObj.get("protocol_number"));
//            Info.filaLog.add("Protocolo: " + protocolNumber);
            String imei = String.valueOf(dataObj.get("imei")).replaceAll("\"", "");
//            Info.filaLog.add("Imei: " + imei);
            String model = String.valueOf(dataObj.get("model"));
//            Info.filaLog.add("Modelo: " + model);

            if (protocolNumber.equalsIgnoreCase("1")) {
                Info.filaLog.add("dados: " + dataObj);
                String language = String.valueOf(dataObj.get("language"));
//                Info.filaLog.add("Language: " + language);
                String gmt = String.valueOf(dataObj.get("gmt"));
//                Info.filaLog.add("GMT: " + gmt);
//                Info.filaLog.add("Tamnho do pacote: " + jsonObj.getAsJsonObject("lenght"));
                Info.filaLog.add("Protocolo: " + protocolNumber + " Imei: " + imei + " Modelo: " + model + " Language: " + language + " GMT: " + gmt + " Tamnho do pacote: " + jsonObj.getAsJsonObject("lenght"));
            } else {
                if (protocolNumber.equalsIgnoreCase("34")) { // Location Message or ALARM MESSAGE
                    Info.filaLog.add("dados: " + dataObj);
                    String dateTime = String.valueOf(dataObj.get("datetime"));
//                    Info.filaLog.add("Date and time: " + dateTime);
//                    Info.filaLog.add("Data formatada: " + Util.formatarDataHora(dateTime));
                    String dataSql = Util.formatarDataHoraSQL(Util.formatarDataHora(dateTime));
                    String quantity_gps_satellites = String.valueOf(dataObj.get("quantity_gps_satellites"));
//                    Info.filaLog.add("quantity_gps_satellites: " + quantity_gps_satellites);
                    String latitude = String.valueOf(dataObj.get("latitude"));
//                    Info.filaLog.add("latitude: " + latitude);
                    String longitude = String.valueOf(dataObj.get("longitude"));
//                    Info.filaLog.add("longitude: " + longitude);
                    String speed = String.valueOf(dataObj.get("speed"));
//                    Info.filaLog.add("speed: " + speed);
//                  String acc = String.valueOf(dataObj.get("acc"));
                    String acc = (String.valueOf(dataObj.get("acc")) == null || String.valueOf(dataObj.get("acc")).equalsIgnoreCase("0")) ? "false" : (String.valueOf(dataObj.get("acc")).equalsIgnoreCase("0") ? "false" : "true");
//                    Info.filaLog.add("acc: " + acc); // (1) = Ligado; (0) = Desligado;             
                    String gps_real_time = String.valueOf(dataObj.get("gps_real_time")); // (0) = Atualizado; 1 = Memória;
//                    Info.filaLog.add("gps_real_time: " + gps_real_time);
                    String gps_been_positoning = String.valueOf(dataObj.get("gps_been_positoning"));
//                    Info.filaLog.add("gps_been_positoning: " + gps_been_positoning);
                    String longitude_hemisphere = String.valueOf(dataObj.get("longitude_hemisphere"));
//                    Info.filaLog.add("longitude_hemisphere: " + longitude_hemisphere);
                    String latitude_hemisphere = String.valueOf(dataObj.get("latitude_hemisphere"));
//                    Info.filaLog.add("latitude_hemisphere: " + latitude_hemisphere);
                    String course = String.valueOf(dataObj.get("course")); // Direção em graus do veículo
//                    Info.filaLog.add("course: " + course);
                    String mcc = String.valueOf(dataObj.get("mcc")); // código de área do país onde o GPS comunicou
//                    Info.filaLog.add("mcc[Código de área do GPS]: " + mcc);
                    String mnc = String.valueOf(dataObj.get("mnc")); // código de rede de onde se localiza o GPS
//                    Info.filaLog.add("mcc[Código de rede do GPS]: " + mnc);
                    String lac = String.valueOf(dataObj.get("lac")); // código de área da torre que ele usuou pra se comunicar
//                    Info.filaLog.add("lac[código de área da torre de comunicação]: " + lac);
                    String cell_id = String.valueOf(dataObj.get("cell_id")); // Indentificador da torre que usou para se comunicar
//                    Info.filaLog.add("cell_id: " + cell_id);
                    String data_upload_mode = String.valueOf(dataObj.get("data_upload_mode"));
//                    Info.filaLog.add("data_upload_mode: " + data_upload_mode);

                    String oil_electricity_status = String.valueOf(dataObj.get("oil_electricity_status")); // (0) = Normal; 1 = Bloqueado;
//                    Info.filaLog.add("oil_electricity_status: " + oil_electricity_status);
                    String gps_tracking_status = String.valueOf(dataObj.get("gps_tracking_status")); // GPS tracking (1)Ligado ou (0)Desligado
//                    Info.filaLog.add("gps_tracking_status: " + gps_tracking_status);
                    String charge = String.valueOf(dataObj.get("charge"));
//                    Info.filaLog.add("charge: " + charge); // Equipamento conectado a bateria externa (1)Ligado ou (0)Desligado
                    String defence = String.valueOf(dataObj.get("defence")); // Modo defencivo (1)Ativado ou (0)Desligado
//                    Info.filaLog.add("defence: " + defence);

                    String voltage_level = String.valueOf(dataObj.get("voltage_level")); // Nível de Bateria Tabela Voltage Level
                    /*
                     "Error", Erro
                     "No Power" Sem energia
                     "Extremely Low Battery" Bateria extremamente baixa
                     "Very Low Battery" Bateria muito baixa
                     "Low Battery" Bateria baixa
                     "Medium" Bateria normal
                     "High" Carga quase completa
                     "Very High" Carga cheia
                     */
//                    Info.filaLog.add("voltage_level: " + voltage_level);

                    String gsm_signal_strength = String.valueOf(dataObj.get("gsm_signal_strength")); // Nível de Sinal Tabela Signal Level
                    /*
                     "Error", Erro
                     "No Signal" Sem sinal
                     "Extremely Weak Signal" Sinal extremamente baixo
                     "Very Weak Signal" Sinal muito baixo
                     "Good Signal" Sinal bom
                     "Strong Signal" Sinal forte
                     */
//                    Info.filaLog.add("[gsm_signal_strength]: " + gsm_signal_strength);
                    String alarm_sos = String.valueOf(dataObj.get("alarm_sos")); // Pânico ou entrada um (1)=Ativado; (0)=Desativado;
//                    Info.filaLog.add("[alarm_sos]: " + alarm_sos);
                    String alarm_low_battery = String.valueOf(dataObj.get("alarm_low_battery")); // Alarme de bateria baixa (1)=Ativado; (0)=Desativado;
//                    Info.filaLog.add("[alarm_low_battery]: " + alarm_low_battery);
                    String alarm_power_cut = String.valueOf(dataObj.get("alarm_power_cut")); // Alarme de corte de energia (1)=Ativado; (0)=Desativado;
//                    Info.filaLog.add("alarm_power_cut: " + alarm_power_cut);
                    String alarm_vibration = String.valueOf(dataObj.get("alarm_vibration")); // Alarme de vibrac~ao (1)=Ativado; (0)=Desativado;
//                    Info.filaLog.add("alarm_vibration: " + alarm_vibration);
                    String alarm_enter_fence = String.valueOf(dataObj.get("alarm_enter_fence")); // Entrada em cerca (0)=Normal; (1)=Acionado;
//                    Info.filaLog.add("alarm_enter_fence: " + alarm_enter_fence);
                    String alarm_exit_fence = String.valueOf(dataObj.get("alarm_exit_fence"));  // Saída de cerca (0)=Normal; (1)=Acionado;
//                    Info.filaLog.add("alarm_exit_fence: " + alarm_exit_fence);
                    String alarm_over_speed = String.valueOf(dataObj.get("alarm_over_speed")); // Acima da velocidade (0)=Normal; (1)=Acionado;
//                    Info.filaLog.add("alarm_over_speed: " + alarm_over_speed);
                    String alarm_moving = String.valueOf(dataObj.get("alarm_moving")); // Acima de movimento (0)=Normal; (1)=Acionado;
//                    Info.filaLog.add("alarm_moving: " + alarm_moving);
                    String alarm_enter_gps_dead_zone = String.valueOf(dataObj.get("alarm_enter_gps_dead_zone")); // Entrada de zona sem GPS (0)=Normal; (1)=Acionado;
//                    Info.filaLog.add("alarm_enter_gps_dead_zone: " + alarm_enter_gps_dead_zone);
                    String alarm_exit_gps_dead_zone = String.valueOf(dataObj.get("alarm_exit_gps_dead_zone")); // Saida de zona sem GPS (0)=Normal; (1)=Acionado;
//                    Info.filaLog.add("alarm_exit_gps_dead_zone: " + alarm_exit_gps_dead_zone);
                    String alarm_power_on = String.valueOf(dataObj.get("alarm_power_on")); // Alerta ao ligar (0)=Normal; (1)=Acionado;
//                    Info.filaLog.add("alarm_power_on: " + alarm_power_on);
                    String alarm_gps_first_fix = String.valueOf(dataObj.get("alarm_gps_first_fix")); // Alarme ao Fixar GPS (0)=Normal; (1)=Acionado;
//                    Info.filaLog.add("alarm_gps_first_fix: " + alarm_gps_first_fix);
                    String alarm_low_battery_protection = String.valueOf(dataObj.get("alarm_low_battery_protection")); // Violação de proteao de bateria (0)=Normal; (1)=Acionado;
//                    Info.filaLog.add("alarm_low_battery_protection: " + alarm_low_battery_protection);
                    String alarm_sim_change = String.valueOf(dataObj.get("alarm_sim_change")); // Troca de Chip (0)=Normal; (1)=Acionado;
//                    Info.filaLog.add("alarm_sim_change: " + alarm_sim_change);
                    String alarm_power_low_off = String.valueOf(dataObj.get("alarm_power_low_off"));// Corte Desligamento de energia (0)=Normal; (1)=Acionado;
//                    Info.filaLog.add("alarm_power_low_off: " + alarm_power_low_off);
                    String alarm_airplane_mode = String.valueOf(dataObj.get("alarm_airplane_mode")); // Modo avião (0)=Normal; (1)=Acionado;
//                    Info.filaLog.add("alarm_airplane_mode: " + alarm_airplane_mode);
                    String alarm_disessemble = String.valueOf(dataObj.get("alarm_disessemble")); // Alarm desmontagem (0)=Normal; (1)=Acionado;
//                    Info.filaLog.add("alarm_disessemble: " + alarm_disessemble);
                    String alarm_door = String.valueOf(dataObj.get("alarm_door")); // Alarme de porta (0)=Normal; (1)=Acionado;
//                    Info.filaLog.add("Porta: " + alarm_door);
                    String alarm_shutdown = String.valueOf(dataObj.get("alarm_shutdown")); // Alarme de desligamento (0)=Normal; (1)=Acionado;
//                    Info.filaLog.add("alarm_shutdown: " + alarm_shutdown);
                    /*
                     Upload by time interval Envio por intervalo de tempo
                     Upload by distance interval Envio por intervalo de distância
                     Infection point upload Envio por inection point
                     ACC status upload Envio por ignição
                     Re-upload the last GPS point when back to static. Envio por movimento
                     Upload the last eective point when network recovers. Envio de Reconecção
                     */
                    //Inicio Teste
                    try {
                        Registro r = new Registro();
                        r.addParametro("modelo", "Concox");
                        r.addParametro("id", Long.parseLong(imei));
                        r.addParametro("latitude", latitude);
                        r.addParametro("longitude", longitude);
                        r.addParametro("satellites", Integer.parseInt(quantity_gps_satellites));
                        r.addParametro("speed", Integer.parseInt(speed));
                        r.addParametro("temperatura", null);
                        if (Info.accCrx1.containsKey(Long.parseLong(imei))) {
                            if(Integer.parseInt(speed) > 3){
                                r.addParametro("acc", "true");
                            }else {
                            r.addParametro("acc", Info.accCrx1.get(Long.parseLong(imei)));
                            }
                        } else {
                            if(Integer.parseInt(speed) > 3){
                                r.addParametro("acc", "true");
                            }else {
                            r.addParametro("acc", acc);
                            }
                        }
                        r.addParametro("dataHora", dataSql);
                        r.addParametro("direcao", Integer.parseInt(course));
                        r.addParametro("memoria", gps_real_time);
                        TreeMap<String, String> motorista = ComandoBancoPostgresSQL.buscarMotorista(imei);
                        Info.filaLog.add("Id=" + imei + " Motorista=" + motorista.get("codMotorista"));
                        r.addParametro("motorista", motorista.get("codMotorista"));
                        Info.filaDeRegistrosExternos.add(r);
                        Info.filaLog.add("Protocolo: " + protocolNumber + " Imei: " + imei + " Modelo: " + model + " Lat: " + latitude + " Lon: " + longitude + " Sat: " + quantity_gps_satellites + " speed: " + speed + " acc: " + acc + " data: " + dataSql + " direção: " + course + " GPS: " + gps_real_time);
                        if (Info.comandosHydra.containsKey(Long.parseLong(imei))) {
                            String command_id = String.valueOf(Info.comandosHydra.get(Long.parseLong(imei)));
                            TreeMap<String, String> responseCmd = Info.sendGet(imei, command_id);
                            if (responseCmd != null && (responseCmd.get("status").equalsIgnoreCase("To Send") || responseCmd.get("status").equalsIgnoreCase("Sended"))) {
                                ComandoBancoPostgresSQL.atualizaComandos(2, null, "2", null, responseCmd.get("dateTime"), null, Long.parseLong(imei));
                                Info.comandosHydra.remove(Long.parseLong(imei));
                                Info.filaLog.add("[COMANDOS] - Comando enviado com sucesso SUNTECH:\n");
                            }
                        }
                    } catch (Exception e) {
                        Info.filaLog.add("Erro ao enviar Registro: " + e.toString());
                    }
                    //Fim Teste

                } else if (protocolNumber.equalsIgnoreCase("18") || protocolNumber.equalsIgnoreCase("22")) { // Position Data
                    Info.filaLog.add("dados: " + dataObj);
                    String cell_id = String.valueOf(dataObj.get("cell_id")); // Indentificador da torre que usou para se comunicar
//                    Info.filaLog.add("cell_id: " + cell_id);
                    String mcc = String.valueOf(dataObj.get("mcc")); // código de área do país onde o GPS comunicou
//                    Info.filaLog.add("mcc[Código de área do GPS]: " + mcc);
                    String dateTime = String.valueOf(dataObj.get("datetime"));
//                    Info.filaLog.add("Date and time: " + dateTime);
//                    Info.filaLog.add("Data formatada: " + Util.formatarDataHora(dateTime));
                    String dataSql = Util.formatarDataHoraSQL(Util.formatarDataHora(dateTime));
                    String course = String.valueOf(dataObj.get("course")); // Direção em graus do veículo
//                    Info.filaLog.add("course: " + course);
                    String longitude_hemisphere = String.valueOf(dataObj.get("longitude_hemisphere"));
//                    Info.filaLog.add("longitude_hemisphere: " + longitude_hemisphere);
                    String latitude_hemisphere = String.valueOf(dataObj.get("latitude_hemisphere"));
//                    Info.filaLog.add("latitude_hemisphere: " + latitude_hemisphere);
                    String acc = null;
                    if(dataObj.has("acc")){
                        acc = String.valueOf(dataObj.get("acc")).equalsIgnoreCase("0") ? "false" : "true";
                    }else {
                        acc = (String.valueOf(dataObj.get("acc")) == null || String.valueOf(dataObj.get("acc")).equalsIgnoreCase("0")) ? "false" : (String.valueOf(dataObj.get("acc")).equalsIgnoreCase("0") ? "false" : "true");
                    }
                    Info.filaLog.add("ACC 18 : " + dataObj.get("acc")+" IMEI 18: "+imei); // (1) = Ligado; (0) = Desligado; 
                    String lac = String.valueOf(dataObj.get("lac")); // código de área da torre que ele usuou pra se comunicar
//                    Info.filaLog.add("lac[código de área da torre de comunicação]: " + lac);
                    String speed = String.valueOf(dataObj.get("speed"));
//                    Info.filaLog.add("speed: " + speed);
                    String gps_been_positoning = String.valueOf(dataObj.get("gps_been_positoning"));
//                    Info.filaLog.add("gps_been_positoning: " + gps_been_positoning);
                    String mnc = String.valueOf(dataObj.get("mnc")); // código de rede de onde se localiza o GPS
//                    Info.filaLog.add("mcc[Código de rede do GPS]: " + mnc);
                    String quantity_gps_satellites = String.valueOf(dataObj.get("quantity_gps_satellites"));
//                    Info.filaLog.add("quantity_gps_satellites: " + quantity_gps_satellites);
                    String latitude = String.valueOf(dataObj.get("latitude"));
//                    Info.filaLog.add("latitude: " + latitude);
                    String longitude = String.valueOf(dataObj.get("longitude"));
//                    Info.filaLog.add("longitude: " + longitude);
                    String gps_real_time = String.valueOf(dataObj.get("gps_real_time")); // (0) = Atualizado; 1 = Memória;
//                    Info.filaLog.add("gps_real_time: " + gps_real_time);
                    //Inicio Teste
                    try {
                        Registro r = new Registro();
                        r.addParametro("modelo", "Concox");
                        r.addParametro("id", Long.parseLong(imei));
                        r.addParametro("latitude", latitude);
                        r.addParametro("longitude", longitude);
                        r.addParametro("satellites", Integer.parseInt(quantity_gps_satellites));
                        r.addParametro("speed", Integer.parseInt(speed));
                        r.addParametro("temperatura", null);
                        if (Info.accCrx1.containsKey(Long.parseLong(imei))) {
                            if(Integer.parseInt(speed) > 3){
                                r.addParametro("acc", "true");
                            }else {
                            r.addParametro("acc", Info.accCrx1.get(Long.parseLong(imei)));
                            }
                        } else {
                            if (Integer.parseInt(speed) > 3) {
                                r.addParametro("acc", "true");
                            } else {
                                r.addParametro("acc", acc);
                            }
                        }
                        r.addParametro("dataHora", dataSql);
                        r.addParametro("direcao", Integer.parseInt(course));
                        r.addParametro("memoria", gps_real_time);
                        TreeMap<String, String> motorista = ComandoBancoPostgresSQL.buscarMotorista(imei);
                        Info.filaLog.add("Id=" + imei + " Motorista=" + motorista.get("codMotorista"));
                        r.addParametro("motorista", motorista.get("codMotorista"));
                        Info.filaDeRegistrosExternos.add(r);
                        Info.filaLog.add("Protocolo: " + protocolNumber + " Imei: " + imei + " Modelo: " + model + " Lat: " + latitude + " Lon: " + longitude + " Sat: " + quantity_gps_satellites + " speed: " + speed + " acc: " + acc + " data: " + dataSql + " direção: " + course + " GPS: " + gps_real_time);
                        if (Info.comandosHydra.containsKey(Long.parseLong(imei))) {
                            String command_id = String.valueOf(Info.comandosHydra.get(Long.parseLong(imei)));
                            TreeMap<String, String> responseCmd = Info.sendGet(imei, command_id);
                            if (responseCmd != null && (responseCmd.get("status").equalsIgnoreCase("To Send") || responseCmd.get("status").equalsIgnoreCase("Sended"))) {
                                ComandoBancoPostgresSQL.atualizaComandos(2, null, "2", null, responseCmd.get("dateTime"), null, Long.parseLong(imei));
                                Info.comandosHydra.remove(Long.parseLong(imei));
                                Info.filaLog.add("[COMANDOS] - Comando enviado com sucesso SUNTECH:\n");
                            }
                        }
                    } catch (Exception e) {
                        Info.filaLog.add("Erro ao enviar Registro: " + e.toString());
                    }
                    //Fim Teste

                } else if (protocolNumber.equalsIgnoreCase("19")) { // Heartbeat Message
                    Info.filaLog.add("dados: " + dataObj);
                    String acc = String.valueOf(dataObj.get("acc"));
//                    Info.filaLog.add("acc: " + acc); // (1) = Ligado; (0) = Desligado;              
                    String oil_electricity_status = String.valueOf(dataObj.get("oil_electricity_status")); // (0) = Normal; 1 = Bloqueado;
//                    Info.filaLog.add("oil_electricity_status: " + oil_electricity_status);
                    String gps_tracking_status = String.valueOf(dataObj.get("gps_tracking_status")); // GPS tracking (1)Ligado ou (0)Desligado
//                    Info.filaLog.add("gps_tracking_status: " + gps_tracking_status);
                    String charge = String.valueOf(dataObj.get("charge"));
//                    Info.filaLog.add("charge: " + charge); // Equipamento conectado a bateria externa (1)Ligado ou (0)Desligado
                    String defence = String.valueOf(dataObj.get("defence")); // Modo defencivo (1)Ativado ou (0)Desligado
//                    Info.filaLog.add("defence: " + defence);
                    String voltage_level = String.valueOf(dataObj.get("voltage_level")); // Nível de Bateria Tabela Voltage Level
                    /*
                     "Error", Erro
                     "No Power" Sem energia
                     "Extremely Low Battery" Bateria extremamente baixa
                     "Very Low Battery" Bateria muito baixa
                     "Low Battery" Bateria baixa
                     "Medium" Bateria normal
                     "High" Carga quase completa
                     "Very High" Carga cheia
                     */
//                    Info.filaLog.add("voltage_level: " + voltage_level);
                    String gsm_signal_strength = String.valueOf(dataObj.get("gsm_signal_strength")); // Nível de Sinal Tabela Signal Level
                    /*
                     "Error", Erro
                     "No Signal" Sem sinal
                     "Extremely Weak Signal" Sinal extremamente baixo
                     "Very Weak Signal" Sinal muito baixo
                     "Good Signal" Sinal bom
                     "Strong Signal" Sinal forte
                     */
//                    Info.filaLog.add("[gsm_signal_strength]: " + gsm_signal_strength);
                    String alarm_sos = String.valueOf(dataObj.get("alarm_sos")); // Pânico ou entrada um (1)=Ativado; (0)=Desativado;
//                    Info.filaLog.add("[alarm_sos]: " + alarm_sos);
                    String alarm_low_battery = String.valueOf(dataObj.get("alarm_low_battery")); // Alarme de bateria baixa (1)=Ativado; (0)=Desativado;
//                    Info.filaLog.add("[alarm_low_battery]: " + alarm_low_battery);
                    String alarm_power_cut = String.valueOf(dataObj.get("alarm_power_cut")); // Alarme de corte de energia (1)=Ativado; (0)=Desativado;
//                    Info.filaLog.add("alarm_power_cut: " + alarm_power_cut);
                    String alarm_vibration = String.valueOf(dataObj.get("alarm_vibration")); // Alarme de vibrac~ao (1)=Ativado; (0)=Desativado;
//                    Info.filaLog.add("alarm_vibration: " + alarm_vibration);
                    //Inicio Teste
                    try {
                        TreeMap<String, String> ultimaPosicao = ComandoBancoPostgresSQL.buscarUltimaPosicaoID(imei);
                        if (ultimaPosicao != null && acc != null) {
                            String accOff = acc.equalsIgnoreCase("0") ? "false" : "true";
                            Info.accCrx1.put(Long.parseLong(imei), accOff);
                            Registro r = new Registro();
                            r.addParametro("modelo", "Concox");
                            r.addParametro("id", Long.parseLong(imei));
                            r.addParametro("latitude", ultimaPosicao.get("lat"));
                            r.addParametro("longitude", ultimaPosicao.get("lon"));
                            r.addParametro("satellites", Integer.parseInt(ultimaPosicao.get("sat")));
                            r.addParametro("speed", Integer.parseInt("0"));
                            r.addParametro("temperatura", null);
                            r.addParametro("acc", accOff);
                            TimeZone timeZoneBelem = TimeZone.getTimeZone("America/Belem");
                            SimpleDateFormat formatadorData = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            formatadorData.setTimeZone(timeZoneBelem);
                            long dataBd = Long.parseLong(ultimaPosicao.get("dataHora"));
                            long dataDesligado = dataBd + 60000;
                            String dataSql = "";
                            dataSql = formatadorData.format(new Date(System.currentTimeMillis()));
//                            }
//                            Info.filaLog.add("Data SQL: " + dataSql);
                            r.addParametro("dataHora", dataSql);
                            r.addParametro("direcao", Integer.parseInt("0"));
                            r.addParametro("memoria", ultimaPosicao.get("gps"));
                            if (!Boolean.parseBoolean(ultimaPosicao.get("ign")) && Integer.parseInt(ultimaPosicao.get("vel")) < 3 && !Boolean.parseBoolean(accOff)) {
                                TreeMap<String, String> motorista = ComandoBancoPostgresSQL.buscarMotorista(imei);
                                Info.filaLog.add("Id=" + imei + " Motorista=" + motorista.get("codMotorista"));
                                r.addParametro("motorista", motorista.get("codMotorista"));
                                Info.filaDeRegistrosExternos.add(r);
                            }
                            Info.filaLog.add("Protocolo: " + protocolNumber + " Imei: " + imei + " Modelo: " + model + " Lat: " + ultimaPosicao.get("lat") + " Lon: " + ultimaPosicao.get("lon") + " Sat: " + ultimaPosicao.get("sat") + " speed: 0 acc: " + acc + " data: " + dataSql + " direção: 0 GPS: " + ultimaPosicao.get("gps"));
                            if (Info.comandosHydra.containsKey(Long.parseLong(imei))) {
                                String command_id = String.valueOf(Info.comandosHydra.get(Long.parseLong(imei)));
                                TreeMap<String, String> responseCmd = Info.sendGet(imei, command_id);
                                if (responseCmd != null && (responseCmd.get("status").equalsIgnoreCase("To Send") || responseCmd.get("status").equalsIgnoreCase("Sended"))) {
                                    ComandoBancoPostgresSQL.atualizaComandos(2, null, "2", null, responseCmd.get("dateTime"), null, Long.parseLong(imei));
                                    Info.comandosHydra.remove(Long.parseLong(imei));
                                    Info.filaLog.add("[COMANDOS] - Comando enviado com sucesso SUNTECH:\n");
                                }
                            }
                        }
                    } catch (Exception e) {
                        Info.filaLog.add("Erro ao enviar Registro: " + e.toString());
                    }
                    //Fim Teste

                } else if (protocolNumber.equalsIgnoreCase("40")) { // LBS MULTIPLE BASES EXTENSION PACKET
                    Info.filaLog.add("dados: " + dataObj);
                    String dateTime = String.valueOf(dataObj.get("datetime"));
                    Info.filaLog.add("Date and time: " + dateTime);
                    Info.filaLog.add("Data formatada: " + Util.formatarDataHora(dateTime));
                    String mcc = String.valueOf(dataObj.get("mcc")); // código de área do país onde o GPS comunicou
                    Info.filaLog.add("mcc[Código de área do GPS]: " + mcc);
                    String mnc = String.valueOf(dataObj.get("mnc")); // código de rede de onde se localiza o GPS
                    Info.filaLog.add("mcc[Código de rede do GPS]: " + mnc);
                    String lac = String.valueOf(dataObj.get("lac")); // código de área da torre que ele usuou pra se comunicar
                    Info.filaLog.add("lac[código de área da torre de comunicação]: " + lac);
                    String cell_id = String.valueOf(dataObj.get("cell_id")); // Indentificador da torre que usou para se comunicar
                    Info.filaLog.add("cell_id: " + cell_id);
                    String rssi = String.valueOf(dataObj.get("rssi")); // RSSI community forca do sinal possveis valores["Strong","Weak"]
                    Info.filaLog.add("rssi: " + rssi);
                    String nlac1 = String.valueOf(dataObj.get("nlac1")); // Código de area da torre que usou para se comunicar
                    Info.filaLog.add("nlac1: " + nlac1);
                    String nci1 = String.valueOf(dataObj.get("nci1")); // Indentificador da torre que usou para se comunicar
                    Info.filaLog.add("nci1: " + nci1);
                    String nrssi1 = String.valueOf(dataObj.get("nrssi1")); // Indentificador da torre que usou para se comunicar
                    Info.filaLog.add("nrssi1: " + nrssi1);
                    String nlac2 = String.valueOf(dataObj.get("nlac2")); // Indentificador da torre que usou para se comunicar
                    Info.filaLog.add("nlac2: " + nlac2);
                    String nci2 = String.valueOf(dataObj.get("nci2")); // Indentificador da torre que usou para se comunicar
                    Info.filaLog.add("nci2: " + nci2);
                    String nrssi2 = String.valueOf(dataObj.get("nrssi2")); // Indentificador da torre que usou para se comunicar
                    Info.filaLog.add("nrssi2: " + nrssi2);
                    String nlac3 = String.valueOf(dataObj.get("nlac3")); // Indentificador da torre que usou para se comunicar
                    Info.filaLog.add("nlac3: " + nlac3);
                    String nci3 = String.valueOf(dataObj.get("nci3")); // Indentificador da torre que usou para se comunicar
                    Info.filaLog.add("nci3: " + nci3);
                    String nrssi3 = String.valueOf(dataObj.get("nrssi3")); // Indentificador da torre que usou para se comunicar
                    Info.filaLog.add("nrssi3: " + nrssi3);
                    String nlac4 = String.valueOf(dataObj.get("nlac4")); // Indentificador da torre que usou para se comunicar
                    Info.filaLog.add("nlac4: " + nlac4);
                    String nci4 = String.valueOf(dataObj.get("nci4")); // Indentificador da torre que usou para se comunicar
                    Info.filaLog.add("nci4: " + nci4);
                    String nrssi4 = String.valueOf(dataObj.get("nrssi4")); // Indentificador da torre que usou para se comunicar
                    Info.filaLog.add("nrssi4: " + nrssi4);
                    String nlac5 = String.valueOf(dataObj.get("nlac5")); // Indentificador da torre que usou para se comunicar
                    Info.filaLog.add("nlac5: " + nlac5);
                    String nci5 = String.valueOf(dataObj.get("nci5")); // Indentificador da torre que usou para se comunicar
                    Info.filaLog.add("nci5: " + nci5);
                    String nrssi5 = String.valueOf(dataObj.get("nrssi5")); // Indentificador da torre que usou para se comunicar
                    Info.filaLog.add("nrssi5: " + nrssi5);
                    String nlac6 = String.valueOf(dataObj.get("nlac6")); // Indentificador da torre que usou para se comunicar
                    Info.filaLog.add("nlac6: " + nlac6);
                    String nci6 = String.valueOf(dataObj.get("nci6")); // Indentificador da torre que usou para se comunicar
                    Info.filaLog.add("nci6: " + nci6);
                    String nrssi6 = String.valueOf(dataObj.get("nrssi6")); // Indentificador da torre que usou para se comunicar
                    Info.filaLog.add("nrssi6: " + nrssi6);
                    String time_advance = String.valueOf(dataObj.get("time_advance")); // Tempo atual do sinal da torre conectado
                    Info.filaLog.add("time_advance: " + time_advance);
                    String language = String.valueOf(dataObj.get("language")); // Possíveis Valores ["Chinese","English"]
                    Info.filaLog.add("language: " + language);
                    if (Info.comandosHydra.containsKey(Long.parseLong(imei))) {
                        String command_id = String.valueOf(Info.comandosHydra.get(Long.parseLong(imei)));
                        TreeMap<String, String> responseCmd = Info.sendGet(imei, command_id);
                        if (responseCmd != null && (responseCmd.get("status").equalsIgnoreCase("To Send") || responseCmd.get("status").equalsIgnoreCase("Sended"))) {
                            ComandoBancoPostgresSQL.atualizaComandos(2, null, "2", null, responseCmd.get("dateTime"), null, Long.parseLong(imei));
                            Info.comandosHydra.remove(Long.parseLong(imei));
                            Info.filaLog.add("[COMANDOS] - Comando enviado com sucesso SUNTECH:\n");
                        }
                    }

                } else if (protocolNumber.equalsIgnoreCase("138")) {
                    Info.filaLog.add("dados: " + dataObj);
                    if (Info.comandosHydra.containsKey(Long.parseLong(imei))) {
                        String command_id = String.valueOf(Info.comandosHydra.get(Long.parseLong(imei)));
                        TreeMap<String, String> responseCmd = Info.sendGet(imei, command_id);
                        if (responseCmd != null && (responseCmd.get("status").equalsIgnoreCase("To Send") || responseCmd.get("status").equalsIgnoreCase("Sended"))) {
                            ComandoBancoPostgresSQL.atualizaComandos(2, null, "2", null, responseCmd.get("dateTime"), null, Long.parseLong(imei));
                            Info.comandosHydra.remove(Long.parseLong(imei));
                            Info.filaLog.add("[COMANDOS] - Comando enviado com sucesso SUNTECH:\n");
                        }
                    }

                } else if (protocolNumber.equalsIgnoreCase("148")) {
                    Info.filaLog.add("dados: " + dataObj);
                    String sub_protocol_number = String.valueOf(dataObj.get("sub_protocol_number")); // Sub protocol number 0 (EXTERNAL BATTERY - 0x00)
                    Info.filaLog.add("sub_protocol_number: " + sub_protocol_number);
                    String external_battery = String.valueOf(dataObj.get("external_battery")); // Nível da bateria externa
                    Info.filaLog.add("external_battery: " + external_battery);
                    if (Info.comandosHydra.containsKey(Long.parseLong(imei))) {
                        String command_id = String.valueOf(Info.comandosHydra.get(Long.parseLong(imei)));
                        TreeMap<String, String> responseCmd = Info.sendGet(imei, command_id);
                        if (responseCmd != null && (responseCmd.get("status").equalsIgnoreCase("To Send") || responseCmd.get("status").equalsIgnoreCase("Sended"))) {
                            ComandoBancoPostgresSQL.atualizaComandos(2, null, "2", null, responseCmd.get("dateTime"), null, Long.parseLong(imei));
                            Info.comandosHydra.remove(Long.parseLong(imei));
                            Info.filaLog.add("[COMANDOS] - Comando enviado com sucesso SUNTECH:\n");
                        }
                    }
                } else {
                    Info.filaLog.add("DADOS ELSE: " + dataObj);
                    if (Info.comandosHydra.containsKey(Long.parseLong(imei))) {
                        String command_id = String.valueOf(Info.comandosHydra.get(Long.parseLong(imei)));
                        TreeMap<String, String> responseCmd = Info.sendGet(imei, command_id);
                        if (responseCmd != null && (responseCmd.get("status").equalsIgnoreCase("To Send") || responseCmd.get("status").equalsIgnoreCase("Sended"))) {
                            ComandoBancoPostgresSQL.atualizaComandos(2, null, "2", null, responseCmd.get("dateTime"), null, Long.parseLong(imei));
                            Info.comandosHydra.remove(Long.parseLong(imei));
                            Info.filaLog.add("[COMANDOS] - Comando enviado com sucesso SUNTECH:\n");
                        }
                    }
                }
            }

        } catch (Exception e) {
            Info.filaLog.add("[Erro na TreadRecebeJson]: " + e.toString());
        }
    }

}
