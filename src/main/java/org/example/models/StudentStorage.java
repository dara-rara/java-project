package org.example.models;

import java.util.HashMap;

public class StudentStorage {
    private static HashMap<String, Student> students = new HashMap<>();
    private static HashMap<String, Theme> themesMax = new HashMap<>();
    private static HashMap<String, Integer> citiesNumber;
    private static HashMap<String, Integer> themesNumber;
    private static HashMap<String, Integer> typeTask = new HashMap<>() {{
        put("Упр", 0);
        put("ДЗ", 1);
        put("Сем", 2);
        put("Акт", 3);
    }};
    private static HashMap<String, Integer> mapActivities;
    private static HashMap<String, Integer> mapSeminar;
    private static HashMap<String, HashMap<String, Integer>> mapExercises;
    private static HashMap<String, HashMap<String, Integer>> mapHomeworks;


    public void addStudent(Student student) {
        students.put(student.getName(), student);
    }

    public static HashMap<String, Theme> getThemesMax() {
        return themesMax;
    }

    public void setThemesMax(HashMap<String, Theme> themesMax) {
        this.themesMax = themesMax;
    }

    public static Student getStudent(String name) {
        var student = students.get(name);
        return student;
    }

    public static void listStudents() {
        for (var student : students.values()) {
            System.out.print(student.toString());
        }
    }

    public void setCitiesNumber(HashMap<String, Integer> citiesNumber) {
        this.citiesNumber = citiesNumber;
    }

    public void setThemesNumber(HashMap<String, Integer> themesNumber) {
        this.themesNumber = themesNumber;
    }

    public void setMapActivities(HashMap<String, Integer> mapActivities) {
        this.mapActivities = mapActivities;
    }

    public static HashMap<String, Integer> getMapActivities() {
        return mapActivities;
    }

    public void setMapSeminar(HashMap<String, Integer> mapSeminar) {
        this.mapSeminar = mapSeminar;
    }

    public static HashMap<String, Integer> getMapSeminar() {
        return mapSeminar;
    }

    public void setMapExercises(HashMap<String, HashMap<String, Integer>> mapExercises) {
        this.mapExercises = mapExercises;
    }

    public static HashMap<String, HashMap<String, Integer>> getMapExercises() {
        return mapExercises;
    }

    public void setMapHomeworks(HashMap<String, HashMap<String, Integer>> mapHomeworks) {
        this.mapHomeworks = mapHomeworks;
    }

    public static HashMap<String, HashMap<String, Integer>> getMapHomeworks() {
        return mapHomeworks;
    }

    public static HashMap<String, Integer> getCitiesNumber() {
        return citiesNumber;
    }

    public static HashMap<String, Student> getStudents() {
        return students;
    }

    public static HashMap<String, Integer> getTypeTask() {
        return typeTask;
    }

    public static HashMap<String, Integer> getThemesNumber() {
        return themesNumber;
    }
}
