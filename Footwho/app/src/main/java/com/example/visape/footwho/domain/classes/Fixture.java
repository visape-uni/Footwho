package com.example.visape.footwho.domain.classes;

/**
 * Created by Visape on 14/10/2017.
 */

public class Fixture {
    private int id;
    private int competitionId;
    private String date;
    private String status;
    private int matchday;
    private String homeTeamName;
    private String awayTeamName;
    private int homeTeamId;
    private int awayTeamId;
    private Result result;

    public Fixture(int id, int competitionId, String date, String status, int matchday, String homeTeamName, String awayTeamName, int homeTeamId, int awayTeamId, Result result) {
        this.id = id;
        this.competitionId = competitionId;
        this.date = date;
        this.status = status;
        this.matchday = matchday;
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(int competitionId) {
        this.competitionId = competitionId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMatchday() {
        return matchday;
    }

    public void setMatchday(int matchday) {
        this.matchday = matchday;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public void setAwayTeamName(String awayTeamName) {
        this.awayTeamName = awayTeamName;
    }

    public int getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(int homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public int getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(int awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getGoalsHomeTeam () {
        return result.getGoalsHomeTeam();
    }

    public void setGoalsHomeTeam (String goalsHomeTeam) {
        result.setGoalsHomeTeam(goalsHomeTeam);
    }

    public String getGoalsAwayTeam() {
        return result.getGoalsAwayTeam();
    }

    public void setGoalsAwayTeam(String goalsAwayTeam) {
        result.setGoalsAwayTeam(goalsAwayTeam);
    }

    private class Result {

        private String goalsHomeTeam;
        private String goalsAwayTeam;

        public Result(String goalsHomeTeam, String goalsAwayTeam) {
            this.goalsHomeTeam = goalsHomeTeam;
            this.goalsAwayTeam = goalsAwayTeam;
        }

        public String getGoalsHomeTeam() {
            return goalsHomeTeam;
        }

        public void setGoalsHomeTeam(String goalsHomeTeam) {
            this.goalsHomeTeam = goalsHomeTeam;
        }

        public String getGoalsAwayTeam() {
            return goalsAwayTeam;
        }

        public void setGoalsAwayTeam(String goalsAwayTeam) {
            this.goalsAwayTeam = goalsAwayTeam;
        }
    }
}
