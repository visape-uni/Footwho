package com.example.visape.footwho.domain.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.visape.footwho.data.ApiService;
import com.example.visape.footwho.data.Database;
import com.example.visape.footwho.data.GetCompetitionsTask;
import com.example.visape.footwho.domain.classes.Competition;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Visape on 24/10/2017.
 */

public class CompetitionController {
    //SINGLETON CLASS
    private static CompetitionController instance = null;

    private CompetitionController () {}

    public static CompetitionController getInstance() {
        if (instance == null) {
            instance = new CompetitionController();

        }
        return  instance;
    }

    public ArrayList<Competition> getAllCompetitions() throws ExecutionException, InterruptedException {
        GetCompetitionsTask getCompetitionsTask = new GetCompetitionsTask();
        getCompetitionsTask.execute();

        //Wait for the response
        while(getCompetitionsTask.getStatus() != AsyncTask.Status.FINISHED);

        //TODO: error control

        return getCompetitionsTask.get();
    }

    public ArrayList<Competition> getMyCompetitions(Context context) {
        Database db = new Database(context);
        return db.getCompetitions();

        //TODO: error control
    }

    public boolean isCompetition(Context context, int competitionId) {
        Database db = new Database(context);
        return db.isCompetition(competitionId);
    }

    public void deleteCompetition(Context context, int competitionId) {
        Database db = new Database(context);
        db.deleteCompetition(competitionId);
    }

    public void storeCompetition (Context context, Competition competition) {
        Database db = new Database(context);
        db.storeCompetition(competition);
        //TODO: error control (IF COMPETITION IS ALREADY IN THE DB)
    }
}
