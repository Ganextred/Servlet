package org.example.luxuryhotel.framework.security;

import org.apache.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncoder {
    private final static Logger logger = Logger.getLogger(PasswordEncoder.class);
    public static String encode(String password){
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            logger.error("Cant convert password");
            e.printStackTrace();
        }
        byte[] code = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return code.toString();
    }
}