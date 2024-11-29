package gitlet;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.io.File;

import static gitlet.Utils.*;

public class CommitTree implements Serializable {
    private static class node {
        String id;
        node parent;
        // TODO: what is a merged node? and what makes it merged?
        boolean merged;
        /** if merged, it will have one more parent */
        node mergedParent;

        public node(String theId, node theParent) {
            id = theId;
            parent = theParent;
            merged = false;
            mergedParent = null;
        }

        public void show() {
            Commit c = Commit.readCommit(id);
            if (!merged) {
                System.out.println(c.toString());
            } else {

            }
        }
    }

    private node tailSentinel = null;
    /** master branch */
    private node master;
    /** all other branches of a commit tree */
    private LinkedList<node> branches;


    public CommitTree(Commit c) {
        // initialize the commitTree
        node p = new node(c.getId(), tailSentinel);
        master = p;
        branches = new LinkedList<>();
    }

    /** add new commit to commit tree master branch
     * actually private
     * */
    public void addMaster(Commit c) {
        // update tree
        master = new node(c.getId(), master);
    }

    /** save tree obj */
    public void saveCommitTree() {
        writeObject(Repository.COMMIT_TREE_FILE, this);
    }

    /** add new commit to commit tree master branch
     * true interface
     * */
    public static void addCommitTree(Commit c) {
        // read previous tree info if exists
        CommitTree ct;
        if (Repository.COMMIT_TREE_FILE.exists()) {
            ct = readObject(Repository.COMMIT_TREE_FILE, CommitTree.class);
        } else {
            // if no previous tree, initialize one
            // TODO: maybe redundant logic
            ct = new CommitTree(c);
            ct.saveCommitTree();
            return;
        }
        // update tree
        ct.addMaster(c);
        // write new tree back
        ct.saveCommitTree();
    }

    public static CommitTree readCommitTree() {
        return readObject(Repository.COMMIT_TREE_FILE, CommitTree.class);
    }

    /** this method will log out all commits starting by head */
    public void display () {
        node p = master;
        while (p != tailSentinel) {
            System.out.println("===");
            p.show();
            p = p.parent;
            System.out.println();
        }
    }

    /** this method will log out every commit in the commits folder */
    public static void displayAll() {
        List<String> folders = plainFilenamesIn(Repository.COMMIT_FOLDER);
        List<String> files;
        if (folders == null) {
            throw new GitletException("sth went wrong in commitTree");
        }
        for (String folder: folders) {
            File path = join(Repository.COMMIT_FOLDER, folder);
            files = plainFilenamesIn(path);
            for (String file: files) {
                File filePath = join(path, file);
                Commit c = readObject(filePath, Commit.class);
                System.out.println("===");
                System.out.println(c);
                System.out.println();
            }
        }
    }

}
