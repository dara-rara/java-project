package org.example;

import org.example.db.ManagerDB;
import org.example.models.StudentStorage;
import org.example.parser.ParserCSV;
import org.example.visualization.mapper.Graphics;
import org.example.vkAPI.ObjectVK;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Scanner;

import static java.lang.System.out;

public class Main {
    public static void main(String[] args) throws Exception {
        try {
            System.setOut(new PrintStream(out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        var scanner = new Scanner(System.in);
        ManagerDB.Conn();
        var vk = new ObjectVK();
        while (true) {
            instructions();
            switch (scanner.nextLine()) {
                case "USE_vkAPI":
                    if (ObjectVK.getMapCity().isEmpty()) {
                        ObjectVK.createMapCity();
                        out.println("Данные получены!");
                    } else {
                        out.println("Данные уже были получены!");
                    }
                    break;
                case "USE_parser":
                     if (ObjectVK.getMapCity().isEmpty() && StudentStorage.getStudents().isEmpty()) {
                    out.println("Сначала получите все данные о студентах их вк!");
                    } else if (StudentStorage.getStudents().isEmpty()) {
                        ParserCSV.readFile("table/basicprogramming.csv");
                        ParserCSV.createMapTheme();
                        ParserCSV.createMapSeminar();
                        ParserCSV.createMapExercisesAndHomeworks();
                        ParserCSV.createStudentStorage();
                        out.println("Данные получены!");
                    }
                    else {
                        out.println("Файл уже был разобран!");
                    }
                    break;
                case "ADD_DB":
                    if (findDB()) {
                        ManagerDB.CreateDB();
                        out.println("Данные загружены!");
                    } else if (!findDB() && StudentStorage.getStudents().isEmpty()) {
                        out.println("Сначала получите все данные о студентах!");
                    } else {
                        out.println("Данные уже были загружены!");
                    }
                    break;
                case "GET_student":
                    if (StudentStorage.getStudents().isEmpty()) {
                        out.println("Сначала получите все данные о студентах!");
                    } else {
                        out.println("Введите имя студента: ");
                        var name = scanner.nextLine();
                        if (StudentStorage.getStudents().containsKey(name)) {
                            out.println(StudentStorage.getStudent(name).toString());
                        } else {
                            out.println("Неправильный ввод или отсутствует запись о студенте!");
                        }
                    }
                    break;
                case "GET_students":
                    if (StudentStorage.getStudents().isEmpty()) {
                        out.println("Сначала получите все данные о студентах!");
                    } else {
                        StudentStorage.listStudents();
                    }
                    break;
                case "GET_CitiesStatistics":
                    if (findDB()) {
                        out.println("Сначала создайте базу данных!");
                    } else {
                        Graphics.drawCitiesStatistics();
                    }
                    break;
                case "GET_ThemesStatistics":
                    if (findDB()) {
                        out.println("Сначала создайте базу данных!");
                    } else {
                        Graphics.drawThemesStatistics();
                    }
                    break;
                case "GET_TaskStatistic":
                    if (findDB()) {
                        out.println("Сначала создайте базу данных!");
                    } else {
                        out.println("Введите название задачи: ");
                        Graphics.drawTaskStatistics(scanner.nextLine());
                    }
                    break;
                case "EXIT":
                    ManagerDB.CloseDB();
                    System.exit(0);
                    break;
                default: out.println("Некорректный ввод!");
            }
        }
    }

    private static boolean findDB() throws ClassNotFoundException {
        var tableName = "Journal";
        var finder = false;
        try (var tables = ManagerDB.getConn().getMetaData().getTables(null, null, tableName, null)) {
            if (tables.next()) {

            } else if (!tables.next() && !StudentStorage.getStudents().isEmpty()) {

            } else {
               finder = true;
            }
        } catch (SQLException e) {
            out.println(e.getMessage());
        }
        return finder;
    }


    private static void instructions() {
        out.println("***************************************************************************************\n" +
                "\t\t\t\t\tПредлагаю воспользовать данным приложением!\n" +
                "Выберите команду:\n" +
                "  *  USE_vkAPI - получить все данные о студентах из вк\n" +
                "  *  USE_parser - получить все данные о студентах из файла\n" +
                "  *  ADD_DB - загрузить данные о студентах в базу данных\n" +
                "  *  GET_student - получить информацию об одном студенте\n" +
                "  *  GET_students - получить информацию о всех студентах\n" +
                "  *  GET_CitiesStatistics - получить статистику городов студентов, записанных на курс\n" +
                "  *  GET_ThemesStatistics - получить статистику выполнения тем\n" +
                "  *  GET_TaskStatistic - получить статистику распределения баллов по задачи\n" +
                "  *  EXIT - выйти из приложения\n" +
                "***************************************************************************************\n");
    }
}