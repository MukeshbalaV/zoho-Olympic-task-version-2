package com.olympictask;

public class Athlete {
    private int sno;
    private String name;
    private int age;
    private String sex;
    private String teamName;
    private String sportName;
    private String games;
    private int year;
    private String season;
    private String medalString; // Gold, Silver, Bronze, None

    // Getters and Setters
    public int getSno() { return sno; }
    public void setSno(int sno) { this.sno = sno; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public String getSportName() { return sportName; }
    public void setSportName(String sportName) { this.sportName = sportName; }

    public String getGames() { return games; }
    public void setGames(String games) { this.games = games; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getSeason() { return season; }
    public void setSeason(String season) { this.season = season; }

    public String getMedalString() { return medalString; }
    public void setMedalString(String medalString) { this.medalString = medalString; }
}
