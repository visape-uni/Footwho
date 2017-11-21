package com.example.visape.footwho.domain.classes;

/**
 * Created by Visape on 13/10/2017.
 */

public class Competition {
    private int id;
    private String caption;
    private String league;
    private String year;
    private int currentMatchday;
    private int numberOfMatchdays;
    private int numberOfTeams;
    private int numberOfGames;
    private String lastUpdated;

    public Competition(int id, String league, String caption, int currentMatchday, int numberOfMatchdays) {
        this.id = id;
        this.league = league;
        this.caption = caption;
        this.currentMatchday = currentMatchday;
        this.numberOfMatchdays = numberOfMatchdays;
    }

    public Competition(int id, String caption, String league, String year, int currentMatchday, int numberOfMatchdays, int numberOfTeams, int numberOfGames, String lastUpdated) {
        this.id = id;
        this.caption = caption;
        this.league = league;
        this.year = year;
        this.currentMatchday = currentMatchday;
        this.numberOfMatchdays = numberOfMatchdays;
        this.numberOfTeams = numberOfTeams;
        this.numberOfGames = numberOfGames;
        this.lastUpdated = lastUpdated;
    }

    public Competition() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getCurrentMatchday() {
        return currentMatchday;
    }

    public void setCurrentMatchday(int currentMatchday) {
        this.currentMatchday = currentMatchday;
    }

    public int getNumberOfMatchdays() {
        return numberOfMatchdays;
    }

    public void setNumberOfMatchdays(int numberOfMatchdays) {
        this.numberOfMatchdays = numberOfMatchdays;
    }

    public int getNumberOfTeams() {
        return numberOfTeams;
    }

    public void setNumberOfTeams(int numberOfTeams) {
        this.numberOfTeams = numberOfTeams;
    }

    public int getNumberOfGames() {
        return numberOfGames;
    }

    public void setNumberOfGames(int numberOfGames) {
        this.numberOfGames = numberOfGames;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "Competition{" +
                "id=" + id +
                ", caption='" + caption + '\'' +
                ", league='" + league + '\'' +
                ", year='" + year + '\'' +
                ", currentMatchday=" + currentMatchday +
                ", numberOfMatchdays=" + numberOfMatchdays +
                ", numberOfTeams=" + numberOfTeams +
                ", numberOfGames=" + numberOfGames +
                ", lastUpdated='" + lastUpdated + '\'' +
                '}';
    }
}
