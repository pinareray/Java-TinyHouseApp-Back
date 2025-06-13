package com.example.tinyhouse.core.utilities.security.hashing;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashingHelper {
    public static String createPasswordHash(String password)
            throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hash = messageDigest.digest(password.getBytes("UTF-8"));
        String hashString = bytesToHex(hash);
        return hashString;
    }

    public static boolean verifyPasswordHash(String password, String passwordHash)
            throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException
    {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hash = messageDigest.digest(password.getBytes("UTF-8"));
        String calculatedHashString = bytesToHex(hash);
        if (passwordHash.equals(calculatedHashString)) {
            return true;
        } else {
            return false;
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}