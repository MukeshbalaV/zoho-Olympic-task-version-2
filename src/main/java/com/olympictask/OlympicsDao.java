package com.olympictask;

import java.sql.*;
import java.util.List;

public class OlympicsDao {
    private static final String URL = "jdbc:mysql://localhost:3306/olympics_app";
    private static final String USER = "root";
    private static final String PASSWORD = "mukesh24";

    // ------------------- TEAMS -------------------
    public void resetTeamsTable() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = con.createStatement()) {

            stmt.executeUpdate("DROP TABLE IF EXISTS teams");
            stmt.executeUpdate("CREATE TABLE teams (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "teamName VARCHAR(255) NOT NULL UNIQUE" +
                    ") AUTO_INCREMENT=1");
        }
    }

    public int insertTeam(String teamName) throws SQLException {
        if (teamName == null || teamName.trim().isEmpty()) return 0;

        String insertQuery = "INSERT IGNORE INTO teams(teamName) VALUES(?)";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pst = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, teamName.trim());
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    // ------------------- SPORTS -------------------
    public void resetSportsTable() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = con.createStatement()) {

            stmt.executeUpdate("DROP TABLE IF EXISTS sports");
            stmt.executeUpdate("CREATE TABLE sports (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "sportName VARCHAR(255) NOT NULL UNIQUE" +
                    ") AUTO_INCREMENT=1");
        }
    }

    public int insertSport(String sportName) throws SQLException {
        if (sportName == null || sportName.trim().isEmpty()) return 0;

        String insertQuery = "INSERT IGNORE INTO sports(sportName) VALUES(?)";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pst = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, sportName.trim());
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    // ------------------- OLYMPIC PLAYERS -------------------
    public void resetOlympicPlayersTable() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = con.createStatement()) {

            stmt.executeUpdate("DROP TABLE IF EXISTS olympic_players");
            stmt.executeUpdate("CREATE TABLE olympic_players (" +
                    "sno INT PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "age INT NOT NULL, " +
                    "sex VARCHAR(10) NOT NULL, " +
                    "teamName VARCHAR(255), " +
                    "sportName VARCHAR(255), " +
                    "games VARCHAR(255), " +
                    "year INT, " +
                    "season VARCHAR(50), " +
                    "medal VARCHAR(10)" +
                    ")");
        }
    }

    public void insertOlympicPlayers(List<Athlete> athletes) throws SQLException {
        if (athletes == null || athletes.isEmpty()) return;

        String insertQuery = "INSERT INTO olympic_players(sno, name, age, sex, teamName, sportName, games, year, season, medal) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?)";

        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pst = con.prepareStatement(insertQuery)) {

            for (Athlete a : athletes) {
                pst.setInt(1, a.getSno());
                pst.setString(2, a.getName().trim());
                pst.setInt(3, a.getAge());
                pst.setString(4, a.getSex().trim());
                pst.setString(5, a.getTeamName().trim());
                pst.setString(6, a.getSportName().trim());
                pst.setString(7, a.getGames().trim());
                pst.setInt(8, a.getYear());
                pst.setString(9, a.getSeason().trim());
                pst.setString(10, a.getMedalString() != null ? a.getMedalString().trim() : "None");

                pst.executeUpdate();
            }
        }
    }
}
