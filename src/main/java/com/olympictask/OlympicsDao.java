package com.olympictask;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;

public class OlympicsDao {
    private static final String URL = "jdbc:mysql://localhost:3306/olympics_app";
    private static final String USER = "root";
    private static final String PASSWORD = "mukesh24";
    // ------------------- OLYMPIC PLAYERS -------------------
    public void resetOlympicPlayersTable() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = con.createStatement()) {

            stmt.executeUpdate("DROP TABLE IF EXISTS olympic_players");
            stmt.executeUpdate("CREATE TABLE olympic_players (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "sno INT," +
                    "name VARCHAR(255) NOT NULL," +
                    "age INT NOT NULL," +
                    "sex VARCHAR(10) NOT NULL," +
                    "teamName VARCHAR(255)," +
                    "sportName VARCHAR(255)," +
                    "games VARCHAR(255)," +
                    "year INT," +
                    "season VARCHAR(50)," +
                    "medal VARCHAR(10)," +
                    "row_hash CHAR(64) NOT NULL," +
                    "UNIQUE KEY uniq_row_hash (row_hash)" +
                    ")");
        }
    }

    public void insertOlympicPlayers(List<Athlete> athletes) throws SQLException {
        if (athletes == null || athletes.isEmpty()) return;

        String insertQuery = "INSERT IGNORE INTO olympic_players" +
                "(sno, name, age, sex, teamName, sportName, games, year, season, medal, row_hash) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pst = con.prepareStatement(insertQuery)) {

            for (Athlete a : athletes) {
                String hash = generateRowHash(a);

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
                pst.setString(11, hash);

                pst.executeUpdate();
            }
        }
    }

    private String generateRowHash(Athlete a) {
        try {
            // Exclude sno and medal from hash
            String rowData = a.getName().trim() + "|" + a.getAge() + "|" + a.getSex().trim()
                    + "|" + a.getTeamName().trim() + "|" + a.getSportName().trim()
                    + "|" + a.getGames().trim() + "|" + a.getYear() + "|" + a.getSeason().trim();

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(rowData.getBytes("UTF-8"));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException("Error generating hash", e);
        }
    }

}
