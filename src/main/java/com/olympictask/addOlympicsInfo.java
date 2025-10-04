package com.olympictask;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@WebServlet("/addOlympicsInfo")
public class addOlympicsInfo extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            OlympicsDao dao = new OlympicsDao();

            // --- Reset tables ---
            dao.resetTeamsTable();
            dao.resetSportsTable();
            dao.resetOlympicPlayersTable();

            // --- Read CSV ---
            String csvFile = "D:/ecp-projects/zoho-ad-task/zoho-olympic-task/src/main/resources/Olympics2016.csv";
            List<Athlete> athletes = readAthletesCSV(csvFile);

            // --- Insert unique teams and sports ---
            Set<String> teamsSet = new LinkedHashSet<>();
            Set<String> sportsSet = new LinkedHashSet<>();
            for (Athlete a : athletes) {
                teamsSet.add(a.getTeamName());
                sportsSet.add(a.getSportName());
            }

            for (String team : teamsSet) dao.insertTeam(team);
            for (String sport : sportsSet) dao.insertSport(sport);

            // --- Insert full Olympics data ---
            dao.insertOlympicPlayers(athletes);

            // --- Redirect to search page ---
            response.sendRedirect("search.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }

    private List<Athlete> readAthletesCSV(String filePath) throws IOException {
        List<Athlete> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // skip header
                }

                // Split on commas outside quotes
                String[] cols = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                if (cols.length < 10) {
                    System.out.println("Skipping invalid row: " + line);
                    continue;
                }

                // Remove quotes and trim
                for (int i = 0; i < cols.length; i++) {
                    cols[i] = cols[i].replaceAll("^\"|\"$", "").trim();
                }

                Athlete a = new Athlete();
                try {
                    a.setSno(Integer.parseInt(cols[0]));
                    a.setName(cols[1]);
                    a.setSex(cols[2]);
                    a.setAge(Integer.parseInt(cols[3]));
                    a.setTeamName(cols[4]);
                    a.setGames(cols[5]);
                    a.setYear(Integer.parseInt(cols[6]));
                    a.setSeason(cols[7]);
                    a.setSportName(cols[8]);
                    // Store medal as string to convert later in DAO
                    a.setMedalString(cols[9]); 
                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid row: " + line);
                    continue;
                }
                list.add(a);
            }
        }
        return list;
    }
}
