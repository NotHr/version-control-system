package in.nothr.vcs.commands;

import java.io.File;

import in.nothr.vcs.exceptions.JitException;

public class Init extends Add{
    private static final String REPO_DIR = ".jit";
    private static final String OBJECTS_DIR = REPO_DIR + File.separator + "objects";
    private static final String COMMITS_DIR = REPO_DIR + File.separator + "refs" + File.separator + "heads";

    public static void init() {
        File repoDir = new File(REPO_DIR);
        if(repoDir.exists()){
            throw new JitException("A Repo is already initilized");
        }else{
            new File(REPO_DIR).mkdirs();
        new File(OBJECTS_DIR).mkdirs();
        new File(COMMITS_DIR).mkdirs();
        System.out.println("Initialized empty Git repository in " + new File(REPO_DIR).getAbsolutePath());
        }
    }
}
