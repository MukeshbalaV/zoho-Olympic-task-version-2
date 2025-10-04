<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.olympictask.LeaderboardEntry" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sport Leaderboard</title>
</head>
<body>
<div align="center">
<h1>Sport Leaderboard</h1>

<form action="getOlympicsInfo" method="get">
    <input type="text" name="sportName" placeholder="Enter sport name"/>
    <button type="submit">SEARCH</button>
</form>

<br>

<%-- Render leaderboard if available --%>
<%
    List<LeaderboardEntry> leaderboard = (List<LeaderboardEntry>) request.getAttribute("leaderboard");
    if (leaderboard != null && !leaderboard.isEmpty()) {
%>
    <table border="1" cellpadding="8">
        <tr>
            <th>Rank</th>
            <th>Team</th>
            <th>Gold</th>
            <th>Silver</th>
            <th>Bronze</th>
            <th>Total</th>
        </tr>
        <%
            int rank = 1;
            for (LeaderboardEntry entry : leaderboard) {
        %>
        <tr>
            <td><%= rank++ %></td>
            <td><%= entry.getTeamName() %></td>
            <td><%= entry.getGold() %></td>
            <td><%= entry.getSilver() %></td>
            <td><%= entry.getBronze() %></td>
            <td><%= entry.getTotal() %></td>
        </tr>
        <% } %>
    </table>
<% } else if(request.getAttribute("leaderboard") != null) { %>
    <p>No results found for this sport.</p>
<% } %>

</div>
</body>
</html>
