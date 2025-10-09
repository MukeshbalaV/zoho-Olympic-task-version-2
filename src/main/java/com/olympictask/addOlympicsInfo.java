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
//            dao.resetOlympicPlayersTable();

            // --- Read CSV from request body instead of file ---
            List<Athlete> athletes = readAthletesCSVFromBody(request.getReader());

        

            // --- Insert full Olympics data ---
            dao.insertOlympicPlayers(athletes);

            // --- Redirect to search page ---
//            response.sendRedirect("search.jsp");
         // After successful insertion
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().println("Olympics data inserted successfully!");


        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
            response.getWriter().println("Error: " + e.getMessage());

        }
    }

    private List<Athlete> readAthletesCSVFromBody(BufferedReader br) throws IOException {
        List<Athlete> list = new ArrayList<>();
        String line;
        boolean firstLine = true;

        while ((line = br.readLine()) != null) {

            if (firstLine) {
                firstLine = false; // skip header
                continue;
            }

            // Split on commas outside quotes
            String[] cols = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

            if (cols.length < 10) {
                System.out.println("Skipping invalid row: " + line);
                System.out.println("Skipping invalid row: " + cols.length);

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
                a.setMedalString(cols[9]);
            } catch (NumberFormatException e) {
                System.out.println("Skipping invalid row: " + line);
                continue;
            }
            list.add(a);
        }
        return list;
    }
}
