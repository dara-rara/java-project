package org.example;

import java.util.HashMap;

public class Theme {
    private final HashMap<String, Integer> exercises;
    private final HashMap<String, Integer> homeworks;
    private final int activities;
    private final int seminars;
    private final String nameTheme;


    public String getNameTheme() {
        return nameTheme;
    }

    public Theme(HashMap<String, Integer> exercises, HashMap<String, Integer> homeworks, int activities, int seminars, String nameTheme) {
        this.exercises = exercises;
        this.homeworks = homeworks;
        this.activities = activities;
        this.seminars = seminars;
        this.nameTheme = nameTheme;
    }

    public int getSumPointsExercises() {
        var sum = 0;
        for (var exercise : exercises.entrySet()) {
            sum += exercise.getValue();
        }
        return sum;
    }

    public int getSumPointsHomeworks() {
        var sum = 0;
        for (var homework : homeworks.entrySet()) {
            sum += homework.getValue();
        }
        return sum;
    }

    public int getAllPoints() {
        return getSumPointsExercises() + getSumPointsHomeworks() + activities + seminars;
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        builder.append("\n" + nameTheme + "\n");
        for (var exercise : exercises.entrySet())
            builder.append(exercise.getKey() + "-" + exercise.getValue() + ";");
        for (var homework : homeworks.entrySet())
            builder.append(homework.getKey() + "-" + homework.getValue() + ";");
        builder.append("\nАкт - " + activities + ";");
        builder.append("\nСем - " + seminars + ";");
        return builder.toString();
    }
}

