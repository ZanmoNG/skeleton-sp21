package gitlet;

import java.io.File;
import static gitlet.Utils.*;

/** this class will provide methods fr you to act with head repository */
public class Head {

    // TODO: setup repository?

    public static void updateHead(String id) {
        writeContents(Repository.HEAD_FILE, id);
    }

    public static String readHead() {
        return readContentsAsString(Repository.HEAD_FILE);
    }

    public static Commit readHeadAsCommit() {
        String headId = readHead();
        return Commit.readCommit(headId);
    }

}
