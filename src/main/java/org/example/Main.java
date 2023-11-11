package org.example;
public class Main {
    public static void main(String[] args) {
        ParserCSV parser = new ParserCSV();
        parser.readFile("table/basicprogramming1.csv");
        parser.createMapTheme();
        parser.createMapSeminar();
        parser.createMapExercisesAndHomeworks();
        var students = parser.createStudentStorage();

    }
}