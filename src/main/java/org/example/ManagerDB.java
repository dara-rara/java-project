package org.example;

import java.sql.*;
import java.util.HashMap;

public class ManagerDB {
    private static Connection conn;
    public static Statement statmt;

    public static void Conn() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:table/basicprogramming.db");
        statmt = conn.createStatement();

        System.out.println("База подключена!");
    }

    public static boolean isConnected() {
        return conn != null && statmt != null;
    }

    public static void CreateDB(StudentStorage storage) throws ClassNotFoundException, SQLException {
        statmt.execute("PRAGMA foreign_keys=on");
        CreateTownsTable();
        CreateStudentsTable();
        CreateTypeTaskTable();
        CreateThemesTable();
        CreateTasksTable();
        CreateJournalTable();
        WriteTowns(storage.getCitiesNumber());
        WriteStudents(storage.getStudents(), storage.getCitiesNumber());
        WriteTypeTask(storage.getTypeTask());
        WriteThemes(storage.getThemesNumber());
        WriteTasks(storage);
        WriteJournal(storage);
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
//        ResultSet resSet = statmt.executeQuery(
//                "SELECT * FROM Towns;"
//        );
//        if (resSet.isClosed()) {
//            return;
//        }
//
//        var strB = new StringBuilder();
//        strB.append("Имя          Фамилия\n-----------------------\n");
//        while (resSet.next()) {
//            var firstname = resSet.getInt("town_id");
//            var lastname = resSet.getString("town_name");
//            strB.append(String.format("%d  %s\n", firstname, lastname));
//        }
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

    private static void WriteTasks(StudentStorage storage) throws SQLException {
        var command =  "INSERT INTO 'Tasks' ('task_id', 'themes_id', 'type_task_id', 'task_name', 'points_max') " +
                "VALUES (?, ?, ?, ?, ?); ";
        var prStatmt = conn.prepareStatement(command);

        var tasksAct = storage.getMapActivities();
        for (var task : tasksAct.entrySet()) {
            prStatmt.setInt(1, task.getValue());
            prStatmt.setInt(2, storage.getThemesNumber().get(task.getKey()));
            prStatmt.setInt(3, storage.getTypeTask().get("Акт"));
            prStatmt.setString(4, "Акт");
            prStatmt.setInt(5, storage.getThemesMax().get(task.getKey()).getActivities());
            prStatmt.executeUpdate();
        }

        var tasksSem = storage.getMapSeminar();
        for (var task : tasksSem.entrySet()) {
            prStatmt.setInt(1, task.getValue());
            prStatmt.setInt(2, storage.getThemesNumber().get(task.getKey()));
            prStatmt.setInt(3, storage.getTypeTask().get("Сем"));
            prStatmt.setString(4, "Сем");
            prStatmt.setInt(5, storage.getThemesMax().get(task.getKey()).getSeminars());
            prStatmt.executeUpdate();
        }

        var tasksEx = storage.getMapExercises();
        for (var theme : tasksEx.entrySet()) {
            for (var task : theme.getValue().entrySet()) {
                prStatmt.setInt(1, task.getValue());
                prStatmt.setInt(2, storage.getThemesNumber().get(theme.getKey()));
                prStatmt.setInt(3, storage.getTypeTask().get("Упр"));
                prStatmt.setString(4, task.getKey());
                prStatmt.setInt(5,
                        storage.getThemesMax().get(theme.getKey()).getExercises().get(task.getKey()));
                prStatmt.executeUpdate();
            }
        }

        var tasksHom = storage.getMapHomeworks();
        for (var theme : tasksHom.entrySet()) {
            for (var task : theme.getValue().entrySet()) {
                prStatmt.setInt(1, task.getValue());
                prStatmt.setInt(2, storage.getThemesNumber().get(theme.getKey()));
                prStatmt.setInt(3, storage.getTypeTask().get("ДЗ"));
                prStatmt.setString(4, task.getKey());
                prStatmt.setInt(5,
                        storage.getThemesMax().get(theme.getKey()).getHomeworks().get(task.getKey()));
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

    private static void WriteJournal(StudentStorage storage) throws SQLException {
        var command = "INSERT INTO 'Journal' ('student_id', 'task_id', 'points') VALUES (?, ?, ?)";
        var prStatmt = conn.prepareStatement(command);

        var students = storage.getStudents();
        for (var student : students.values()) {
            var tasksAct = storage.getMapActivities();
            for (var task : tasksAct.entrySet()) {
                prStatmt.setInt(1, student.getStudentID());
                prStatmt.setInt(2, task.getValue());
                prStatmt.setInt(3, student.getThemes().get(task.getKey()).getActivities());
                prStatmt.executeUpdate();
            }

            var tasksSem = storage.getMapSeminar();
            for (var task : tasksSem.entrySet()) {
                prStatmt.setInt(1, student.getStudentID());
                prStatmt.setInt(2, task.getValue());
                prStatmt.setInt(3, student.getThemes().get(task.getKey()).getSeminars());
                prStatmt.executeUpdate();
            }

            var tasksEx = storage.getMapExercises();
            for (var theme : tasksEx.entrySet()) {
                for (var task : theme.getValue().entrySet()) {
                    prStatmt.setInt(1, student.getStudentID());
                    prStatmt.setInt(2, task.getValue());
                    prStatmt.setInt(3, student.getThemes().get(theme.getKey()).getExercises().get(task.getKey()));
                    prStatmt.executeUpdate();
                }
            }

            var tasksHom = storage.getMapHomeworks();
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
