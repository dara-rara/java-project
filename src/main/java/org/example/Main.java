package org.example;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class Main {
    public static void main(String[] args) throws Exception {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        var parser = new ParserCSV();
        parser.readFile("table/basicprogramming.csv");
        parser.createMapTheme();
        parser.createMapSeminar();
        parser.createMapExercisesAndHomeworks();
        var students = parser.createStudentStorage();
//        students.listStudents();
        ManagerDB.Conn();
        ManagerDB.CreateDB();
    }
}