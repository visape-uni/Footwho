package com.example.visape.footwho.data;

import android.os.AsyncTask;
import android.util.Log;

import com.example.visape.footwho.domain.classes.Team;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Visape on 16/10/2017.
 */

public class GetTeamsOfCompetitionTask extends AsyncTask<Void,Void, ArrayList<Team>>{

    private ApiService apiService = new ApiService();

    private static final String TEAMS = "teams";
    private static final String COMPETITIONS = "competitions";

    private Gson gson = new Gson();

    private int competitionId;
    private String league;
    private Map<String, String> relationLeagueCountry = new HashMap<>();

    public GetTeamsOfCompetitionTask(int competitionId, String league) {
        this.competitionId = competitionId;
        this.league = league;
        setRelationLeagueCountry();
    }

    @Override
    protected ArrayList<Team> doInBackground(Void... params) {

        ArrayList<Team> teamsList = new ArrayList<>();

        try {
            HttpURLConnection apiConnection = apiService.connect(COMPETITIONS + "/" + String.valueOf(competitionId) + "/" + TEAMS);
            apiService.addToken(apiConnection);
            apiService.minifiedResponse(apiConnection);

            switch (apiConnection.getResponseCode()) {
                case HttpURLConnection.HTTP_OK:
                    InputStream teamsResponse = apiConnection.getInputStream();
                    InputStreamReader teamsResponseReader = new InputStreamReader(teamsResponse, "UTF-8");

                    CompetitionTeams apiResponse = gson.fromJson(teamsResponseReader, CompetitionTeams.class);
                    apiResponse.addCompetitionIdAndCountry(competitionId, league, relationLeagueCountry);
                    teamsList = apiResponse.getTeams();
                    Log.d("GetTeamsOfCompetition", "Succesfull");
                    break;

                case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
                    Log.d("GetTeamsOfCompetition", "Gateway Time out");
                    teamsList.add(new Team(-1, "Gateway time out...", null, null, null, null, -1, null));
                    break;

                case HttpURLConnection.HTTP_UNAVAILABLE:
                    Log.d("GetTeamsOfCompetition", "API unavailable");
                    teamsList.add(new Team(-1, "Api not available, please try later...", null, null, null, null, -1, null));
                    break;

                case 429:
                    Log.d("GetTeamsOfCompetition", "Too many requests, please try later...");
                    teamsList.add(new Team(-1, "Too many requests, please try later...", null, null, null, null, -1, null));
                    break;

                default:
                    Log.d("GetTeamsOfCompetition", "Unknown api error " + String.valueOf(apiConnection.getResponseCode()));
                    teamsList.add(new Team(-1, "Unknown api error, please try later...", null, null, null, null, -1, null));
                    break;
            }

            apiService.disconnect(apiConnection);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return teamsList;
    }

    private void setRelationLeagueCountry () {
        relationLeagueCountry.put("BL1", "Germany");
        relationLeagueCountry.put("BL2", "Germany");
        relationLeagueCountry.put("BL3", "Germany");
        relationLeagueCountry.put("DFB", "Germany");
        relationLeagueCountry.put("PL", "England");
        relationLeagueCountry.put("ELC", "England");
        relationLeagueCountry.put("EL1", "England");
        relationLeagueCountry.put("EL2", "England");
        relationLeagueCountry.put("SA", "Italy");
        relationLeagueCountry.put("SB", "Italy");
        relationLeagueCountry.put("PD", "Spain");
        relationLeagueCountry.put("SD", "Spain");
        relationLeagueCountry.put("CDR", "Spain");
        relationLeagueCountry.put("FL1", "France");
        relationLeagueCountry.put("FL2", "France");
        relationLeagueCountry.put("DED", "Netherlands");
        relationLeagueCountry.put("PPL", "Portugal");
        relationLeagueCountry.put("GSL", "Greece");
        relationLeagueCountry.put("BSA", "Brasil");
        relationLeagueCountry.put("CL", "Europe");
        relationLeagueCountry.put("EL", "Europe");
        relationLeagueCountry.put("EC", "Europe");
        relationLeagueCountry.put("WC", "World-Cup");
        relationLeagueCountry.put("AAL", "Australia");
    }

    //CLASS FOR THE COMPETITIONS RETURNED BY THE API
    private class CompetitionTeams {
        int count;
        ArrayList<Team> teams;

        public CompetitionTeams(int count, ArrayList<Team> teams) {
            this.count = count;
            this.teams = teams;
        }

        public void addCompetitionIdAndCountry(int competitionId, String league, Map<String, String> relationLeagueCountry) {
            //Add id of competition where the team plays
            for(int i = 0; i < teams.size(); ++i) {
                teams.get(i).setIdCompetition(competitionId);
                if (relationLeagueCountry.containsKey(league)) {
                    teams.get(i).setCountry(relationLeagueCountry.get(league));
                } else {
                    teams.get(i).setCountry("Unknown country");
                }
            }
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public ArrayList<Team> getTeams() {
            return teams;
        }

        public void setTeams(ArrayList<Team> teams) {
            this.teams = teams;
        }
    }
}
