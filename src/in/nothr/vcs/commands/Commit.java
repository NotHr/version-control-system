package in.nothr.vcs.commands;

import in.nothr.vcs.utils.HashUtil;

import java.io.*;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Commit extends Reset{

    private static final String REPO_DIR = ".jit";
    private static final String INDEX_FILE = REPO_DIR + File.separator + "index";
    private static final String OBJECTS_DIR = REPO_DIR + File.separator + "objects";
    private static final String COMMIT_LOG_FILE = REPO_DIR + File.separator + "log";
    private static final String COMMITS_DIR = REPO_DIR + File.separator + "refs" + File.separator + "heads";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z");

    public static void commit(String message) {
        try {
            List<String> indexEntries = readIndex();
            if (indexEntries.isEmpty()) {
                System.err.println("Error: No files added to commit.");
                return;
            }
            String treeHash = hashTree(indexEntries);
            StringBuilder commitContent = new StringBuilder();
            commitContent.append("tree ").append(treeHash).append("\n");
            commitContent.append("author: ").append(System.getProperty("user.name")).append(" <").append(System.getProperty("user.name")).append("@localhost>\n");
            commitContent.append("date: ").append(DATE_FORMAT.format(new Date())).append("\n");
            commitContent.append("\n").append(message).append("\n");
            String commitHash = HashUtil.hashObject(commitContent.toString().getBytes());
            File commitFile = new File(COMMITS_DIR + File.separator + commitHash);
            FileWriter writer = new FileWriter(commitFile);
            writer.write(commitContent.toString());
            writer.close();
            FileWriter logWriter = new FileWriter(COMMIT_LOG_FILE, true);
            logWriter.write(commitHash + ": " + message + "\n");
            logWriter.close();
            System.out.println("Committed changes with commit hash: " + commitHash);
        } catch (IOException | NoSuchAlgorithmException e) {
            System.err.println("Error committing changes: " + e.getMessage());
        }
    }

    private static List<String> readIndex() throws IOException {
        List<String> indexEntries = new ArrayList<>();
        File indexFile = new File(INDEX_FILE);
        if (indexFile.exists()) {
            FileReader reader = new FileReader(indexFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                indexEntries.add(line);
            }
            bufferedReader.close();
        }
        return indexEntries;
    }

    private static String hashTree(List<String> indexEntries) {
        try {
            StringBuilder treeContent = new StringBuilder();
            for (String entry : indexEntries) {
                treeContent.append("100644 blob ").append(entry).append("\n");
            }
            String treeContentString = treeContent.toString();
            String treeHash = HashUtil.hashObject(treeContentString.getBytes());
            File treeFile = new File(OBJECTS_DIR + File.separator + treeHash.substring(0, 2));
            treeFile.mkdirs();
            FileWriter writer = new FileWriter(new File(treeFile, treeHash.substring(2)));
            writer.write(treeContentString);
            writer.close();
            return treeHash;
        } catch (IOException | NoSuchAlgorithmException e) {
            System.err.println("Error hashing tree: " + e.getMessage());
            return null;
        }
    }
}
