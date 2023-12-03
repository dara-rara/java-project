package org.example;

import java.util.HashMap;

public class Student {
    private final String name;
    private final String group;
    private final String city;
    private HashMap<String, Theme> themes = new HashMap<>();

    public Student(String name, String group, String city) {
        this.name = name;
        this.group = group;
        this.city = city;
    }

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

    @Override
    public String toString() {
        var builder = new StringBuilder();
        builder.append("\n\n" + name + " - " + group + " - " + city + "\n\n");
        for (var theme : themes.values())
            builder.append(theme.toString());
        return builder.toString();
    }
}
