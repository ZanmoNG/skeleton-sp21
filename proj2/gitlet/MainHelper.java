package gitlet;

import java.io.File;
import java.util.HashMap;

import static gitlet.Utils.*;

public class MainHelper {

    /** current working directory */
    static final File CWD = new File (System.getProperty("user.dir"));

    static final File GITLET_FOLDER = join(CWD, ".gitlet");

    /** Creates a new Gitlet version-control system in the current directory.
     *  This system will automatically start with one commit: a commit
     *  that contains no files and has the commit message initial commit
     *  It will have a single branch: master, which initially points to
     *  this initial commit, and master will be the current branch.
     *  The timestamp for this initial commit will be
     *  00:00:00 UTC, Thursday, 1 January 1970
     *  in whatever format you choose for dates
     */
    public static void initHelper() {
        //  If there is already a Gitlet version-control system
        //  in the current directory, it should abort.
        if (GITLET_FOLDER.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        // build persistence
        Repository.setupRepository();
        // initial commit
        Commit initCommit = new Commit("initial commit");
        // save commit
        initCommit.saveCommit();
        // update commitTree and head
        CommitTree.addCommitTree(initCommit);
        Head.updateHead(initCommit.getId());
    }


    /**  Adds a copy of the file as it currently exists to
     *   the staging area (see the description of the commit command).
     *   For this reason, adding a file is also called
     *   staging the file for addition. Staging an already-staged file
     *   overwrites the previous entry in the staging area with
     *   the new contents. The staging area should be somewhere in .gitlet.
     *   If the current working version of the file is identical to
     *   the version in the current commit, do not stage it to be added,
     *   and remove it from the staging area if it is already there
     *   (as can happen when a file is changed, added, and then
     *   changed back to its original version). The file will no longer
     *   be staged for removal (see gitlet rm), if it was at
     *   the time of the command.
     */
    public static void addHelper(String filename) {
        // add files to the blobs and staging area
        // failure cases: if files does not exist, exit
        File file = join(Repository.CWD, filename);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        // save to Blob
        Blob.saveBlob(file);
        // save to stagingArea
        StagingArea sa = new StagingArea();
        sa.saveStagingArea(file, filename);

        // correct? maybe if no changed we can cache it

    }

    /** java gitlet.Main commit [message] */
    public static void commitHelper(String msg) {
        Commit cm = new Commit(msg);
        // persistence
        cm.saveCommit();
        CommitTree.addCommitTree(cm);
        Head.updateHead(cm.getId());
        // clear the staging area
        StagingArea.clearArea();
    }

    /** java gitlet.Main rm [file name] */
    public static void rmHelper(String filename) {
        // failure case: no need to rm
        Commit head = Head.readHeadAsCommit();
        StagingArea sa = StagingArea.readStagingArea();
        HashMap<String, String> saMap= sa.getAddedFiles();
        boolean headContains = head.contains(filename);
        boolean saContains = saMap.containsKey(filename);
        if (!headContains && !saContains) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }

        if (saContains) {
            saMap.remove(filename);
        }
        if (headContains) {
            sa.setRemoval(filename);
            File now = join(Repository.CWD, filename);
            if (now.exists()) {
                now.delete();
            }
        }
    }

    /** java gitlet.Main log */
    public static void logHelper() {
        CommitTree ct = CommitTree.readCommitTree();
        ct.display();
    }

    /** java gitlet.Main global-log */
    public static void globalLogHelper() {
        CommitTree.displayAll();
    }

    /** java gitlet.Main global-log */
    public static void findHelper() {

    }




}
