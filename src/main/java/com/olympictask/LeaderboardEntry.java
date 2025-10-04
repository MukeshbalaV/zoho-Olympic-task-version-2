package com.olympictask;

public class LeaderboardEntry {
    private String teamName;
    private int gold;
    private int silver;
    private int bronze;
    private int total;

    // Getters and Setters
    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public int getGold() { return gold; }
    public void setGold(int gold) { this.gold = gold; }

    public int getSilver() { return silver; }
    public void setSilver(int silver) { this.silver = silver; }

    public int getBronze() { return bronze; }
    public void setBronze(int bronze) { this.bronze = bronze; }

    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }
}
