package in.nothr.vcs.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CheckSum {
    public static String create(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = md.digest(input.getBytes());
            StringBuilder hash = new StringBuilder();
            for (byte b : hashBytes) {
                hash.append(String.format("%02x", b));
            }
            return hash.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error calculating checksum: " + e.getMessage());
            return null;
        }
    }
}
