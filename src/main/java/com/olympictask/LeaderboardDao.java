package com.olympictask;

import java.sql.*;
import java.util.*;

public class LeaderboardDao {
    private static final String URL = "jdbc:mysql://localhost:3306/olympics_app"; 
    private static final String USER = "root";
    private static final String PASSWORD = "mukesh24";

    // LRU CACHE - set size 5
    private static final int CACHE_SIZE = 5;

    private static final Map<String, List<LeaderboardEntry>> cache = new LinkedHashMap<>(CACHE_SIZE, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, List<LeaderboardEntry>> eldest) {
            return size() > CACHE_SIZE; // Remove oldest when limit exceeded
        }
    };

    public List<LeaderboardEntry> getLeaderboardBySportAndYear(String sportName, int year) throws Exception {
        String cacheKey = sportName.toLowerCase() + "-" + year;

        // Check if present in cache
        if (cache.containsKey(cacheKey)) {
            System.out.println(" Cache hit for " + cacheKey);
            return cache.get(cacheKey);
        }

//        System.out.println(" Cache miss — fetching from DB...");
        List<LeaderboardEntry> leaderboard = new ArrayList<>();

        String query = "SELECT teamName, " +
                "SUM(CASE WHEN medal = '1' THEN 1 ELSE 0 END) AS gold, " +
                "SUM(CASE WHEN medal = '2' THEN 1 ELSE 0 END) AS silver, " +
                "SUM(CASE WHEN medal = '3' THEN 1 ELSE 0 END) AS bronze, " +
                "SUM(CASE WHEN medal IN ('1','2','3') THEN 1 ELSE 0 END) AS total " +
                "FROM olympic_players " +
                "WHERE sportName = ? AND year = ? " +
                "GROUP BY teamName " +
                "ORDER BY total DESC, gold DESC, silver DESC, bronze DESC";

        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, sportName);
            pst.setInt(2, year);

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

        //  Store in cache
        cache.put(cacheKey, leaderboard);

        return leaderboard;
    }
}
