package com.fschmatz.evento_service_v3.util;

public class Encrypt {

    public static String encrypt(String msg){
        StringBuffer sb = new StringBuffer(msg);
        String a = sb.reverse().toString();
        StringBuilder sc = new StringBuilder(a);

        String p =  sc.insert(1, "GñopDS1G 978F 7CS7F-{54").toString();

        p = sc.insert(0, "D(GS G9õmoga16*.98F {´´´798!?").toString();
        p = sc.insert(p.length() - 1, "´´sg  dg5d4àóîís g2dg9s7").toString();
        p = sc.insert(p.length(), "kl sco pesaox").toString();

        return p.toUpperCase();
    }

}