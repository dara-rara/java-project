package org.example.parser;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.example.models.Student;
import org.example.models.StudentStorage;
import org.example.models.Theme;
import org.example.vkAPI.ObjectVK;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

public class ParserCSV {
    private static List<String[]> table;
    private static HashMap<String, Integer> mapTheme;
    private static HashMap<String, Integer> mapSeminar;
    private static HashMap<String, HashMap<String, Integer>> mapExercises;
    private static HashMap<String, HashMap<String, Integer>> mapHomeworks;
    private static HashMap<String, Integer> numberThemes;

    public static void readFile(String file) {
        CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();
        try (CSVReader reader = new CSVReaderBuilder(
                new InputStreamReader(new FileInputStream(file), "UTF-8"))
                .withCSVParser(csvParser)
                .build()) {
            table = reader.readAll();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createMapTheme() {
        var firstLine = table.get(0);
        mapTheme = new HashMap<>();
        numberThemes = new HashMap<>();
        for (var i = 8; i < firstLine.length; i++) {
            if (firstLine[i] != null && !firstLine[i].equals("")) {
                mapTheme.put(firstLine[i], i);
                numberThemes.put(firstLine[i], i);
            }
        }
    }

    public static void createMapSeminar() {
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

    public static void createMapExercisesAndHomeworks() {
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

    public static Student createStudent(Integer id, String[] data, HashMap<String, String> cities) {
        String city = "";
        if (cities.containsKey(data[0])) {
            city = cities.get(data[0]);
        }
        var student = new Student(id, data[0], data[1], city);
        student.setThemes(createThemes(data));
        return student;
    }

    public static StudentStorage createStudentStorage() {
        var studentStorage = new StudentStorage();
        studentStorage.setThemesMax(createThemes(table.get(2)));
        studentStorage.setMapActivities(mapTheme);
        studentStorage.setMapSeminar(mapSeminar);
        studentStorage.setMapExercises(mapExercises);
        studentStorage.setMapHomeworks(mapHomeworks);

        var vk = new ObjectVK();
        ObjectVK.createMapCity();
        var cities = ObjectVK.getMapCity();
        studentStorage.setCitiesNumber(ObjectVK.getCitiesNumbers());
        studentStorage.setThemesNumber(numberThemes);
        for (var i = 3; i < table.size(); i++) {
            studentStorage.addStudent(createStudent(i - 3, table.get(i), cities));
        }
        return studentStorage;
    }

    private static HashMap<String, Theme> createThemes(String[] data) {
        HashMap<String, Theme> themes = new HashMap<>();
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
            themes.put(theme.getNameTheme(), theme);
        }
        return themes;
    }
}