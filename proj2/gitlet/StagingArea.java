package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static gitlet.Utils.*;

public class StagingArea implements Serializable {
    /** the map from String file names to their code */
    private HashMap<String, String> addedFiles;
    private LinkedList<String> removal;

    // TODO: setup repository in Repository class

    public StagingArea() {
        // if exists, read previous stagingArea
        if (Repository.STAGING_FILE.exists()) {
            this.addedFiles = readObject(Repository.STAGING_FILE, StagingArea.class).getAddedFiles();
        }
        else { // else create new
            addedFiles = new HashMap<>();
        }
        removal = new LinkedList<>();
    }

    /** this method save this instance to STAGING_FILE */
    public void saveStagingArea(File file, String filename) {
        addedFiles.put(filename, sha1(file));

        // TODO: if the file is identical to the one in current commit, delete it

        writeObject(Repository.STAGING_FILE, this);

    }

    public void setRemoval(String filename) {
        removal.addLast(filename);

        writeObject(Repository.STAGING_FILE, this);
    }

    public List<String> getRemoval() {
        return removal;
    }

    /** this method will clear the staging area */
    public static void clearArea() {
        Repository.STAGING_FILE.delete();
    }

    public HashMap<String, String> getAddedFiles() {
        return addedFiles;
    }

    /** this method reads the staging area and returns the map of file to id */
    public static StagingArea readStagingArea() {
        StagingArea r = readObject(Repository.STAGING_FILE, StagingArea.class);
        return r;
    }

    public static boolean isEmpty() {
        return !Repository.STAGING_FILE.exists();
    }


    // TODO more methods?
    // maybe isEmpty
}
