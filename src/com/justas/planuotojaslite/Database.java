package com.justas.planuotojaslite;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class Database {
    private static Database instance = null;
    private Connection c = null;

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }


    public void createDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");
            createTables();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        System.out.println("Opened database successfully");
    }

    private void createTables() {
        createTasks();
    }

    private void createTasks() {
        String sql = "CREATE TABLE IF NOT EXISTS Tasks (\n" + "	id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n"  // ID, pvz: 1
                + "	name TEXT NOT NULL,\n" // Užduoties pavadinimas, Pvz: Objektinio programavimo kursinis darbas
                + "	date TEXT NOT NULL \n" // Formatas yra text pvz: 2017-12-20
                + ");";
        try (Statement stmt = c.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void insertTask(String taskName, String taskDate) {
        String sql = "INSERT INTO Tasks(name, date) VALUES(?,?)";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setString(1, taskName);
            pstmt.setString(2, taskDate);
            pstmt.execute();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void removeTask(int id) {
        String sql = "DELETE FROM Tasks " + "WHERE id = ?";

        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public ArrayList<Tasks> returnTasks(LocalDate date) {
        return getTasks(date);
    }

    private ArrayList<Tasks> getTasks(LocalDate date) {
        ArrayList<Tasks> tasks = new ArrayList<>();
        String sql = "SELECT id, name, date FROM Tasks " + "WHERE date LIKE ?";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            if (date == null) {
                pstmt.setString(1, "%" + "" + "%");
            } else {
                pstmt.setString(1, "%" + date.toString() + "%");
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date datel = format.parse(rs.getString("date"));
                tasks.add(new Tasks(id, name, datel.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
            }
        } catch (SQLException | ParseException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return tasks;
    }

    public void updateTask(int id, String name, String date) {
        String sql = "UPDATE  Tasks " + "SET name = ?, date = ?" + "WHERE id = ?";

        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, date);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}
