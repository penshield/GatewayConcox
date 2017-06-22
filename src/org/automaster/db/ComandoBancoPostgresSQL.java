/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.automaster.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.TreeMap;
import org.automaster.Info;
import org.automaster.Util;

/**
 *
 * @author Renato
 */
public class ComandoBancoPostgresSQL {

    public static Connection conexao = null;
    /*
     public static Connection getConexao() {
     try {
     Class.forName("com.mysql.jdbc.Driver");
     return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/youtrack", "automaster", "dbAutomaster2011");
     } catch (ClassNotFoundException e) {
     //JOptionPane.showMessageDialog(null, "O driver JDBC não foi encontrado!");
     System.out.println("O driver JDBC não foi encontrado!");
     e.printStackTrace();
     return null;
     } catch (SQLException e) {
     //JOptionPane.showMessageDialog(null, "Não foi possível conectar ao banco!\n\nErro: " + e.getMessage());
     System.out.println("Não foi possível conectar ao banco!\n\nErro: " + e.getMessage());
     e.printStackTrace();
     return null;
     }
     }*/

    public static void verificaConexao() {
        try {
            if (conexao != null) {
                if (conexao.isClosed()) {
                    abreConexao();
                }
                //System.out.println("Conectado com sucesso!!");
            } else {
                abreConexao();
            }
        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, "Não foi possível verificar a conexao com o banco!\n\nErro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void abreConexao() {
        try {
            //Class.forName("com.mysql.jdbc.Driver");
            //conexao = DriverManager.getConnection("jdbc:mysql://188.138.109.93:3306/youtrack", "automaster", "dbAutomaster2011");
            Class.forName("org.postgresql.Driver");
            conexao = DriverManager.getConnection("jdbc:postgresql://85.93.91.19:5432/autoView", "automaster", "dbAutomaster2015");
            //conexao = DriverManager.getConnection("jdbc:mysql://192.168.168.105:3306/youtrack", "automaster", "dbAutomaster2011");
        } catch (ClassNotFoundException e) {
            //JOptionPane.showMessageDialog(null, "O driver JDBC não foi encontrado!");
            e.printStackTrace();
        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, "Não foi possível conectar ao banco!\n\nErro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void fechaConexao() {
        try {
            conexao.close();
        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, "Não foi possível fechar a conexão com o banco!\n\nErro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void executaSql(String sql) {
        verificaConexao();
        try {
            Statement prepara = conexao.createStatement();
            prepara.executeUpdate(sql);
            prepara.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static TreeMap<String, String> buscarMotorista(String id) {

        verificaConexao();
        // Comando SQL
        String sql = "select m.cod_motorista from motorista m \n"
                + "join veiculo v on v.cod_veiculo=m.veiculo_cod_veiculo\n"
                + "join equipamento e on e.cod_equipamento=v.equipamento_cod_equipamento\n"
                + "where e.id='"+id+"'";
        // Statments
        try {
            PreparedStatement preparadorSQL = conexao.prepareStatement(sql);
//            preparadorSQL.setString(1, id);
            // Commit no banco
            ResultSet res = preparadorSQL.executeQuery();
            //Tirando do Resultset e colocando no objeto
            if (res.next()) {
                TreeMap<String, String> map = new TreeMap<String, String>();
                map.put("codMotorista", (res.getString("cod_motorista") == null) ? "0" : res.getString("cod_motorista"));
                return map;
            } else {
                TreeMap<String, String> map = new TreeMap<String, String>();
                map.put("codMotorista", "0");
                return map;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            TreeMap<String, String> map = new TreeMap<String, String>();
            map.put("codMotorista", "0");
            return map;
        }
    }

    public static TreeMap<String, String> buscarEquipamentoID(String id) {
        verificaConexao();
        //Connection conexao = getConexao();
        TreeMap<String, String> registro = new TreeMap<String, String>();
        try {
            Statement prepara = conexao.createStatement();
            ResultSet resultado = prepara.executeQuery("select * from public.equipamento where id='" + id + "';");
            if (resultado.next()) {
                registro.put("codEquipamento", resultado.getString("cod_equipamento"));
                registro.put("codModelo", resultado.getString("modelo_equipamento_cod_modelo_equipamento"));
                registro.put("codChip", resultado.getString("chip_cod_chip"));
                registro.put("codUnidade", resultado.getString("unidade_cod_unidade"));
                registro.put("descricao", resultado.getString("descricao"));
                registro.put("codEstado", resultado.getString("estado_equipamento_cod_estado"));
                registro.put("id", resultado.getString("id"));
                registro.put("esn", resultado.getString("esn"));
                resultado.close();
                prepara.close();
                return registro;
            }

        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, "Não foi possível consultar veículos!\n\nErro: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static TreeMap<String, String> buscarUltimaPosicaoID(String id) {
        verificaConexao();
        //Connection conexao = getConexao();
        TreeMap<String, String> registro = new TreeMap<String, String>();
        try {
            Statement prepara = conexao.createStatement();
            ResultSet resultado = prepara.executeQuery("select ud.ign, ud.dataehora, ud.lat, ud.lon, ud.sat, ud.gps from public.ultimos_200 ud \n"
                    + "join veiculo v on v.cod_veiculo=ud.veiculo_cod_veiculo \n"
                    + "join equipamento e on e.cod_equipamento=v.equipamento_cod_equipamento\n"
                    + "where id='" + id + "' order by dataehora desc limit 1;");
            if (resultado.next()) {
                registro.put("dataHora", String.valueOf(resultado.getTimestamp("dataehora").getTime()));
                registro.put("lat", String.valueOf(resultado.getDouble("lat")));
                registro.put("lon", String.valueOf(resultado.getDouble("lon")));
                registro.put("sat", String.valueOf(resultado.getInt("sat")));
                registro.put("gps", String.valueOf(resultado.getInt("gps")));
                registro.put("ign", String.valueOf(resultado.getBoolean("ign")));
                resultado.close();
                prepara.close();
                return registro;
            }

        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, "Não foi possível consultar veículos!\n\nErro: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static ArrayList<TreeMap<String, String>> verificaComandos() {
        verificaConexao();
        //Connection conexao = getConexao();
        ArrayList<TreeMap<String, String>> saida = new ArrayList<TreeMap<String, String>>();
        try {
            Statement prepara = conexao.createStatement();
            ResultSet resultado = prepara.executeQuery("select c.cod_comando, c.comando, c.parametros, e.cod_equipamento, e.id, ch.cod_chip, ch.numero, ch.serial, v.placa, me.cod_modelo_equipamento, mecf.cod_mecf, mecf.nome from public.comando c \n"
                    + "join veiculo v on v.cod_veiculo=c.veiculo_cod_veiculo  \n"
                    + "join msg_status_cod msg on msg.cod_msg_status_cod=c.msg_status_cod_cod_msg_status_cod\n"
                    + "join equipamento e on e.cod_equipamento=v.equipamento_cod_equipamento\n"
                    + "join modelo_equipamento me on me.cod_modelo_equipamento=e.modelo_equipamento_cod_modelo_equipamento\n"
                    + "join modelo_equipamento_has_configuracao_fios mecf on mecf.cod_mecf=v.configuracao\n"
                    + "left join chip ch on ch.cod_chip=e.chip_cod_chip\n"
                    + "where c.msg_status_cod_cod_msg_status_cod=1 and me.cod_modelo_equipamento=11 order by c.cod_comando asc");
            while (resultado.next()) {
                TreeMap<String, String> comando = new TreeMap<>();
                comando.put("cod", String.valueOf(resultado.getInt("cod_comando")));
                comando.put("comando", resultado.getString("comando").trim());
                comando.put("parametros", resultado.getString("parametros"));
                comando.put("codEquipamento", String.valueOf(resultado.getInt("cod_equipamento")));
                comando.put("id", resultado.getString("id").trim());
                comando.put("codChip", String.valueOf(resultado.getInt("cod_chip")));
                comando.put("numero", resultado.getString("numero").trim());
                comando.put("serial", resultado.getString("serial").trim());
                comando.put("placa", resultado.getString("placa"));
                comando.put("modelo", ((resultado.getInt("cod_modelo_equipamento") < 1) ? null : String.valueOf(resultado.getInt("cod_modelo_equipamento"))));
                comando.put("codMecf", String.valueOf(resultado.getInt("cod_mecf")));
                comando.put("pinos", resultado.getString("nome"));
                saida.add(comando);
            }
            prepara.close();
            resultado.close();
            //conexao.close();
            return saida;
        } catch (SQLException e) {
            e.printStackTrace();
            return saida;
        }
    }

    public static void atualizaComandos(int tipo, String cod, String msgStatusCod, String msgStatusDescricao, String dataehoraAtualizacao, String veiculoCod, long id) {
        verificaConexao();
        //Connection conexao = getConexao();
        try {
            Statement prepara = conexao.createStatement();
            String sql = "";
            if (tipo == 1) {
                sql = "update public.comando set msg_status_cod_cod_msg_status_cod=" + msgStatusCod + ", dataehora_atualizacao='" + dataehoraAtualizacao + "' where cod_comando=" + cod + ";";
                //System.out.println("Comando atualizado com sucesso 1");
            } else {
                sql = "update public.comando c set msg_status_cod_cod_msg_status_cod=" + msgStatusCod + ", "
                        + "dataehora_atualizacao= '" + dataehoraAtualizacao + "'\n"
                        + "where c.veiculo_cod_veiculo = (select cod_veiculo from public.veiculo v join equipamento e on e.cod_equipamento=v.equipamento_cod_equipamento "
                        + "where e.id = '" + String.valueOf(id) + "' \n"
                        + "and e.modelo_equipamento_cod_modelo_equipamento=11) "
                        + "and c.dataehora in (select dataehora from comando order by dataehora desc limit 1);";
                //System.out.println("Comando atualizado com sucesso 2");
            }
            prepara.executeUpdate(sql);
            prepara.close();
            //conexao.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
