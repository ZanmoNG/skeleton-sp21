public class CS61BLauncher {
    public static void main(String args){
        CS61BStudent studentOne; // declare new variable of class CS61BStudent
        studentOne = new CS61BStudent(32259); // Instantiate and assign
        CS61BStudent studentTwo = new CS61BStudent(19324); // both

        studentOne.watchLecture(); // instance method can be called on instances

        CS61BStudent.updateGrades();
        // class methods can be called on both instance or class
        studentOne.updateGrades(); // legal, but seems bad idea



    }
}
