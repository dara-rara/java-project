package org.example;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class Main {
    public static void main(String[] args) throws ClientException, ApiException {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        var parser = new ParserCSV();
        parser.readFile("table/basicprogramming1.csv");
        parser.createMapTheme();
        parser.createMapSeminar();
        parser.createMapExercisesAndHomeworks();
        var students = parser.createStudentStorage();
        //students.listStudents();
        System.out.println(students.getStudent("Глебова Дарья"));
    }
}