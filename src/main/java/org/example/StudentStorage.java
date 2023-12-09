package org.example;

import java.util.HashMap;

public class StudentStorage {
    private HashMap<String, Student> students = new HashMap<>();
    private HashMap<String, Theme> themesMax = new HashMap<>();
    private HashMap<String, Integer> citiesNumber;
    private HashMap<String, Integer> themesNumber;
    private HashMap<String, Integer> typeTask = new HashMap<>() {{
        put("Упр", 0);
        put("ДЗ", 1);
        put("Сем", 2);
        put("Акт", 3);
    }};
    private HashMap<String, Integer> mapActivities;
    private HashMap<String, Integer> mapSeminar;
    private HashMap<String, HashMap<String, Integer>> mapExercises;
    private HashMap<String, HashMap<String, Integer>> mapHomeworks;


    public void addStudent(Student student) {
        students.put(student.getName(), student);
    }

    public HashMap<String, Theme> getThemesMax() {
        return themesMax;
    }

    public void setThemesMax(HashMap<String, Theme> themesMax) {
        this.themesMax = themesMax;
    }

    public Student getStudent(String name) {
        var student = students.get(name);
        return student;
    }

    public void listStudents() {
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

    public HashMap<String, Integer> getMapActivities() {
        return mapActivities;
    }

    public void setMapSeminar(HashMap<String, Integer> mapSeminar) {
        this.mapSeminar = mapSeminar;
    }

    public HashMap<String, Integer> getMapSeminar() {
        return mapSeminar;
    }

    public void setMapExercises(HashMap<String, HashMap<String, Integer>> mapExercises) {
        this.mapExercises = mapExercises;
    }

    public HashMap<String, HashMap<String, Integer>> getMapExercises() {
        return mapExercises;
    }

    public void setMapHomeworks(HashMap<String, HashMap<String, Integer>> mapHomeworks) {
        this.mapHomeworks = mapHomeworks;
    }

    public HashMap<String, HashMap<String, Integer>> getMapHomeworks() {
        return mapHomeworks;
    }

    public HashMap<String, Integer> getCitiesNumber() {
        return citiesNumber;
    }

    public HashMap<String, Student> getStudents() {
        return students;
    }

    public HashMap<String, Integer> getTypeTask() {
        return typeTask;
    }

    public HashMap<String, Integer> getThemesNumber() {
        return themesNumber;
    }
}
