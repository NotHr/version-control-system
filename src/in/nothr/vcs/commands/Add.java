package in.nothr.vcs.commands;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import in.nothr.vcs.utils.HashUtil;

public class Add extends Commit {
    private static final String REPO_DIR = ".jit";
    private static final String OBJECTS_DIR = REPO_DIR + File.separator + "objects";
    private static final String INDEX_FILE = REPO_DIR + File.separator + "index";

    public static void add(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.err.println("Error: File not found.");
                return;
            }
            String fileHash = HashUtil.hashFile(file);
            File destDir = new File(OBJECTS_DIR + File.separator + fileHash.substring(0, 2));
            if (!destDir.exists()) {
                destDir.mkdirs(); 
            }
            File destFile = new File(destDir, fileHash.substring(2)); 
            Files.copy(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            appendToIndex(file.getName(), fileHash); 
            System.out.println("Added " + filePath + " to index.");
        } catch (IOException | NoSuchAlgorithmException e) {
            System.err.println("Error adding file: " + e.getMessage());
        }
    }

    private static void appendToIndex(String filename, String hash) throws IOException {
        FileWriter writer = new FileWriter(INDEX_FILE, true);
        writer.write(hash + " " + filename + "\n");
        writer.close();
    }
}
