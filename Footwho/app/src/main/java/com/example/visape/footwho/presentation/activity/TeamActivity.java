package com.example.visape.footwho.presentation.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.visape.footwho.R;
import com.example.visape.footwho.domain.classes.Fixture;
import com.example.visape.footwho.domain.classes.LeagueTeam;
import com.example.visape.footwho.domain.classes.Team;
import com.example.visape.footwho.domain.controller.FixtureController;
import com.example.visape.footwho.domain.controller.LeagueController;
import com.example.visape.footwho.presentation.adapter.MyFixtureAdapter;
import com.example.visape.footwho.presentation.adapter.MyLeagueTableAdapter;
import com.larvalabs.svgandroid.SVGParser;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Visape on 20/11/2017.
 */

public class TeamActivity extends AppCompatActivity {

    private TextView teamNameLbl;
    private TextView countryOfTeamLbl;
    private Team team;
    private ImageView teamImageView;
    private ListView fixtureList;
    private ListView leagueTableList;

    private MyLeagueTableAdapter leagueTableAdapter;
    private MyFixtureAdapter fixtureAdapter;

    private ProgressBar progressBarFixture;
    private ProgressBar progressBarLeagueTable;

    private TextView errorMessageFixture;
    private TextView errorMessageLeagueTable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        team = (Team) getIntent().getExtras().getSerializable("team");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(team.getName());

        teamNameLbl = (TextView) findViewById(R.id.teamNameLbl);
        countryOfTeamLbl = (TextView) findViewById(R.id.countryLbl);
        teamImageView = (ImageView) findViewById(R.id.teamImageView);
        fixtureList = (ListView) findViewById(R.id.fixtureList);
        leagueTableList = (ListView) findViewById(R.id.leagueTableList);
        progressBarFixture = (ProgressBar) findViewById(R.id.indeterminateBarFixture);
        progressBarLeagueTable = (ProgressBar) findViewById(R.id.indeterminateBarLeagueTable);
        errorMessageFixture = (TextView) findViewById(R.id.error_fixture);
        errorMessageLeagueTable = (TextView) findViewById(R.id.error_league_table);

        teamNameLbl.setText(team.getName());
        countryOfTeamLbl.setText(team.getCountry());

        //Download and display image
        new Thread(new Runnable() {
            @Override
            public void run() {
                final HttpImageRequestTask httpImageRequestTask = new HttpImageRequestTask();
                httpImageRequestTask.execute(team.getCrestUrl());

                while (httpImageRequestTask.getStatus() != AsyncTask.Status.FINISHED);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            teamImageView.setImageDrawable(httpImageRequestTask.get());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            //TODO: HANDLE ERROR
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();

        setFixtures();
        setLeagueTable();

    }

    private void setLeagueTable() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<LeagueTeam> leagueTeams = new ArrayList<>();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBarLeagueTable.setVisibility(View.VISIBLE);
                    }
                });

                try {
                    leagueTeams = LeagueController.getInstance().getLeagueTable(team.getIdCompetition());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                final ArrayList<LeagueTeam> finalLeagueTeams = leagueTeams;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBarLeagueTable.setVisibility(View.GONE);

                        if (finalLeagueTeams.isEmpty()) {
                            leagueTableList.setVisibility(View.GONE);
                            errorMessageLeagueTable.setText("You have no internet connection...");
                            errorMessageLeagueTable.setVisibility(View.VISIBLE);
                        } else if (finalLeagueTeams.get(0).getRank() == -1) {
                            leagueTableList.setVisibility(View.GONE);
                            errorMessageLeagueTable.setText(finalLeagueTeams.get(0).getTeam());
                            errorMessageLeagueTable.setVisibility(View.VISIBLE);
                        } else {
                            leagueTableList.setVisibility(View.VISIBLE);
                            errorMessageLeagueTable.setVisibility(View.GONE);
                            leagueTableAdapter = new MyLeagueTableAdapter(getBaseContext(), finalLeagueTeams);
                            leagueTableList.setAdapter(leagueTableAdapter);
                            //TODO: PUT A FILL STAR (FAV) TO THE COMPETITIONS THAT ARE ALREADY ADDED AND AN EMPTY STAR TO THE OTHERS
                        }
                    }
                });
            }
        }).start();
    }

    private void setFixtures() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Fixture> fixtures = new ArrayList<>();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBarFixture.setVisibility(View.VISIBLE);
                    }
                });

                try {
                    fixtures = FixtureController.getInstance().getFixturesOfTeam(team.getId());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                final ArrayList<Fixture> finalFixtures = fixtures;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBarFixture.setVisibility(View.GONE);

                        if (finalFixtures.isEmpty()) {
                            errorMessageFixture.setText("You have no internet connection...");
                            errorMessageFixture.setVisibility(View.VISIBLE);
                        } else if (finalFixtures.get(0).getId() == -1) {
                            errorMessageFixture.setText(finalFixtures.get(0).getDate());
                            errorMessageFixture.setVisibility(View.VISIBLE);
                        } else {
                            errorMessageFixture.setVisibility(View.GONE);
                            fixtureAdapter = new MyFixtureAdapter(getBaseContext(), finalFixtures);
                            fixtureList.setAdapter(fixtureAdapter);
                            //TODO: PUT A FILL STAR (FAV) TO THE COMPETITIONS THAT ARE ALREADY ADDED AND AN EMPTY STAR TO THE OTHERS
                        }
                    }
                });
            }
        }).start();
    }

    //CLASS TO GET IMAGES
    private class HttpImageRequestTask extends AsyncTask<String, Void, Drawable> {
        @Override
        protected Drawable doInBackground(String... params) {
            try {
                String urlString = params[0];

                //if it have no url, show error image
                if(urlString == null) {
                    return ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_image_not_available);
                }
                //Change Http to https
                if (urlString.startsWith("http:")) {
                    urlString = urlString.replace("http:", "https:");
                }

                URL url = new URL(urlString);
                InputStream inputStream = url.openConnection().getInputStream();

                if (urlString.endsWith("svg")) {
                    return SVGParser.getSVGFromInputStream(inputStream).createPictureDrawable();
                } else if(urlString.endsWith("png"))  {
                    return Drawable.createFromStream(inputStream, "src");
                }
            } catch (Exception e) {
                Log.e("DEBUG", e.getMessage(), e);
                //TODO: HANDLE ERROR
            }
            return ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_image_not_available);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
