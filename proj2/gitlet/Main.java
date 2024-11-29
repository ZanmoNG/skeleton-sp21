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
            // TODO: FILL THE REST IN
            /* If a user inputs a command that doesn’t exist,
            print the message and exit. */
            default:
                System.out.println("No command with that name exists");
                System.exit(0);
        }
    }
}
