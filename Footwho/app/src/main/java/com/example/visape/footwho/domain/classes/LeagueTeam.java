package com.example.visape.footwho.domain.classes;

/**
 * Created by Visape on 21/11/2017.
 */

public class LeagueTeam {

    private int rank;
    private String team;
    private int teamId;
    private int playedGames;
    private String crestURI;
    private int points;
    private int goals;
    private int goalsAgainst;
    private int goalsDifference;

    public LeagueTeam(int rank, String team, int teamId, int playedGames, String crestURI, int points, int goals, int goalsAgainst, int goalsDifference) {
        this.rank = rank;
        this.team = team;
        this.teamId = teamId;
        this.playedGames = playedGames;
        this.crestURI = crestURI;
        this.points = points;
        this.goals = goals;
        this.goalsAgainst = goalsAgainst;
        this.goalsDifference = goalsDifference;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getPlayedGames() {
        return playedGames;
    }

    public void setPlayedGames(int playedGames) {
        this.playedGames = playedGames;
    }

    public String getCrestURI() {
        return crestURI;
    }

    public void setCrestURI(String crestURI) {
        this.crestURI = crestURI;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public void setGoalsAgainst(int goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    public int getGoalsDifference() {
        return goalsDifference;
    }

    public void setGoalsDifference() {
        this.goalsDifference = goals - goalsAgainst;
    }
}

