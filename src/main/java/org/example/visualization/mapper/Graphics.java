package org.example.visualization.mapper;

import org.example.db.ManagerDB;
import org.example.visualization.drawer.BarCitiesStatistics;
import org.example.visualization.drawer.LinesThemesStatistics;
import org.example.visualization.drawer.PieTaskStatistics;

import java.awt.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Graphics {
    public static void drawCitiesStatistics() throws SQLException {
        var citiesStatistics = getCitiesStatistics();
        EventQueue.invokeLater(() -> {
            BarCitiesStatistics
                    .draw("Города студентов", citiesStatistics);
        });
    }

    public static void drawTaskStatistics(String taskName) throws SQLException {
        var taskStatistics = getTaskStatistics(taskName);
        EventQueue.invokeLater(() -> {
            PieTaskStatistics.
                    draw("Распределение баллов для задачи \"" + taskName + "\"", taskStatistics);
        });
    }

    public static void drawThemesStatistics() throws SQLException {
        var themesStatistics = getThemesStatistics();
        EventQueue.invokeLater(() -> {
            LinesThemesStatistics
                    .draw("Процент выполнения тем студентами", themesStatistics);
        });
    }

    private static Map<String, Integer> getCitiesStatistics() throws SQLException {
        var command =
                "SELECT Towns.town_name, COUNT(*) 'count' FROM Towns, Students " +
                        "WHERE Towns.town_id = Students.town_id GROUP BY Towns.town_name ORDER BY [count] DESC LIMIT 10 OFFSET 1";
        var resultSet = ManagerDB.getStatmt().executeQuery(command);
        if (resultSet.isClosed()) {
            return null;
        }
        var citiesStatistics = new HashMap<String, Integer>();
        while (resultSet.next()) {
            citiesStatistics.put(resultSet.getString("town_name"), resultSet.getInt(2));
        }
        return citiesStatistics;
    }

    private static Map<Integer, Integer> getTaskStatistics(String taskName) throws SQLException {
        var command =
                "SELECT task_id FROM Tasks WHERE task_name = '" + taskName + "'";
        var resultSet = ManagerDB.getStatmt().executeQuery(command);
        if (resultSet.isClosed()) {
            System.out.println("Некорректное название задачи!");
            return null;
        }
        resultSet.next();
        var task_id = resultSet.getInt("task_id");

        command =
                "SELECT points, COUNT(*) FROM Journal WHERE task_id = " + task_id + " GROUP BY points";
        resultSet = ManagerDB.getStatmt().executeQuery(command);
        if (resultSet.isClosed()) {
            return null;
        }
        var taskStatistics = new HashMap<Integer, Integer>();
        while (resultSet.next()) {
            taskStatistics.put(resultSet.getInt("points"), resultSet.getInt(2));
        }
        return taskStatistics;
    }

    private static Map<String, Double> getThemesStatistics() throws SQLException {
        var command =
                "SELECT Tasks.themes_id, themes_name, SUM(points_max) FROM Tasks, Themes " +
                        "WHERE Tasks.themes_id = Themes.themes_id GROUP BY Tasks.themes_id, themes_name";
        var resultSet = ManagerDB.getStatmt().executeQuery(command);
        if (resultSet.isClosed()) {
            return null;
        }
        var themesMaxPoints = new HashMap<String, Integer>();
        while (resultSet.next()) {
            themesMaxPoints.put(resultSet.getString("themes_name"), resultSet.getInt(3));
        }

        command =
                "SELECT COUNT(*) FROM Students";
        resultSet = ManagerDB.getStatmt().executeQuery(command);
        if (resultSet.isClosed()) {
            return null;
        }
        resultSet.next();
        var countStudents = resultSet.getInt(1);

        command =
                "SELECT Themes.themes_name, SUM(points) FROM Journal, Themes, Tasks " +
                        "WHERE Tasks.task_id = Journal.task_id AND Themes.themes_id = Tasks.themes_id " +
                        "GROUP BY Themes.themes_name";
        resultSet = ManagerDB.getStatmt().executeQuery(command);
        if (resultSet.isClosed()) {
            return null;
        }
        var themesAverage = new HashMap<String, Double>();
        while (resultSet.next()) {
            themesAverage.put(resultSet.getString("themes_name"), resultSet.getDouble(2) / countStudents);
        }
        var themesStatistics = new HashMap<String, Double>();
        for (var entrySet : themesAverage.entrySet()) {
            themesStatistics.put(entrySet.getKey(), entrySet.getValue() / themesMaxPoints.get(entrySet.getKey()) * 100.0);
        }
        return themesStatistics;
    }
}

