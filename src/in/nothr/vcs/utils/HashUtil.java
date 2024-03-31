package in.nothr.vcs.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class HashUtil {
    public static String hashFile(File file) throws IOException, NoSuchAlgorithmException {
        byte[] fileBytes = Files.readAllBytes(file.toPath());
        return hashObject(fileBytes);
    }

    public static String hashTree(List<String> indexEntries) throws NoSuchAlgorithmException {
        StringBuilder treeContent = new StringBuilder();
        for (String entry : indexEntries) {
            treeContent.append("100644 blob ").append(entry).append("\n");
        }
        return hashObject(treeContent.toString().getBytes());
    }
    public static String hashObject(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] hashBytes = md.digest(bytes);
        StringBuilder hash = new StringBuilder();
        for (byte b : hashBytes) {
            hash.append(String.format("%02x", b));
        }
        return hash.toString();
    }
}
