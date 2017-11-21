package com.example.visape.footwho.domain.controller;

import android.os.AsyncTask;

import com.example.visape.footwho.data.GetLeagueTableTask;
import com.example.visape.footwho.domain.classes.LeagueTeam;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Visape on 21/11/2017.
 */

public class LeagueController {

    private static LeagueController instance = null;

    private LeagueController() {}

    public static LeagueController getInstance() {
        if (instance == null) {
            instance = new LeagueController();
        }
        return instance;
    }

    public ArrayList<LeagueTeam> getLeagueTable (int competitionId) throws ExecutionException, InterruptedException {

        GetLeagueTableTask getLeagueTableTask = new GetLeagueTableTask(competitionId);
        getLeagueTableTask.execute();

        while (getLeagueTableTask.getStatus() != AsyncTask.Status.FINISHED);

        return getLeagueTableTask.get();
    }
}
