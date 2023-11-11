package org.example;

import java.util.HashMap;

public class Student {
    private final String name;
    private final String group;
    private final HashMap<String, Theme> themes = new HashMap<>();

    public Student(String name, String group) {
        this.name = name;
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }


    public void addTheme(Theme theme) {
        themes.put(theme.getNameTheme(), theme);
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        builder.append(name + "-" + group + "\n");
        for (var theme : themes.values())
            builder.append(theme.toString());
        return builder.toString();
    }
}
