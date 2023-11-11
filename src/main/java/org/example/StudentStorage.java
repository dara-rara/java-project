package org.example;

import java.util.HashMap;

public class StudentStorage {
    private final HashMap<String, Student> students = new HashMap<>();

    public void addStudent(Student student) {
        students.put(student.getName(), student);
    }

    public Student getStudent(String name) {
        var student = students.get(name);
        return student;
    }
}
