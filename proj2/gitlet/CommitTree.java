package gitlet;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.io.File;

import static gitlet.Utils.*;

public class CommitTree implements Serializable {
    private static class node implements Serializable{
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
            System.out.println(c.toString());
        }
    }

    public static class branch implements Serializable {
        private node p;
        private String msg;
        private node latest = null;

        public branch(node theNode, String theMessage) {
            p = theNode;
            msg = theMessage;
        }

        public String getMsg() {
            return msg;
        }

    }

    private node tailSentinel = null;
    /** master branch */
    private branch master;
    /** all other branches of a commit tree */
    private LinkedList<branch> branches;

    private node findNode(String id) {
        boolean found = false;
        LinkedList<node> ls = new LinkedList<>();
        if (master.latest == null) {
            master.latest = master.p;
        }
        ls.addLast(master.latest);
        for (branch b: branches) {
            if (b.latest == null) {
                b.latest = b.p;
            }
            ls.addLast(b.latest);
        }
        node p = null;
        while(!ls.isEmpty()) {
            p = ls.removeFirst();
            if (p == tailSentinel) {
                continue;
            } else if (!p.merged) {
                ls.addLast(p.parent);
            } else if (p.merged) {
                ls.addLast(p.parent);
                ls.addLast(p.mergedParent);
            }
            if (p.id.equals(id)) {
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        } else {
            return p;
        }
        return null;
    }

    public CommitTree(Commit c) {
        // initialize the commitTree
        node p = new node(c.getId(), tailSentinel);
        if (c.isMerged()) {
            p.merged = true;
            p.mergedParent = this.findNode(c.getMergedParent());
        }
        master = new branch(p, "master");
        branches = new LinkedList<>();
    }

    /** add new commit to commit tree master branch
     * actually private
     * */
    public void addMaster(Commit c) {
        // update tree
        master.p = new node(c.getId(), master.p);
        if (c.isMerged()) {
            master.p.merged = true;
            master.p.mergedParent = this.findNode(c.getMergedParent());
        }
    }

    /** save tree obj */
    public void saveCommitTree() {
        writeObject(Repository.COMMIT_TREE_FILE, this);
    }

    private void addNewBranch(String name) {
        for (branch b: branches) {
            if (b.msg.equals(name)) {
                System.out.println("A branch with that name does not exist.");
                System.exit(0);
            }
        }
        branches.addLast(new branch(master.p, name));
    }

    public static void addBranch(String name) {
        CommitTree ct;
        ct = readObject(Repository.COMMIT_TREE_FILE, CommitTree.class);
        ct.addNewBranch(name);
        ct.saveCommitTree();
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

    private void removeBranch(String name) {
        if (master.msg.equals(name)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        boolean found = false;
        for (branch b: branches) {
            if (b.msg.equals(name)) {
                branches.remove(b);
                found = true;
            }
        }
        if (!found) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
    }

    public static void rmBranch(String name) {
        CommitTree ct = readObject(Repository.COMMIT_TREE_FILE, CommitTree.class);
        ct.removeBranch(name);
        ct.saveCommitTree();
    }

    public static CommitTree readCommitTree() {
        return readObject(Repository.COMMIT_TREE_FILE, CommitTree.class);
    }

    /** this method will log out all commits starting by head */
    public void display () {
        node p = master.p;
        while (p != tailSentinel) {
            System.out.println("===");
            p.show();
            p = p.parent;
            System.out.println();
        }
    }

    /** this method will log out every commit in the commits folder */
    public static void displayAll() {
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
                    System.out.println("===");
                    System.out.println(c);
                    System.out.println();
                }
            }
        }
    }

    /** get the master branch */
    public branch getMaster() {
        return master;
    }

    /** get the branches */
    public List<branch> getBranches() {
        return branches;
    }

    /** find the id of the given branch name, if not found return null */
    private String getBranch_(String name) {
        if (name.equals(master.msg)) {
            return master.p.id;
        }
        for (branch b: branches) {
            if (b.msg.equals(name)) {
                return b.p.id;
            }
        }
        return null;
    }

    /** interface */
    public static String getBranch(String branchName) {
        CommitTree ct = CommitTree.readCommitTree();
        return ct.getBranch_(branchName);
    }

    private void changeMaster_(String branchName) {
        branch newBranch = new branch(master.p, master.msg);
        newBranch.latest = master.latest;
        branches.addLast(newBranch);
        for (branch b: branches) {
            if (b.msg.equals(branchName)) {
                master = b;
                branches.remove(b);
                break;
            }
        }
    }

    public static void changeMaster(String branchName) {
        CommitTree ct = CommitTree.readCommitTree();
        ct.changeMaster_(branchName);
        ct.saveCommitTree();
    }

    /** this method move the head of master to the node of id */
    public void moveHead_(String id) {
        boolean found = false;
        LinkedList<node> ls = new LinkedList<>();
        if (master.latest == null) {
            master.latest = master.p;
        }
        ls.addLast(master.latest);
        for (branch b: branches) {
            if (b.latest == null) {
                b.latest = b.p;
            }
            ls.addLast(b.latest);
        }
        node p = null;
        while(!ls.isEmpty()) {
            p = ls.removeFirst();
            if (p == tailSentinel) {
                continue;
            } else if (!p.merged) {
                ls.addLast(p.parent);
            } else if (p.merged) {
                ls.addLast(p.parent);
                ls.addLast(p.mergedParent);
            }
            if (p.id.equals(id)) {
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        } else {
            node newLatest = master.latest;
            master = new branch(p, master.msg);
            master.latest = newLatest;
        }
    }

    /** this method is the class interface of move head */
    public static void moveHead(String id) {
        CommitTree ct = CommitTree.readCommitTree();
        ct.moveHead_(id);
        ct.saveCommitTree();
    }

    private void addToList(node p, LinkedList<node> ls) {
        LinkedList<node> nodesToTravel = new LinkedList<>();
        nodesToTravel.addLast(p);
        while (!nodesToTravel.isEmpty()) {
            node n = nodesToTravel.removeFirst();
            ls.addFirst(n);
            if (n == tailSentinel) {
                continue;
            } else if (!n.merged) {
                nodesToTravel.addLast(n.parent);
            } else {
                nodesToTravel.addLast(n.parent);
                nodesToTravel.addLast(n.mergedParent);
            }
        }
    }

    private node findLatestCommonAncestor_(node n1, node n2) {
        LinkedList<node> ls1 = new LinkedList<node>();
        LinkedList<node> ls2 = new LinkedList<node>();
        CommitTree ct = readCommitTree();
        ct.addToList(n1, ls1);
        ct.addToList(n2, ls2);
        ls1.retainAll(ls2);
        return ls1.getLast();
    }

    /** this method will find the LCA of current commit and given branch
     *  and return its id
     */
    public static String findLatestCommonAncestor(String branchName) {
        CommitTree ct = CommitTree.readCommitTree();
        node n1 = ct.master.p;
        node n2 = ct.getAimedBranch(branchName);
        if (n2 == null) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        return ct.findLatestCommonAncestor_(n1, n2).id;
    }

    /** return the branchName's node, if not found return null */
    private node getAimedBranch (String branchName) {
        for (branch b: branches) {
            if (b.msg.equals(branchName)) {
                return b.p;
            }
        }
        return null;
    }

    public static String getAimedBranchId (String branchName) {
        CommitTree ct = CommitTree.readCommitTree();
        for (branch b: ct.branches) {
            if (b.msg.equals(branchName)) {
                return b.p.id;
            }
        }
        System.out.println("A branch with that name does not exist.");
        System.exit(0);
        return null;
    }

    public static boolean noNeedToCheckout(String branchName) {
        CommitTree ct = CommitTree.readCommitTree();
        if (ct.getMaster().msg.equals(branchName)) {
            return true;
        } else {
            return false;
        }
    }
}
