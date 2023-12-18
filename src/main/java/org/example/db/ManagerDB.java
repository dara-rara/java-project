package org.example.db;

import org.example.models.Student;
import org.example.models.StudentStorage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class ManagerDB {
    private static Connection conn;
    private static Statement statmt;

    public static Statement getStatmt () {
        return statmt;
    }
    public static Connection getConn () { return conn; }

    public static void Conn() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:table/basicprogramming.db");
        statmt = conn.createStatement();
    }

    public static void CloseDB() throws ClassNotFoundException, SQLException {
        statmt.close();
        conn.close();
    }

    public static boolean isConnected() {
        return conn != null && statmt != null;
    }

    public static void CreateDB() throws ClassNotFoundException, SQLException {
        statmt.execute("PRAGMA foreign_keys=on");
        CreateTownsTable();
        CreateStudentsTable();
        CreateTypeTaskTable();
        CreateThemesTable();
        CreateTasksTable();
        CreateJournalTable();
        WriteTowns(StudentStorage.getCitiesNumber());
        WriteStudents(StudentStorage.getStudents(), StudentStorage.getCitiesNumber());
        WriteTypeTask(StudentStorage.getTypeTask());
        WriteThemes(StudentStorage.getThemesNumber());
        WriteTasks();
        WriteJournal();
    }

    private static void CreateTownsTable() throws SQLException {
        statmt.execute(
                "CREATE TABLE if not exists 'Towns' " +
                        "('town_id' INTEGER PRIMARY KEY, " +
                        "'town_name' text); "
        );
    }

    private static void WriteTowns(HashMap<String, Integer> citiesNumber) throws SQLException {
        var command = "INSERT INTO 'Towns' ('town_id', 'town_name')VALUES (?, ?)";
        var prStatmt = conn.prepareStatement(command);
        for (var city : citiesNumber.entrySet()) {
            prStatmt.setInt(1, city.getValue());
            prStatmt.setString(2, city.getKey());
            prStatmt.executeUpdate();
        }
    }

    private static void CreateStudentsTable() throws SQLException {
        statmt.execute(
                "CREATE TABLE if not exists 'Students' " +
                        "('student_id' INTEGER PRIMARY KEY, " +
                        "'student_name' text, " +
                        "'group' text, " +
                        "'town_id' INTEGER, " +
                        "FOREIGN KEY (town_id) REFERENCES Towns (town_id));"
        );
    }

    private static void WriteStudents(HashMap<String, Student> students, HashMap<String, Integer> citiesNumber) throws SQLException {
        var command = "INSERT INTO 'Students' ('student_id', 'student_name', 'group', 'town_id') VALUES (?, ?, ?, ?)";
        var prStatmt = conn.prepareStatement(command);
        for (var student : students.values()) {
            prStatmt.setInt(1, student.getStudentID());
            prStatmt.setString(2, student.getName());
            prStatmt.setString(3, student.getGroup());
            prStatmt.setInt(4, citiesNumber.get(student.getCity()));
            prStatmt.executeUpdate();
        }
    }

    private static void CreateTypeTaskTable() throws SQLException {
        statmt.execute(
                "CREATE TABLE if not exists 'TypeTask' " +
                        "('type_task_id' INTEGER PRIMARY KEY, " +
                        "'type_task_name' text); "
        );
    }

    private static void WriteTypeTask(HashMap<String, Integer> typeTask) throws SQLException {
        var command = "INSERT INTO 'TypeTask' ('type_task_id', 'type_task_name') VALUES (?, ?)";
        var prStatmt = conn.prepareStatement(command);
        for (var type : typeTask.entrySet()) {
            prStatmt.setInt(1, type.getValue());
            prStatmt.setString(2, type.getKey());
            prStatmt.executeUpdate();
        }
    }

    private static void CreateThemesTable() throws SQLException {
        statmt.execute(
                "CREATE TABLE if not exists 'Themes' " +
                        "('themes_id' INTEGER PRIMARY KEY, " +
                        "'themes_name' text); "
        );
    }

    private static void WriteThemes(HashMap<String, Integer> themes) throws SQLException {
        var command = "INSERT INTO 'Themes' ('themes_id', 'themes_name') VALUES (?, ?)";
        var prStatmt = conn.prepareStatement(command);
        for (var theme : themes.entrySet()) {
            prStatmt.setInt(1, theme.getValue());
            prStatmt.setString(2, theme.getKey());
            prStatmt.executeUpdate();
        }
    }

    private static void CreateTasksTable() throws SQLException {
        statmt.execute(
                "CREATE TABLE if not exists 'Tasks' " +
                        "('task_id' INTEGER PRIMARY KEY, " +
                        "'themes_id' INTEGER, " +
                        "'type_task_id' INTEGER, " +
                        "'task_name' text, " +
                        "'points_max' INTEGER, " +
                        "FOREIGN KEY (themes_id) REFERENCES Themes (themes_id), " +
                        "FOREIGN KEY (type_task_id) REFERENCES TypeTask (type_task_id));"
        );
    }

    private static void WriteTasks() throws SQLException {
        var command = "INSERT INTO 'Tasks' ('task_id', 'themes_id', 'type_task_id', 'task_name', 'points_max') " +
                "VALUES (?, ?, ?, ?, ?); ";
        var prStatmt = conn.prepareStatement(command);

        var tasksAct = StudentStorage.getMapActivities();
        for (var task : tasksAct.entrySet()) {
            prStatmt.setInt(1, task.getValue());
            prStatmt.setInt(2, StudentStorage.getThemesNumber().get(task.getKey()));
            prStatmt.setInt(3, StudentStorage.getTypeTask().get("Акт"));
            prStatmt.setString(4, "Акт");
            prStatmt.setInt(5, StudentStorage.getThemesMax().get(task.getKey()).getActivities());
            prStatmt.executeUpdate();
        }

        var tasksSem = StudentStorage.getMapSeminar();
        for (var task : tasksSem.entrySet()) {
            prStatmt.setInt(1, task.getValue());
            prStatmt.setInt(2, StudentStorage.getThemesNumber().get(task.getKey()));
            prStatmt.setInt(3, StudentStorage.getTypeTask().get("Сем"));
            prStatmt.setString(4, "Сем");
            prStatmt.setInt(5, StudentStorage.getThemesMax().get(task.getKey()).getSeminars());
            prStatmt.executeUpdate();
        }

        var tasksEx = StudentStorage.getMapExercises();
        for (var theme : tasksEx.entrySet()) {
            for (var task : theme.getValue().entrySet()) {
                prStatmt.setInt(1, task.getValue());
                prStatmt.setInt(2, StudentStorage.getThemesNumber().get(theme.getKey()));
                prStatmt.setInt(3, StudentStorage.getTypeTask().get("Упр"));
                prStatmt.setString(4, task.getKey());
                prStatmt.setInt(5,
                        StudentStorage.getThemesMax().get(theme.getKey()).getExercises().get(task.getKey()));
                prStatmt.executeUpdate();
            }
        }

        var tasksHom = StudentStorage.getMapHomeworks();
        for (var theme : tasksHom.entrySet()) {
            for (var task : theme.getValue().entrySet()) {
                prStatmt.setInt(1, task.getValue());
                prStatmt.setInt(2, StudentStorage.getThemesNumber().get(theme.getKey()));
                prStatmt.setInt(3, StudentStorage.getTypeTask().get("ДЗ"));
                prStatmt.setString(4, task.getKey());
                prStatmt.setInt(5,
                        StudentStorage.getThemesMax().get(theme.getKey()).getHomeworks().get(task.getKey()));
                prStatmt.executeUpdate();
            }
        }
    }

    private static void CreateJournalTable() throws SQLException {
        statmt.execute(
                "CREATE TABLE if not exists 'Journal' " +
                        "('id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "'student_id' INTEGER, " +
                        "'task_id' INTEGER, " +
                        "'points' INTEGER, " +
                        "FOREIGN KEY (student_id) REFERENCES Students (student_id), " +
                        "FOREIGN KEY (task_id) REFERENCES Tasks (task_id));"
        );
    }

    private static void WriteJournal() throws SQLException {
        var command = "INSERT INTO 'Journal' ('student_id', 'task_id', 'points') VALUES (?, ?, ?)";
        var prStatmt = conn.prepareStatement(command);

        var students = StudentStorage.getStudents();
        for (var student : students.values()) {
            var tasksAct = StudentStorage.getMapActivities();
            for (var task : tasksAct.entrySet()) {
                prStatmt.setInt(1, student.getStudentID());
                prStatmt.setInt(2, task.getValue());
                prStatmt.setInt(3, student.getThemes().get(task.getKey()).getActivities());
                prStatmt.executeUpdate();
            }

            var tasksSem = StudentStorage.getMapSeminar();
            for (var task : tasksSem.entrySet()) {
                prStatmt.setInt(1, student.getStudentID());
                prStatmt.setInt(2, task.getValue());
                prStatmt.setInt(3, student.getThemes().get(task.getKey()).getSeminars());
                prStatmt.executeUpdate();
            }

            var tasksEx = StudentStorage.getMapExercises();
            for (var theme : tasksEx.entrySet()) {
                for (var task : theme.getValue().entrySet()) {
                    prStatmt.setInt(1, student.getStudentID());
                    prStatmt.setInt(2, task.getValue());
                    prStatmt.setInt(3, student.getThemes().get(theme.getKey()).getExercises().get(task.getKey()));
                    prStatmt.executeUpdate();
                }
            }

            var tasksHom = StudentStorage.getMapHomeworks();
            for (var theme : tasksHom.entrySet()) {
                for (var task : theme.getValue().entrySet()) {
                    prStatmt.setInt(1, student.getStudentID());
                    prStatmt.setInt(2, task.getValue());
                    prStatmt.setInt(3, student.getThemes().get(theme.getKey()).getHomeworks().get(task.getKey()));
                    prStatmt.executeUpdate();
                }
            }
        }
    }
}
