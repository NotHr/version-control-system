package in.nothr.vcs.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Reset {
    private static final String REPO_DIR = ".jit";
    private static final String COMMITS_DIR = REPO_DIR + File.separator + "refs" + File.separator + "heads";
    private static final String INDEX_FILE = REPO_DIR + File.separator + "index";

    public static void reset(String commitHash, String mode) {
        try {
            File commitFile = new File(COMMITS_DIR + File.separator + commitHash);
            if (!commitFile.exists()) {
                System.err.println("Error: Commit not found.");
                return;
            }

            List<String> commitEntries = readCommitEntries(commitFile);

            switch (mode) {
                case "--SOFT":
                    Files.copy(commitFile.toPath(), new File(COMMITS_DIR + File.separator + "HEAD").toPath(), StandardCopyOption.REPLACE_EXISTING);
                    break;
                case "--MIXED":
                    Files.copy(commitFile.toPath(), new File(COMMITS_DIR + File.separator + "HEAD").toPath(), StandardCopyOption.REPLACE_EXISTING);
                    updateIndexEntries(commitEntries);
                    break;
            }

            System.out.println("Reset to commit " + commitHash);
        } catch (IOException e) {
            System.err.println("Error resetting to commit: " + e.getMessage());
        }
    }



    private static List<String> readCommitEntries(File commitFile) throws IOException {
        try (Stream<String> lines = Files.lines(commitFile.toPath())) {
            return lines
                    .filter(line -> line.startsWith("100644 blob"))
                    .map(line -> line.split(" ")[2] + " " + line.split(" ")[3])
                    .collect(Collectors.toList());
        }
    }

    private static void updateIndexEntries(List<String> commitEntries) throws IOException {
        Path indexPath = Paths.get(INDEX_FILE);
        Files.write(indexPath, commitEntries);
    }
}