package gitlet;

import static gitlet.MainHelper.*;
import static gitlet.Utils.*;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Zanmo
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        /* If a user doesn’t input any arguments,
         print the message. and exit. */
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }

        String firstArg = args[0];
        if (!firstArg.equals("init") && !Repository.GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        switch(firstArg) {
            // TODO: handle wrong number of args for add, commit....
            case "init":
                initHelper();
                break;
            case "add":
                addHelper(args[1]);
                break;
            case "commit":
                if (args.length == 1 || args[1].isEmpty()) {
                    System.out.println("Please enter a commit message.");
                    System.exit(0);
                }
                commitHelper(args[1]);
                break;
            case "rm":
                rmHelper(args[1]);
                break;
            case "log":
                logHelper();
                break;
            case "global-log":
                globalLogHelper();
                break;
            case "find":
                findHelper(args[1]);
                break;
            case "status":
                statusHelper();
                break;
            case "checkout":
                int len = args.length;
                if (len == 2) {
                    checkoutHelper_3(args[1]);
                } else if (len == 3) {
                    checkoutHelper_1(args[2]);
                } else if (len == 4) {
                    if (!args[2].equals("--")) {
                        System.out.println("Incorrect operands.");
                        System.exit(0);
                    }
                    checkoutHelper_2(args[1], args[3]);
                }
                break;
            case "branch":
                branchHelper(args[1]);
                break;
            case "rm-branch":
                rmBranchHelper(args[1]);
                break;
            case "reset":
                resetHelper(args[1]);
                break;
            case "merge":
                mergeHelper(args[1]);
                break;
            /* If a user inputs a command that doesn’t exist,
            print the message and exit. */
            default:
                System.out.println("No command with that name exists");
                System.exit(0);
        }
    }
}
