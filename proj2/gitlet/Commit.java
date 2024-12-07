package gitlet;


import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.text.SimpleDateFormat;
import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Zanmo
 */
public class Commit implements Serializable {
    /** the SHA-1 id of the obj */
    private String id;
    /** The message of this Commit. */
    private String message;
    /** the files map of the obj */
    private HashMap<String, String> files;
    /** the parent of the obj */
    private String parent;
    /** the timestamp of the obj */
    private Date timestamp;
    // TODO: what is a merged node? and what makes it merged?
    private boolean merged = false;
    /** if merged, it will have one more parent */
    private String mergedParent = null;

    // maybe more stuff

    public Commit(String msg) {
        if (msg == null || msg.equals("")) {
            throw new GitletException("Please enter a commit message.");
        }

        if (msg.equals("initial commit")) {
            message = msg;
            files = new HashMap<>();
            parent = null;
            timestamp = new Date(0);
        } else {
            // Clone the HEAD commit
            String headId = Head.readHead();
            Commit head = Commit.readCommit(headId);
            files = head.files;
            // modify msg, Par according to parameter
            message = msg;
            parent = head.id;
            timestamp = new Date();
            // reading the staging area
            if (StagingArea.isEmpty()) {
                throw new GitletException("No changes added to the commit.");
            }
            StagingArea sa = StagingArea.readStagingArea();
            // modify files, id
            HashMap<String, String> map = sa.getAddedFiles();
            for (String key : map.keySet()) {
                files.put(key, map.get(key));
            }
            // remove files in the removal
            List<String> ls = sa.getRemoval();
            for (String key: ls) {
                files.remove(key);
            }
        }
        id = sha1(serialize(this));
        // save the persistence
    }

    /* TODO: fill in the rest of this class. */

    /** this method will save the Commit object */
    public void saveCommit() {
        // calculate the hashcode of this instance
        String hashFolder = id.substring(0,2);
        // save it into corresponding folder
        File folder = join(Repository.COMMIT_FOLDER, hashFolder);
        if (!folder.exists()) {
            folder.mkdir();
        }
        File path = join(folder, id);
        writeObject(path, this);
        StagingArea.clearArea();
    }

    /** this method will read the commit instance according to provided id */
    public static Commit readCommit(String id) {
        // calculate the hashcode of this instance
        String hashFolder = id.substring(0,2);
        // TODO: what if file does not exist?
        File path = join(Repository.COMMIT_FOLDER, hashFolder, id);
        Commit c = readObject(path, Commit.class);
        return c;
    }

    public boolean contains(String filename) {
        return files.containsKey(filename);
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        if (!merged) {
            String result = "commit " + id
                    + "\nDate: " + getFormatTime()
                    + "\n" + message;
            return result;
        } else {
            String result = "commit " + id
                    + "\nMerge: " + parent.substring(0,7) + " " + mergedParent.substring(0,7)
                    + "\nDate: " + getFormatTime()
                    + "\nMerged development into master";
            return result;
        }
    }

    public String getFormatTime() {
        SimpleDateFormat sdf =
                new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-08:00"));
        String formattedDate = sdf.format(timestamp);
        formattedDate += " -0800";
        return formattedDate;
    }

    public Map<String, String> getCommitFiles() {
        return files;
    }

    /** this method returns the path of the given filename
     * if there is one in the commit,
     * otherwise return null
     *
     */
    public File getFile(String filename) {
        String id = files.get(filename);
        return Blob.readBlob(id);
    }
}
