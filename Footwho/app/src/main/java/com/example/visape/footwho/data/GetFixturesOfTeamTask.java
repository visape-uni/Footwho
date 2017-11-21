package com.example.visape.footwho.data;

import android.os.AsyncTask;
import android.util.Log;

import com.example.visape.footwho.domain.classes.Fixture;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by Visape on 20/11/2017.
 */

public class GetFixturesOfTeamTask extends AsyncTask<Void, Void, ArrayList<Fixture>> {

    private ApiService apiService = new ApiService();

    private static final String TEAMS = "teams";
    private static final String FIXTURES = "fixtures";

    private Gson gson = new Gson();

    private int teamId;

    public GetFixturesOfTeamTask(int teamId) {
        this.teamId = teamId;
    }

    @Override
    protected ArrayList<Fixture> doInBackground(Void... params) {
        ArrayList<Fixture> fixtures = new ArrayList<>();

        try {
            HttpURLConnection apiConnection = apiService.connect(TEAMS + "/" + String.valueOf(teamId) + "/" + FIXTURES);
            apiService.addToken(apiConnection);
            apiService.minifiedResponse(apiConnection);

            switch (apiConnection.getResponseCode()) {
                case HttpURLConnection.HTTP_OK:
                    InputStream teamsResponse = apiConnection.getInputStream();
                    InputStreamReader teamsResponseReader = new InputStreamReader(teamsResponse, "UTF-8");

                    FixtureTeams apiResponse = gson.fromJson(teamsResponseReader, FixtureTeams.class);
                    fixtures = apiResponse.getFixtures();

                    Log.d("GetFixturesOfTeamTask", "Succesfull");
                    break;

                case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
                    Log.d("GetFixturesOfTeamTask", "Gateway Time out");
                    fixtures.add(new Fixture(-1, -1, "Gateway time out...", null, -1, null, null, -1, -1, null));
                    break;

                case HttpURLConnection.HTTP_UNAVAILABLE:
                    Log.d("GetFixturesOfTeamTask", "API unavailable");
                    fixtures.add(new Fixture(-1, -1, "Api not available, please try later...", null, -1, null, null, -1, -1, null));
                    break;

                case 429:
                    Log.d("GetFixturesOfTeamTask", "Too many requests, please try later...");
                    fixtures.add(new Fixture(-1, -1, "Too many requests, please try later...", null, -1, null, null, -1, -1, null));
                    break;

                default:
                    Log.d("GetFixturesOfTeamTask", "Unknown api error " + String.valueOf(apiConnection.getResponseCode()));
                    fixtures.add(new Fixture(-1, -1, "Unknown api error, please try later...", null, -1, null, null, -1, -1, null));
                    break;
            }

            apiService.disconnect(apiConnection);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return fixtures;
    }

    //CLASS FOR THE FIXTURES RETURNED BY THE API
    private class FixtureTeams {
        private String timeFrameStart;
        private String timeFrameEnd;
        private int count;
        private ArrayList<Fixture> fixtures;

        public FixtureTeams(String timeFrameStart, String timeFrameEnd, int count, ArrayList<Fixture> fixtures) {
            this.timeFrameStart = timeFrameStart;
            this.timeFrameEnd = timeFrameEnd;
            this.count = count;
            this.fixtures = fixtures;
        }

        public String getTimeFrameStart() {
            return timeFrameStart;
        }

        public void setTimeFrameStart(String timeFrameStart) {
            this.timeFrameStart = timeFrameStart;
        }

        public String getTimeFrameEnd() {
            return timeFrameEnd;
        }

        public void setTimeFrameEnd(String timeFrameEnd) {
            this.timeFrameEnd = timeFrameEnd;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public ArrayList<Fixture> getFixtures() {
            return fixtures;
        }

        public void setFixtures(ArrayList<Fixture> fixtures) {
            this.fixtures = fixtures;
        }
    }

}
