package org.example;

import java.util.HashMap;

public class StudentStorage {
    private HashMap<String, Student> students = new HashMap<>();

    public void addStudent(Student student) {
        students.put(student.getName(), student);
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
}
