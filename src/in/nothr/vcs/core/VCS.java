package in.nothr.vcs.core;

import in.nothr.vcs.commands.Add;
import in.nothr.vcs.commands.Commit;
import in.nothr.vcs.commands.Revert;
import in.nothr.vcs.commands.Init;
public class VCS {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java vcs.VCS <command> [<args>]");
            return;
        }
        String command = args[0];
        switch (command) {
            case "init":
                Init.init();
                break;
            case "add":
                if (args.length < 2) {
                    System.err.println("Error: Missing file path.");
                    return;
                }
                Add.add(args[1]);
                break;
            case "commit":
                if (args.length < 2) {
                    System.err.println("Error: Missing commit message.");
                    return;
                }
                Commit.commit(args[1]);
                break;
            case "revert":
                if (args.length < 2) {
                    System.err.println("Error: Missing commit hash.");
                    return;
                }
                Revert.revert(args[1]);
                break;
            default:
                System.err.println("Error: Unknown command.");
        }
    }
}
