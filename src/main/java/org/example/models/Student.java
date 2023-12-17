package org.example.models;

import java.util.HashMap;

public class Student {
    private final Integer studentID;
    private final String name;
    private final String group;
    private final String city;

    private HashMap<String, Theme> themes = new HashMap<>();

    public Student(Integer studentID, String name, String group, String city) {
        this.studentID = studentID;
        this.name = name;
        this.group = group;
        this.city = city;
    }

    public Integer getStudentID() { return studentID; }
    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public String getCity() {
        return city;
    }

    public void setThemes(HashMap<String, Theme> themes) {
        this.themes = themes;
    }

    public HashMap<String, Theme> getThemes() {
        return themes;
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        builder.append("\n\n" + name + " - " + group + " - " + city + "\n\n");
        for (var theme : themes.values())
            builder.append(theme.toString());
        return builder.toString();
    }
}
