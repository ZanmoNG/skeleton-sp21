package gitlet;

import static gitlet.MainHelper.*;

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
            System.out.println("Please enter a  command.");
            System.exit(0);
        }

        String firstArg = args[0];
        switch(firstArg) {
            // TODO: handle wrong number of args for add, commit....
            case "init":
                initHelper();
                break;
            case "add":
                addHelper(args[1]);
                break;
            case "commit":
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
                    checkoutHelper_3();
                } else if (len == 3) {
                    checkoutHelper_1(args[2]);
                } else if (len == 4) {
                    checkoutHelper_2(args[1], args[3]);
                }
                break;
            case "branch":
                branchHelper(args[1]);
            /* If a user inputs a command that doesn’t exist,
            print the message and exit. */
            default:
                System.out.println("No command with that name exists");
                System.exit(0);
        }
    }
}
