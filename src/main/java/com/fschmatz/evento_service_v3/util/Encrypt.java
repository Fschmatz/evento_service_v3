package com.fschmatz.evento_service_v3.util;

import java.util.Base64;

public class Encrypt {

    public static String encriptar(String msg){
        return new String(Base64.getEncoder().encode(msg.getBytes()));
    }

    public static String descriptografar(String msgCripto) {
        return new String(Base64.getDecoder().decode(msgCripto.getBytes()));
    }
}