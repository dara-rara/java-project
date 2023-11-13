package org.example;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

public class ParserCSV {
    private List<String[]> table;
    private HashMap<String, Integer> mapTheme;
    private HashMap<String, Integer> mapSeminar;
    private HashMap<String, HashMap<String, Integer>> mapExercises;
    private HashMap<String, HashMap<String, Integer>> mapHomeworks;


    public void readFile(String file) {
        CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();
        try (CSVReader reader = new CSVReaderBuilder(
                new InputStreamReader(new FileInputStream(file),"UTF-8"))
                .withCSVParser(csvParser)
                .build()) {
            table = reader.readAll();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void createMapTheme() {
        var firstLine = table.get(0);
        mapTheme = new HashMap<>();
        for (var i = 8; i < firstLine.length; i++) {
            if (firstLine[i] != null && !firstLine[i].equals("")) {
                mapTheme.put(firstLine[i], i);
            }
        }
    }

    public void createMapSeminar() {
        var secondLine = table.get(1);
        mapSeminar = new HashMap<>();
        for (var nameTheme : mapTheme.entrySet()) {
            for (var i = nameTheme.getValue(); i < secondLine.length; i++) {
                if (secondLine[i].equals("Сем")) {
                    mapSeminar.put(nameTheme.getKey(), i);
                    break;
                }
            }
        }
    }

    public void createMapExercisesAndHomeworks() {
        mapExercises = new HashMap<>();
        mapHomeworks = new HashMap<>();
        var secondLine = table.get(1);
        for (var nameTheme : mapTheme.entrySet()) {
            mapExercises.put(nameTheme.getKey(), new HashMap<>());
            mapHomeworks.put(nameTheme.getKey(), new HashMap<>());
            for (var i = nameTheme.getValue(); i < secondLine.length; i++) {
                if (secondLine[i].equals("Сем")) {
                    break;
                }
                if (secondLine[i].contains("Упр:")) {
                    var temp = mapExercises.get(nameTheme.getKey());
                    temp.put(secondLine[i], i);
                }
                if (secondLine[i].contains("ДЗ:")) {
                    var temp = mapHomeworks.get(nameTheme.getKey());
                    temp.put(secondLine[i], i);
                }
            }
        }
    }

    public Student createStudent(String[] data, HashMap<String, String> cities) {
        String city = "город не указан";
        if (cities.containsKey(data[0])) {
            city = cities.get(data[0]);
        }
        var student = new Student(data[0], data[1], city);
        for (var nameTheme : mapTheme.entrySet()) {
            var themeExercises = new HashMap<String, Integer>();
            var themeHomeworks = new HashMap<String, Integer>();
            var exercises = mapExercises.get(nameTheme.getKey());
            for (var exercise : exercises.entrySet()) {
                themeExercises.put(exercise.getKey(), Integer.parseInt(data[exercise.getValue()]));
            }
            var homeworks = mapHomeworks.get(nameTheme.getKey());
            for (var homework : homeworks.entrySet()) {
                themeHomeworks.put(homework.getKey(), Integer.parseInt(data[homework.getValue()]));
            }

            var theme = new Theme(themeExercises, themeHomeworks,
                    Integer.parseInt(data[nameTheme.getValue()]),
                    Integer.parseInt(data[mapSeminar.get(nameTheme.getKey())]), nameTheme.getKey());
            student.addTheme(theme);
        }
        return student;
    }

    public StudentStorage createStudentStorage() {
        var studentStorage = new StudentStorage();
        var vk = new ObjectVK();
        var cities = vk.createMapCity();
        for (var i = 3; i < table.size(); i++) {
            studentStorage.addStudent(createStudent(table.get(i), cities));
        }
        return studentStorage;
    }
}