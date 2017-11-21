package com.example.visape.footwho.data;

import android.os.AsyncTask;
import android.util.Log;

import com.example.visape.footwho.domain.classes.LeagueTeam;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by Visape on 21/11/2017.
 */

public class GetLeagueTableTask extends AsyncTask<Void, Void, ArrayList<LeagueTeam>>{

    private ApiService apiService = new ApiService();

    private static final String LEAGUE_TABLE = "leagueTable";
    private static final String COMPETITIONS = "competitions";

    private Gson gson = new Gson();

    private int competitionId;

    public GetLeagueTableTask (int competitionId) {
        this.competitionId = competitionId;
    }

    @Override
    protected ArrayList<LeagueTeam> doInBackground(Void... params) {
        ArrayList<LeagueTeam> leagueTable = new ArrayList<>();

        try {
            HttpURLConnection apiConnection = apiService.connect(COMPETITIONS + "/" + String.valueOf(competitionId) + "/" + LEAGUE_TABLE);
            apiService.addToken(apiConnection);
            apiService.minifiedResponse(apiConnection);

            switch (apiConnection.getResponseCode()) {
                case HttpURLConnection.HTTP_OK:
                    InputStream leagueTableRespone = apiConnection.getInputStream();
                    InputStreamReader leagueTableResponseReader = new InputStreamReader(leagueTableRespone, "UTF-8");

                    LeagueTable apiResponse = gson.fromJson(leagueTableResponseReader, LeagueTable.class);
                    apiResponse.calculeGoalDifference();
                    leagueTable = apiResponse.getStanding();

                    Log.d("GetLeagueTableTask", "Succesfull");
                    break;

                case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
                    Log.d("GetLeagueTableTask", "Gateway Time out");
                    leagueTable.add(new LeagueTeam(-1,"Gateway time out...", -1, -1, null, -1, -1, -1, -1));
                    break;

                case HttpURLConnection.HTTP_UNAVAILABLE:
                    Log.d("GetLeagueTableTask", "API unavailable");
                    leagueTable.add(new LeagueTeam(-1, "Api not available, please try later...", -1, -1, null, -1, -1, -1, -1));
                    break;

                case 429:
                    Log.d("GetLeagueTableTask", "Too many requests, please try later...");
                    leagueTable.add(new LeagueTeam(-1, "Too many requests, please try later...", -1, -1, null, -1, -1, -1, -1));
                    break;

                default:
                    Log.d("GetLeagueTableTask", "Unknown api error " + String.valueOf(apiConnection.getResponseCode()));
                    leagueTable.add(new LeagueTeam(-1, "Unknown api error, please try later...", -1, -1, null, -1, -1, -1, -1));
                    break;
            }

            apiService.disconnect(apiConnection);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return leagueTable;
    }

    private class LeagueTable {
        private String leagueCaption;
        private int matchday;
        private ArrayList<LeagueTeam> standing;

        public LeagueTable(String leagueCaption, int matchday, ArrayList<LeagueTeam> standing) {
            this.leagueCaption = leagueCaption;
            this.matchday = matchday;
            this.standing = standing;
        }

        public String getLeagueCaption() {
            return leagueCaption;
        }

        public void setLeagueCaption(String leagueCaption) {
            this.leagueCaption = leagueCaption;
        }

        public int getMatchday() {
            return matchday;
        }

        public void setMatchday(int matchday) {
            this.matchday = matchday;
        }

        public ArrayList<LeagueTeam> getStanding() {
            return standing;
        }

        public void setStanding(ArrayList<LeagueTeam> standing) {
            this.standing = standing;
        }

        public void calculeGoalDifference() {
            if (standing != null) {
                for (int i = 0; i < standing.size(); ++i) {
                    standing.get(i).setGoalsDifference();
                }
            } else {
                standing = new ArrayList<>();
            }
        }
    }
}

