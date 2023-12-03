package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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

    public static void CreateDB() throws ClassNotFoundException, SQLException {
        statmt.execute("PRAGMA foreign_keys=on");
        CreateTownsTable();
        CreateStudentsTable();
        CreateTypeTaskTable();
        CreateThemesTable();
        CreateTasksTable();
        CreateJournalTable();
        System.out.println("База данных успешно создана!");
    }

    private static void CreateTownsTable() throws SQLException {
        statmt.execute(
                "CREATE TABLE if not exists 'Towns' " +
                        "('town_id' INTEGER PRIMARY KEY, " +
                        "'town_name' text); "
        );

        System.out.println("Таблица Towns создана или уже существует.");
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

        System.out.println("Таблица Students создана или уже существует.");
    }

    private static void CreateTypeTaskTable() throws SQLException {
        statmt.execute(
                "CREATE TABLE if not exists 'TypeTask' " +
                        "('type_task_id' INTEGER PRIMARY KEY, " +
                        "'type_task_name' text); "
        );

        System.out.println("Таблица TypeTask создана или уже существует.");
    }

    private static void CreateThemesTable() throws SQLException {
        statmt.execute(
                "CREATE TABLE if not exists 'Themes' " +
                        "('themes_id' INTEGER PRIMARY KEY, " +
                        "'themes_name' text); "
        );

        System.out.println("Таблица Themes создана или уже существует.");
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

        System.out.println("Таблица Tasks создана или уже существует.");
    }

    private static void CreateJournalTable() throws SQLException {
        statmt.execute(
                "CREATE TABLE if not exists 'Journal' " +
                        "('id' INTEGER IDENTITY(1,1) PRIMARY KEY, " +
                        "'student_id' INTEGER, " +
                        "'task_id' INTEGER, " +
                        "'points' INTEGER, " +
                        "FOREIGN KEY (student_id) REFERENCES Students (student_id), " +
                        "FOREIGN KEY (task_id) REFERENCES Tasks (task_id));"
        );

        System.out.println("Таблица Journal создана или уже существует.");
    }
}
