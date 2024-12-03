package gitlet;

import java.io.File;
import java.util.*;

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

    /** java gitlet.Main find [commit message] */
    public static void findHelper(String msg) {
        // read through all folders
        for (int i = 1; i <= 256; i++) {
            String folderId = Integer.toHexString(i);
            File folder = join(Repository.COMMIT_FOLDER, folderId);
            if (folder.exists()) {
                List<String> fileList = plainFilenamesIn(folder);
                // read all files in folder
                for (String file: fileList) {
                    Commit c = Commit.readCommit(file);
                    if (c.getMessage().equals(msg)) {
                        System.out.println(c.getId());
                    }
                }
            }
        }
    }

    /** java gitlet.Main status */
    public static void statusHelper() {
        // === Branches ===
            // read the CommitTree master and branches
        String branchesString = "=== Branches ===\n";
        CommitTree ct = CommitTree.readCommitTree();
        CommitTree.branch master = ct.getMaster();
        List<CommitTree.branch> branches = ct.getBranches();
        branchesString = branchesString + "*" + master.getMsg() + "\n";
        for (CommitTree.branch branch: branches) {
            branchesString = branchesString + branch.getMsg() + '\n';
        }
        System.out.println(branchesString);

        // === Staged Files ===
            // read the StagingArea addedFiles
        String stagedFilesString = "=== Staged Files ===\n";
        StagingArea sa = StagingArea.readStagingArea();
        Set<String> addedFileNames = sa.getAddedFiles().keySet();
        for (String name: addedFileNames) {
            stagedFilesString = stagedFilesString + name + "\n";
        }
        System.out.println(stagedFilesString);

        // === Removed Files ===
            // read the StagingArea removal
        String removedFilesString = "=== Removed Files ===";
        List<String> deletedFileNames = sa.getRemoval();
        for (String name: deletedFileNames) {
            removedFilesString = removedFilesString + name + "\n";
        }
        System.out.println(removedFilesString);

        System.out.println("=== Modifications Not Staged For Commit ===\n"
                            + "\n=== Untracked Files ===\n");

        // TODO: extra credit
        /*
        // === Modifications Not Staged For Commit ===
        String modificatioinsString = "=== Modifications Not Staged For Commit ===";
        LinkedList<String> modifiedFiles = new LinkedList<>();
            // read the current commit
        Commit currentCommit = Head.readHeadAsCommit();
        Map<String, String> currentCommitFiles = currentCommit.getCommitFiles();
        for(String fileName: currentCommitFiles.keySet()) {
            File path = join(Repository.CWD, fileName);
            if (!path.exists()) {
                // TODO: add to modifiedFiles
            }
        }
            // compare to StagingArea



        // === Untracked Files ===
        */
    }

    /** decide which checkout method to choose from */
    public static void checkoutHelper() {

    }

    /** java gitlet.Main checkout -- [file name]*/
    public static void checkoutHelper_1(String filename) {
        // read the head
        Commit h = Head.readHeadAsCommit();
        // read the file
        File file = h.getFile(filename);
        if (file == null) {
            throw error("File does not exist in that commit.");
        }
        // rewrite the file
        String contents = readContentsAsString(file);
        file = join(Repository.CWD, filename);
        writeContents(file, contents);
    }

    /** java gitlet.Main checkout [commit id] -- [file name] */
    public static void checkoutHelper_2(String id, String filename) {
        // read the commit
        Commit c = Commit.readCommit(id);
        // read the file
        File file = c.getFile(filename);
        if (file == null) {
            throw error("File does not exist in that commit.");
        }
        // rewrite the file
        String contents = readContentsAsString(file);
        file = join(Repository.CWD, filename);
        writeContents(file, contents);
    }

    /** java gitlet.Main checkout [branch name] */
    public static void checkoutHelper_3() {
        // read the branch

        // read the file

        // add to CWD

        // change Head

        // clear staging area
    }

    /** java gitlet.Main branch [branch name]
     *  Creates a new branch with the given name,
     *  and points it at the current head commit.
     *  This command does NOT immediately switch to the newly created branch
     *  A branch is nothing more than a name for a reference
     * */
    public static void branchHelper(String name) {
        // create new branch in commitTree, with the provided name
        CommitTree.addBranch(name);
    }

    /** java gitlet.Main rm-branch [branch name] */
    public static void rmBranchHelper(String name) {
        // remove in the commitTree's branches
        CommitTree.rmBranch(name);
    }

    /** java gitlet.Main reset [commit id] */
    public static void resetHelper() {
        // remove all files in CWD

        // read the commit and cp all the files into CWD

        // move the head

        /*TODO: maybe we can delete files and move head, and just checkout then*/
    }

    /** java gitlet.Main merge [branch name]
     *  Merges files from the given branch into the current branch.
     * */
    public static void mergeHelper() {
        //
    }
}
