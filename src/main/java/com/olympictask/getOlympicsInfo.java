package com.olympictask;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/getOlympicsInfo")
public class getOlympicsInfo extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sportName = request.getParameter("sportName");

        if (sportName == null || sportName.trim().isEmpty()) {
            request.setAttribute("error", "Sport name is required");
            request.getRequestDispatcher("search.jsp").forward(request, response);
            return;
        }

        LeaderboardDao dao = new LeaderboardDao();
        try {
            List<LeaderboardEntry> leaderboard = dao.getLeaderboardBySport(sportName.trim());
            request.setAttribute("leaderboard", leaderboard);
            request.getRequestDispatcher("search.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error fetching leaderboard: " + e.getMessage());
            request.getRequestDispatcher("search.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
