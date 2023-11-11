package org.example;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class Main {
    public static void main(String[] args) {
        ParserCSV parser = new ParserCSV();
        parser.readFile("table/basicprogramming1.csv");
        parser.createMapTheme();
        parser.createMapSeminar();
        parser.createMapExercisesAndHomeworks();
        var students = parser.createStudentStorage();
        var student = students.getStudent("Новикова Мария");
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(student.toString());
    }
}