package in.nothr.vcs.commands;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;


public class Revert {
    private static final String REPO_DIR = ".jit";
    private static final String OBJECTS_DIR = REPO_DIR + File.separator + "objects";
    private static final String COMMITS_DIR = REPO_DIR + File.separator + "refs"+ File.separator + "heads";
    private static final String INDEX_FILE = REPO_DIR + File.separator + "index";

    public static void revert(String commitHash) {
        try {
            File commitFile = new File(COMMITS_DIR + File.separator + commitHash);
            if (!commitFile.exists()) {
                System.err.println("Error: Commit not found.");
                return;
            }
            List<String> indexEntriesBefore = readIndex();
            List<String> indexEntriesAfter = new ArrayList<>();

            // Read the commit content
            FileReader fileReader = new FileReader(commitFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            StringBuilder commitContent = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                commitContent.append(line).append("\n");
                if (line.startsWith("100644 blob")) {
                    String[] parts = line.split(" ");
                    String blobHash = parts[2];
                    indexEntriesAfter.add(blobHash + " " + parts[3]);
                }
            }
            bufferedReader.close();

            // Calculate file changes
            List<String> deletedFiles = new ArrayList<>(indexEntriesBefore);
            deletedFiles.removeAll(indexEntriesAfter);
            List<String> addedFiles = new ArrayList<>(indexEntriesAfter);
            addedFiles.removeAll(indexEntriesBefore);

            // Revert changes
            for (String entry : deletedFiles) {
                File file = new File(OBJECTS_DIR + File.separator + entry.split(" ")[0]);
                if (file.exists()) {
                    file.delete();
                }
            }
            for (String entry : addedFiles) {
                String[] parts = entry.split(" ");
                File sourceFile = new File(OBJECTS_DIR + File.separator + parts[0]);
                File destFile = new File(parts[1]);
                Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            new FileWriter(INDEX_FILE).close();

            // Update HEAD to point to the reverted commit
            Files.copy(commitFile.toPath(), new File(COMMITS_DIR + File.separator + "HEAD").toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Display file changes
            System.out.println("Reverted to commit " + commitHash);
            if (!deletedFiles.isEmpty()) {
                System.out.println("Deleted files:");
                for (String entry : deletedFiles) {
                    System.out.println(entry.split(" ")[1]);
                }
            }
            if (!addedFiles.isEmpty()) {
                System.out.println("Added files:");
                for (String entry : addedFiles) {
                    System.out.println(entry.split(" ")[1]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reverting to commit: " + e.getMessage());
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
}
