package com.example.visape.footwho.data;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Visape on 13/10/2017.
 */

public class ApiService {

    //API USED -> http://football-data.org/index

    private static final String API_TOKEN = "fc5b24b780814830915a8e3fbfee39d0";
    private static final String API_URL = "https://api.football-data.org/v1/";

    private static final String COMPETITIONS = "competitions";
    private static final String TEAMS = "teams";
    private static final String LEAGUETABLE = "leagueTable";
    private static final String FIXTURES = "fixtures";
    private static final String PLAYERS = "players";

    public ApiService() {
    }

    public HttpURLConnection connect(String URI) throws IOException {
        URL footballApi = new URL(API_URL + URI);
        return (HttpURLConnection) footballApi.openConnection();
    }

    public void addToken(HttpURLConnection httpURLConnection) {
        httpURLConnection.setRequestProperty("X-Auth-Token", API_TOKEN);
    }

    public void minifiedResponse(HttpURLConnection httpURLConnection) {
        httpURLConnection.setRequestProperty("X-Response-Control", "minified");
    }

    public void disconnect(HttpURLConnection httpURLConnection) {
        httpURLConnection.disconnect();
    }
}
