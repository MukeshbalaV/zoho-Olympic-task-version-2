package com.olympictask;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardDao {
    private static final String URL = "jdbc:mysql://localhost:3306/olympics_app"; 
    private static final String USER = "root";
    private static final String PASSWORD = "mukesh24";

    public List<LeaderboardEntry> getLeaderboardBySport(String sportName) throws Exception {
        List<LeaderboardEntry> leaderboard = new ArrayList<>();

        String query = "SELECT teamName, " +
                "SUM(CASE WHEN medal = '1' THEN 1 ELSE 0 END) AS gold, " +
                "SUM(CASE WHEN medal = '2' THEN 1 ELSE 0 END) AS silver, " +
                "SUM(CASE WHEN medal = '3' THEN 1 ELSE 0 END) AS bronze, " +
                "SUM(CASE WHEN medal IN ('1','2','3') THEN 1 ELSE 0 END) AS total " +
                "FROM olympic_players " +
                "WHERE sportName = ? " +
                "GROUP BY teamName " +
                "ORDER BY total DESC, gold DESC, silver DESC, bronze DESC";


        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, sportName.trim());
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                LeaderboardEntry entry = new LeaderboardEntry();
                entry.setTeamName(rs.getString("teamName"));
                entry.setGold(rs.getInt("gold"));
                entry.setSilver(rs.getInt("silver"));
                entry.setBronze(rs.getInt("bronze"));
                entry.setTotal(rs.getInt("total"));
                leaderboard.add(entry);
            }
        }

        return leaderboard;
    }
}
