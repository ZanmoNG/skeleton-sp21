# Gitlet Design Document

**Name**: Zanmo

## Classes and Data Structures

### Class MainHelper
this class provides helper method for 
Main class

### Class Repository

this class serves as  a bridge between MainHelper
and other specific repository class(Commit, 
staging area...)

#### Fields
its fields are actually the path of
inner structure for building persistence

    * CWD
    * GITLET_DIR
    * STAGING_FOLDER
    * STAGING_FILE
    * OBJ_FOLDER
    * BLOBS_FOLDER
    * COMMIT_FOLDER
    * HEAD_FILE

#### Methods

    * setupRepository

### Class Commit
this class represents the commit information
and provides methods to save and read them.

#### Fields
its fields are essential information of a commit

    * id - the SHA-1 code of the obj 
    * message - the String message provided by user
    * files - a map from String files name to its id
    * parent - the String id of its parent commit
    * timestamp - the creating time

#### methods
help build persistence and read commit files

    * saveCommit
    * readCommit

### Class StagingArea
represents a staging area in .gitlet/staging

#### Fields

    * addedFiles - the map of all added files

#### Methods
the ways programs interact with staging area

    * getAddedFiles
    * saveStagingArea - add a file to staging area
    * clearArea
    * readStagingArea - return the map
    * isEmpty

### Class Blob
this class provides you the methods 
to act with blobs folder

#### Fields
no fields.

#### Methods
    
    * saveBlob - save the file to the blob
    * readBlob - read the file according to provided id


### Class Head
to be done...



## Algorithms

## Persistence

