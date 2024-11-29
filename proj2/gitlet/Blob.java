package gitlet;

import java.io.File;

import static gitlet.Utils.*;

/** this class provides you the methods to act with blobs folder */
public class Blob {

    /** save the file to the blob */
    public static void saveBlob(File file) {
        if (!file.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        // read the file
        String contents = readContentsAsString(file);

        // calculate its SHA-1 code
        String id = sha1(contents);

        // put it in the blob
        int hashcode = id.hashCode() % 256 + 1;
        String hashFolder = Integer.toHexString(hashcode);


        File path = join(Repository.BLOBS_FOLDER, hashFolder, id);
        writeContents(path, contents);
    }

    /** return file according to the id */
    public static File readBlob(String id) {
        // find the file according to id
        int hashcode = id.hashCode() % 256 + 1;
        String hashFolder = Integer.toHexString(hashcode);

        File path = join(Repository.BLOBS_FOLDER, hashFolder, id);
        // return the file

        return path;
        // TODO: return path or contents?
    }

    // TODO: more methods?
}
