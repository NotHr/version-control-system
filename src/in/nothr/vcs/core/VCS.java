package in.nothr.vcs.core;


import in.nothr.vcs.commands.Init;
public class VCS extends Init{
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java vcs.VCS <command> [<args>]");
            return;
        }
        String command = args[0];
        switch (command) {
            case "init":
                VCS.init();
                break;
            case "add":
                if (args.length < 2) {
                    System.err.println("Error: Missing file path.");
                    return;
                }
                VCS.add(args[1]);
                break;
            case "commit":
                if (args.length < 2) {
                    System.err.println("Error: Missing commit message.");
                    return;
                }
                VCS.commit(args[1]);
                break;
            case "reset":
                if (args.length < 2) {
                    System.err.println("Error: Missing commit hash.");
                    return;
                }
                VCS.reset(args[1], args[2]);
                break;
            default:
                System.err.println("Error: Unknown command.");
        }
    }
}
