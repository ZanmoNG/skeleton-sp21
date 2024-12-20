package gitlet;

import java.io.File;
import java.text.DecimalFormat;
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
        StagingArea sa = StagingArea.readStagingArea();
        sa.saveStagingArea(file, filename);
        // correct? maybe if no changed we can cache it

    }

    /** java gitlet.Main commit [message] */
    public static void commitHelper(String msg) {
        if (StagingArea.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
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
        StagingArea sa = null;
        if (StagingArea.isEmpty()) {
            sa = new StagingArea();
        } else {
            sa = StagingArea.readStagingArea();
        }
        HashMap<String, String> saMap= sa.getAddedFiles();
        boolean headContains = head.contains(filename);
        boolean saContains = saMap.containsKey(filename);
        if (!headContains && !saContains) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }

        if (saContains) {
            sa.rmStagedFiles(filename);
        }
        if (headContains) {
            sa.setRemoval(filename);
            File now = join(Repository.CWD, filename);
            if (now.exists()) {
                now.delete();
            }
        }
        ;
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
        boolean found = false;
        for (int i = 1; i <= 256; i++) {
            String folderId = Integer.toHexString(i);
            File folder = join(Repository.COMMIT_FOLDER, folderId);
            if (folder.exists()) {
                List<String> fileList = plainFilenamesIn(folder);
                // read all files in folder
                for (String file: fileList) {
                    Commit c = Commit.readCommit(file);
                    if (c.getMessage().equals(msg)) {
                        found = true;
                        System.out.println(c.getId());
                    }
                }
            }
        }
        if (!found) {
            System.out.println("Found no commit with that message.");
            System.exit(0);
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

        LinkedList<String> stagedFiles = new LinkedList<>();
        LinkedList<String> removedFiles = new LinkedList<>();
        LinkedList<String> modifiedFiles = new LinkedList<>();
        LinkedList<String> untrackedFiles = new LinkedList<>();

        // read all files in current commit

        // for each file:
        //      same as CWD?
        //      1. same
        //      2. different
        //          in staging area? same with the one in staging?
        //      3. not found
        Commit head = Head.readHeadAsCommit();
        Map<String, String> currentCommitFiles = head.getCommitFiles();
        StagingArea sa;
        if (StagingArea.isEmpty()) {
            sa = new StagingArea();
        } else {
            sa = StagingArea.readStagingArea();
        }
        for (String file: currentCommitFiles.keySet()) {
            File CWDfile = join(Repository.CWD, file);
            if (!CWDfile.exists()) {
                if (sa.removalContainsFile(file)) {
                    // case 1 removed
                    removedFiles.addLast(file);
                } else {
                    // case 2 removed but not staged
                    modifiedFiles.addLast(file + " (deleted)");
                }
            } else {
                // this line will get the contents of the file in current commit
                String commitContents = readContentsAsString(Blob.readBlob(currentCommitFiles.get(file)));
                String CWDContents = readContentsAsString(CWDfile);
                if (!commitContents.equals(CWDContents)) {
                    // else same, nothing to do
                    if (sa.stagedFilesContainsAndSame(CWDfile, file)) {
                        // case 1 staged modification
                        stagedFiles.addLast(file);
                    } else {
                        // case 2 modified and not staged
                        modifiedFiles.addLast(file + " (modified)");
                    }
                }
            }
        }

        // for all files that in CWD but not in current commit
        // case 1: untracked
        // case 2: staged
        // case 3: staged but modified then

        for (String filename: plainFilenamesIn(Repository.CWD)) {
            // in CWD but not in commit
            if (!head.contains(filename)) {
                if (!sa.getAddedFiles().containsKey(filename)) {
                    // case 1: not staged
                    untrackedFiles.addLast(filename);
                } else if (sa.stagedFilesContainsAndSame(join(Repository.CWD, filename), filename)){
                    // case 2: staged
                    stagedFiles.addLast(filename);
                } else {
                    // case 3: staged but not same
                    modifiedFiles.addLast(filename + " (modified)");
                }
            }
        }

        // staged, but deleted in CWD
        for (String file: sa.getAddedFiles().keySet()) {
            File CWDFile = join(Repository.CWD, file);
            if (!CWDFile.exists()) {
                modifiedFiles.addLast(file + " (deleted)");
            }
        }
        Collections.sort(stagedFiles);
        Collections.sort(removedFiles);
        Collections.sort(modifiedFiles);
        Collections.sort(untrackedFiles);

        System.out.println("=== Staged Files ===");
        printlnHelper(stagedFiles);
        System.out.println("=== Removed Files ===");
        printlnHelper(removedFiles);
        System.out.println("=== Modifications Not Staged For Commit ===");
        printlnHelper(modifiedFiles);
        System.out.println("=== Untracked Files ===");
        printlnHelper(untrackedFiles);
    }

    private static void printlnHelper(List<String> ls) {
        for (String name: ls) {
            System.out.println(name);
        }
        System.out.println();
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

    private static String shortUid(String shortVer) {
        for(int i = 1; i <= 256; i++) {
            String hex2Id = Integer.toHexString(i);
            if (hex2Id.length() == 1) {
                hex2Id = "0" + hex2Id;
            }
            File folder = join(Repository.COMMIT_FOLDER, hex2Id);
            if (folder.exists()) {
                List<String> files = plainFilenamesIn(folder);
                for (String file : files) {
                    File filePath = join(folder, file);
                    Commit c = readObject(filePath, Commit.class);
                    if (c.getId().startsWith(shortVer)) {
                        return c.getId();
                    }
                }
            }
        }
        return null;
    }

    /** java gitlet.Main checkout [commit id] -- [file name] */
    public static void checkoutHelper_2(String id, String filename) {
        id = shortUid(id);
        if (id == null) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        // read the commit
        Commit c = Commit.readCommit(id);
        // read the file
        File file = c.getFile(filename);
        if (file == null) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        // rewrite the file
        String contents = readContentsAsString(file);
        file = join(Repository.CWD, filename);
        writeContents(file, contents);
    }

    /** java gitlet.Main checkout [branch name] */
    public static void checkoutHelper_3(String branchName) {
        Commit head = Head.readHeadAsCommit();
        StagingArea sa;
        if (StagingArea.isEmpty()) {
            sa = new StagingArea();
        } else {
            sa = StagingArea.readStagingArea();
        }
        for (String filename: plainFilenamesIn(Repository.CWD)) {
            // in CWD but not in commit
            if (!head.contains(filename)) {
                if (!sa.getAddedFiles().containsKey(filename)) {
                    System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                    System.exit(0);
                }
            }
        }

        // read the branch
        String id = CommitTree.getBranch(branchName);
        if (id == null) {
            System.out.println("No such branch exists.");
            System.exit(0);
        } else if (CommitTree.noNeedToCheckout(branchName)) {
            System.out.println("No need to checkout the current branch.");
        }

        // read the file
        Commit c = Commit.readCommit(id);
        Map<String, String> files = c.getCommitFiles();

        // delete all files in CWD
        List<String> CWDFiles = plainFilenamesIn(Repository.CWD);
        for (String filename: CWDFiles) {
            File path = join(Repository.CWD, filename);
            path.delete();

        }

        // add to CWD
        for (String name: files.keySet()) {
            String contents = readContentsAsString(Blob.readBlob(files.get(name)));
            File f = join(Repository.CWD, name);
            writeContents(f, contents);
        }

        // change Head
        Head.updateHead(id);
        CommitTree.changeMaster(branchName);

        // clear staging area
        StagingArea.clearArea();
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
    public static void resetHelper(String id) {
        Commit head = Head.readHeadAsCommit();
        StagingArea sa = StagingArea.readStagingArea();
        for (String filename: plainFilenamesIn(Repository.CWD)) {
            // in CWD but not in commit
            if (!head.contains(filename)) {
                if (!sa.getAddedFiles().containsKey(filename)) {
                    System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                    System.exit(0);
                }
            }
        }

        // remove all files in CWD
        List<String> CWDFiles = plainFilenamesIn(Repository.CWD);
        for (String file: CWDFiles) {
            File path = join(Repository.CWD, file);
            path.delete();
        }
        // read the commit and cp all the files into CWD
        Commit c = Commit.readCommit(id);
        for (String filename: c.getCommitFiles().keySet()) {
            File CWDPath = join(Repository.CWD, filename);
            File blobFile = Blob.readBlob(c.getCommitFiles().get(filename));
            String contents = readContentsAsString(blobFile);
            writeContents(CWDPath, contents);
        }
        // move the head
        Head.updateHead(id);
        CommitTree.moveHead(id);
        StagingArea.clearArea();

        /*TODO: maybe we can delete files and move head, and just checkout then*/
    }

    /** java gitlet.Main merge [branch name]
     *  Merges files from the given branch into the current branch.
     * */
    public static void mergeHelper(String branchName) {
        CommitTree ct = CommitTree.readCommitTree();
        if (branchName.equals(ct.getMaster().getMsg())) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
        // read the branch files
        String branchId = CommitTree.getAimedBranchId(branchName);
        Map<String, String> branchFiles = Commit.readCommit(branchId).getCommitFiles();
        Set<String> branchFilesName = branchFiles.keySet();

        // find the latest common ancestor， read files
        String LCAId = CommitTree.findLatestCommonAncestor(branchName);
        Map<String, String> LCAFiles = Commit.readCommit(LCAId).getCommitFiles();
        Set<String> LCAFilesName = LCAFiles.keySet();

        if (LCAId.equals(branchId)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            System.exit(0);
        }

        // read current files
        String headId = Head.readHeadAsCommit().getId();
        Map<String, String> headFiles = Head.readHeadAsCommit().getCommitFiles();
        Set<String> headFilesName = headFiles.keySet();

        if (headId.equals(LCAId)) {
            checkoutHelper_3(branchName);
            System.out.println("Current branch fast-forwarded.");
            System.exit(0);
        }

        if (!StagingArea.isEmpty()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
        StagingArea sa = StagingArea.readStagingArea();

        // is there any untracked files?
        List<String> CWDFiles = plainFilenamesIn(Repository.CWD);
        for (String file: CWDFiles) {
            if (!headFilesName.contains(file)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
        }

        // cases:
        //      Given branch is an ancestor of the current branch.
        //

        boolean conflict = false;

        for (String fileName: LCAFilesName) {
            File CWDPath = join(Repository.CWD, fileName);

            boolean modifiedInCurrent = false;
            boolean modifiedInBranch = false;
            boolean deletedInCurrent = false;
            boolean deletedInBranch = false;

            String branchContents = "";
            String currentContents = "";

            File LCAPath = Blob.readBlob(LCAFiles.get(fileName));
            if (!branchFilesName.contains(fileName)) {
                deletedInBranch = true;
            } else {
                File branchPath = Blob.readBlob(branchFiles.get(fileName));
                branchContents = readContentsAsString(branchPath);
                if (!LCAPath.equals(branchPath)) {
                    modifiedInBranch = true;
                }
            }
            if (!headFilesName.contains(fileName)) {
                deletedInCurrent = true;
            } else {
                File headPath = Blob.readBlob(headFiles.get(fileName));
                currentContents = readContentsAsString(headPath);
                if (!LCAPath.equals(headPath)) {
                    modifiedInCurrent = true;
                }
            }

            if (!modifiedInCurrent && !modifiedInBranch
                && !deletedInBranch && !deletedInCurrent) {
                continue;
            } else if (modifiedInBranch && !modifiedInCurrent && !deletedInCurrent) {
                // case 1 in spec
                writeContents(CWDPath, branchContents);
                sa.saveStagingArea(CWDPath, fileName);
            } else if (modifiedInCurrent && !modifiedInBranch && !deletedInBranch) {
                // case 2 in spec
                writeContents(CWDPath, currentContents);
                sa.saveStagingArea(CWDPath, fileName);
            } else if (modifiedInBranch && modifiedInCurrent) {
                if (currentContents.equals(branchContents)) {
                    // same, case3A in spec
                    writeContents(CWDPath, currentContents);
                    sa.saveStagingArea(CWDPath, fileName);
                } else {
                    // different, case 8A
                    String result = "<<<<<<< HEAD\n" +
                            currentContents +
                            "=======\n" +
                            branchContents +
                            ">>>>>>>\n";
                    writeContents(CWDPath, result);
                    conflict = true;
                    addHelper(fileName);
                    sa = StagingArea.readStagingArea();
                }
            } else if (!modifiedInCurrent && deletedInBranch) {
                // case 6
                CWDPath.delete();
                sa.setRemoval(fileName);
            } else if (!modifiedInBranch && deletedInCurrent) {
                // case 7
                if (CWDPath.exists()) {
                    CWDPath.delete();
                }
            } else if (deletedInBranch && deletedInCurrent) {
                // case 3B
                sa.setRemoval(fileName);
            } else if (modifiedInBranch && deletedInCurrent) {
                String result = "<<<<<<< HEAD\n" +
                        currentContents +
                        "=======\n" +
                        branchContents +
                        ">>>>>>>\n";
                writeContents(CWDPath, result);
                conflict = true;
                addHelper(fileName);
                sa = StagingArea.readStagingArea();
            } else if (modifiedInCurrent && deletedInBranch) {
                String result = "<<<<<<< HEAD\n" +
                        currentContents +
                        "=======\n" +
                        branchContents +
                        ">>>>>>>\n";
                writeContents(CWDPath, result);
                conflict = true;
                addHelper(fileName);
                sa = StagingArea.readStagingArea();
            }
        }
        for (String fileName: headFilesName) {
            File CWDPath = join(Repository.CWD, fileName);
            File headPath = Blob.readBlob(headFiles.get(fileName));
            String currentContents = readContentsAsString(headPath);
            if (!LCAFilesName.contains(fileName)) {
                if (!branchFilesName.contains(fileName)) {
                    writeContents(CWDPath, currentContents);
                    sa.saveStagingArea(CWDPath, fileName);
                } else {
                    File branchPath = Blob.readBlob(branchFiles.get(fileName));
                    String branchContents = readContentsAsString(branchPath);
                    if (branchContents.equals(currentContents)) {
                        writeContents(CWDPath, currentContents);
                        sa.saveStagingArea(CWDPath, fileName);
                    } else {
                        String result = "<<<<<<< HEAD\n" +
                                currentContents +
                                "=======\n" +
                                branchContents +
                                ">>>>>>>\n";
                        writeContents(CWDPath, result);
                        conflict = true;
                        addHelper(fileName);
                        sa = StagingArea.readStagingArea();
                    }
                }
            }
        }
        for (String fileName: branchFilesName) {
            File CWDPath = join(Repository.CWD, fileName);
            File branchPath = Blob.readBlob(branchFiles.get(fileName));
            String branchContents = readContentsAsString(branchPath);
            if (!LCAFilesName.contains(fileName)) {
                if (!headFilesName.contains(fileName)) {
                    writeContents(CWDPath, branchContents);
                    sa.saveStagingArea(CWDPath, fileName);
                } else {
                    File headPath = Blob.readBlob(headFiles.get(fileName));
                    String currentContents = readContentsAsString(headPath);
                    if (branchContents.equals(currentContents)) {
                        writeContents(CWDPath, currentContents);
                        sa.saveStagingArea(CWDPath, fileName);
                    } else {
                        String result = "<<<<<<< HEAD\n" +
                                currentContents +
                                "=======\n" +
                                branchContents +
                                ">>>>>>>\n";
                        writeContents(CWDPath, result);
                        conflict = true;
                        addHelper(fileName);
                        sa = StagingArea.readStagingArea();
                    }
                }
            }
        }
        String msg = "Merged " + branchName + " into " + ct.getMaster().getMsg() + ".";

        Commit c = new Commit(msg);
        c.setMergeParent(branchId);
        c.saveCommit();
        CommitTree.addCommitTree(c);
        StagingArea.clearArea();
        Head.updateHead(c.getId());
        if (conflict) {
            System.out.println("Encountered a merge conflict.");
        }
    }

    /** this method accepts 2 ids and return whether they are modified */
    private static boolean isModified(String id1, String id2) {
        File f1 = Blob.readBlob(id1);
        File f2 = Blob.readBlob(id2);
        if (f1.equals(f2)) {
            return false;
        } else {
            return true;
        }
    }
}
