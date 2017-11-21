package com.example.visape.footwho.domain.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.visape.footwho.data.Database;
import com.example.visape.footwho.data.GetCompetitionsTask;
import com.example.visape.footwho.data.GetTeamsOfCompetitionTask;
import com.example.visape.footwho.domain.classes.Competition;
import com.example.visape.footwho.domain.classes.Team;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Visape on 16/10/2017.
 */

public class TeamController {
    //SINGLETON CLASS
    private static TeamController instance = null;

    private TeamController () {}

    public static TeamController getInstance() {
        if (instance == null) {
            instance = new TeamController();

        }
        return  instance;
    }

    public ArrayList<Team> getTeamsOfCompetition (int competitionId, String league) throws ExecutionException, InterruptedException {

        GetTeamsOfCompetitionTask getTeamsOfCompetitionTask = new GetTeamsOfCompetitionTask(competitionId, league);
        getTeamsOfCompetitionTask.execute();

        while (getTeamsOfCompetitionTask.getStatus() != AsyncTask.Status.FINISHED);

        return getTeamsOfCompetitionTask.get();
    }

    public boolean isTeam(Context context, int teamId) {
        Database db = new Database(context);
        return db.isTeam(teamId);
    }

    public ArrayList<Team> getMyTeams(Context context) {
        Database db = new Database(context);
        return db.getTeams();
    }

    public void deleteTeam(Context context, int teamId) {
        Database db = new Database(context);
        db.deleteTeam(teamId);
    }

    public void storeTeam (Context context, Team team) {
        Database db = new Database(context);
        db.storeTeam(team);
    }
}
