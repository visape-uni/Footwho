package com.example.visape.footwho.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.visape.footwho.domain.classes.Competition;
import com.example.visape.footwho.presentation.adapter.MyCompetitionsAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by Visape on 14/10/2017.
 */

public class GetCompetitionsTask extends AsyncTask<Void, Void, ArrayList<Competition>> {

    private ApiService apiService = new ApiService();

    private static final String COMPETITIONS = "competitions";
    private Gson gson = new Gson();

    @Override
    protected ArrayList<Competition> doInBackground(Void... params) {
        ArrayList<Competition> competitionsList = new ArrayList<>();

        try {
            HttpURLConnection apiConnection = apiService.connect(COMPETITIONS);
            apiService.addToken(apiConnection);
            apiService.minifiedResponse(apiConnection);

            switch (apiConnection.getResponseCode()) {
                case HttpURLConnection.HTTP_OK:
                    InputStream competitionsResponse = apiConnection.getInputStream();
                    InputStreamReader competitionsResponseReader = new InputStreamReader(competitionsResponse, "UTF-8");

                    //Transform response to arraylist of competitions
                    Type competitionsListType = new TypeToken<ArrayList<Competition>>(){}.getType();
                    competitionsList = gson.fromJson(competitionsResponseReader, competitionsListType);
                    //TODO: CACHE ANSWER
                    Log.d("GetCompetitionsTask", "Succesfull");
                    break;

                case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
                    Log.d("GetCompetitionsTask", "Gateway Time out");
                    competitionsList.add(new Competition(-1, "Gateway time out...", null, -1, -1));
                    break;

                case HttpURLConnection.HTTP_UNAVAILABLE:
                    Log.d("GetCompetitionsTask", "API unavailable");
                    competitionsList.add(new Competition(-1, "Api not available, please try later...", null, -1, -1));
                    break;

                case 429:
                    Log.d("GetCompetitionsTask", "Too many requests, please try later...");
                    competitionsList.add(new Competition(-1, "Too many requests, please try later...", null, -1, -1));
                    break;

                default:
                    Log.d("GetCompetitionsTask", "Unknown api error " + String.valueOf(apiConnection.getResponseCode()));
                    competitionsList.add(new Competition(-1, "Unknown api error, please try later...", null, -1, -1));
                    break;
            }
            apiService.disconnect(apiConnection);
        } catch (IOException e) {
            //TODO: ERROR CONTROL
            Log.d("GetCompetitionsTask", e.toString());
            e.printStackTrace();
        }
        return competitionsList;
    }

}
