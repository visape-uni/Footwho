package com.example.visape.footwho.domain.controller;

import android.os.AsyncTask;

import com.example.visape.footwho.data.GetFixturesOfTeamTask;
import com.example.visape.footwho.domain.classes.Fixture;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Visape on 20/11/2017.
 */

public class FixtureController {
    private static FixtureController instance = null;

    private FixtureController(){}

    public static FixtureController getInstance() {
        if (instance == null) {
            instance = new FixtureController();
        }
        return instance;
    }

    public ArrayList<Fixture> getFixturesOfTeam (int teamId) throws ExecutionException, InterruptedException {

        GetFixturesOfTeamTask getFixturesOfTeamTask = new GetFixturesOfTeamTask(teamId);
        getFixturesOfTeamTask.execute();

        while (getFixturesOfTeamTask.getStatus() != AsyncTask.Status.FINISHED);

        return getFixturesOfTeamTask.get();
    }
}
