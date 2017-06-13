/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.automaster;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JoãoRenato
 */
public abstract class Util {
    
    public static final int MSG_LOGIN = 0x01;
    public static final int MSG_GPS = 0x10;
    public static final int MSG_LBS = 0x11;
    public static final int MSG_GPS_LBS_1 = 0x12;
    public static final int MSG_GPS_LBS_2 = 0x22;
    public static final int MSG_STATUS = 0x13;
    public static final int MSG_SATELLITE = 0x14;
    public static final int MSG_STRING = 0x15;
    public static final int MSG_GPS_LBS_STATUS_1 = 0x16;
    public static final int MSG_GPS_LBS_STATUS_2 = 0x26;
    public static final int MSG_GPS_LBS_STATUS_3 = 0x27;
    public static final int MSG_LBS_PHONE = 0x17;
    public static final int MSG_LBS_EXTEND = 0x18;
    public static final int MSG_LBS_STATUS = 0x19;
    public static final int MSG_GPS_PHONE = 0x1A;
    public static final int MSG_GPS_LBS_EXTEND = 0x1E;
    public static final int MSG_COMMAND_0 = 0x80;
    public static final int MSG_COMMAND_1 = 0x81;
    public static final int MSG_COMMAND_2 = 0x82;
    public static final int MSG_INFO = 0x94;
    public static final char[] EXTENDED = {0x00C7, 0x00FC, 0x00E9, 0x00E2,
        0x00E4, 0x00E0, 0x00E5, 0x00E7, 0x00EA, 0x00EB, 0x00E8, 0x00EF,
        0x00EE, 0x00EC, 0x00C4, 0x00C5, 0x00C9, 0x00E6, 0x00C6, 0x00F4,
        0x00F6, 0x00F2, 0x00FB, 0x00F9, 0x00FF, 0x00D6, 0x00DC, 0x00A2,
        0x00A3, 0x00A5, 0x20A7, 0x0192, 0x00E1, 0x00ED, 0x00F3, 0x00FA,
        0x00F1, 0x00D1, 0x00AA, 0x00BA, 0x00BF, 0x2310, 0x00AC, 0x00BD,
        0x00BC, 0x00A1, 0x00AB, 0x00BB, 0x2591, 0x2592, 0x2593, 0x2502,
        0x2524, 0x2561, 0x2562, 0x2556, 0x2555, 0x2563, 0x2551, 0x2557,
        0x255D, 0x255C, 0x255B, 0x2510, 0x2514, 0x2534, 0x252C, 0x251C,
        0x2500, 0x253C, 0x255E, 0x255F, 0x255A, 0x2554, 0x2569, 0x2566,
        0x2560, 0x2550, 0x256C, 0x2567, 0x2568, 0x2564, 0x2565, 0x2559,
        0x2558, 0x2552, 0x2553, 0x256B, 0x256A, 0x2518, 0x250C, 0x2588,
        0x2584, 0x258C, 0x2590, 0x2580, 0x03B1, 0x00DF, 0x0393, 0x03C0,
        0x03A3, 0x03C3, 0x00B5, 0x03C4, 0x03A6, 0x0398, 0x03A9, 0x03B4,
        0x221E, 0x03C6, 0x03B5, 0x2229, 0x2261, 0x00B1, 0x2265, 0x2264,
        0x2320, 0x2321, 0x00F7, 0x2248, 0x00B0, 0x2219, 0x00B7, 0x221A,
        0x207F, 0x00B2, 0x25A0, 0x00A0};
    
    public static String formatarPacote(String packet){
        String packetFormatado="";
        for(int i=0; i< packet.length(); i++){
            packetFormatado += packet.substring(i, i+2)+" ";
            i++;        
        }
        return packetFormatado.trim();
    }
     public String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static final char getAscii(int code) {
//        System.out.println("code: "+code);
        if (code >= 0x80 && code <= 0xFF) {
            return EXTENDED[code - 0x7F];
        }
        return (char) code;
    }
    // convert a string in hex to a bin string
    public static String conversor_bin(String s) {
        StringBuilder t = new StringBuilder();
//        ByteArrayOutputStream t = new ByteArrayOutputStream();
        int n = s.length();
        if (n % 2 != 0) {
            return t.toString();
        }
        for (int x = 0; x < n; x++) {
//            System.out.println("Hex: "+s.substring(x, x + 2));
//            t.append(Util.toBinaryString(Util.hexToInt(s.substring(x, x + 2))));
            int numero = Util.hexToInt(s.substring(x, x + 2));
            String aux;
            if (numero < 127) {
                aux = Character.toString((char) numero);
            } else {
                aux = Short.toString(Short.decode("0x"+s.substring(x, x + 2)));
            }
            t.append(aux);
            x++;
        }
        return t.toString();
    }
    public static char[] formataAscii(String hex) {
        String[] letras = hex.split(" ");
        char[] c = new char[letras.length];
        int i = 0;
        for (String letra : letras) {
//            System.out.println("Decimal: "+Integer.parseInt(letra, 16));
            char valor = (char) Integer.parseInt(letra, 16);
            c[i] = valor;
        }
        return c;
    }
    
    public static String conversor_bin2(String s) {
        try {
            String text = s; // translating text String to 7 bit ASCII encoding 
            byte[] bytes = text.getBytes("US-ASCII");
            System.out.println("ASCII value of " + text + " is following");
            return  Arrays.toString(bytes);
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static String fromHexString(String hex) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < hex.length(); i+=2) {
            System.out.println("HEx: "+hex.substring(i, i + 2));
            System.out.println("Int: "+Integer.parseInt(hex.substring(i, i + 2), 16));
            str.append((char) Integer.parseInt(hex.substring(i, i + 2), 16));
        }
        return str.toString();
    }

    public static byte[] intToByteArray(int a) {

        return new byte[]{
            (byte) ((a >> 24) & 0xFF),
            (byte) ((a >> 16) & 0xFF),
            (byte) ((a >> 8) & 0xFF),
            (byte) (a & 0xFF)
        };
    }
    public static int bytesToInt(byte[] int_bytes) throws IOException {
    ByteArrayInputStream bis = new ByteArrayInputStream(int_bytes);
    ObjectInputStream ois = new ObjectInputStream(bis);
    int my_int = ois.readInt();
    ois.close();
    return my_int;
}

    public static String bytesToHex (byte[] bytes) {
        String saida = "";
        for (byte b : bytes) {
            if (b < 0) {
                saida += Integer.toHexString(b + 256);
            } else if (b < 16) {
                saida += "0" + Integer.toHexString(b);
            } else {
                saida += Integer.toHexString(b);
            }
        }
        return saida;
    }
    
    public static String byteToBinary(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
        for (int i = 0; i < Byte.SIZE * bytes.length; i++) {
            sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
        }
        return sb.toString();
    }
    
    public static byte[] binaryToByte(String s) {
        int sLen = s.length();
        byte[] toReturn = new byte[(sLen + Byte.SIZE - 1) / Byte.SIZE];
        char c;
        for (int i = 0; i < sLen; i++) {
            if ((c = s.charAt(i)) == '1') {
                toReturn[i / Byte.SIZE] = (byte) (toReturn[i / Byte.SIZE] | (0x80 >>> (i % Byte.SIZE)));
            } else if (c != '0') {
                throw new IllegalArgumentException();
            }
        }
        return toReturn;
    }
    
    public static String decToHex(int dec) {

        int sizeOfIntInHalfBytes = 8;
        int numberOfBitsInAHalfByte = 4;
        int halfByte = 0x0F;
        char[] hexDigits = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        StringBuilder hexBuilder = new StringBuilder(sizeOfIntInHalfBytes);
        hexBuilder.setLength(sizeOfIntInHalfBytes);
        for (int i = sizeOfIntInHalfBytes - 1; i >= 0; --i) {
            int j = dec & halfByte;
            hexBuilder.setCharAt(i, hexDigits[j]);
            dec >>= numberOfBitsInAHalfByte;
        }
        return hexBuilder.toString();
    }
    
    public static String byteToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }
    
    public static int hexToInt (String hex) {
        return Integer.parseInt(hex, 16);
    }
    public static String toBinaryString(int decimal) {
        return Integer.toBinaryString(decimal);
    }
     public static int hex2Decimal(String s) {
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        int val = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16*val + d;
        }
        return val;
    }
     public static char[] convertByte(String mensagem) {

        String pacote = Util.formatarPacote(mensagem);
        String[] hexas = pacote.split(" ");
        String fim = "";
//        System.out.println("Hexadecimal: "+Util.formatarPacote(hex));
        for (String hexa : hexas) {
//            System.out.println("Hexa:"+hexas[i]);
            String t = String.valueOf(Util.hexToInt(hexa)) + " ";
            //            System.out.println("T: "+t);
            fim += t;
        }
//        System.out.println("Bin 01: " + fim.trim()+".");
        String[] decimal = fim.trim().split(" ");
        char[] binarario = new char[decimal.length];
        for (int i = 0; i < decimal.length; i++) {
//            System.out.println("Int:["+decimal[i]+"]");
//            System.out.println("dec: "+Character.toString((char) Integer.parseInt(decimal[i])));
            binarario[i] = (char) Integer.parseInt(decimal[i]);
//            System.out.println("Char: "+binarario[i]);
//            int k = (int) binarario[i];
//            System.out.println("K: "+k);
        }
        return binarario;
    }
    // precondition:  d is a nonnegative integer
    public static String decimal2Hex(int d) {
        String digits = "0123456789ABCDEF";
        if (d == 0) return "0";
        String hex = "";
        while (d > 0) {
            int digit = d % 16;                // rightmost digit
            hex = digits.charAt(digit) + hex;  // string concatenation
            d = d / 16;
        }
        return hex;
    }
    public static String intToBit8 (int n) {
        String saida = Integer.toBinaryString(n);
        int tam = saida.length();
        if (saida.length() < 8) {
            for (int i = 0; i < 8 - tam; i++) {
                saida = "0" + saida;
            }
        }
        return saida;
    }
    public static String converteDecimalParaBinario(int valor) {
        int resto = -1;
        StringBuilder sb = new StringBuilder();

        if (valor == 0) {
            return "0";
        }
        // enquanto o resultado da divisão por 2 for maior que 0 adiciona o resto ao início da String de retorno
        while (valor > 0) {
            resto = valor % 2;
            valor = valor / 2;
            sb.insert(0, resto);
        }

        return sb.toString();
    }
    public static int converteBinarioParaDecimal(String valorBinario) {
        int valor = 0;
        // soma ao valor final o dígito binário da posição * 2 elevado ao contador da posição (começa em 0)
        for (int i = valorBinario.length(); i > 0; i--) {
            valor += Integer.parseInt(valorBinario.charAt(i - 1) + "") * Math.pow(2, (valorBinario.length() - i));
        }

        return valor;
    }
    public static String converteHexadecimalParaBinario(String valorHexa) {
        int posicao = 0;
        int posicaoArrayHexa = -1;
        StringBuilder sb = new StringBuilder();
        String valorConvertidoParaBinario = null;
        char[] hexa = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        // enquanto tem caracteres em hexa para converter
        while (posicao < valorHexa.length()) {
            // pega a posição do caractere no array de caracteres hexadecimais
            posicaoArrayHexa = Arrays.binarySearch(hexa, valorHexa.charAt(posicao));
            // pega o valor em decimal (correspondente à posição do caractere no array de hexadecimais) e converte para binário
            valorConvertidoParaBinario = converteDecimalParaBinario(posicaoArrayHexa);
            /*
             *  Se o valor convertido para binário tem menos de 4 dígitos, complementa o valor convertido com '0' à esquerda até
             *  ficar com 4 dígitos, se não for o caractere mais à esquerda do valor em hexadecimal
             */
            if (valorConvertidoParaBinario.length() != 4 && posicao > 0) {
                for (int i = 0; i < (4 - valorConvertidoParaBinario.length()); i++) {
                    sb.append("0");
                }
            }

            sb.append(valorConvertidoParaBinario);
            posicao++;
        }
        return sb.toString();
    }
    
    public static String converteBinarioParaHexadecimal(String binario) {
   char[] hexa = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
   StringBuilder sb = new StringBuilder();
   int posicaoInicial = 0;
   int posicaoFinal = 0;
   char caractereEncontrado = '-';
 
   // começa a pegar grupos de 4 dígitos da direita para a esquerda, por isso posicaoInicial e posicaoFinal ficam na posição de fim da String
   posicaoInicial = binario.length();
   posicaoFinal = posicaoInicial;
 
   while (posicaoInicial > 0) {
      // pega de 4 em 4 caracteres da direita para a esquerda. Se o último grupo à esquerda tiver menos de 4 caracteres, pega os restantes (1, 2 ou 3)
      posicaoInicial = ((posicaoInicial - 4) >= 0) ? posicaoInicial - 4 : 0;
 
      /*
      *  Transforma o grupo de 4 caracteres em um dígito hexadecimal. Primeiro converte o grupo de 4 (ou menos) caracteres em decimal e depois pega
      *  o caractere correspondente no array de hexadecimais. Utilize o método converteBinarioParaDecimal(String) mais acima.
      */
      caractereEncontrado = hexa[converteBinarioParaDecimal(binario.substring(posicaoInicial, posicaoFinal))];
      // insere no início da String de retorno
      sb.insert(0, caractereEncontrado);
 
      posicaoFinal = posicaoInicial;
   }
 
   return sb.toString();
}
    public static byte[] hexToBytes (String msgHex) {
        int tam = msgHex.length() / 2;
        byte[] bytes = new byte[tam];
        for (int i = 0; i < tam; i++) {
            bytes[i] = (byte)Integer.parseInt(msgHex.substring(i * 2, (i * 2) + 2), 16);
            //System.out.println(msgHex.substring(i * 2, (i * 2) + 2) + " - " + bytes[i] + " - " + intToBit8(bytes[i]));
        }
        return bytes;
    }
    
    public static String formataDataHoraSql (Date dataHora) {
        return String.valueOf(dataHora.getYear() + 1900) + "-" + String.valueOf(dataHora.getMonth() + 1) + "-" + String.valueOf(dataHora.getDate()) + " " + dataHora.toString().substring(11, 19);
    }
    public static String formatarDataHora(String data) {
        try {
            // Padrão de entrada do parâmetro data:  "2017-06-06T13:05:30"
            String dataHora = data.replaceAll("T", " ").replaceAll("-", "/").replaceAll("\"", ""); 
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
            // Padrão de entrada do parâmetro data: dd/MM/yyyy HH:mm:ss //
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
    
    public static String decodificaCoordenada (String hexCoordenada) {
        String longValueBits = longToBit(Long.valueOf(hexCoordenada, 16));
        boolean negativo = ((longValueBits.charAt(0) == '1') ? true : false);
        long comp2longValue = bitToLong(reverteBitsComplem2(longValueBits));
        double coordenada = comp2longValue * Math.pow(10, -7);
        coordenada = coordenada * ((negativo) ? -1 : 1);
        NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
        nf.setMaximumFractionDigits(6);
        return nf.format(coordenada);
    }
    
    public static String decodificaTemperatura(String hexCoordenada) {
        try {
//            System.out.println("Temperatura HexDecimal: " + hexCoordenada);
            int temperatura = hexToInt((hexCoordenada.charAt(0) == 'F') ? "0" + hexCoordenada.substring(1, hexCoordenada.length()) : hexCoordenada);
            String longValueBits = intToBit8(temperatura);
//            System.out.println("Tem Bit: " + longValueBits);
            boolean negativo = hexCoordenada.charAt(0) == 'F';
            String bits = "";
            if (negativo) {
                bits = reverteBitsComplem2(longValueBits);
//                System.out.println("Bit reverso: " + bits);
            } else {
                temperatura = hexToInt(hexCoordenada) / 16;
//                System.out.println(" Temperatura Inteira 01: " + hexToInt(hexCoordenada));
                bits = intToBit8(temperatura);
            }
//            System.out.println("Verifica o primeiro bit 0 ou 1: " + negativo);
            Long temp = bitToLong(bits);
            temp = (temp * ((negativo) ? -1 : 1));
//            System.out.println("Temperatura Final: " + temp);
            NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
            nf.setMaximumFractionDigits(6);
//            System.out.println("T: " + nf.format(temp));
            return nf.format((temp < 0) ? temp.doubleValue() - 1 : temp.doubleValue());
        } catch (Exception e) {
//            System.out.println("ERRO: "+e.getMessage());
            return null;
        }

    }
    
    public static String longToBit (long n) {
        return Long.toBinaryString(n);
    }
    
    public static Long bitToLong (String bits) {
        return Long.parseLong(bits, 2);
    }
    
    public static String reverteBitsComplem2 (String bits) {
        String saida = "";
        for (int i = 0; i < bits.length(); i++)
            saida += (bits.charAt(i) == '0' ? "1" : "0");
        return saida;
    }
    
}
