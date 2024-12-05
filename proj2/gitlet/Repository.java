package gitlet;

import java.io.File;
import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The staging area directory*/
    public static final File STAGING_FOLDER = join(GITLET_DIR, "staging");
    /** The staging files txt ... may be not listed here. */
    public static final File STAGING_FILE = join(STAGING_FOLDER, "stagingArea");
    /** The obj directory, which contains commits and blobs obj */
    public static final File OBJ_FOLDER = join(GITLET_DIR, "obj");
    /** The blobs directory */
    public static final File BLOBS_FOLDER = join(OBJ_FOLDER, "blobs");
    /** The commits directory */
    public static final File COMMIT_FOLDER = join(OBJ_FOLDER, "commits");
    /** The head directory */
    public static final File HEAD_FILE = join(GITLET_DIR, "head");
    /** The commit tree file */
    public static final File COMMIT_TREE_FILE = join(OBJ_FOLDER, "commitTree");

    // TODO:  more variables


    public static void setupRepository() {
        GITLET_DIR.mkdir();
        // staging
        STAGING_FOLDER.mkdir();
        // obj
        OBJ_FOLDER.mkdir();
        BLOBS_FOLDER.mkdir();
        COMMIT_FOLDER.mkdir();
        // head

    }





    /* TODO: fill in the rest of this class. */
}
